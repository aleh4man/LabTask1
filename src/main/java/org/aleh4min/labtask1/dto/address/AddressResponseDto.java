package org.aleh4min.labtask1.dto.address;

public record AddressResponseDto(
        Long id,
        String street,
        int houseNumber,
        int doorNumber
) {}
