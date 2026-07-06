package org.aleh4min.labtask1.dto.user;

import lombok.Data;
import org.aleh4min.labtask1.dto.address.AddressResponseDto;

import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private short age;
    private String email;
    private List<AddressResponseDto> addresses;
}
