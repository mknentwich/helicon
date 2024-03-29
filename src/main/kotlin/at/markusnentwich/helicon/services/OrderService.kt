package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.ScoreOrderDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.Resource
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping(ORDER_SERVICE)
@Tag(
    name = "Order Service",
    description = "The Order Service allows user to order products or to view orders from the past."
)
interface OrderService {

    @RequestMapping(method = [RequestMethod.GET])
    @Operation(description = "return all orders. root permissions are required.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN)
        ]
    )
    fun getAll(@RequestHeader(name = "Authorization") jwt: String): ResponseEntity<Iterable<ScoreOrderDto>>

    @RequestMapping("/{id}", method = [RequestMethod.GET])
    @Operation(description = "return a order by id. only the order purchaser can request that. users with root permissions can request all.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = OK,
                content = [Content(schema = Schema(implementation = ScoreOrderDto::class))]
            ),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no order with such id found")
        ]
    )
    fun getOrderById(
        @Parameter(description = "the id of the order") @PathVariable id: UUID,
        @RequestHeader(name = "Authorization") jwt: String
    ): ResponseEntity<ScoreOrderDto>

    @RequestMapping(method = [RequestMethod.POST])
    @Operation(description = "Perform an order. if the identity is embedded, the state only has to be provided via its id. the same applies to the scores. the attribute items must not be provided, cart should be used instead as items is only used for get requests.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = OK,
                content = [Content(schema = Schema(implementation = ScoreOrderDto::class))]
            ),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN)
        ]
    )
    fun order(
        @RequestBody order: ScoreOrderDto,
        @RequestHeader(name = "Authorization", required = false) jwt: String?
    ): ResponseEntity<ScoreOrderDto>

    @RequestMapping("/confirmations/{id}", method = [RequestMethod.PUT])
    @Operation(description = "confirm an order. if the order is done by a registered user, the user must be authenticated for that")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = OK,
                content = [Content(schema = Schema(implementation = ScoreOrderDto::class))]
            ),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no order found with such id")
        ]
    )
    fun confirm(
        @Parameter(description = "the id of the order") @PathVariable id: UUID,
        @RequestHeader(name = "Authorization", required = false) jwt: String?
    ): ResponseEntity<ScoreOrderDto>

    @RequestMapping("/bills/{id}", method = [RequestMethod.GET], produces = [MediaType.APPLICATION_PDF_VALUE])
    @Operation(description = "returns a bill as PDF. customers can only access their own bills. user with the 'owner' role can access all bills")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no bill with such id exists")
        ]
    )
    fun bill(
        @Parameter(description = "the id of the order") @PathVariable id: UUID,
        @RequestHeader(name = "Authorization") jwt: String
    ): ResponseEntity<Array<Byte>>

    @RequestMapping("/bills/collection", method = [RequestMethod.GET], produces = ["application/zip"])
    @Operation(description = "returns the collection of bills")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN)
        ]
    )
    fun billCollection(
        @Parameter(description = "begin of the bill collection, inclusive")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        from: LocalDate,
        @Parameter(description = "end of the bill collection, exclusive")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        to: LocalDate,
        @Parameter(description = "only collect bills which are confirmed") confirmed: Boolean,
        @RequestHeader(name = "Authorization") jwt: String
    ): ResponseEntity<Resource>
}
