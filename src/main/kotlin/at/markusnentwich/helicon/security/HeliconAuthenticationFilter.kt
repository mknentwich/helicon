package at.markusnentwich.helicon.security

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val AUTH_HEADER_KEY = "Authorization"
const val BASIC_AUTH_PREFIX = "Basic "

class HeliconAuthenticationFilter(
    private val tokenManager: TokenManager,
    private val configurationProperties: HeliconConfigurationProperties,
    private val am: AuthenticationManager
) :
    UsernamePasswordAuthenticationFilter() {

    init {
        setFilterProcessesUrl("/login")
        authenticationManager = am
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (!configurationProperties.login.enableLogin) {
            response?.status = HttpStatus.FORBIDDEN.value()
            throw DisabledAuthenticationException()
        }
        try {
            val head = request?.getHeader(AUTH_HEADER_KEY)
            if (head == null || !head.startsWith(BASIC_AUTH_PREFIX)) {
                throw BadCredentialsException("not a basic auth")
            }
            val basicHeader = String(
                Base64.getDecoder().decode(
                    request.getHeader(AUTH_HEADER_KEY).removePrefix(
                        BASIC_AUTH_PREFIX
                    )
                ),
                StandardCharsets.UTF_8
            )
            val authData = basicHeader.split(':', ignoreCase = true, limit = 2)
            if (authData.size != 2) {
                throw BadCredentialsException("no colon provided")
            }
            val username = authData[0]
            val password = authData[1]
            return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: IOException) {
            throw AuthenticationCredentialsNotFoundException("failed to resolve authentication credentials", e)
        } catch (e: IllegalArgumentException) {
            throw BadCredentialsException("invalid base64", e)
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
                configurationProperties.login.jwt.prefix + tokenManager.generateToken(user)
            )
        } else {
            response?.status = 500
        }
    }
}

class DisabledAuthenticationException : AuthenticationException("authentication disabled")
