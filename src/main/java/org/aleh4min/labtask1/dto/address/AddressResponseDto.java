package org.aleh4min.labtask1.dto.address;

import lombok.Data;

@Data
public class AddressResponseDto {
    private Long id;
    private String street;
    private int houseNumber;
    private int doorNumber;
}
