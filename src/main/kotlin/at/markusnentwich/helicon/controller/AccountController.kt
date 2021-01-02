package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.AccountDto
import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.RoleDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
interface AccountController {

    fun getUser(username: String): AccountDto
    fun createAccount(user: AccountDto): ResponseEntity<AccountDto>
    fun updateAccount(user: AccountDto, username: String): ResponseEntity<AccountDto>
    fun deleteAccount(username: String): ResponseEntity<Void>
    fun getRoles(): ResponseEntity<Iterable<RoleDto>>

    fun register(user: AccountDto): ResponseEntity<AccountDto>
    fun login(authorization: String): ResponseEntity<Void>
}
