package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
    @MockBean
    private UserRepository userRepository; // Mock du repository

    @Autowired
    private UserService userService; // Service réel

    @Autowired
    private UserController userController; // Contrôleur réel

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(false);
    }

    @Test
    void testFindById_UserExists() {
        // Arrange : Simuler la réponse du repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act : Appeler la méthode du contrôleur
        ResponseEntity<?> response = userController.findById("1");

        // Assert : Vérifier les résultats
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(user.getEmail(), ((UserDto) response.getBody()).getEmail());

        // Vérifier que le repository a été appelé
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_UserNotFound() {
        // Arrange : Simuler un utilisateur non trouvé
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act : Appeler la méthode du contrôleur
        ResponseEntity<?> response = userController.findById("1");

        // Assert : Vérifier que le code retourne 404
        assertEquals(404, response.getStatusCodeValue());

        // Vérifier que le repository a été appelé
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_InvalidId() {
        // Act : Appeler la méthode avec un ID invalide
        ResponseEntity<?> response = userController.findById("invalid");

        // Assert : Vérifier que le code retourne 400
        assertEquals(400, response.getStatusCodeValue());

        // Vérifier que le repository n'a pas été appelé
        verify(userRepository, times(0)).findById(anyLong());
    }
}
