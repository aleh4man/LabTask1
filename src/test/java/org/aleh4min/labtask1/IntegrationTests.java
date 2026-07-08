package org.aleh4min.labtask1;

import org.aleh4min.labtask1.dto.address.AddressRequestDto;
import org.aleh4min.labtask1.dto.user.UserCreateDto;
import org.aleh4min.labtask1.dto.user.UserRequestDto;
import org.aleh4min.labtask1.dto.user.UserResponseDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class IntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    private UserCreateDto userDto;
    @BeforeEach
    public void createUser() {
        userDto = new UserCreateDto();
        userDto.setFirstName("Петр");
        userDto.setLastName("Сидоров");
        userDto.setAge((short) 25);
        userDto.setEmail("petr@mail.ru");
    }





    @Test
    public void createUser_withoutAddresses_shouldReturn201() {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";



        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(
                "/api/users", userDto, UserResponseDto.class
        );



        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals(firstName, response.getBody().getFirstName());
        Assertions.assertEquals(lastName, response.getBody().getLastName());
        Assertions.assertEquals(age, response.getBody().getAge());
        Assertions.assertEquals(email, response.getBody().getEmail());
        Assertions.assertNull(response.getBody().getAddresses());
    }





    @Test
    public void createUser_withAddress_shouldReturn201() {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";

        String street = "Пушкина";
        int houseNumber = 2;
        int doorNumber = 67;

        AddressRequestDto addressDto = new AddressRequestDto();
        addressDto.setStreet(street);
        addressDto.setHouseNumber(houseNumber);
        addressDto.setDoorNumber(doorNumber);
        userDto.setAddresses(List.of(addressDto));



        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(
                "/api/users", userDto, UserResponseDto.class
        );



        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody().getAddresses());
        Assertions.assertNotNull(response.getBody().getAddresses().getFirst().getId());
        Assertions.assertEquals(street, response.getBody().getAddresses().getFirst().getStreet());
        Assertions.assertEquals(houseNumber, response.getBody().getAddresses().getFirst().getHouseNumber());
        Assertions.assertEquals(doorNumber, response.getBody().getAddresses().getFirst().getDoorNumber());
    }





    @Test
    public void getUserById_existingUser_shouldReturn200() {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";

        String street = "Пушкина";
        int houseNumber = 2;
        int doorNumber = 67;

        AddressRequestDto addressDto = new AddressRequestDto();
        addressDto.setStreet(street);
        addressDto.setHouseNumber(houseNumber);
        addressDto.setDoorNumber(doorNumber);

        userDto.setAddresses(List.of(addressDto));



        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(
                "/api/users", userDto, UserResponseDto.class
        );
        long id = response.getBody().getId();
        response = restTemplate.getForEntity(
                "/api/users/" + id, UserResponseDto.class
        );



        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(id, response.getBody().getId());
        Assertions.assertEquals(firstName, response.getBody().getFirstName());
        Assertions.assertEquals(lastName, response.getBody().getLastName());
        Assertions.assertEquals(age, response.getBody().getAge());
        Assertions.assertEquals(email,  response.getBody().getEmail());
        Assertions.assertNotNull(response.getBody().getAddresses());
    }

    @Test
    public void updateUser_ageChanging_shouldReturn200() {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 27;
        String email = "petr@mail.ru";



        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(
                "/api/users", userDto, UserResponseDto.class
        );

        long id = response.getBody().getId();

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setId(id);
        userRequestDto.setFirstName(response.getBody().getFirstName());
        userRequestDto.setLastName(response.getBody().getLastName());
        userRequestDto.setAge(age);
        userRequestDto.setEmail(response.getBody().getEmail());

        HttpEntity<UserRequestDto> request = new HttpEntity<>(userRequestDto);
        response = restTemplate.exchange(
                "/api/users/" + id,
                HttpMethod.PATCH,
                request,
                UserResponseDto.class
        );



        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.getBody().getId());
        Assertions.assertEquals(firstName, response.getBody().getFirstName());
        Assertions.assertEquals(lastName, response.getBody().getLastName());
        Assertions.assertEquals(age, response.getBody().getAge());
        Assertions.assertEquals(email, response.getBody().getEmail());
    }





    @Test
    public void deleteUser_shouldReturn204() {
        String firstName = "Петр";
        String lastName = "Сидоров";
        short age = 25;
        String email = "petr@mail.ru";



        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(
                "/api/users", userDto, UserResponseDto.class
        );
        long id = response.getBody().getId();

        response = restTemplate.exchange(
                "/api/users/" + id,
                HttpMethod.DELETE,
                null,
                UserResponseDto.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }





    @Test
    public void getUserById_nonExistentUser_shouldReturn404() {
        Long nonExistentId = 9999L;



        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/users/" + nonExistentId,
                String.class
        );



        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
