package at.markusnentwich.helicon

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.entities.OrderEntity
import org.asciidoctor.AttributesBuilder.attributes
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.jruby.AsciidoctorJRuby.Factory.create
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
class BillPDFCreationTest(
    @Autowired val config: HeliconConfigurationProperties
) {

    @Test
    fun printBillToLocalPDFFile(){
        val asciidoctor = create()

        val order = OrderEntity()
        order.confirmed = LocalDateTime.now()

        // -- set attributes and options --
        val options = options()
            .backend("pdf")
            .inPlace(true)
            .attributes(attributes()
                .attribute("pdf-themesdir", "src/main/resources/assets/bill/themes")
                .attribute("pdf-theme", "mknen-theme.yml")
                /*.attribute("billOrder", ordersAsCSV(order))
                .attribute("billNumber")*/

                .attribute("billDate", order.confirmed?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))

                /*.attribute("billTotal", order.total())
                .attributes("bill.orders=${ordersAsCSV(order)}")*/
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
                .attribute("customerStreet", order.identity.address.street)
                .attribute("customerStreetNumber", order.identity.address.streetNumber)
                .attribute("customerPostcode", order.identity.address.postCode)
                .attribute("customerCity", order.identity.address.city)
                .attribute("customerState", order.identity.address.state.name)
                .attribute("bankName", config.bill.bank.name)
                .attribute("bankBic", config.bill.bank.bic)
                .attribute("bankIban", config.bill.bank.iban)
                .attribute("bankInstitute", config.bill.bank.institute)
                .attribute("bankReference").get()
            ).get()

        asciidoctor.convertFile(File("src/main/resources/assets/bill/bill.adoc"), options)
    }
}