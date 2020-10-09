package at.markusnentwich.helicon.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Identity")
data class IdentityDto(
        @Schema(description = "the id of this identity", example = "73")
        var id: Long?,
        @Schema(description = "the first name of this identity", example = "Markus")
        var firstName: String,
        @Schema(description = "the last name of this identity", example = "Nentwich")
        var lastName: String,
        @Schema(description = "the email address of this identity", example = "nentwich94@gmx.at")
        var email: String,
        @Schema(description = "the telephone number of this identity. this attribute does not have any specific format as it is used only in human readable tests such as bills", example = "004366446382168")
        var telephone: String,
        @Schema(description = "the address of this identity")
        var address: AddressDto
)