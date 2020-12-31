package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Address")
data class AddressDto(
    var id: Long? = null,
    @Schema(description = "the city part of the address", example = "Leopoldsdorf im Marchfelde", required = true)
    var city: String = "Leopoldsdorf im Marchfelde",
    @Schema(description = "the postcode of the city", example = "2285", required = true)
    var postCode: String = "2285",
    @Schema(description = "the street of the address", example = "Hauptplatz", required = true)
    var street: String = "Hauptplatz",
    @Schema(description = "the street number of the address", example = "1", required = true)
    var streetNumber: String = "1",
    @Schema(description = "the id of the state", writeOnly = true, required = true)
    var stateId: Long? = null,
    @Schema(description = "the state of the address", readOnly = true, required = false)
    var state: StateDto = StateDto()
)
