package at.markusnentwich.helicon

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.controller.NotFoundException
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.entities.OrderScoreEntity
import at.markusnentwich.helicon.entities.ScoreEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import org.asciidoctor.AttributesBuilder.attributes
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.SafeMode
import org.asciidoctor.jruby.AsciidoctorJRuby.Factory.create
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
class BillPDFCreationTest(
    @Autowired val config: HeliconConfigurationProperties,
    @Autowired val accountRepository: AccountRepository
) {

    @Test
    fun printBillToLocalPDFFile() {
        val asciidoctor = create()
        val owner = accountRepository.getOwner()

        // delivery address
        val delivery = AddressEntity()
        delivery.street = "Postgasse"
        delivery.streetNumber = "10"
        delivery.postCode = "2285"
        delivery.city = "Breitstetten"
        delivery.state.name = "Österreich"

        // scores
        val s1 = ScoreEntity()
        s1.title = "Eine letzte Runde"
        s1.price = 4900
        s1.groupType = "Blasorchester"
        val s2 = ScoreEntity()
        s2.title = "Eine letzte Runde"
        s2.price = 3900
        s2.groupType = "Ensemble"
        val s3 = ScoreEntity()
        s3.title = "Kaiserwalzer"
        s3.price = 4950
        s3.groupType = "Ensemble"
        val s4 = ScoreEntity()
        s4.title = "Black or White"
        s4.price = 2495
        s4.groupType = "Kommerz"

        // order
        val order = OrderEntity()
        order.deliveryAddress = delivery
        order.confirmed = LocalDateTime.now()
        order.identity.company = "Musikverein Leopoldsdorf"
        order.identity.salutation = "Herr"
        order.billingNumber = "2021010101"

        // order scores
        val os1 = OrderScoreEntity(s1, order)
        os1.amount = 2
        val os2 = OrderScoreEntity(s2, order)
        val os3 = OrderScoreEntity(s3, order)
        val os4 = OrderScoreEntity(s4, order)
        os4.amount = 3
        order.items = mutableSetOf(os1, os2, os3, os4)

        val file = ordersAsCSV(order)

        // -- set attributes and options --
        val options = options()
            .safe(SafeMode.UNSAFE)
            .backend("pdf")
            .inPlace(true)
            .attributes(
                attributes()
                    // TODO: change path to themes directory
                    .attribute("pdf-themesdir", "src/main/resources/assets/bill/themes")
                    .attribute("pdf-theme", "mknen-theme.yml")
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
        asciidoctor.convertFile(File("src/main/resources/assets/bill/bill.adoc"), options)
        file.delete()
    }

    private fun price(price: Int): String {
        return "${price / 100}.${String.format("%02d", price % 100)} €"
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
            // logger.error("could not create temporary csv file on basic temporary file location:\n{}", e.message)
        }
        if (file == null) {
            // logger.error("file not found: null reference to temporary file occurred")
            throw NotFoundException()
        }
        FileWriter(file)
            .use { fw ->
                fw.append(builder.toString())
                fw.flush()
            }
        return file
    }
}