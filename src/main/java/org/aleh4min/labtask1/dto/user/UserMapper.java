package org.aleh4min.labtask1.dto.user;

import org.aleh4min.labtask1.dto.address.AddressMapper;
import org.aleh4min.labtask1.entities.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
uses = {AddressMapper.class})
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(UserRequestDto user);

    @Mapping(target = "id", ignore = true)
    User toUser(UserCreateDto user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserRequestDto dto, @MappingTarget User user);
}
