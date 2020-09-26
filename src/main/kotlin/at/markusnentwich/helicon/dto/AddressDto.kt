package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Address")
data class AddressDto(
        @Schema(description = "the city part of the address", example = "Leopoldsdorf im Marchfelde")
        val city: String,
        @Schema(description = "the postcode of the city", example = "2285")
        val postCode: String,
        @Schema(description = "the street of the address", example = "Hauptplatz")
        val street: String,
        @Schema(description = "the street number of the address", example = "1")
        val streetNumber: String,
        @Schema(description = "the state of the address")
        val state: StateDto
)