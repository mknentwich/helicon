package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.OrderEntity
import org.asciidoctor.Asciidoctor
import org.asciidoctor.AttributesBuilder
import org.asciidoctor.OptionsBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.io.File

@Controller
class AsciidoctorPDFBillConverter(
    @Autowired val asciidoctor: Asciidoctor,
    @Autowired val config: HeliconConfigurationProperties
) : BillConverter {

    override fun createBill(order: OrderEntity, owner: IdentityEntity): Array<Byte> {
        val o = OptionsBuilder.options().backend("pdf").attributes(
            AttributesBuilder
                .attributes("bill.orders=${ordersAsCSV(order)}")
                .attribute("bill.order", ordersAsCSV(order))
                .attribute("bill.number")
                .attribute("bill.date")
                .attribute("bill.total", order.total())
                .attribute("owner.firstname", owner.firstName)
                .attribute("owner.lastname", owner.lastName)
                .attribute("owner.street", owner.address.street)
                .attribute("owner.streetnumber", owner.address.streetNumber)
                .attribute("owner.postcode", owner.address.postCode)
                .attribute("owner.city", owner.address.city)
                .attribute("owner.telephone", owner.telephone)
                .attribute("owner.email", owner.email)
                .attribute("owner.website", config.domain)
                .attribute("customer.company", order.identity.company)
                .attribute("customer.firstname", order.identity.firstName)
                .attribute("customer.lastname", order.identity.lastName)
                .attribute("customer.street", order.identity.address.street)
                .attribute("customer.streetnumber", order.identity.address.streetNumber)
                .attribute("customer.postcode", order.identity.address.postCode)
                .attribute("customer.city", order.identity.address.city)
                .attribute("customer.state", order.identity.address.state.name)
                .attribute("bank.bic", config.bank.bic)
                .attribute("bank.iban", config.bank.iban)
                .attribute("bank.name", config.bank.name)
                .attribute("bank.reference")
        )
        asciidoctor.loadFile(File(""), o.asMap())
        return arrayOf()
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
