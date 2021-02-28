package at.markusnentwich.helicon.controller

import at.markusnentwich.helicon.dto.AccountDto
import at.markusnentwich.helicon.dto.AddressDto
import at.markusnentwich.helicon.dto.IdentityDto
import at.markusnentwich.helicon.dto.RoleDto
import org.springframework.stereotype.Controller

@Controller
interface AccountController {

    fun getUser(username: String): AccountDto
    fun createAccount(user: AccountDto): AccountDto
    fun updateAccount(user: AccountDto, username: String): AccountDto

    /**
     * Update the identity information of the user with 'username'.
     * This will only update the identity entity itself but not attributes of references.
     *
     * @param username the username of the the user whose identity should be updated
     * @param identity the new identity information
     *
     * @throws NotFoundException when no user with 'username' exists
     */
    fun updateIdentity(username: String, identity: IdentityDto): IdentityDto

    /**
     * Update the address information of the user with 'username'.
     * This will only update the address entity itself but not attributes of references.
     *
     * @param username the username of the the user whose identity should be updated
     * @param address the new address information
     *
     * @throws NotFoundException when no user with 'username' exists
     */
    fun updateAddress(username: String, address: AddressDto): AddressDto
    fun deleteAccount(username: String)
    fun getRoles(): Iterable<RoleDto>

    fun register(user: AccountDto): AccountDto
    fun login(authorization: String)
}
