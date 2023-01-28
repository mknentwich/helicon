package at.markusnentwich.helicon.configuration

import at.markusnentwich.helicon.security.*
import at.markusnentwich.helicon.services.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableMethodSecurity
class SecurityConfiguration(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val userDetailsService: HeliconUserDetailsService,
) {

    @Bean
    fun configureSecurityFilterChain(
        http: HttpSecurity,
        authenticationFilter: HeliconAuthenticationFilter,
        authorizationFilter: HeliconAuthorizationFilter
    ): SecurityFilterChain {
        http.cors().and().csrf().disable()
            .headers().frameOptions().disable().and()
            .addFilter(authenticationFilter).addFilter(authorizationFilter)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeHttpRequests()
            .requestMatchers(
                HttpMethod.GET,
                "$ASSET_SERVICE/**",
                "$CATALOGUE_SERVICE/**",
                "$META_SERVICE/**",
                "$ACCOUNT_SERVICE/login",
                "/swagger.html",
                "/swagger-ui/**",
                "/api-docs/**"
            ).permitAll()
            .requestMatchers(HttpMethod.PUT, "$ORDER_SERVICE/confirmations/**").permitAll()

            .requestMatchers(HttpMethod.PUT, "$ASSET_SERVICE/**").hasAuthority(ASSET_ROLE)
            .requestMatchers(HttpMethod.DELETE, "$ASSET_SERVICE/**").hasAuthority(ASSET_ROLE)

            .requestMatchers(HttpMethod.POST, "$CATALOGUE_SERVICE/**").hasAuthority(CATALOGUE_ROLE)
            .requestMatchers(HttpMethod.PUT, "$CATALOGUE_SERVICE/**").hasAuthority(CATALOGUE_ROLE)
            .requestMatchers(HttpMethod.DELETE, "$CATALOGUE_SERVICE/**").hasAuthority(CATALOGUE_ROLE)

            .requestMatchers(HttpMethod.POST, "$META_SERVICE/**").hasAuthority(META_ROLE)
            .requestMatchers(HttpMethod.PUT, "$META_SERVICE/**").hasAuthority(META_ROLE)
            .requestMatchers(HttpMethod.DELETE, "$META_SERVICE/**").hasAuthority(META_ROLE)

            .requestMatchers(HttpMethod.GET, "$ORDER_SERVICE/**").hasAuthority(ORDER_ROLE)

            .requestMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*/identity").authenticated()
            .requestMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*/identity/address").authenticated()
            .requestMatchers(HttpMethod.DELETE, "$ACCOUNT_SERVICE/users/*").hasAuthority(ACCOUNT_ROLE)
            .requestMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*/roles").hasAuthority(ACCOUNT_ROLE)
            .requestMatchers(HttpMethod.PUT, "$ACCOUNT_SERVICE/users/*").authenticated()
            .requestMatchers(HttpMethod.POST, "$ACCOUNT_SERVICE/**").hasAuthority(ACCOUNT_ROLE)

        if (configurationProperties.order.allowAnonymous) {
            http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, ORDER_SERVICE).permitAll()
        } else {
            http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, ORDER_SERVICE).authenticated()
        }

        http.authorizeHttpRequests().anyRequest().denyAll()
        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
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
        return SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8()
    }

    private fun tokenManager(): TokenManager {
        return TokenManager(configurationProperties, userDetailsService)
    }

    @Bean
    fun authenticationFilter(authenticationManager: AuthenticationManager): HeliconAuthenticationFilter {
        return HeliconAuthenticationFilter(tokenManager(), configurationProperties, authenticationManager)
    }

    @Bean
    fun authorizationFilter(authenticationManager: AuthenticationManager): HeliconAuthorizationFilter {
        return HeliconAuthorizationFilter(authenticationManager, configurationProperties, tokenManager())
    }
}
