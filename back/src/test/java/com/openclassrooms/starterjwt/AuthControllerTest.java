package com.openclassrooms.starterjwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.controllers.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Mock objects
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("dpsss.malik@yahoo.fr");
        signupRequest.setPassword("dpsss.malik@yahoo.fr");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Test");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // Invoke the method
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
    }

    @Test
    void testRegisterUser_EmailAlreadyTaken() {
        // Mock objects
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("dpsss.malik@yahoo.fr");
        signupRequest.setPassword("dpsss.malik@yahoo.fr");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Test");


        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // Invoke the method
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }
}
