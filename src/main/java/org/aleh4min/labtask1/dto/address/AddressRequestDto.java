package org.aleh4min.labtask1.dto.address;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddressRequestDto(
        @NotBlank(message = "Street is required")
        String street,

        @Min(value = 1, message = "House number must be positive")
        int houseNumber,

        @Min(value = 1, message = "Door number must be positive")
        int doorNumber
) {}