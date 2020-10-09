package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Category")
data class CategoryProductDto(
        @Schema(description = "the id of this category", example = "73")
        var id: Long?,
        @Schema(description = "the name of this category", example = "Marsch")
        var name: String?,
        @Schema(description = "the plural name of this category", example = "Märsche")
        var namePlural: String?,
        @Schema(description = "the parent parent category. this may be null, if so, this category represents a root node of a category tree", required = false)
        var parent: CategoryProductDto?,
        @Schema(description = "the scores which are in this category")
        var scores: List<ScoreProductDto>?
)