package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Address")
data class AddressDto(
    var id: Long? = null,
    @Schema(description = "the city part of the address", example = "Leopoldsdorf im Marchfelde")
    var city: String = "Leopoldsdorf im Marchfelde",
    @Schema(description = "the postcode of the city", example = "2285")
    var postCode: String = "2285",
    @Schema(description = "the street of the address", example = "Hauptplatz")
    var street: String = "Hauptplatz",
    @Schema(description = "the street number of the address", example = "1")
    var streetNumber: String = "1",
    @Schema(description = "the state of the address")
    var state: StateDto = StateDto()
)
