package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(name = "Order")
data class ScoreOrderDto(
        @Schema(description = "the id of this order")
        val id: UUID?,
        @Schema(description = "the address for the bill. this may be null when a registered user orders something, if so the users address will be used")
        val billingAddress: AddressDto?,
        @Schema(description = "the number of the bill. this is only used during confirmation", example = "2020092701")
        val billingNumber: String?,
        @Schema(description = "the receiving company or society of this order", example = "Musikverein Leopoldsdorf")
        val company: String?,
        @Schema(description = "the date when the order was confirmed from the user")
        val confirmed: LocalDateTime?,
        @Schema(description = "the delivery address. this may be null, if so, the billingAddress will be used")
        val deliveryAddress: AddressDto?,
        @Schema(description = "the identity of this order")
        val identity: IdentityDto,
        @Schema(description = "the date since when the order is in progress")
        val inProgress: LocalDateTime?,
        @Schema(description = "the shopping cart, this is only used when committing an order. the key is the score id and the value the amount")
        val cart: Map<Int, Int>?,
        @Schema(description = "the items which belong to this order, this will only be available after ordering. the key is the score id and the value the amount")
        val items: Map<ScoreProductDto, Int>?,
        @Schema(description = "the price of the order in subunits, e.g. cent. this is only during confirmation", example = "3995")
        val price: Int?,
        @Schema(description = "the date when the backend received the order. will be null while ordering")
        val receivedOn: LocalDateTime?,
        @Schema(description = "the date when the order was sent")
        val sent: LocalDateTime?
)