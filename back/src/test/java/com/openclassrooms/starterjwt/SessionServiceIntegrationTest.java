package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SessionServiceIntegrationTest {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createAndFindAll_shouldWorkCorrectly() {
        Session session = new Session();
        session.setName("Test Session");
        session.setDescription("Test Description");
        session.setDate(new Date());
        sessionService.create(session);

        List<Session> sessions = sessionService.findAll();

        assertEquals(1, sessions.size());
        assertEquals("Test Session", sessions.get(0).getName());
    }



    @Test
    void testUpdateSession_Success() {
        // Arrange: Crée une session dans la base de données
        Session session = new Session();
        session.setDate(new Date());
        session.setName("Original Name");
        session.setDescription("Original Description");
        session = sessionRepository.save(session);

        Long sessionId = session.getId(); // Récupère l'ID de la session persistée

        // Act: Met à jour la session avec de nouvelles valeurs
        Session updatedSession = new Session();
        updatedSession.setDate(new Date());
        updatedSession.setName("Updated Name");
        updatedSession.setDescription("Updated Description");
        Session result = sessionService.update(sessionId, updatedSession);

        // Assert: Vérifie que les modifications ont été correctement appliquées
        assertNotNull(result);
        assertEquals(sessionId, result.getId()); // L'ID doit rester le même
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void noLongerParticipate_ShouldRemoveUser_WhenUserParticipatesInSession() {
        // Arrange
        Session session = new Session();
        session.setName("Test Session");
        session.setDate(new Date());
        session.setUsers(new ArrayList<>());
        session.setDescription("Test Description");

        session = sessionRepository.save(session);

        User user = userRepository.findById(1L).orElseThrow(NotFoundException::new);

        // Ajouter l'utilisateur à la session
        session.getUsers().add(user);
        sessionRepository.save(session);

        // Act: Retirer l'utilisateur de la session
        sessionService.noLongerParticipate(session.getId(), user.getId());

        // Assert
        Session updatedSession = sessionRepository.findById(session.getId()).orElseThrow(() -> new NotFoundException());
        assertFalse(updatedSession.getUsers().contains(user)); // Vérifier que l'utilisateur n'est plus dans la session
    }

    @Test
    void noLongerParticipate_ShouldThrowNotFoundException_WhenSessionDoesNotExist() {
        // Arrange
        Long nonExistentSessionId = 999L;
        Long userId = 1L;

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(nonExistentSessionId, userId));
    }

    @Test
    void noLongerParticipate_ShouldThrowBadRequestException_WhenUserDoesNotParticipate() {
        // Arrange
        Session session = new Session();
        session.setDescription("Test Description");
        session.setName("Test Session");
        session.setDate(new Date());
        session.setUsers(new ArrayList<>());

        session = sessionRepository.save(session);

        User user = userRepository.findById(1L).orElseThrow(NotFoundException::new);

        // Act & Assert
        Session finalSession = session;
        User finalUser = user;
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(finalSession.getId(), finalUser.getId()));
    }

    @Test
    void update_ShouldUpdateSession_WhenSessionExists() {
        // Arrange: Crée une session initiale
        Session session = new Session();
        session.setName("Original Session");
        session.setDescription("Original Description");
        session.setDate(new Date());
        session = sessionRepository.save(session);

        Long sessionId = session.getId();

        // Act: Mettre à jour les détails de la session
        Session updatedSession = new Session();
        updatedSession.setName("Updated Session");
        updatedSession.setDescription("Updated Description");
        updatedSession.setDate(new Date());

        Session result = sessionService.update(sessionId, updatedSession);

        // Assert: Vérifie que les détails ont été mis à jour
        assertNotNull(result);
        assertEquals(sessionId, result.getId()); // L'ID doit rester inchangé
        assertEquals("Updated Session", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenSessionDoesNotExist() {
        // Arrange: Utiliser un ID qui n'existe pas
        Long nonExistentSessionId = 999L;

        Session session = new Session();
        session.setName("Non-existent Session");
        session.setDescription("This session does not exist");
        session.setDate(new Date());

        // Act & Assert: Vérifie que NotFoundException est levée
        assertThrows(NotFoundException.class, () -> {
            Session existingSession = sessionRepository.findById(nonExistentSessionId)
                    .orElseThrow(NotFoundException::new);
            sessionService.update(existingSession.getId(), session);
        });
    }

    @Test
    void participate_ShouldThrowBadRequestException_WhenUserAlreadyParticipates() {
        // Arrange
        Session session = new Session();
        session.setDescription("Test Description");
        session.setDate(new Date());
        session.setName("Test Session");
        session.setUsers(new ArrayList<>());
        session = sessionRepository.save(session);

        User user = userRepository.findById(1L).orElseThrow(NotFoundException::new);

        // Ajouter l'utilisateur à la session pour simuler qu'il participe déjà
        session.getUsers().add(user);
        sessionRepository.save(session);

        // Act & Assert
        Long sessionId = session.getId();
        Long userId = user.getId();

        assertThrows(
                BadRequestException.class,
                () -> sessionService.participate(sessionId, userId),
                "Expected BadRequestException when user already participates"
        );
    }

}
