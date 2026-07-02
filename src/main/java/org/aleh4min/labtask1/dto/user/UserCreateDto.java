package org.aleh4min.labtask1.dto.user;

import lombok.Data;

@Data
public class UserCreateDto {
    private String firstName;
    private String lastName;
    private byte age;
    private String email;
}
