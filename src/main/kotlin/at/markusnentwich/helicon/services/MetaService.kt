package at.markusnentwich.helicon.services

import at.markusnentwich.helicon.dto.StateDto
import at.markusnentwich.helicon.dto.ZoneDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("/meta")
@Tag(name = "Meta Service", description = "The meta Service manages all data which does not belong to an ordering process directly such as states and zones.")
interface MetaService {

    @RequestMapping("/state", method = [RequestMethod.GET])
    @Operation(summary = "returns all states")
    @ApiResponses(
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = Iterable::class))])
    )
    fun getAllStates(): ResponseEntity<Iterable<StateDto>>

    @RequestMapping("/state/{id}", method = [RequestMethod.GET])
    @Operation(summary = "returns a state by id")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = StateDto::class))]),
                ApiResponse(responseCode = "404", description = "no state found with such id")
            ]
    )
    fun getStateById(@Parameter(description = "the id of the state") @PathVariable id: Int): ResponseEntity<StateDto>

    @RequestMapping("/state", method = [RequestMethod.POST])
    @Operation(summary = "create a new state")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = StateDto::class))]),
                ApiResponse(responseCode = "400", description = BAD_REQUEST),
                ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                ApiResponse(responseCode = "403", description = FORBIDDEN),
                ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
            ]
    )
    fun createState(@Parameter(description = "the new state") @RequestBody state: StateDto): ResponseEntity<StateDto>

    @RequestMapping("/state/{id}", method = [RequestMethod.PUT])
    @Operation(summary = "update a state")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = StateDto::class))]),
                ApiResponse(responseCode = "400", description = BAD_REQUEST),
                ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                ApiResponse(responseCode = "403", description = FORBIDDEN),
                ApiResponse(responseCode = "404", description = "no state found with such id"),
                ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
            ]
    )
    fun updateState(@Parameter(description = "the state to update") @RequestBody state: StateDto,
                    @Parameter(description = "id of the state") @PathVariable id: Int): ResponseEntity<StateDto>

    @RequestMapping("/state/{id}", method = [RequestMethod.DELETE])
    @Operation(summary = "deletes an existing state")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK),
                ApiResponse(responseCode = "400", description = BAD_REQUEST),
                ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                ApiResponse(responseCode = "403", description = FORBIDDEN),
                ApiResponse(responseCode = "404", description = "no state with such id found")
            ]
    )
    fun deleteState(@Parameter(description = "the id of the state") @PathVariable id: Int)

    @RequestMapping("/zone", method = [RequestMethod.GET])
    @Operation(summary = "returns all zones")
    @ApiResponses(
            ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = Iterable::class))])
    )
    fun getAllZones(): ResponseEntity<Iterable<ZoneDto>>

    @RequestMapping("/zone/{id}", method = [RequestMethod.GET])
    @Operation(summary = "returns a zone by id")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = ZoneDto::class))]),
                ApiResponse(responseCode = "404", description = "no zone with such id found")
            ]
    )
    fun getZoneById(@Parameter(description = "the id of the zone") @PathVariable id: Int): ResponseEntity<ZoneDto>

    @RequestMapping("/zone", method = [RequestMethod.POST])
    @Operation(summary = "insert a new zone")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = ZoneDto::class))]),
                ApiResponse(responseCode = "400", description = BAD_REQUEST),
                ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                ApiResponse(responseCode = "403", description = FORBIDDEN),
                ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY)
            ]
    )
    fun createZone(@Parameter(description = "the new zone") @RequestBody zone: ZoneDto): ResponseEntity<ZoneDto>

    @RequestMapping("/zone/{id}", method = [RequestMethod.PUT])
    @Operation(summary = "update a zone")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK, content = [Content(schema = Schema(implementation = ZoneDto::class))]),
                ApiResponse(responseCode = "400", description = BAD_REQUEST),
                ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                ApiResponse(responseCode = "403", description = FORBIDDEN),
                ApiResponse(responseCode = "404", description = "no zone with such id found")
            ]
    )
    fun updateZone(@Parameter(description = "the zone to update") @RequestBody zone: ZoneDto,
                   @Parameter(description = "id of the zone") @PathVariable id: Int): ResponseEntity<ZoneDto>

    @RequestMapping("/zone/{id}", method = [RequestMethod.DELETE])
    @Operation(summary = "delete an existing zone")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = OK),
                ApiResponse(responseCode = "400", description = BAD_REQUEST),
                ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                ApiResponse(responseCode = "403", description = FORBIDDEN),
                ApiResponse(responseCode = "404", description = "no zone with such id found")
            ]
    )
    fun deleteZone(@Parameter(description = "the id of the zone") @PathVariable id: Int)
}