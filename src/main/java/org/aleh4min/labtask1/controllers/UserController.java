package org.aleh4min.labtask1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.aleh4min.labtask1.dto.user.UserCreateDto;
import org.aleh4min.labtask1.dto.user.UserRequestDto;
import org.aleh4min.labtask1.dto.user.UserResponseDto;
import org.aleh4min.labtask1.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User API", description = "Тест описания документации")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @Operation(summary = "Создать нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @PostMapping
    public @ResponseBody ResponseEntity<?> create(@RequestBody UserCreateDto request) {
        UserResponseDto newUser;
        try {
            newUser = userService.createUser(request);
        }
        catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + ": \n" + request);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("unknown error: " + e.getMessage());
        }

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }



    @Operation(summary = "Найти пользователя по id")
    @ApiResponse(responseCode = "400", description = "Пользователь не найден")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @GetMapping(value = "/{userId}")
    public @ResponseBody ResponseEntity<?> findById(@PathVariable Long userId) {
        UserResponseDto user;

        try {
            user = userService.getUserById(userId);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


        return ResponseEntity.ok(user);
    }



    @Operation(summary = "Обновить пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь обновлён")
    @PatchMapping(value = "/{userId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> update(
            @PathVariable Long userId,
            @RequestBody UserRequestDto request) {
        request.setId(userId);

        UserResponseDto updated = userService.updateUser(request);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }



    @Operation(summary = "Удалить пользователя")
    @ApiResponse(responseCode = "204", description = "Пользователь удалён")
    @DeleteMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> delete(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
