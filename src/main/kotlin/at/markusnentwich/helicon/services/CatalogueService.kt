package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.CategoryProductDto
import at.markusnentwich.helicon.dto.ScoreProductDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/catalogue", produces = ["application/json"])
@Tag(name = "Catalogue Service", description = "This service allows to view and modify all available products and categories.")
interface CatalogueService {

    @RequestMapping("/", method = [RequestMethod.GET])
    @Operation(summary = "return the whole catalogue which is represented by an imaginary root category")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = CategoryProductDto::class))])
    )
    fun getCatalogue(): ResponseEntity<CategoryProductDto>

    @RequestMapping("/category/{id}", method = [RequestMethod.GET])
    @Operation(summary = "Returns the category with the provided id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = CategoryProductDto::class))]),
            ApiResponse(responseCode = "404", description = "no category with such id found")
        ]
    )
    fun getCategory(
        @Parameter(description = "id of the category") @PathVariable id: Long,
        @Parameter(description = "set to true if scores should be embedded", required = false) @RequestParam(defaultValue = "false") embed: Boolean
    ): ResponseEntity<CategoryProductDto>

    @RequestMapping("/category", method = [RequestMethod.POST])
    @Operation(summary = "create a new category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = CategoryProductDto::class))]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun createCategory(@Parameter(description = "the new category") @RequestBody category: CategoryProductDto): ResponseEntity<CategoryProductDto>

    @RequestMapping("/category/{id}", method = [RequestMethod.PUT])
    @Operation(summary = "create a new category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = CategoryProductDto::class))]),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no category with such id found"),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun updateCategory(
        @Parameter(description = "the category") @RequestBody category: CategoryProductDto,
        @Parameter(description = "id of the category") @PathVariable id: Long
    ): ResponseEntity<CategoryProductDto>

    @RequestMapping("/category/{id}", method = [RequestMethod.DELETE])
    @Operation(summary = "delete a category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no category with such id found"),
        ]
    )
    fun deleteCategory(@Parameter(description = "id of the category") @PathVariable id: Long): ResponseEntity<Void>

    @RequestMapping("/score", method = [RequestMethod.GET])
    @Operation(summary = "return all scores which are available to buy")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = ScoreProductDto::class))])

    )
    fun getScores(): ResponseEntity<Iterable<ScoreProductDto>>

    @RequestMapping("/score/{id}", method = [RequestMethod.GET])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = ScoreProductDto::class))]),
            ApiResponse(responseCode = "404", description = "no score with such id found")
        ]
    )
    fun getScoreById(@Parameter(description = "id of the score") @PathVariable id: Long): ResponseEntity<ScoreProductDto>

    @RequestMapping("/score", method = [RequestMethod.POST])
    @Operation(summary = "create a new score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun createScore(@Parameter(description = "the new score") @RequestBody score: ScoreProductDto): ResponseEntity<ScoreProductDto>

    @RequestMapping("/score/{id}", method = [RequestMethod.PUT])
    @Operation(summary = "update an existing score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no score found with such id"),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun updateScore(@Parameter(description = "id of the score") @PathVariable id: Long): ResponseEntity<ScoreProductDto>

    @RequestMapping("/score/{id}", method = [RequestMethod.DELETE])
    @Operation(summary = "delete a score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no score with such id found")
        ]
    )
    fun deleteScore(@Parameter(description = "id of the score") @PathVariable id: Long): ResponseEntity<Void>
}
