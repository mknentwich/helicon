package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "State")
data class StateDto(
    @Schema(description = "the id of this state", example = "73", readOnly = true)
    var id: Long? = null,
    @Schema(description = "the name of this state", example = "Österreich", required = true)
    var name: String = "Austria",
    @Schema(description = "the zone where this states belongs to", readOnly = true)
    var zone: ZoneDto = ZoneDto(),
    @Schema(description = "the id of the zone", example = "73", writeOnly = true, required = true)
    var zoneId: Long? = null
)

@Schema(name = "Zone")
data class ZoneDto(
    @Schema(description = "the id of this zone", example = "73", readOnly = true)
    var id: Long? = null,
    @Schema(description = "the name of this zone", example = "EU", required = true)
    var name: String = "EU",
    @Schema(description = "the shipping of this zone in subunits (e.g. cent)", example = "700", required = true)
    var shipping: Int = 700,
    @Schema(description = "the states which belong to that zone", readOnly = true)
    var states: List<StateDto>? = null
)
