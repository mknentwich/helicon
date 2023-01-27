package at.markusnentwich.helicon.security

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class HeliconAuthorizationFilter(
    val am: AuthenticationManager,
    val configuration: HeliconConfigurationProperties,
    val tokenManager: TokenManager
) : BasicAuthenticationFilter(am) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader(AUTH_HEADER_KEY)
        if (header == null || !header.startsWith(configuration.login.jwt.prefix)) {
            chain.doFilter(request, response)
            return
        }
        val authentication = getAuthentication(request)
        SecurityContextHolder.getContext().authentication = toAuthenticationToken(authentication)
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UserDetails {
        val header = request.getHeader(AUTH_HEADER_KEY)
        if (header != null && header.startsWith(configuration.login.jwt.prefix)) {
            val errorMessage = try {
                return tokenManager.userDetailsFromToken(header.removePrefix(configuration.login.jwt.prefix))
            } catch (e: ExpiredJwtException) {
                "Expired token"
            } catch (e: UnsupportedJwtException) {
                "Unsupported token"
            } catch (e: MalformedJwtException) {
                "Malformed token"
            } catch (e: Exception) {
                "Token not resolved"
            }
            throw AccessDeniedException(errorMessage)
        } else {
            throw AccessDeniedException("No token received")
        }
    }
}
