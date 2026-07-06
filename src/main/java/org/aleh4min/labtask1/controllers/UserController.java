package org.aleh4min.labtask1.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.aleh4min.labtask1.dto.user.UserCreateDto;
import org.aleh4min.labtask1.dto.user.UserRequestDto;
import org.aleh4min.labtask1.dto.user.UserResponseDto;
import org.aleh4min.labtask1.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserCreateDto request) {
        UserResponseDto newUser;
        try {
            newUser = userService.createUser(request);
        }
        catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        UserResponseDto user;

        try {
            user = userService.getUserById(userId);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


        return ResponseEntity.ok(user);
    }

    @PatchMapping(value = "/{userId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long userId,
            @RequestBody UserRequestDto request) {
        request.setId(userId);
        UserResponseDto updated = userService.updateUser(request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
