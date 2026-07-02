package org.aleh4min.labtask1.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.aleh4min.labtask1.dto.user.UserCreateDto;
import org.aleh4min.labtask1.dto.user.UserRequestDto;
import org.aleh4min.labtask1.dto.user.UserResponseDto;
import org.aleh4min.labtask1.entities.User;
import org.aleh4min.labtask1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public UserResponseDto createUser(UserCreateDto user) {
        /*
         * TODO: валидация данных
         */

        User u = null; //TODO: маппинг (CreateRequest -> Entity)
        userRepository.save(u);

        return null; //TODO: маппинг (Entity -> Response)
    }

    @Transactional
    public UserResponseDto getUserById(long id) {
        Optional<User> optUser = userRepository.findById(id);

        if (optUser.isPresent())
            return null; //TODO: маппинг (Entity -> Response)
        else
            throw new EntityNotFoundException("Пользователь с ID №" + id + " не найден");
    }

    public UserResponseDto updateUser(UserRequestDto user) {
        Optional<User> userOpt = userRepository.findById(user.getId());

        userRepository.save(new User());
        return null; //TODO: маппинг (Entity -> Response)
    }

    public void deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);

    }



    private static class RequestChecker {
        //TODO: валидация данных
    }
}
