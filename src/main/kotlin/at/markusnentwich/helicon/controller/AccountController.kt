package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.AccountDto
import at.markusnentwich.helicon.dto.RoleDto
import org.springframework.stereotype.Controller

@Controller
interface AccountController {

    fun getUser(username: String): AccountDto
    fun createAccount(user: AccountDto): AccountDto
    fun updateAccount(user: AccountDto, username: String): AccountDto
    fun deleteAccount(username: String)
    fun getRoles(): Iterable<RoleDto>

    fun register(user: AccountDto): AccountDto
    fun login(authorization: String)
}
