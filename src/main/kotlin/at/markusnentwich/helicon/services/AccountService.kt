package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.AccountDto
import at.markusnentwich.helicon.dto.AddressDto
import at.markusnentwich.helicon.dto.IdentityDto
import at.markusnentwich.helicon.dto.RoleDto
import at.markusnentwich.helicon.security.ACCOUNT_ROLE
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ACCOUNT_SERVICE, produces = ["application/json"])
@Tag(name = "Account Service", description = "The Account Service provides the ability to manage user accounts and logins.")
interface AccountService {

    @RequestMapping("/users/{username}", method = [RequestMethod.GET])
    @Operation(summary = "return the user information corresponding to this username. the password will not be returned. every user can request itself. only users with the 'root' role are able to request other users than their self.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = AccountDto::class))]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no user with such username found")
        ]
    )
    fun getUser(@Parameter(description = "username of the user") @PathVariable username: String): ResponseEntity<AccountDto>

    @RequestMapping("/users", method = [RequestMethod.POST])
    @Operation(summary = "create a new user. the password will not be returned. only users with the 'root' role are able to create users manually. the roles will not be ignored.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = AccountDto::class))]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "409", description = CONFLICT),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun createAccount(@Parameter(description = "the new user") @RequestBody user: AccountDto): ResponseEntity<AccountDto>

    @RequestMapping("/users/{username}", method = [RequestMethod.PUT])
    @Operation(summary = "update a user. the password will not be returned. only users with the 'root' role are able to update other users manually. the roles will not be ignored if the requesting user has the role 'root'. users without the role 'root' are only able to change their identity information and their password.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = AccountDto::class))]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no user with such username found"),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun updateAccount(
        @Parameter(description = "the user") @RequestBody user: AccountDto,
        @Parameter(description = "the username") @PathVariable username: String
    ): ResponseEntity<AccountDto>

    @RequestMapping("/users/{username}", method = [RequestMethod.DELETE])
    @Operation(summary = "delete a user", description = "delete a user. account role is required if the issuer is not the username.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = AccountDto::class))]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no user with such username found"),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    @PreAuthorize("hasAuthority('$ACCOUNT_ROLE') or #username == authentication.name")
    fun deleteAccount(@Parameter(description = "the username") @PathVariable username: String): ResponseEntity<Void>

    @RequestMapping("/roles", method = [RequestMethod.GET])
    @Operation(summary = "return all available roles. only users with the role 'root' are able to perform this.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN)
        ]
    )
    fun getRoles(): ResponseEntity<Iterable<RoleDto>>

    @RequestMapping("/register", method = [RequestMethod.POST])
    @Operation(summary = "register a new user. roles will be ignored, the password will not be returned.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = AccountDto::class))]),
            ApiResponse(responseCode = "409", description = "$CONFLICT a user with this username already exists."),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun register(@Parameter(description = "the user to register") @RequestBody user: AccountDto): ResponseEntity<AccountDto>

    @RequestMapping("/login", method = [RequestMethod.GET])
    @Operation(summary = "perform a login")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, headers = [Header(name = "Authorization", description = "contains the JWT which is required for certain actions")]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST)
        ]
    )
    fun login(@RequestHeader(name = "Authorization", required = true) authorization: String): ResponseEntity<Void>

    @RequestMapping("/users/{username}/identity", method = [RequestMethod.PUT])
    @Operation(summary = "update the identity information of a user", description = "update the identity information of a user. account roles is required if the issuer is not the username. attributes of references are not affected")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = IdentityDto::class))]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404")
        ]
    )
    @PreAuthorize("hasAuthority('$ACCOUNT_ROLE') or #username == authentication.name")
    fun updateIdentity(@PathVariable(required = true) username: String, @RequestBody identity: IdentityDto): ResponseEntity<IdentityDto>

    @RequestMapping("/users/{username}/identity/address", method = [RequestMethod.PUT])
    @Operation(summary = "update the address of a user", description = "update the address of a user. account role is required if the issuer is not the username. attributes of references are not affected")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = AddressDto::class))]),
            ApiResponse(responseCode = "400", description = "state with 'statId' does not exist"),
            ApiResponse(responseCode = "403"),
            ApiResponse(responseCode = "404")
        ]
    )
    @PreAuthorize("hasAuthority('$ACCOUNT_ROLE') or #username == authentication.name")
    fun updateAddress(@PathVariable(required = true) username: String, @RequestBody address: AddressDto): ResponseEntity<AddressDto>
}
