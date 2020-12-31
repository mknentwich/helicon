package at.markusnentwich.helicon.dto

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Category")
data class CategoryProductDto(
    @Schema(description = "the id of this category", example = "73", readOnly = true)
    var id: Long? = null,
    @Schema(description = "the name of this category", example = "Marsch", required = true)
    var name: String? = "Marsch",
    @Schema(description = "the plural name of this category", example = "Märsche", required = true)
    var namePlural: String? = "Märsche",
    @Schema(
        description = "the parent category. this may be null, if so, this category represents a root node of a category tree",
        required = false,
        writeOnly = true
    )
    @JsonBackReference
    var parent: CategoryProductDto? = null,
    @Schema(description = "the id of the parent category", required = false, writeOnly = true)
    var parentId: Long? = null,
    @Schema(description = "the scores which are in this category", readOnly = true)
    @JsonManagedReference
    var scores: MutableSet<ScoreProductDto>? = mutableSetOf(),
    @Schema(description = "children of this category", readOnly = true)
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
