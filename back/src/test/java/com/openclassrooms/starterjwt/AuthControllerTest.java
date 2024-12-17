package com.openclassrooms.starterjwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {


    @MockBean
    private UserRepository userRepositoryMock;

    @MockBean
    private PasswordEncoder passwordEncoderMock;

    @Autowired
    private AuthController authController;


    @Test
    void testRegisterUser_Success() {
        // Mock objects
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("dpsss.malik@yahoo.fr");
        signupRequest.setPassword("dpsss.malik@yahoo.fr");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Test");

        when(userRepositoryMock.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoderMock.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // Invoke the method
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        // Assert
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
        verify(userRepositoryMock, times(1)).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoderMock, times(1)).encode(signupRequest.getPassword());
    }

    @Test
    void testRegisterUser_EmailAlreadyTaken() {
        // Mock objects
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("dpsss.malik@yahoo.fr");
        signupRequest.setPassword("dpsss.malik@yahoo.fr");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Test");


        when(userRepositoryMock.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // Invoke the method
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }
}
