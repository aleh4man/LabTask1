package org.aleh4min.labtask1.dto.user;

import lombok.Data;
import org.aleh4min.labtask1.dto.address.AddressResponseDto;

import java.util.List;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        short age,
        String email,
        List<AddressResponseDto> addresses
) {}
