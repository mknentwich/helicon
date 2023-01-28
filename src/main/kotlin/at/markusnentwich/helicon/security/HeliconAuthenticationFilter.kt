package at.markusnentwich.helicon.security

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.services.ACCOUNT_SERVICE
import org.slf4j.LoggerFactory
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
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

const val AUTH_HEADER_KEY = "Authorization"
const val BASIC_AUTH_PREFIX = "Basic "

class HeliconAuthenticationFilter(
    private val tokenManager: TokenManager,
    private val configurationProperties: HeliconConfigurationProperties,
    private val am: AuthenticationManager
) :
    UsernamePasswordAuthenticationFilter() {

    private val hlogger = LoggerFactory.getLogger(HeliconAuthenticationFilter::class.java)

    init {
        setFilterProcessesUrl("$ACCOUNT_SERVICE/login")
        authenticationManager = am
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (!configurationProperties.login.enableLogin) {
            response?.status = HttpStatus.FORBIDDEN.value()
            hlogger.error("Received login request but login service is disabled")
            throw DisabledAuthenticationException("login disabled")
        }
        try {
            val head = request?.getHeader(AUTH_HEADER_KEY)
            if (head == null || !head.startsWith(BASIC_AUTH_PREFIX)) {
                hlogger.error("Received bad login request")
                response?.status = HttpStatus.BAD_REQUEST.value()
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
                hlogger.error("Received bad login request")
                response?.status = HttpStatus.BAD_REQUEST.value()
                throw BadCredentialsException("no colon provided")
            }
            val username = authData[0]
            val password = authData[1]
            if (username.lowercase() == "root" && !configurationProperties.login.root.enable) {
                hlogger.error("Received root login request which is disabled")
                throw DisabledAuthenticationException("root is disabled")
            }
            return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: IOException) {
            hlogger.error("Received login with bad credentials")
            throw AuthenticationCredentialsNotFoundException("failed to resolve authentication credentials", e)
        } catch (e: IllegalArgumentException) {
            hlogger.error("Received login with invalid base64")
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
            hlogger.info("Login was successful")
        } else {
            hlogger.error("Cannot generate token")
            response?.status = 500
        }
    }
}

class DisabledAuthenticationException(msg: String) : AuthenticationException(msg)
