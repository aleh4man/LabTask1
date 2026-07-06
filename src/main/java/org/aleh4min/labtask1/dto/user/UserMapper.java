package org.aleh4min.labtask1.dto.user;

import org.aleh4min.labtask1.entities.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "addresses", source = "addresses")
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", source = "addresses")
    User toUser(UserRequestDto user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", source = "addresses")
    User toUser(UserCreateDto user);
}
