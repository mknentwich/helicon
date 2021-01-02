package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.configuration.HeliconConfigurationProperties
import at.markusnentwich.helicon.dto.*
import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.CategoryEntity
import at.markusnentwich.helicon.entities.ROOT_CATEGORY_NAME
import at.markusnentwich.helicon.entities.ScoreEntity
import at.markusnentwich.helicon.repositories.AccountRepository
import at.markusnentwich.helicon.repositories.CategoryRepository
import at.markusnentwich.helicon.repositories.ScoreRepository
import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import java.lang.reflect.Type

@Controller
class AccountControllerImpl(
    @Autowired val config: HeliconConfigurationProperties,
    @Autowired val accountRepo: AccountRepository,
    @Autowired val mapper: ModelMapper
) : AccountController {

    val logger = LoggerFactory.getLogger(OrderControllerImpl::class.java)

    override fun getUser(username: String): AccountDto{
        if (!accountRepo.existsById(username)) {
            logger.error("order with id {} does not exist", username)
            throw NotFoundException()
        }

        val entity = accountRepo.findById(username).get()
        val dto = mapper.map(entity, AccountDto::class.java)
        dto.identity = mapper.map(entity.identity, IdentityDto::class.java)
        dto.password = if (entity.password.isNullOrEmpty()) null else mapper.map(entity.password, String::class.java)
        dto.roles  = if (entity.roles == null) null else listOf(mapper.map(entity.roles, RoleDto::class.java))
        dto.username = if (entity.username.isNullOrEmpty()) "" else mapper.map(entity.username, String::class.java)

        val items = mutableListOf<RoleDto>()
        entity.roles.forEach { items.add(mapper.map(it, RoleDto::class.java)) }
        dto.roles = items
        return dto
    }

    override fun createAccount(account: AccountDto): ResponseEntity<AccountDto>{
        val accountEntity = mapper.map(account, AccountEntity::class.java)
        if (accountEntity.username != null && accountEntity.password != null && accountEntity.identity != null) {
            throw BadPayloadException()
        }
        var accountdto = accountRepo.findByIdOrNull(account.username)
        val saved = accountRepo.save(accountdto)
        return ResponseEntity.accepted().body(account);



    }
    override fun updateAccount(account: AccountDto, username: String): ResponseEntity<AccountDto>{
        val accountEntity = mapper.map(account, AccountEntity::class.java)
        if (accountEntity.username != null && accountEntity.password != null && accountEntity.identity != null) {
            throw BadPayloadException()
        }
        val saved = accountRepo.save(accountEntity)
        return ResponseEntity.ok(account)
    }

    override fun deleteAccount(account: String): ResponseEntity<Void>{
        val accountEntity = accountRepo.findById(account).get() ?: throw NotFoundException()
        if (accountEntity.identity != null ) {
            throw DependentEntriesException()
        }
        accountRepo.deleteById(account)
        return ResponseEntity.ok().build()
    }
    override fun getRoles(): ResponseEntity<Iterable<RoleDto>>{

        val entity = accountRepo.findById("username").get()

        Iterable<RoleDto> role =  object : TypeToken<List<AccountDto>>() {}.ty

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<Iterable<User>>(user, headers, HttpStatus.OK);


        val listType: Type = object : TypeToken<List<ScoreProductDto>>() {}.type
        return mapper.map(AccountRepository.(), listType)
    }

    override fun register(user: AccountDto): ResponseEntity<AccountDto> {
        TODO("Not yet implemented")
    }

    override fun login(authorization: String): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }
}
