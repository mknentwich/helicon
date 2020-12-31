package at.markusnentwich.helicon.dto

import com.fasterxml.jackson.annotation.JsonBackReference
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Score")
data class ScoreProductDto(
    @Schema(description = "the id of this score", example = "73")
    var id: Long? = null,
    @Schema(description = "author of this score", example = "Markus Nentwich")
    var author: String = "Markus Nentwich",
    @Schema(description = "the category of this score", writeOnly = true)
    @JsonBackReference
    var category: CategoryProductDto? = null,
    @Schema(description = "the difficulty of this score", example = "2", minimum = "1", maximum = "5")
    var difficulty: Int = 2,
    @Schema(description = "the group type of this score", example = "Blasorchester")
    var groupType: String = "Blasorchester",
    @Schema(description = "the instrumentation of this score", example = "1. Klarinette in B, 2. Klarinette in B")
    var instrumentation: String = "Posaunen",
    @Schema(description = "the price of this score represented in subunits (e.g. cent)", example = "3995")
    var price: Int? = 3995,
    @Schema(description = "the quantity of this score. only used during an order. during an order not null", example = "2")
    var quantity: Int? = null,
    @Schema(description = "the title of this score", example = "Eine letzte Runde")
    var title: String? = "Eine letzte Runde",
)
