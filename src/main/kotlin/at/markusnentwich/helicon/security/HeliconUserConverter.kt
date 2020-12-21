package at.markusnentwich.helicon.security

import at.markusnentwich.helicon.entities.AccountEntity
import at.markusnentwich.helicon.entities.RoleEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

fun toUser(account: AccountEntity): User {
    return User(
        account.username,
        account.password,
        account.roles.map { r -> toGrantedAuthority(r) }
    )
}

fun toGrantedAuthority(role: RoleEntity): GrantedAuthority {
    return HeliconAuthority(role.name)
}

class HeliconAuthority(private val roleName: String) : GrantedAuthority {
    override fun getAuthority(): String {
        return roleName
    }
}
