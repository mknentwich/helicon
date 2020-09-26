package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.ScoreOrderDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
@Tag(name = "Order Service", description = "The Order Service allows user to order products or to view orders from the past.")
interface OrderService {

    fun getAll()
    fun getOrderById()

    @RequestMapping("/", method = [RequestMethod.POST])
    @Operation(description = "Perform an order.")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "OK")
            ]
    )
    fun order(@RequestBody order: ScoreOrderDto)
}