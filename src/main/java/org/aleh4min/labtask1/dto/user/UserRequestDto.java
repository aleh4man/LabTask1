package org.aleh4min.labtask1.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.aleh4min.labtask1.dto.address.AddressRequestDto;

import java.util.List;

public record UserRequestDto(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Min(value = 1, message = "Age must be greater than 0")
        @Max(value = 150, message = "Age must be less than 150")
        short age,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Valid
        List<AddressRequestDto> addresses
) {}