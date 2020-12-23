package at.markusnentwich.helicon.security

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.entities.AccountEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val AUTH_HEADER_KEY = "Authorization"

class HeliconAuthenticationFilter(
    val am: AuthenticationManager,
    val tokenManager: TokenManager,
    val configurationProperties: HeliconConfigurationProperties
) : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        try {
            val user = ObjectMapper().readValue(request?.inputStream, AccountEntity::class.java)
            return authenticationManager.authenticate(toAuthenticationToken(user))
        } catch (e: IOException) {
            throw AuthenticationCredentialsNotFoundException("failed to resolve authentication credentials", e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val user = authResult?.principal
        if (user is User) {
            response?.addHeader(
                AUTH_HEADER_KEY,
                configurationProperties.login.jwt.prefix + tokenManager.generateToken(user.username)
            )
        } else {
            response?.status = 500
        }
    }
}
