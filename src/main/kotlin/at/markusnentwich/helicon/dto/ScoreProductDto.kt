package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Score")
data class ScoreProductDto(
    @Schema(description = "the id of this score", example = "73", readOnly = true)
    var id: Long? = null,
    @Schema(description = "author of this score", example = "Markus Nentwich")
    var author: String = "Markus Nentwich",
    @Schema(description = "the id of the category", writeOnly = true)
    var categoryId: Long? = null,
    @Schema(description = "the difficulty of this score", example = "2", minimum = "1", maximum = "5")
    var difficulty: Int = 2,
    @Schema(description = "the group type of this score", example = "Blasorchester")
    var groupType: String = "Blasorchester",
    @Schema(description = "the instrumentation of this score", example = "1. Klarinette in B, 2. Klarinette in B")
    var instrumentation: String = "Posaunen",
    @Schema(description = "the price of this score represented in subunits (e.g. cent)", example = "3995")
    var price: Int? = 3995,
    @Schema(description = "the quantity of this score", example = "2", minimum = "1", readOnly = true, required = false)
    var quantity: Int? = null,
    @Schema(description = "the title of this score", example = "Eine letzte Runde")
    var title: String? = "Eine letzte Runde",
    @Schema(description = "the summary of this score", example = "Eine leichte Kapellenpolka über die Luft im Marchfeld", required = false)
    var summary: String? = null,
    @Schema(description = "the description of the score, comprehensive version of 'summary'", example = "Diese Polka soll an die Luft im Marchfeld erinnern. Daran gearbeitet wurde dabei selbstverständlich nur auf der Terrasse des Elternhauses von Markus Nentwich, wobei immer ein Bier aus Marchfelder Getreide Beistand leistete.", required = false)
    var description: String? = null
)

@Schema(name = "ScoreItem")
data class ScoreItemDto(
    @Schema(description = "the id of the score", example = "73")
    var id: Long = 73,
    @Schema(description = "the quantity of this score", example = "2", minimum = "1")
    var quantity: Int = 0
)
