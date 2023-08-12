package at.markusnentwich.helicon.configuration

import at.markusnentwich.helicon.security.*
import at.markusnentwich.helicon.services.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val userDetailsService: HeliconUserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()
            ?.headers()?.frameOptions()?.disable()?.and()
            ?.addFilter(authenticationFilter())?.addFilter(authorizationFilter())
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()
            ?.authorizeRequests()
            ?.antMatchers(
                HttpMethod.GET,
                "$ASSET_SERVICE/**",
                "$CATALOGUE_SERVICE/**",
                "$META_SERVICE/**",
                "$ACCOUNT_SERVICE/login",
                "/swagger.html",
                "/swagger-ui/**",
                "/api-docs/**"
            )?.permitAll()
            ?.antMatchers(HttpMethod.PUT, "$ORDER_SERVICE/confirmations/**")?.permitAll()

            ?.antMatchers(HttpMethod.PUT, "$ASSET_SERVICE/**")?.hasAuthority(ASSET_ROLE)
            ?.antMatchers(HttpMethod.DELETE, "$ASSET_SERVICE/**")?.hasAuthority(ASSET_ROLE)

            ?.antMatchers(HttpMethod.POST, "$CATALOGUE_SERVICE/**")?.hasAuthority(CATALOGUE_ROLE)
            ?.antMatchers(HttpMethod.PUT, "$CATALOGUE_SERVICE/**")?.hasAuthority(CATALOGUE_ROLE)
            ?.antMatchers(HttpMethod.DELETE, "$CATALOGUE_SERVICE/**")?.hasAuthority(CATALOGUE_ROLE)

            ?.antMatchers(HttpMethod.POST, "$META_SERVICE/**")?.hasAuthority(META_ROLE)
            ?.antMatchers(HttpMethod.PUT, "$META_SERVICE/**")?.hasAuthority(META_ROLE)
            ?.antMatchers(HttpMethod.DELETE, "$META_SERVICE/**")?.hasAuthority(META_ROLE)

            ?.antMatchers(HttpMethod.GET, "$ORDER_SERVICE/bill/collection")?.hasAuthority(ORDER_ROLE)
            ?.antMatchers(HttpMethod.GET, "$ORDER_SERVICE/**")?.hasAuthority(ORDER_ROLE)

            ?.antMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*/identity")?.authenticated()
            ?.antMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*/identity/address")?.authenticated()
            ?.antMatchers(HttpMethod.DELETE, "$ACCOUNT_SERVICE/users/*")?.hasAuthority(ACCOUNT_ROLE)
            ?.antMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*/roles")?.hasAuthority(ACCOUNT_ROLE)
            ?.antMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*")?.authenticated()
            ?.antMatchers(HttpMethod.POST, "$ACCOUNT_SERVICE/**")?.hasAuthority(ACCOUNT_ROLE)

        if (configurationProperties.order.allowAnonymous) {
            http?.authorizeRequests()?.antMatchers(HttpMethod.POST, ORDER_SERVICE)?.permitAll()
        } else {
            http?.authorizeRequests()?.antMatchers(HttpMethod.POST, ORDER_SERVICE)?.authenticated()
        }

        http?.authorizeRequests()?.anyRequest()?.denyAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(passwordEncoder())
    }

    @Bean
    fun configurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*")
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return SCryptPasswordEncoder()
    }

    private fun tokenManager(): TokenManager {
        return TokenManager(configurationProperties, userDetailsService)
    }

    private fun authenticationFilter(): HeliconAuthenticationFilter {
        return HeliconAuthenticationFilter(tokenManager(), configurationProperties, authenticationManagerBean())
    }

    private fun authorizationFilter(): HeliconAuthorizationFilter {
        return HeliconAuthorizationFilter(authenticationManager(), configurationProperties, tokenManager())
    }
}
