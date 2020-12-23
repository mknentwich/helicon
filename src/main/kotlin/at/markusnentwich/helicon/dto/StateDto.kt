package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "State")
data class StateDto(
    @Schema(description = "the id of this state", example = "73")
    var id: Long? = null,
    @Schema(description = "the name of this state", example = "Österreich")
    var name: String = "Austria",
    @Schema(description = "the zone where this states belongs to")
    var zone: ZoneDto = ZoneDto()
)

@Schema(name = "Zone")
data class ZoneDto(
    @Schema(description = "the id of this zone", example = "73")
    var id: Long? = null,
    @Schema(description = "the name of this zone", example = "EU")
    var name: String = "EU",
    @Schema(description = "the shipping of this zone in subunits (e.g. cent)", example = "700")
    var shipping: Int = 700,
    @Schema(description = "the states which belong to that zone")
    var states: List<StateDto>? = null
)
