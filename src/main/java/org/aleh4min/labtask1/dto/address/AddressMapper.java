package org.aleh4min.labtask1.dto.address;

import org.aleh4min.labtask1.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    AddressResponseDto toAddressResponseDto(Address addr);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toAddress(AddressRequestDto addr);
}
