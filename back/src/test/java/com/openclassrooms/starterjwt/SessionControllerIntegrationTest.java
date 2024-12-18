package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class SessionControllerIntegrationTest {

    @MockBean
    private SessionRepository sessionRepository; // Mock du repository

    @MockBean
    private SessionMapper sessionMapper; // Mock du mapper

    @Autowired
    private SessionService sessionService; // Service réel

    @Autowired
    private SessionController sessionController; // Contrôleur réel

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setName("test");
        session.setDescription("description");
        session.setTeacher(new Teacher());
        session.setUsers(new ArrayList<>());

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("test");
        sessionDto.setDescription("description");
    }

    @Test
    void findById_withValidId_returnsSession() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionMapper, times(1)).toDto(session);
    }


    @Test
    void createSession_returnsCreatedSession() {
        // Arrange
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionRepository, times(1)).save(session);
        verify(sessionMapper, times(1)).toDto(session);
    }


}
