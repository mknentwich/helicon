package at.markusnentwich.helicon.configuration

import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import at.markusnentwich.helicon.repositories.AddressRepository
import at.markusnentwich.helicon.repositories.IdentityRepository
import at.markusnentwich.helicon.repositories.StateRepository
import at.markusnentwich.helicon.repositories.ZoneRepository
import at.markusnentwich.helicon.security.HeliconAuthenticationFilter
import at.markusnentwich.helicon.security.HeliconAuthorizationFilter
import at.markusnentwich.helicon.security.HeliconUserDetailsService
import at.markusnentwich.helicon.security.TokenManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val accountRepository: AccountRepository,
    @Autowired val addressRepository: AddressRepository,
    @Autowired val identityRepository: IdentityRepository,
    @Autowired val stateRepository: StateRepository,
    @Autowired val zoneRepository: ZoneRepository,
    @Autowired val userDetailsService: HeliconUserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        // http?.addFilter(authenticationFilter())?.addFilter(authorizationFilter())
        //
        // if (configurationProperties.order.allowAnonymous) {
        //     http?.authorizeRequests()?.antMatchers(HttpMethod.POST, "$ORDER_SERVICE/")?.permitAll()
        // } else {
        //     http?.authorizeRequests()?.antMatchers(HttpMethod.POST, "$ORDER_SERVICE/")?.authenticated()
        // }
        // http?.authorizeRequests()?.antMatchers("$ORDER_SERVICE/confirm/**")?.permitAll()
        // http?.authorizeRequests()?.antMatchers(HttpMethod.GET, "$ASSET_SERVICE/**")?.permitAll()
        // http?.authorizeRequests()?.antMatchers(HttpMethod.GET, "$CATALOGUE_SERVICE/**")?.permitAll()
        // http?.authorizeRequests()?.antMatchers(HttpMethod.GET, "$META_SERVICE/**")?.permitAll()
        //
        // if (configurationProperties.login.enableLogin) {
        //     http?.httpBasic()?.realmName("Helicon Realm")
        // }
        //
        // http?.cors()?.and()
        //     ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http?.cors()?.and()?.csrf()?.disable()
            ?.addFilter(authenticationFilter())
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        if (configurationProperties.login.root.enable) {
            val rootIdent = IdentityEntity(
                telephone = "000",
                lastName = "root",
                firstName = "root",
                email = "root@example.org",
                address = AddressEntity()
            )
            val rootAcc = AccountEntity(
                identity = rootIdent,
                username = "root",
                password = passwordEncoder().encode(configurationProperties.login.root.password)
            )
            zoneRepository.save(rootIdent.address.state.zone)
            stateRepository.save(rootIdent.address.state)
            addressRepository.save(rootIdent.address)
            identityRepository.save(rootIdent)
            accountRepository.save(rootAcc)
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(passwordEncoder())
        // auth?.authenticationProvider(databaseAuthenticationProvider)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return SCryptPasswordEncoder()
    }

    // @Bean
    // override fun authenticationManager(): AuthenticationManager {
    //     return super.authenticationManagerBean()
    // }

    private fun tokenManager(): TokenManager {
        return TokenManager(configurationProperties)
    }

    private fun authenticationFilter(): HeliconAuthenticationFilter {
        return HeliconAuthenticationFilter(tokenManager(), configurationProperties, authenticationManagerBean())
    }

    private fun authorizationFilter(): HeliconAuthorizationFilter {
        return HeliconAuthorizationFilter(authenticationManager(), configurationProperties, tokenManager())
    }
}
