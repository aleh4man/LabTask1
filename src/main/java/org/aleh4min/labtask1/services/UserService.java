package org.aleh4min.labtask1.services;

import org.aleh4min.labtask1.dto.address.AddressRequestDto;
import org.aleh4min.labtask1.dto.user.*;
import org.aleh4min.labtask1.entities.User;
import org.aleh4min.labtask1.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository ur, UserMapper um) {
        userRepository = ur;
        userMapper = um;
    }

    @Transactional
    public UserResponseDto createUser(UserCreateDto userDto) {
        RequestValidator.validateUser(userDto);

        if (userDto.getAddresses() != null && !userDto.getAddresses().isEmpty()) {
            for (AddressRequestDto addressDto : userDto.getAddresses()) {
                RequestValidator.validateAddress(addressDto);
            }
        }

        User u = userMapper.toUser(userDto);
        userRepository.save(u);

        return userMapper.toUserResponseDto(u);
    }

    @Transactional
    public UserResponseDto getUserById(long id) {
        Optional<User> optUser = userRepository.findById(id);

        if (optUser.isPresent())
            return userMapper.toUserResponseDto(optUser.get());
        else
            throw new EntityNotFoundException("Пользователь с ID №" + id + " не найден");
    }

    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        Optional<User> userOpt = userRepository.findById(userRequestDto.getId());

        User user;
        if (userOpt.isEmpty()) throw new EntityNotFoundException("Пользователь не найден");
        else user = userOpt.get();

        RequestValidator.validateUser(userRequestDto);

        if (userRequestDto.getAddresses() != null && !userRequestDto.getAddresses().isEmpty()) {
            for (AddressRequestDto addressDto : userRequestDto.getAddresses()) {
                RequestValidator.validateAddress(addressDto);
            }
        }

        user = userMapper.toUser(userRequestDto);
        userRepository.save(user);

        return userMapper.toUserResponseDto(user);
    }

    public void deleteUser(long id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) throw new EntityNotFoundException("Пользователь не найден");

        userRepository.deleteById(id);
    }





    private static class RequestValidator {
        public static void validateUser(UserCreateDto userRequestDto) {
            if (!(validateString(userRequestDto.getFirstName())
                    && validateString(userRequestDto.getLastName())))
                throw new IllegalStateException("First or last name is empty");

            if (!validateEmail(userRequestDto.getEmail())) {
                throw new IllegalStateException("Incorrect email");
            }

            if (!validateAge(userRequestDto.getAge())) {
                throw new IllegalStateException("Incorrect age");
            }
        }

        public static void validateUser(UserRequestDto userRequestDto) {
            if (!(validateString(userRequestDto.getFirstName())
                    && validateString(userRequestDto.getLastName())))
                throw new IllegalStateException("First or last name is empty");

            if (!validateEmail(userRequestDto.getEmail())) {
                throw new IllegalStateException("Incorrect email");
            }

            if (!validateAge(userRequestDto.getAge())) {
                throw new IllegalStateException("Incorrect age");
            }
        }

        public static void validateAddress(AddressRequestDto addressRequestDto) {
            if (!(validateString(addressRequestDto.getStreet())))
                throw new IllegalStateException("Street is empty");

            if (!(validateNumber(addressRequestDto.getHouseNumber())))
                throw new IllegalStateException("Incorrect number of house");

            if (!(validateNumber(addressRequestDto.getDoorNumber())))
                throw new IllegalStateException("Incorrect number of door");
        }


        private static boolean validateString(String name) {
            return (name != null && !name.trim().isEmpty());
        }

        private static boolean validateEmail(String email) {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }
            String emailRegex = "^(?!.*\\.\\.)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            return Pattern.matches(emailRegex, email.trim());
        }

        private static boolean validateAge(int age) {
            return (age > 0) && (age < 150);
        }
        private static boolean validateNumber(int number) {return number > 0;}
    }
}
