package at.markusnentwich.helicon.mail

data class MailOrder(
    val receiver: String,
    val items: String,
    val quantity: Int,
    val address: String,
    val deliveryAddress: String?,
    val telephone: String,
    val email: String,
    val company: String?
)
