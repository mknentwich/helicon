package at.markusnentwich.helicon

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import org.asciidoctor.AttributesBuilder.attributes
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.jruby.AsciidoctorJRuby.Factory.create
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class BillPDFCreationTest(
    @Autowired val config: HeliconConfigurationProperties
) {

    @Test
    fun printBillToLocalPDFFile(){
        val asciidoctor = create()

        // -- set attributes and options --
        val options = options()
            .backend("pdf")
            .inPlace(true)
            .attributes(attributes()
                .attribute("pdf-themesdir", "src/main/resources/assets/bill/themes")
                .attribute("pdf-theme", "mknen-theme.yml")
                .attribute("cname", config.bill.address.name)
                /*.attributes("bill.orders=${ordersAsCSV(order)}")
                .attribute("bill.order", ordersAsCSV(order))
                .attribute("bill.number")
                .attribute("bill.date")
                .attribute("bill.total", order.total())
                .attribute("owner.name", config.bill.address.name)
                .attribute("owner.street", config.bill.address.street)
                .attribute("owner.streetNumber", config.bill.address.streetNumber)
                .attribute("owner.postCode", config.bill.address.postCode)
                .attribute("owner.city", config.bill.address.city)
                .attribute("owner.phone", config.bill.address.phone)
                .attribute("owner.email", config.bill.address.mail)
                .attribute("owner.website", config.domain)
                .attribute("customer.company", order.identity.company)
                .attribute("customer.firstname", order.identity.firstName)
                .attribute("customer.lastname", order.identity.lastName)
                .attribute("customer.street", order.identity.address.street)
                .attribute("customer.streetnumber", order.identity.address.streetNumber)
                .attribute("customer.postcode", order.identity.address.postCode)
                .attribute("customer.city", order.identity.address.city)
                .attribute("customer.state", order.identity.address.state.name)
                .attribute("bank.name", config.bill.bank.name)
                .attribute("bank.bic", config.bill.bank.bic)
                .attribute("bank.iban", config.bill.bank.iban)
                .attribute("bank.institute", config.bill.bank.institute)
                .attribute("bank.reference").get()*/
            ).get()

        asciidoctor.convertFile(File("src/main/resources/assets/bill/bill.adoc"), options)
    }
}