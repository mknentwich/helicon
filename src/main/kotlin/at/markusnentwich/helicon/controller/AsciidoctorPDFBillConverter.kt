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
                    .attribute("bank.reference").get()
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
