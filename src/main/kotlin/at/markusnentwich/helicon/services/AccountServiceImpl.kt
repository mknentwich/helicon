package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.AccountDto
import at.markusnentwich.helicon.dto.RoleDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountServiceImpl:AccountService {
    override fun getUser(username: String): ResponseEntity<AccountDto> {
        TODO("Not yet implemented")
    }

    override fun createAccount(user: AccountDto): ResponseEntity<AccountDto> {
        TODO("Not yet implemented")
    }

    override fun updateAccount(user: AccountDto, username: String): ResponseEntity<AccountDto> {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(username: String) {
        TODO("Not yet implemented")
    }

    override fun getRoles(): ResponseEntity<Iterable<RoleDto>> {
        TODO("Not yet implemented")
    }

    override fun register(user: AccountDto): ResponseEntity<AccountDto> {
        TODO("Not yet implemented")
    }

    override fun login(authorization: String) {
        TODO("Not yet implemented")
    }
}