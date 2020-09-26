package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ScoreProductDto(
        @Schema(description = "the id of this score", example = "73")
        val id: Int,
        @Schema(description = "the category of this score")
        val category: CategoryProductDto?,
        @Schema(description = "the difficulty of this score", example = "2", minimum = "1", maximum = "5")
        val difficulty: Int,
        @Schema(description = "the instrumentation of this score", example = "1. Klarinette in B, 2. Klarinette in B")
        val instrumentation: String,
        @Schema(description = "the price of this score represented in subunits (e.g. cent)", example = "3995")
        val price: Int?,
        @Schema(description = "the title of this score")
        val title: String?,
)