package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.controller.AccountController
import at.markusnentwich.helicon.controller.AlreadyExistsException
import at.markusnentwich.helicon.controller.BadPayloadException
import at.markusnentwich.helicon.controller.NotFoundException
import at.markusnentwich.helicon.dto.AccountDto
import at.markusnentwich.helicon.dto.AddressDto
import at.markusnentwich.helicon.dto.IdentityDto
import at.markusnentwich.helicon.dto.RoleDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountServiceImpl(
    @Autowired val accountController: AccountController
) : AccountService {

    private val logger = LoggerFactory.getLogger(AccountServiceImpl::class.java)
    override fun getUser(username: String): ResponseEntity<AccountDto> {
        TODO("Not yet implemented")
    }

    override fun createAccount(user: AccountDto): ResponseEntity<AccountDto> {
        val status = try {
            return ResponseEntity.ok(accountController.createAccount(user))
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: Exception) {
            logger.error("Unexpected Exception during Account creation", e)
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateAccount(user: AccountDto, username: String): ResponseEntity<AccountDto> {
        val status = try {
            return ResponseEntity.ok(accountController.updateAccount(username, user))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: AlreadyExistsException) {
            HttpStatus.CONFLICT
        } catch (e: Exception) {
            logger.error("Unexpected exception", e)
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateRoles(username: String, roles: Array<String>): ResponseEntity<Iterable<RoleDto>> {
        val status = try {
            return ResponseEntity.ok(accountController.updateRoles(username, roles))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: Exception) {
            logger.error("Unexpected exception", e)
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun deleteAccount(username: String): ResponseEntity<Void> {
        val status = try {
            accountController.deleteAccount(username)
            HttpStatus.OK
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: Exception) {
            logger.error("Unexpected exception", e)
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun getRoles(): ResponseEntity<Iterable<RoleDto>> {
        TODO("Not yet implemented")
    }

    override fun register(user: AccountDto): ResponseEntity<AccountDto> {
        TODO("Not yet implemented")
    }

    override fun login(authorization: String): ResponseEntity<Void> {
        // this method should be never called as it should be caught by the authentication filter
        // it is only defined for documentation purposes
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    override fun updateIdentity(username: String, identity: IdentityDto): ResponseEntity<IdentityDto> {
        val status = try {
            return ResponseEntity.ok(accountController.updateIdentity(username, identity))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: Exception) {
            logger.error("Unexpected exception", e)
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }

    override fun updateAddress(username: String, address: AddressDto): ResponseEntity<AddressDto> {
        val status = try {
            return ResponseEntity.ok(accountController.updateAddress(username, address))
        } catch (e: NotFoundException) {
            HttpStatus.NOT_FOUND
        } catch (e: BadPayloadException) {
            HttpStatus.BAD_REQUEST
        } catch (e: Exception) {
            logger.error("Unexpected exception", e)
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).build()
    }
}
