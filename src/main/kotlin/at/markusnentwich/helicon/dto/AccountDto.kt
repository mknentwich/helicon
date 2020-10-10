package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Account")
data class AccountDto(
    @Schema(description = "username of this account")
    var username: String,
    @Schema(description = "cleartext password of this account, will only be used during registration, login and password change")
    var password: String?,
    @Schema(description = "the identity of this account")
    var identity: IdentityDto,
    @Schema(description = "the state of this account, such as locked, unconfirmed, etc.")
    var state: String?,
    @Schema(description = "roles of this account")
    var roles: List<RoleDto>?
)

@Schema(name = "Role")
data class RoleDto(
    @Schema(description = "the name of this role")
    var name: String
)
