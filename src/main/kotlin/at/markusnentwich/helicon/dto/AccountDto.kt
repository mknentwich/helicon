package at.markusnentwich.helicon.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Account")
data class AccountDto(
    @Schema(description = "username of this account")
    var username: String = "nobody",
    @Schema(description = "cleartext password of this account, will only be used during registration, login and password change", writeOnly = true)
    @field:JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String? = null,
    @Schema(description = "the identity of this account")
    var identity: IdentityDto = IdentityDto(),
    @Schema(description = "the state of this account, such as locked, unconfirmed, etc.")
    var state: String? = "unknown",
    @Schema(description = "roles of this account")
    var roles: MutableSet<RoleDto> = mutableSetOf()
)

@Schema(name = "Role")
data class RoleDto(
    @Schema(description = "the name of this role")
    var name: String = "norole"
)
