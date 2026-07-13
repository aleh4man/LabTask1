package org.aleh4min.labtask1.services;

import lombok.RequiredArgsConstructor;
import org.aleh4min.labtask1.dto.user.*;
import org.aleh4min.labtask1.entities.Address;
import org.aleh4min.labtask1.entities.User;
import org.aleh4min.labtask1.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto createUser(UserCreateDto userDto) {
        User user = userMapper.toUser(userDto);

        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            for (Address address : user.getAddresses()) {
                address.setUser(user);
            }
        }

        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с ID №%d не найден", id)
                ));
        return userMapper.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с ID №%d не найден", id)
                ));

        userMapper.updateUserFromDto(userRequestDto, user);

        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            for (Address address : user.getAddresses()) {
                address.setUser(user);
            }
        }
        userRepository.save(user);

        return userMapper.toUserResponseDto(user);
    }

    @Transactional
    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    String.format("Пользователь с ID №%d не найден", id)
            );
        }
        userRepository.deleteById(id);
    }
}