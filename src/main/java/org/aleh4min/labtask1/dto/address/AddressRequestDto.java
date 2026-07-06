package org.aleh4min.labtask1.dto.address;

import lombok.Data;

@Data
public class AddressRequestDto {
    private String street;
    private int houseNumber;
    private int doorNumber;
}
