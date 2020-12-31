package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Identity")
data class IdentityDto(
    @Schema(description = "the id of this identity", example = "73", readOnly = true)
    var id: Long? = null,
    @Schema(description = "salutation of this identity", example = "Herr", required = false)
    var salutation: String? = null,
    @Schema(description = "the first name of this identity", example = "Markus", required = true)
    var firstName: String = "Markus",
    @Schema(description = "the last name of this identity", example = "Nentwich", required = true)
    var lastName: String = "Nentwich",
    @Schema(description = "the company or society of this identity", example = "Musikverein Leopoldsdorf", required = false)
    var company: String? = null,
    @Schema(description = "the email address of this identity", example = "nentwich94@gmx.at", required = true)
    var email: String = "nentwich94@gmx.at",
    @Schema(description = "the telephone number of this identity. this attribute does not have any specific format as it is used only in human readable tests such as bills", example = "004366446382168", required = true)
    var telephone: String = "004366479826501",
    @Schema(description = "the address of this identity", required = true)
    var address: AddressDto = AddressDto()
)
