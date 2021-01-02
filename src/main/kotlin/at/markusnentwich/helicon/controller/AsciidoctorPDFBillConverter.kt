package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.configuration.HeliconResourceLoader
import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import org.asciidoctor.Asciidoctor
import org.asciidoctor.AttributesBuilder.attributes
import org.asciidoctor.OptionsBuilder
import org.asciidoctor.SafeMode
import org.aspectj.util.FileUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.nio.file.Path
import java.time.format.DateTimeFormatter

@Controller
class AsciidoctorPDFBillConverter(
    @Autowired val asciidoctor: Asciidoctor,
    @Autowired val config: HeliconConfigurationProperties,
    @Autowired val accountRepository: AccountRepository,
    @Autowired val heliconResourceLoader: HeliconResourceLoader
) : BillConverter {
    private val logger = LoggerFactory.getLogger(AsciidoctorPDFBillConverter::class.java)

    companion object {
        const val THEME_PREFIX = "mknen"
        const val THEME_SUFFIX = "-theme.yml"
    }

    init {
        copyThemes()
    }

    override fun createBill(order: OrderEntity): InputStream {
        val owner = accountRepository.getOwner()
        val file = ordersAsCSV(order)
        val pipeOut = PipedOutputStream()
        val options = OptionsBuilder.options()
            .baseDir(Path.of(config.assets, "bill").toFile())
            .safe(SafeMode.UNSAFE)
            .backend("pdf")
            .toStream(pipeOut)
            .attributes(
                attributes()
                    .attribute("pdf-themesdir", "themes")
                    .attribute("pdf-theme", "$THEME_PREFIX$THEME_SUFFIX")
                    .attribute("csvFile", file.absolutePath)
                    .attribute("billNumber", order.billingNumber)
                    .attribute("billDate", order.confirmed?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .attribute("ownerName", owner?.identity?.firstName + " " + owner?.identity?.lastName)
                    .attribute("ownerStreet", owner?.identity?.address?.street)
                    .attribute("ownerStreetNumber", owner?.identity?.address?.streetNumber)
                    .attribute("ownerPostCode", owner?.identity?.address?.postCode)
                    .attribute("ownerCity", owner?.identity?.address?.city)
                    .attribute("ownerPhone", owner?.identity?.telephone)
                    .attribute("ownerEmail", owner?.identity?.email)
                    .attribute("ownerWebsite", config.domain)
                    .attribute("customerSalutation", order.identity.salutation)
                    .attribute("customerCompany", order.identity.company)
                    .attribute("customerFirstname", order.identity.firstName)
                    .attribute("customerLastname", order.identity.lastName)
                    .attribute("billingStreet", order.identity.address.street)
                    .attribute("billingStreetNumber", order.identity.address.streetNumber)
                    .attribute("billingPostcode", order.identity.address.postCode)
                    .attribute("billingCity", order.identity.address.city)
                    .attribute("billingState", order.identity.address.state.name)
                    .attribute("deliveryStreet", order.deliveryAddress?.street)
                    .attribute("deliveryStreetNumber", order.deliveryAddress?.streetNumber)
                    .attribute("deliveryPostcode", order.deliveryAddress?.postCode)
                    .attribute("deliveryCity", order.deliveryAddress?.city)
                    .attribute("deliveryState", order.deliveryAddress?.state?.name)
                    .attribute("bankName", config.bill.name)
                    .attribute("bankBic", config.bill.bic)
                    .attribute("bankIban", config.bill.iban)
                    .attribute("bankInstitute", config.bill.institute)
                    .attribute("bankReference", order.billingNumber).get()
            ).get()
        val inputStreamReader = InputStreamReader(heliconResourceLoader.getResource("asset:bill/bill.adoc").inputStream)

        val pipeIn = PipedInputStream(pipeOut)
        Thread {
            logger.debug("Waiting for pipe...")
            try {
                asciidoctor.convert(inputStreamReader, pipeOut.writer(), options)
            } catch (e: IOException) {
                // TODO the ruby converter throws an exception here, that the read
                // closed the pipe, but in the test scenarios everything works.
                // The Java Mail Helper might be closing to early...
                logger.warn("Pipe closed by reader, but may have worked...")
            }
            logger.debug("Flushing pipe...")
            pipeOut.flush()
            logger.debug("Closing pipe...")
            pipeOut.close()
            logger.debug("Deleting csv file...")
            file.delete()
        }.start()
        return pipeIn
    }

    private fun ordersAsCSV(order: OrderEntity): File {
        val builder = StringBuilder()
        builder.append("Menge,Beschreibung,Einzelpreis,Gesamtpreis\r\n")
        order.items.forEach {
            builder.append("${it.amount},${it.score.title} (${it.score.groupType}),${price(it.score.price)},${price(it.score.price * it.amount)}\r\n")
        }
        val shipping: Int = order.deliveryAddress().state.zone.shipping
        builder.append("1,Versand (${order.deliveryAddress().state.name}),,${price(shipping)}\r\n")
        builder.append(",,Summe,${price(order.total())}")

        var file: File? = null
        try {
            file = File.createTempFile("test", ".csv")
        } catch (e: IOException) {
            logger.error("could not create temporary csv file on basic temporary file location:\n{}", e.message)
        }
        if (file == null) {
            logger.error("file not found: null reference to temporary file occurred")
            throw NotFoundException()
        }
        FileWriter(file)
            .use { fw ->
                fw.append(builder.toString())
                fw.flush()
            }
        return file
    }

    private fun price(price: Int): String {
        return "${price / 100}.${String.format("%02d", price % 100)} €"
    }

    private fun copyThemes() {
        val billDir = Path.of(config.assets, "bill").toFile()
        if (!billDir.exists()) {
            logger.info("Bill assets directory does not exist, creating...")
            val billDirRes = heliconResourceLoader.getResource("asset:bill")
            if (!billDirRes.exists()) {
                logger.error("Bill assets is not available")
                return
            }
            FileUtil.copyDir(billDirRes.file, billDir)
        }
    }
}
