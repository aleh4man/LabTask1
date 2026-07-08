package org.aleh4min.labtask1.dto.address;

import org.aleh4min.labtask1.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponseDto toAddressResponseDto(Address addr);

    Address toAddress(AddressRequestDto addr);
}
