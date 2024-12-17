package com.openclassrooms.starterjwt;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SessionServiceTestIntegration {
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
        sessionService.create(session);

        List<Session> sessions = sessionService.findAll();

        assertEquals(1, sessions.size());
        assertEquals("Test Session", sessions.get(0).getName());
    }

    @Test
    void participateAndNoLongerParticipate_shouldWorkCorrectly() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());
        session = sessionRepository.save(session);

        User user = new User();
        user = userRepository.save(user);

        sessionService.participate(session.getId(), user.getId());
        session = sessionRepository.findById(session.getId()).orElseThrow(() -> new NotFoundException());

        assertTrue(session.getUsers().contains(user));

        sessionService.noLongerParticipate(session.getId(), user.getId());
        session = sessionRepository.findById(session.getId()).orElseThrow(() -> new NotFoundException());

        assertFalse(session.getUsers().contains(user));
    }
}
