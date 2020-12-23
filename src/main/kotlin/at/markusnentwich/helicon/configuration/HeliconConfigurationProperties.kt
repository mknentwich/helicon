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
    val bank: BankConfiguration = BankConfiguration(),
    /** The domain of the infrastructure */
    val domain: String = "markus-nentwich.at"
) {
    data class LoginConfiguration(
        /** Enable user logins, this does not apply to administration accounts. */
        val enableLogin: Boolean = true,
        /** Allow new users to register. */
        val enableRegistration: Boolean = true,
        /** root configuration. */
        val root: Root = Root(),
        /** Jwt configuration. */
        val jwt: JwtConfiguration = JwtConfiguration()
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

    data class BankConfiguration(
        /** The iban of the owners bank account. */
        val iban: String = "AT00 0000 0000 0000 0000",
        /** The bic of the owners bank account. */
        val bic: String = "RLNWATZUDJ",
        /** The name of the owners bank */
        val name: String = "Raika"
    )

    data class Root(
        /** Enable the root account, do not use that in production. */
        val enable: Boolean = false,
        /** The default root password. */
        val password: String = "test12345678"
    )

    data class JwtConfiguration(
        /** Expiration of the token in minutes. */
        val expiration: Long = 10,
        /** Prefix of the token. */
        val prefix: String = "Bearer "
    )
}
