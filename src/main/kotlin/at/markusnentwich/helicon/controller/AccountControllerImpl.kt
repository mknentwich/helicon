package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.dto.*
import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.AddressEntity
import at.markusnentwich.helicon.entities.IdentityEntity
import at.markusnentwich.helicon.repositories.*
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
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

    override fun updateAccount(username: String, account: AccountDto): AccountDto {
        val userEntity = accountRepository.findById(username).orElseThrow {
            logger.error("Cannot find user {}", username)
            throw NotFoundException()
        }
        if (account.password != null) {
            logger.info("Changing password of {}", username)
            userEntity.password = passwordEncoder.encode(account.password)
        }
        return mapper.map(accountRepository.save(userEntity), AccountDto::class.java)
    }

    override fun updateRoles(username: String, roles: Array<String>): Iterable<RoleDto> {
        logger.info("Update roles of {}", username)
        val userEntity = accountRepository.findById(username).orElseThrow {
            logger.error("Cannot find user {}", username)
            throw NotFoundException()
        }
        userEntity.roles = roles.asList().map { r ->
            rolesRepository.findById(r).orElseThrow {
                logger.error("Role {} does not exist", r)
                throw BadPayloadException()
            }
        }.toMutableSet()
        logger.info("Roles of {} are now {}", username, userEntity.roles)
        val listType: Type = object : TypeToken<List<RoleDto>>() {}.type
        return mapper.map(accountRepository.save(userEntity).roles, listType)
    }

    override fun updateIdentity(username: String, identity: IdentityDto): IdentityDto {
        val userEntity = accountRepository.findByIdOrNull(username) ?: throw NotFoundException()
        val identityEntity = mapper.map(identity, IdentityEntity::class.java)
        identityEntity.id = userEntity.identity.id
        identityEntity.address = userEntity.identity.address
        return mapper.map(identityRepository.save(identityEntity), IdentityDto::class.java)
    }

    override fun updateAddress(username: String, address: AddressDto): AddressDto {
        val userEntity = accountRepository.findById(username).orElseThrow {
            logger.error("No user with username {}", username)
            throw NotFoundException()
        }
        val addressEntity = mapper.map(address, AddressEntity::class.java)
        val stateEntity = stateRepository.findById(address.stateId!!).orElseThrow {
            logger.error("No state with id {}", address.stateId)
            throw BadPayloadException()
        }
        addressEntity.id = userEntity.identity.address.id
        addressEntity.state = stateEntity
        return mapper.map(addressRepository.save(addressEntity), AddressDto::class.java)
    }

    override fun deleteAccount(username: String) {
        if (accountRepository.existsById(username)) {
            accountRepository.deleteById(username)
            logger.info("Deleted user {}", username)
        } else {
            logger.error("User {} does not exist", username)
            throw NotFoundException()
        }
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
