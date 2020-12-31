package at.markusnentwich.helicon.dto

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Category")
data class CategoryProductDto(
    @Schema(description = "the id of this category", example = "73")
    var id: Long? = null,
    @Schema(description = "the name of this category", example = "Marsch")
    var name: String? = "Marsch",
    @Schema(description = "the plural name of this category", example = "Märsche")
    var namePlural: String? = "Märsche",
    @Schema(
        description = "the parent category. this may be null, if so, this category represents a root node of a category tree",
        required = false,
        writeOnly = true
    )
    @JsonBackReference
    var parent: CategoryProductDto? = null,
    @Schema(description = "the scores which are in this category")
    @JsonManagedReference
    var scores: MutableSet<ScoreProductDto>? = mutableSetOf(),
    @Schema(description = "children of this category")
    @JsonManagedReference
    var children: MutableSet<CategoryProductDto>? = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryProductDto

        if (id != other.id) return false
        if (name != other.name) return false
        if (namePlural != other.namePlural) return false
        if (parent != other.parent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (namePlural?.hashCode() ?: 0)
        return result
    }
}
