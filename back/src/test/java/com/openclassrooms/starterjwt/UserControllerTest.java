package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.controllers.UserController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(false);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
    }

    // Test


    @Test
    void testFindById_UserExists() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(user.getEmail(), ((UserDto) response.getBody()).getEmail());
    }

    @Test
    void testFindById_UserNotFound() {
        // Arrange
        when(userService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindById_InvalidId() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }





}
