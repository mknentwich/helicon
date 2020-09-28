package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "State")
data class StateDto(
        @Schema(description = "the id of this state", example = "73")
        val id: Long?,
        @Schema(description = "the name of this state", example = "Österreich")
        val name: String,
        @Schema(description = "the zone where this states belongs to")
        val zone: ZoneDto?
)

@Schema(name = "Zone")
data class ZoneDto(
        @Schema(description = "the id of this zone", example = "73")
        val id: Long?,
        @Schema(description = "the name of this zone", example = "EU")
        val name: String,
        @Schema(description = "the shipping of this zone in subunits (e.g. cent)", example = "700")
        val shipping: Int,
        @Schema(description = "the states which belong to that zone")
        val states: List<StateDto>?
)