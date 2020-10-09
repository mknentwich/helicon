package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(name = "Order")
data class ScoreOrderDto(
        @Schema(description = "the id of this order")
        var id: UUID?,
        @Schema(description = "the number of the bill. this is only used during confirmation", example = "2020092701")
        var billingNumber: String?,
        @Schema(description = "the receiving company or society of this order", example = "Musikverein Leopoldsdorf")
        var company: String?,
        @Schema(description = "the date when the order was confirmed from the user")
        var confirmed: LocalDateTime?,
        @Schema(description = "the registered user who performed this order")
        var customer: AccountDto?,
        @Schema(description = "the delivery address. this may be null, if so, the billingAddress will be used")
        var deliveryAddress: AddressDto?,
        @Schema(description = "the identity of this order")
        var identity: IdentityDto,
        @Schema(description = "the date since when the order is in progress")
        var inProgress: LocalDateTime?,
        @Schema(description = "the items which belong to this order. only quantity and id of the scores are required")
        var items: List<ScoreProductDto>,
        @Schema(description = "the price of the order in subunits, e.g. cent. this is only during confirmation", example = "3995")
        var price: Int?,
        @Schema(description = "the date when the backend received the order. will be null while ordering")
        var receivedOn: LocalDateTime?,
        @Schema(description = "the date when the order was sent")
        var sent: LocalDateTime?
)