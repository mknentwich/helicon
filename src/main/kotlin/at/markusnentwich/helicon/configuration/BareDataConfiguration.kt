package at.markusnentwich.helicon.configuration

import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.entities.ROOT_CATEGORY
import at.markusnentwich.helicon.entities.ROOT_CATEGORY_NAME
import at.markusnentwich.helicon.entities.RoleEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import at.markusnentwich.helicon.repositories.AddressRepository
import at.markusnentwich.helicon.repositories.CategoryRepository
import at.markusnentwich.helicon.repositories.IdentityRepository
import at.markusnentwich.helicon.repositories.RoleRepository
import at.markusnentwich.helicon.repositories.StateRepository
import at.markusnentwich.helicon.repositories.ZoneRepository
import at.markusnentwich.helicon.security.ALL_ROLES
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import javax.annotation.PostConstruct

@Configuration
class BareDataConfiguration(
    @Autowired val configurationProperties: HeliconConfigurationProperties,
    @Autowired val accountRepository: AccountRepository,
    @Autowired val addressRepository: AddressRepository,
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val identityRepository: IdentityRepository,
    @Autowired val stateRepository: StateRepository,
    @Autowired val zoneRepository: ZoneRepository,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val passwordEncoder: PasswordEncoder
) {

    private val logger = LoggerFactory.getLogger(BareDataConfiguration::class.java)

    @PostConstruct
    fun createRootAccount() {
        logger.trace("Create root account")
        ALL_ROLES.filter { !roleRepository.existsById(it) }.forEach {
            logger.info("Role {} does not exists, creating it...", it)
            roleRepository.save(RoleEntity(name = it))
        }
        if (configurationProperties.login.root.enable && accountRepository.getOwner() == null) {
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
                password = passwordEncoder.encode(configurationProperties.login.root.password),
                roles = roleRepository.findAll().toMutableSet()
            )
            logger.info("Helicon has no owner, creating {} as configured...", rootAcc.username)
            zoneRepository.save(rootIdent.address.state.zone)
            stateRepository.save(rootIdent.address.state)
            addressRepository.save(rootIdent.address)
            identityRepository.save(rootIdent)
            accountRepository.save(rootAcc)
        }
        logger.trace("Done creating root account")
    }

    @PostConstruct
    fun insertRootCategory() {
        logger.trace("Insert root category")
        if (categoryRepository.getRoot() == null) {
            logger.info("Root category does not exist, creating {}...", ROOT_CATEGORY_NAME)
            categoryRepository.save(ROOT_CATEGORY)
        }
        logger.trace("Done creating root category")
    }
}
