package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.configuration.HeliconResourceLoader
import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.asciidoctor.Asciidoctor
import org.asciidoctor.AttributesBuilder.attributes
import org.asciidoctor.OptionsBuilder
import org.asciidoctor.SafeMode
import org.aspectj.util.FileUtil
import org.bouncycastle.util.encoders.Base64
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.*
import java.math.BigDecimal
import java.nio.file.Path
import java.text.DecimalFormat
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
                    .attribute("qrCode", createQRCode(order))
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
                    .attribute("customerSalutation", order.identity.salutation?.replace("&", ""))
                    .attribute("customerCompany", order.identity.company?.replace("&", ""))
                    .attribute("customerFirstname", order.identity.firstName.replace("&", ""))
                    .attribute("customerLastname", order.identity.lastName.replace("&", ""))
                    .attribute("billingStreet", order.identity.address.street.replace("&", ""))
                    .attribute("billingStreetNumber", order.identity.address.streetNumber.replace("&", ""))
                    .attribute("billingPostcode", order.identity.address.postCode.replace("&", ""))
                    .attribute("billingCity", order.identity.address.city.replace("&", ""))
                    .attribute("billingState", order.identity.address.state.name.replace("&", ""))
                    .attribute("deliveryStreet", order.deliveryAddress?.street?.replace("&", ""))
                    .attribute("deliveryStreetNumber", order.deliveryAddress?.streetNumber?.replace("&", ""))
                    .attribute("deliveryPostcode", order.deliveryAddress?.postCode?.replace("&", ""))
                    .attribute("deliveryCity", order.deliveryAddress?.city?.replace("&", ""))
                    .attribute("deliveryState", order.deliveryAddress?.state?.name?.replace("&", ""))
                    .attribute("bankName", config.bill.name)
                    .attribute("bankBic", config.bill.bic)
                    .attribute("bankIban", config.bill.iban)
                    .attribute("bankInstitute", config.bill.institute)
                    .attribute("bankReference", order.billingNumber)
                    .attribute("taxNull", if (order.taxRate == null) "true" else null)
                    .attribute("taxZero", if (order.taxRate?.compareTo(BigDecimal.ZERO) == 0) "true" else null).get()
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
        builder.append("1,Versand (${order.deliveryAddress().state.name}),,${price(order.shipping)}\r\n")
        val taxRate = order.taxRate ?: BigDecimal.ZERO
        if (taxRate.compareTo(BigDecimal.ZERO) == 0) {
            builder.append(",,Gesamtbetrag,${price(order.total())}")
        } else {
            val taxFormat = DecimalFormat()
            taxFormat.minimumFractionDigits = 0
            taxFormat.maximumFractionDigits = 3
            builder.append(",\"Gesamtbetrag netto\",,${price(order.beforeTaxes())}\r\n")
            builder.append(
                ",enthaltene USt. ${
                taxFormat.format(taxRate).replace(",", ".")
                }%,,${price(order.taxes())}\r\n"
            )
            builder.append(",\"Gesamtbetrag brutto\",,${price(order.total())}\r\n")
        }
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

    /**
     * Converts the given order to a qrcode image and returns this as base64 string.
     * @param order order to convert to a qr code
     * @return plain text, just the base64 value
     */
    private fun createQRCode(order: OrderEntity): String {
        val serviceTag = "BCD"
        val version = "001"
        val coding = "1"
        val function = "SCT"
        val amountCurrency = "EUR" + "%.2f".format(order.total().toFloat() / 100)
        val purpose = ""
        val text = ""
        val display = "Ihre Transaktion an Nentwich Verlag wird vorbereitet"
        val data: String =
            serviceTag + "\n" + version + "\n" + coding + "\n" + function + "\n" + config.bill.bic + "\n" +
                config.bill.name + "\n" + config.bill.iban + "\n" + amountCurrency + "\n" + purpose + "\n" +
                order.billingNumber + "\n" + text + "\n" + display

        val hints = mapOf(
            Pair(EncodeHintType.QR_VERSION, 13),
            Pair(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M),
            Pair(EncodeHintType.CHARACTER_SET, "UTF-8")
        )
        val matrix = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 1400, 1400, hints)
        val baos = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(matrix, "png", baos)
        baos.close()
        return Base64.toBase64String(baos.toByteArray())
    }
}
