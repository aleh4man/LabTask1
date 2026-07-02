package org.aleh4min.labtask1.dto.user;

import lombok.Data;
import org.aleh4min.labtask1.dto.address.AddressRequestDto;

import java.util.List;

@Data
public class UserRequestDto {
    private Long id;
    private String firstName;
    private String lastName;
    private byte age;
    private String email;
    private List<AddressRequestDto> addresses;
}
