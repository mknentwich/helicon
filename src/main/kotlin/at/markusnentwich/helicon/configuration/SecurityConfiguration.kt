package at.markusnentwich.helicon.configuration

import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.RoleEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import at.markusnentwich.helicon.repositories.AddressRepository
import at.markusnentwich.helicon.repositories.IdentityRepository
import at.markusnentwich.helicon.repositories.RoleRepository
import at.markusnentwich.helicon.repositories.StateRepository
import at.markusnentwich.helicon.repositories.ZoneRepository
import at.markusnentwich.helicon.security.ALL_ROLES
import at.markusnentwich.helicon.security.ASSET_ROLE
import at.markusnentwich.helicon.security.CATALOGUE_ROLE
import at.markusnentwich.helicon.security.HeliconAuthenticationFilter
import at.markusnentwich.helicon.security.HeliconAuthorizationFilter
import at.markusnentwich.helicon.security.HeliconUserDetailsService
import at.markusnentwich.helicon.security.META_ROLE
import at.markusnentwich.helicon.security.ORDER_ROLE
import at.markusnentwich.helicon.security.TokenManager
import at.markusnentwich.helicon.services.ACCOUNT_SERVICE
import at.markusnentwich.helicon.services.ASSET_SERVICE
import at.markusnentwich.helicon.services.CATALOGUE_SERVICE
import at.markusnentwich.helicon.services.META_SERVICE
import at.markusnentwich.helicon.services.ORDER_SERVICE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
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
    @Autowired val userDetailsService: HeliconUserDetailsService,
    @Autowired val roleRepository: RoleRepository
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()
            ?.addFilter(authenticationFilter())?.addFilter(authorizationFilter())
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()
            ?.authorizeRequests()
            ?.antMatchers(
                HttpMethod.GET,
                "$ASSET_SERVICE/**",
                "$CATALOGUE_SERVICE/**",
                "$META_SERVICE/**",
                "$ACCOUNT_SERVICE/login",
                "/swagger-ui/**",
                "/v3/**"
            )?.permitAll()
            ?.antMatchers(HttpMethod.PUT, "$ORDER_SERVICE/confirm/**")?.permitAll()

            ?.antMatchers(HttpMethod.PUT, "$ASSET_SERVICE/**")?.hasAuthority(ASSET_ROLE)
            ?.antMatchers(HttpMethod.DELETE, "$ASSET_SERVICE/**")?.hasAuthority(ASSET_ROLE)

            ?.antMatchers(HttpMethod.POST, "$CATALOGUE_SERVICE/**")?.hasAuthority(CATALOGUE_ROLE)
            ?.antMatchers(HttpMethod.PUT, "$CATALOGUE_SERVICE/**")?.hasAuthority(CATALOGUE_ROLE)
            ?.antMatchers(HttpMethod.DELETE, "$CATALOGUE_SERVICE/**")?.hasAuthority(CATALOGUE_ROLE)

            ?.antMatchers(HttpMethod.POST, "$META_SERVICE/**")?.hasAuthority(META_ROLE)
            ?.antMatchers(HttpMethod.PUT, "$META_SERVICE/**")?.hasAuthority(META_ROLE)
            ?.antMatchers(HttpMethod.DELETE, "$META_SERVICE/**")?.hasAuthority(META_ROLE)

            ?.antMatchers(HttpMethod.GET, "$ORDER_SERVICE/**")?.hasAuthority(ORDER_ROLE)

        if (configurationProperties.order.allowAnonymous) {
            http?.authorizeRequests()?.antMatchers(HttpMethod.POST, ORDER_SERVICE)?.permitAll()
        } else {
            http?.authorizeRequests()?.antMatchers(HttpMethod.POST, ORDER_SERVICE)?.authenticated()
        }

        http?.authorizeRequests()?.anyRequest()?.denyAll()

        ALL_ROLES.filter { !roleRepository.existsById(it) }.forEach { roleRepository.save(RoleEntity(name = it)) }

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
                password = passwordEncoder().encode(configurationProperties.login.root.password),
                roles = roleRepository.findAll().toMutableSet()
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
