package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ScoreOrderDto(
        @Schema(description = "the id of this order", example = "73")
        val id: Int?,
        @Schema(description = "the address for the bill. this may be null when a registered user orders something, if so the users address will be used")
        val billingAddress: AddressDto?,
        @Schema(description = "the receiving company or society of this order", example = "Musikverein Leopoldsdorf")
        val company: String?,
        @Schema(description = "the delivery address. this may be null, if so, the billingAddress will be used")
        val deliveryAddress: AddressDto?,
        @Schema(description = "the identity of this order")
        val identity: IdentityDto,
        @Schema(description = "the shopping cart, this is only used when committing an order. the key is the score id and the value the amount")
        val cart: Map<Int, Int>?,
        @Schema(description = "the items which belong to this order, this will only be available after ordering. the key is the score id and the value the amount")
        val items: Map<ScoreProductDto, Int>?,
        @Schema(description = "the date when the backend received the order. will be null while ordering")
        val receivedOn: LocalDateTime?
)