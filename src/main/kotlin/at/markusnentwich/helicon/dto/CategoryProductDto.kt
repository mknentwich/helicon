package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Category")
data class CategoryProductDto(
        @Schema(description = "the id of this category", example = "73")
        val id: Int?,
        @Schema(description = "the name of this category", example = "Marsch")
        val name: String?,
        @Schema(description = "the plural name of this category", example = "Märsche")
        val namePlural: String?,
        @Schema(description = "the parent parent category. this may be null, if so, this category represents a root node of a category tree", required = false)
        val parent: CategoryProductDto?,
        @Schema(description = "the scores which are in this category")
        val scores: List<ScoreProductDto>?
)