package org.aleh4min.labtask1.dto.user;

import org.aleh4min.labtask1.dto.address.AddressMapper;
import org.aleh4min.labtask1.entities.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
uses = {AddressMapper.class})
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    User toUser(UserRequestDto user);

    @Mapping(source = "user.addresses", target = "addresses")
    User toUser(UserCreateDto user);
}
