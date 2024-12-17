package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class SessionControllerTest {

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Autowired
    private SessionController sessionController;


    @Test
    void findById_withValidId_returnsSession() {
        Long id = 1L;
        Session session = new Session();
        session.setId(id);
        session.setName("test");
        session.setDescription("description");
        session.setTeacher(new Teacher());
        session.setUsers(new ArrayList<>());
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName("test");
        sessionDto.setDescription("description");


        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionService, times(1)).getById(id);
        verify(sessionMapper, times(1)).toDto(session);
    }


    @Test
    void findAll_returnsListOfSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        List<SessionDto> sessionDtos = new ArrayList<>();

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());
        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(sessions);
    }

    @Test
    void createSession_returnsCreatedSession() {
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionService, times(1)).create(session);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    void updateSession_withValidId_returnsUpdatedSession() {
        Long id = 1L;
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update(String.valueOf(id), sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionService, times(1)).update(id, session);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    void deleteSession_withValidId_returnsOk() {
        Long id = 1L;
        Session session = new Session();

        when(sessionService.getById(id)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).delete(id);
    }

    @Test
    void deleteSession_withInvalidId_returnsBadRequest() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void participate_withValidIds_returnsOk() {
        Long sessionId = 1L;
        Long userId = 2L;

        ResponseEntity<?> response = sessionController.participate(String.valueOf(sessionId), String.valueOf(userId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).participate(sessionId, userId);
    }

    @Test
    void participate_withInvalidIds_returnsBadRequest() {
        ResponseEntity<?> response = sessionController.participate("invalid", "invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    void noLongerParticipate_withValidIds_returnsOk() {
        Long sessionId = 1L;
        Long userId = 2L;

        ResponseEntity<?> response = sessionController.noLongerParticipate(String.valueOf(sessionId), String.valueOf(userId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
    }

    @Test
    void noLongerParticipate_withInvalidIds_returnsBadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }
}
