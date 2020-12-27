package at.markusnentwich.helicon.security

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.Date

private val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)

@Component
class TokenManager(
    @Autowired val configuration: HeliconConfigurationProperties
) {
    fun generateToken(user: UserDetails): String {
        return Jwts.builder()
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setIssuer(configuration.login.jwt.issuer)
            .setSubject(user.username)
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(configuration.login.jwt.expiration).toInstant()))
            .signWith(secretKey)
            .compact()
    }

    fun parseToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token.replace(configuration.login.jwt.prefix, ""))
            .body
            .subject
    }
}
