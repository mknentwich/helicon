package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.entities.OrderEntity
import org.asciidoctor.Asciidoctor
import org.asciidoctor.AttributesBuilder.attributes
import org.asciidoctor.OptionsBuilder
import org.asciidoctor.SafeMode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.format.DateTimeFormatter

@Controller
class AsciidoctorPDFBillConverter(
    @Autowired val asciidoctor: Asciidoctor,
    @Autowired val config: HeliconConfigurationProperties
) : BillConverter {
    val logger = LoggerFactory.getLogger(AsciidoctorPDFBillConverter::class.java)

    override fun createBill(order: OrderEntity): ByteArrayOutputStream {
        val file = ordersAsCSV(order)
        //TODO: change to inputStream
        val baos = ByteArrayOutputStream()
        val options = OptionsBuilder.options()
            .safe(SafeMode.UNSAFE)
            .backend("pdf")
            .toStream(baos)
            .attributes(
                attributes()
                    .attribute("pdf-themesdir", "src/main/resources/assets/bill/themes")
                    .attribute("pdf-theme", "mknen-theme.yml")
                    .attribute("csvFile", file.absolutePath)
                    //TODO: add bill number
                    //.attribute("billNumber")
                    .attribute("billDate", order.confirmed?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .attribute("ownerName", config.bill.address.name)
                    .attribute("ownerStreet", config.bill.address.street)
                    .attribute("ownerStreetNumber", config.bill.address.streetNumber)
                    .attribute("ownerPostCode", config.bill.address.postCode)
                    .attribute("ownerCity", config.bill.address.city)
                    .attribute("ownerPhone", config.bill.address.phone)
                    .attribute("ownerEmail", config.bill.address.mail)
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
                    .attribute("bankName", config.bill.bank.name)
                    .attribute("bankBic", config.bill.bank.bic)
                    .attribute("bankIban", config.bill.bank.iban)
                    .attribute("bankInstitute", config.bill.bank.institute)
                    //TODO: add payment reference
                    .attribute("bankReference").get()
            ).get()
        asciidoctor.convertFile(File("src/main/resources/assets/bill/bill.adoc"), options)
        file.delete()
        return baos
    }

    private fun ordersAsCSV(order: OrderEntity): File {
        val builder = StringBuilder()
        builder.append("Menge,Beschreibung,Einzelpreis,Gesamtpreis\r\n")
        order.items.forEach {
            builder.append("${it.amount},${it.score.title},${price(it.score.price)},${price(it.score.price * it.amount)}\r\n")
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
}
