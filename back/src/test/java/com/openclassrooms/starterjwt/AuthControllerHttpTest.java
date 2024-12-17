package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;  // Ensure this import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;  // Ensure this import

@SpringBootTest  // Load the full application context
@AutoConfigureMockMvc  // Auto-configure MockMvc for the test class
@ActiveProfiles("test")
class AuthControllerHttpTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc will help you simulate HTTP requests

    @Autowired
    private UserRepository userRepository;



    @Test
    void testAuthenticateUser() throws Exception {
        // Perform the POST request to /login
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"yoga@studio.com\", \"password\": \"test!1234\"}"))
                .andExpect(status().isOk())  // Assert that the status code is 200 OK// Assert that the JWT is returned
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("yoga@studio.com"));  // Assert that the username is correct
    }

    @Test
    void testAuthenticateUser_withInvalidCredentials() throws Exception {
        // Perform the POST request to /login with incorrect credentials
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"invalid@studio.com\", \"password\": \"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());  // Assert that the status code is 401 Unauthorized
    }
}
