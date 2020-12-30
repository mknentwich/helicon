package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(name = "Order")
data class ScoreOrderDto(
    @Schema(description = "the id of this order")
    var id: UUID? = null,
    @Schema(description = "the number of the bill. this is only used during confirmation", example = "2020092701")
    var billingNumber: String? = null,
    @Schema(description = "the date when the order was confirmed from the user")
    var confirmed: LocalDateTime? = null,
    @Schema(description = "the registered user who performed this order")
    var customer: AccountDto? = null,
    @Schema(description = "the delivery address. this may be null, if so, the billingAddress will be used")
    var deliveryAddress: AddressDto? = null,
    @Schema(description = "the identity of this order")
    var identity: IdentityDto = IdentityDto(),
    @Schema(description = "the date since when the order is in progress")
    var inProgress: LocalDateTime? = null,
    @Schema(description = "the items which belong to this order. only quantity and id of the scores are required")
    var items: MutableSet<ScoreProductDto> = mutableSetOf(),
    @Schema(description = "the total price of the order in subunits, e.g. cent. this is only during confirmation", example = "3995")
    var total: Int? = null,
    @Schema(description = "the date when the backend received the order. will be null while ordering")
    var receivedOn: LocalDateTime? = null,
    @Schema(description = "the date when the order was sent")
    var sent: LocalDateTime? = null
)
