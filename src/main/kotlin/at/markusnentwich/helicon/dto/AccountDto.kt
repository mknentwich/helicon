package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AccountDto(
        @Schema(description = "username of this account")
        val username: String,
        @Schema(description = "cleartext password of this account, will only be used during registration, login and password change")
        val password: String?,
        @Schema(description = "the identity of this account")
        val identity: IdentityDto,
        @Schema(description = "the state of this account, such as locked, unconfirmed, etc.")
        val state: String?,
        @Schema(description = "roles of this account")
        val roles: List<RoleDto>?
)

data class RoleDto(
        @Schema(description = "the name of this role")
        val name: String
)