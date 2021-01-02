package at.markusnentwich.helicon.mail

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.controller.AsciidoctorPDFBillConverter
import at.markusnentwich.helicon.controller.NoOwnerException
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.OrderEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import freemarker.template.Configuration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.StringWriter
import javax.mail.internet.InternetAddress
import javax.mail.util.ByteArrayDataSource

const val OWNER_BODY_FILE = "owner-order.ftl"
const val OWNER_SUBJECT_FILE = "owner-order-subject.ftl"
const val CUSTOMER_BODY_FILE = "customer-order.ftl"
const val CUSTOMER_SUBJECT_FILE = "customer-order-subject.ftl"

@Service
class OrderMailServiceImpl(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val accountRepository: AccountRepository,
    @Autowired val mailSender: JavaMailSender,
    @Autowired val templateConfiguration: Configuration,
    @Autowired val billConverter: AsciidoctorPDFBillConverter
) : OrderMailService {
    private val logger = LoggerFactory.getLogger(OrderMailServiceImpl::class.java)

    override fun notifyCustomer(order: OrderEntity) {
        val message = mailSender.createMimeMessage()
        val owner = accountRepository.getOwner()
        if (owner == null) {
            logger.error("Helicon has no owner, cancel order")
            throw NoOwnerException()
        }
        message.replyTo = arrayOf(toAddress(owner.identity))
        val helper = MimeMessageHelper(message)
        val mailOrder = toMailOrder(order)
        helper.setTo(toAddress(order.identity))
        helper.setFrom(configurationProperties.mail.identity)
        helper.setSubject(convert(CUSTOMER_SUBJECT_FILE, mailOrder))
        helper.setText(convert(CUSTOMER_BODY_FILE, mailOrder))
        mailSender.send(message)
        logger.info("sent email to customer")
    }

    override fun notifyOwner(order: OrderEntity) {
        val message = mailSender.createMimeMessage()
        val owner = accountRepository.getOwner()
        if (owner == null) {
            logger.error("Helicon has no owner, cancel order")
            throw NoOwnerException()
        }
        message.replyTo = arrayOf(toAddress(owner.identity))
        val helper = MimeMessageHelper(message)
        val monitorAddresses = accountRepository.getMonitors().map { toAddress(it.identity) }.toTypedArray()
        val mailOrder = toMailOrder(order)
        val baos = billConverter.createBill(order)
        val attachment = ByteArrayDataSource(baos.toByteArray(), "application/pdf")
        helper.setTo(toAddress(owner.identity))
        helper.setFrom(configurationProperties.mail.identity)
        helper.setBcc(monitorAddresses)
        helper.setSubject(convert(OWNER_SUBJECT_FILE, mailOrder))
        helper.setText(convert(OWNER_BODY_FILE, mailOrder))
        helper.addAttachment("Rechnung_" + order.billingNumber + ".pdf", attachment)
        mailSender.send(message)
        logger.info("sent email to owner and monitors")
    }

    private fun toAddress(identity: IdentityEntity): InternetAddress {
        return InternetAddress(identity.email, "${identity.firstName} ${identity.lastName}")
    }

    private fun addressToString(address: AddressEntity): String {
        return "${address.street} ${address.streetNumber}, ${address.postCode} ${address.city}, ${address.state.name}"
    }

    private fun toMailOrder(order: OrderEntity): MailOrder {
        return MailOrder(
            receiver = "${order.identity.firstName} ${order.identity.lastName}",
            address = addressToString(order.identity.address),
            deliveryAddress = if (order.deliveryAddress == null) {
                null
            } else {
                addressToString(order.deliveryAddress!!)
            },
            email = order.identity.email,
            telephone = order.identity.telephone,
            quantity = order.items.sumOf { it.amount },
            items = order.items.joinToString(separator = ", ") { "${it.amount} x ${it.score.title} - ${it.score.groupType}" },
            company = order.identity.company
        )
    }

    private fun convert(template: String, model: MailOrder): String {
        val reader = StringWriter()
        templateConfiguration.getTemplate(template).process(mapOf(Pair("order", model)), reader)
        return reader.toString()
    }
}
