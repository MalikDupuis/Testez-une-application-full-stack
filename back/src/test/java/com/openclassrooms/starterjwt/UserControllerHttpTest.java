package com.openclassrooms.starterjwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest  // Load the full application context
@AutoConfigureMockMvc  // Auto-configure MockMvc for the test class
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerHttpTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    @Autowired
    private UserRepository userRepository;

    // Authentification préalable pour récupérer le JWT
    @BeforeEach
    public void setUp() throws Exception {
        String loginPayload = "{\"email\": \"yoga@studio.com\", \"password\": \"test!1234\"}";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"yoga@studio.com\", \"password\": \"test!1234\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        // Analysez la réponse JSON et extrayez le token
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode tokenNode = rootNode.get("token");
        jwtToken = tokenNode.asText();
        System.out.println("coucouccccccc" + jwtToken);



    }



    @Test
    @Order(1)
    public void testDeleteUserNotFound() throws Exception {
        // Essayer de supprimer un utilisateur qui n'existe pas
        long nonExistingUserId = 999L;

        mockMvc.perform(delete("/api/user/{id}", nonExistingUserId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    public void testDeleteUserUnauthorized() throws Exception {
        // Essayer de supprimer un utilisateur avec un autre utilisateur
        long userId = 1L;  // ID de l'utilisateur à supprimer
        String invalidToken = "invalidToken";  // Simuler un token non valide

        mockMvc.perform(delete("/api/user/{id}", userId)
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    public void testDeleteUserBadRequest() throws Exception {
        // Essayer de supprimer un utilisateur avec un id invalide (par exemple, une chaîne de caractères au lieu d'un entier)
        mockMvc.perform(delete("/api/user/{id}", "invalidId")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void testDeleteUserSuccessfully() throws Exception {

        // Simuler un utilisateur qui existe dans la base
        long userId = 1L;  // L'ID d'un utilisateur préexistant
        mockMvc.perform(delete("/api/user/{id}", userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}
