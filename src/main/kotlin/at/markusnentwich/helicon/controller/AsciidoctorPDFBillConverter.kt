package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.entities.OrderEntity
import org.asciidoctor.Asciidoctor
import org.asciidoctor.AttributesBuilder
import org.asciidoctor.OptionsBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.format.DateTimeFormatter

@Controller
class AsciidoctorPDFBillConverter(
    @Autowired val asciidoctor: Asciidoctor,
    @Autowired val config: HeliconConfigurationProperties
) : BillConverter {

    override fun createBill(order: OrderEntity): ByteArrayOutputStream {
        val baos = ByteArrayOutputStream();
        val options = OptionsBuilder.options()
            .backend("pdf")
            .toStream(baos)
            .attributes(
                AttributesBuilder.attributes("bill.orders=${ordersAsCSV(order)}")
                    .attribute("pdf-themesdir", "src/main/resources/assets/bill/themes")
                    .attribute("pdf-theme", "mknen-theme.yml")
                    .attribute("billOrder", ordersAsCSV(order))
                    .attribute("billNumber")
                    .attribute("billDate", order.confirmed?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .attribute("billTotal", order.total())
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
        return baos
    }

    private fun ordersAsCSV(order: OrderEntity): String {
        val builder = StringBuilder()
        order.items.forEach {
            builder.append("${it.amount},${it.score.title},${price(it.score.price)},${price(it.score.price * it.amount)}\n")
        }
        val shipping: Int = if (order.deliveryAddress == null) 0 else order.deliveryAddress!!.state.zone.shipping
        builder.append(",Versand (${order.deliveryAddress().state.name}),,${price(shipping)}")
        builder.append(",,Summe,${price(order.total())}")
        return builder.toString()
    }

    private fun price(price: Int): String {
        return "€ ${price / 100}.${price % 100}"
    }
}
