package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Address")
data class AddressDto(
    var id: Long?,
    @Schema(description = "the city part of the address", example = "Leopoldsdorf im Marchfelde")
    var city: String,
    @Schema(description = "the postcode of the city", example = "2285")
    var postCode: String,
    @Schema(description = "the street of the address", example = "Hauptplatz")
    var street: String,
    @Schema(description = "the street number of the address", example = "1")
    var streetNumber: String,
    @Schema(description = "the state of the address")
    var state: StateDto
)
