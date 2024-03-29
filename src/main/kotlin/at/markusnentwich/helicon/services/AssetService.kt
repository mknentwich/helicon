package at.markusnentwich.helicon.services

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(ASSET_SERVICE)
@Tag(name = "Asset Service", description = "The Asset Service provides the ability to perform requests on certain assets such as PDFs of scores or their audio examples.")
interface AssetService {

    @RequestMapping("/scores/{id}/audio", method = [RequestMethod.GET], produces = ["audio/mpeg"])
    @Operation(summary = "return the audio example of a score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "404", description = "no score or audio with such id found")
        ]
    )
    fun getScoreAudio(@Parameter(description = "id of the score") @PathVariable id: Long): ResponseEntity<InputStreamResource>

    @RequestMapping("/scores/{id}/audio", method = [RequestMethod.PUT], consumes = ["audio/ogg", "audio/mp3", "audio/mpeg", "audio/mp4"])
    @Operation(summary = "update the audio example of a score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no score with such id found"),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun updateScoreAudio(@Parameter(description = "id of the score") @PathVariable id: Long, request: HttpServletRequest): ResponseEntity<Void>

    @RequestMapping("/scores/{id}/audio", method = [RequestMethod.DELETE])
    @Operation(summary = "delete an audio example of a score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no score or audio with such if found")
        ]
    )
    fun deleteScoreAudio(@Parameter(description = "id of the score") @PathVariable id: Long): ResponseEntity<Void>

    @RequestMapping("/scores/{id}/pdf", method = [RequestMethod.GET], produces = ["application/pdf"])
    @Operation(summary = "return the PDF of a score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "404", description = "no score or pdf with such id found")
        ]
    )
    fun getScorePdf(@Parameter(description = "id of the score") @PathVariable id: Long): ResponseEntity<InputStreamResource>

    @RequestMapping("/scores/{id}/pdf", method = [RequestMethod.PUT], consumes = ["application/pdf"])
    @Operation(summary = "update the PDF of a score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no score with such id found"),
            ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
        ]
    )
    fun updateScorePdf(@Parameter(description = "id of the score") @PathVariable id: Long, request: HttpServletRequest): ResponseEntity<Void>

    @RequestMapping("/scores/{id}/pdf", method = [RequestMethod.DELETE])
    @Operation(summary = "delete the PDF of a score")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = OK),
            ApiResponse(responseCode = "400", description = BAD_REQUEST),
            ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            ApiResponse(responseCode = "403", description = FORBIDDEN),
            ApiResponse(responseCode = "404", description = "no score with such id found")
        ]
    )
    fun deleteScorePdf(@Parameter(description = "id of the score") @PathVariable id: Long): ResponseEntity<Void>
}
