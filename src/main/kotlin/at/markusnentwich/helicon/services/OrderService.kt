package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.ScoreOrderDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/order")
@Tag(name = "Order Service", description = "The Order Service allows user to order products or to view orders from the past.")
interface OrderService {

    fun getAll()
    fun getOrderById()

    @RequestMapping("/", method = [RequestMethod.POST])
    @Operation(description = "Perform an order. if the identity is embedded, the state only has to be provided via its id. the same applies to the scores. the attribute items must not be provided, cart should be used instead as items is only used for get requests.")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK)
            ]
    )
    fun order(@RequestBody order: ScoreOrderDto): ResponseEntity<ScoreOrderDto>

    @RequestMapping("/confirm/{id}", method = [RequestMethod.PUT])
    @Operation(description = "confirm an order. if the order is done by a registered user, the user must be authenticated for that")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = ScoreOrderDto::class))]),
                ApiResponse(responseCode = "400", description = BAD_REQUEST),
                ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                ApiResponse(responseCode = "403", description = FORBIDDEN),
                ApiResponse(responseCode = "404", description = "no order found with such id")
            ]
    )
    fun confirm(@Parameter(description = "the id of the order") @PathVariable id: UUID): ResponseEntity<ScoreOrderDto>
}