package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.dto.AccountDto
import at.markusnentwich.helicon.dto.RoleDto
import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.repositories.*
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import java.lang.reflect.Type

@Controller
class AccountControllerImpl(
    @Autowired val config: HeliconConfigurationProperties,
    @Autowired val accountRepository: AccountRepository,
    @Autowired val addressRepository: AddressRepository,
    @Autowired val identityRepository: IdentityRepository,
    @Autowired val rolesRepository: RoleRepository,
    @Autowired val stateRepository: StateRepository,
    @Autowired val mapper: ModelMapper,
    @Autowired val passwordEncoder: PasswordEncoder
) : AccountController {

    private val logger = LoggerFactory.getLogger(OrderControllerImpl::class.java)!!

    override fun getUser(username: String): AccountDto {
        return AccountDto()
    }

    override fun createAccount(user: AccountDto): AccountDto {
        val accountEntity = mapper.map(user, AccountEntity::class.java)
        accountEntity.roles = mutableSetOf()
        logger.debug("The new user claims roles")
        val invalidRoles = user.roles.filter { d -> !rolesRepository.existsById(d.name) }
        if (invalidRoles.isNotEmpty()) {
            invalidRoles.first { d ->
                logger.error("Role {} does not exist", d.name)
                throw BadPayloadException()
            }
        }
        accountEntity.roles = user.roles.map { d -> rolesRepository.findById(d.name).get() }.toMutableSet()
        accountEntity.password = passwordEncoder.encode(user.password)
        val addressEntity = mapper.map(user.identity.address, AddressEntity::class.java)
        if (user.identity.address.stateId == null) {
            logger.error("State id cannot be null")
            throw BadPayloadException()
        }
        addressEntity.state = stateRepository.findById(user.identity.address.stateId!!).orElseThrow {
            logger.error("No state with id {}", user.identity.address.stateId)
            throw BadPayloadException()
        }
        val identity = mapper.map(user.identity, IdentityEntity::class.java)
        identity.address = addressRepository.save(addressEntity)
        accountEntity.identity = identityRepository.save(identity)
        val accountToReturn = accountRepository.save(accountEntity)
        logger.info("Created user {} {}<{}>", accountToReturn.identity.firstName, accountToReturn.identity.lastName, accountToReturn.username)
        return mapper.map(accountToReturn, AccountDto::class.java)
    }

    override fun updateAccount(user: AccountDto, username: String): AccountDto {
        val accountEntity = mapper.map(user, AccountEntity::class.java)
        if (accountEntity.password != null && accountEntity.identity != null) {
            throw BadPayloadException()
        }
        val saved = accountRepository.save(accountEntity)
        return user
    }

    override fun deleteAccount(username: String) {
        val accountEntity = accountRepository.findById(username).get()
        if (accountEntity.identity != null) {
            throw DependentEntriesException()
        }
        accountRepository.deleteById(username)
    }

    override fun getRoles(): Iterable<RoleDto> {

        val listType: Type = object : TypeToken<List<RoleDto>>() {}.type
        return mapper.map(rolesRepository.findAll(), listType)
    }

    override fun register(user: AccountDto): AccountDto {
        TODO("Not yet implemented")
    }

    override fun login(authorization: String) {
        TODO("Not yet implemented")
    }
}
