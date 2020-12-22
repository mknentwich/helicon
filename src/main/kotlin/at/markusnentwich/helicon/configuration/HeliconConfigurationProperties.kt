package at.markusnentwich.helicon.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "helicon")
data class HeliconConfigurationProperties(

    /** Login configuration. */
    val login: LoginConfiguration = LoginConfiguration(),
    /** Order configuration. */
    val order: OrderConfiguration = OrderConfiguration(),
    /** Mail configuration. */
    val mail: MailConfiguration = MailConfiguration(),
    /** Path for the assets such as templates, score samples. */
    val assets: String = "assets",
    /** The bank account for bills. */
    val bill: BillConfiguration = BillConfiguration(),
    /** The domain of the infrastructure */
    val domain: String = "markus-nentwich.at"
) {
    data class LoginConfiguration(
        /** Enable user logins, this does not apply to administration accounts. */
        val enableLogin: Boolean = true,
        /** Allow new users to register. */
        val enableRegistration: Boolean = true
    )

    data class OrderConfiguration(
        /** Allow users to perform orders without being logged in or registered. */
        val allowAnonymous: Boolean = true,
        /** Orders have to be confirmed via a 'confirm' REST call. */
        val confirmation: Boolean = true,
        /** Allow orders at all. */
        val enable: Boolean = true
    )

    data class MailConfiguration(
        /** Mail notifications. */
        val notification: NotificationConfiguration = NotificationConfiguration()
    ) {
        data class NotificationConfiguration(
            /** Notify customers when they perform an order. */
            val customerOnOrder: Boolean = true,
            /** Notify the owner when someone performs an order. */
            val ownerOnOrder: Boolean = true,
            /** Notify users when they change their password. */
            val passwordChange: Boolean = true,
            /** Notify users when they register. */
            val registration: Boolean = true
        )
    }

    data class BillConfiguration(
        /** Address configuration*/
        val address: AddressConfiguration = AddressConfiguration(),
        /** Bank configuration*/
        val bank: BankConfiguration = BankConfiguration()
    ) {
        data class AddressConfiguration(
            /** The name of the seller / The selling company */
            val name: String = "Markus Nentwich",
            /** The street of the seller's address */
            val street: String = "Vereinsgasse",
            /** The street number of the seller's address */
            val streetNumber: String = "25/14",
            /** The post code of the seller's address */
            val postCode: String = "A-1020",
            /** The city of the seller's address */
            val city: String = "Wien",
            /** The phone number of the seller */
            val phone: String = "+43699 / 10329882",
            /** The mail contact address of the seller */
            val mail: String = "kontakt@markus-nentwich.at"
        )

        data class BankConfiguration(
            /** The iban of the owners bank account. */
            val iban: String = "AT40 3209 2000 0025 8475",
            /** The bic of the owners bank account. */
            val bic: String = "RLNWATWWGAE",
            /** The name of the owners bank */
            val institute: String = "Raiffeisen-Regionalbank",
            /** The name of the owners bank account*/
            val name: String = "Markus Nentwich"
        )
    }

}
