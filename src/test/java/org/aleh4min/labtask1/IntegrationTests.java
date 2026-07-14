package org.aleh4min.labtask1;

import org.aleh4min.labtask1.dto.address.AddressRequestDto;
import org.aleh4min.labtask1.dto.address.AddressResponseDto;
import org.aleh4min.labtask1.dto.user.UserCreateDto;
import org.aleh4min.labtask1.dto.user.UserRequestDto;
import org.aleh4min.labtask1.dto.user.UserResponseDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class IntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    private UserCreateDto userDto;
    @BeforeEach
    public void setUp() {
        userDto = new UserCreateDto(
                "Петр",
                "Сидоров",
                (short) 25,
                "petr@mail.ru",
                null
        );
    }

    @Test
    public void createUser_withoutAddresses_shouldReturn201() throws Exception {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";

        String json = objectMapper.writeValueAsString(userDto);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        UserResponseDto response = objectMapper.readValue(responseJson, UserResponseDto.class);

        UserResponseDto expected = new UserResponseDto(
                null, firstName, lastName, age, email, null
        );

        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    public void createUser_withAddress_shouldReturn201() throws Exception {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";

        String street = "Пушкина";
        int houseNumber = 2;
        int doorNumber = 67;

        AddressRequestDto addressDto = new AddressRequestDto(
                street,
                houseNumber,
                doorNumber
        );

        UserCreateDto userWithAddress = new UserCreateDto(
                firstName,
                lastName,
                age,
                email,
                List.of(addressDto)
        );

        String json = objectMapper.writeValueAsString(userWithAddress);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        UserResponseDto response = objectMapper.readValue(responseJson, UserResponseDto.class);

        List<AddressResponseDto> expectedAddress = List.of(
                new AddressResponseDto(null, street, houseNumber, doorNumber)
        );

        assertThat(response.addresses())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedAddress);
    }

    @Test
    public void getUserById_existingUser_shouldReturn200() throws Exception {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";

        String createJson = objectMapper.writeValueAsString(userDto);
        MvcResult createResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto createdUser = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                UserResponseDto.class
        );
        Long userId = createdUser.id();

        MvcResult getResult = mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto response = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                UserResponseDto.class
        );

        UserResponseDto expected = new UserResponseDto(
                userId, firstName, lastName, age, email, List.of()
        );

        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void updateUser_allFieldsChanging_shouldReturn200() throws Exception {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 27;
        String email = "petr@mail.ru";

        String newFirstName = "Алексей";
        String newLastName = "Иванов";
        short newAge = 30;
        String newEmail = "alexey@mail.ru";

        String createJson = objectMapper.writeValueAsString(userDto);
        MvcResult createResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto createdUser = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                UserResponseDto.class
        );
        Long userId = createdUser.id();

        UserRequestDto updateDto = new UserRequestDto(
                newFirstName,
                newLastName,
                newAge,
                newEmail,
                null
        );

        String updateJson = objectMapper.writeValueAsString(updateDto);

        MvcResult updateResult = mockMvc.perform(patch("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto updatedUser = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(),
                UserResponseDto.class
        );

        UserResponseDto expected = new UserResponseDto(
                userId, newFirstName, newLastName, newAge, newEmail, null
        );

        assertThat(updatedUser)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void deleteUser_shouldReturn204() throws Exception {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";

        String createJson = objectMapper.writeValueAsString(userDto);
        MvcResult createResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto createdUser = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                UserResponseDto.class
        );
        Long userId = createdUser.id();

        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserById_nonExistentUser_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/users/9999"))
                .andExpect(status().isNotFound());
    }
}
