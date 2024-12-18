package com.openclassrooms.starterjwt;


import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }

    @Test
    void testToEntity_withValidSessionDto() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(List.of(2L, 3L));

        // Mock TeacherService
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        when(teacherService.findById(1L)).thenReturn(teacher);

        // Mock UserService
        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);
        when(userService.findById(2L)).thenReturn(user1);
        when(userService.findById(3L)).thenReturn(user2);

        // Act
        Session session = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(session);
        assertEquals("Test Description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(2, session.getUsers().size());
        assertTrue(session.getUsers().contains(user1));
        assertTrue(session.getUsers().contains(user2));

        verify(teacherService, times(1)).findById(1L);
        verify(userService, times(1)).findById(2L);
        verify(userService, times(1)).findById(3L);
    }

    @Test
    void testToEntity_withNullTeacherAndUsers() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Description");

        // Act
        Session session = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(session);
        assertEquals("Test Description", session.getDescription());
        assertNull(session.getTeacher());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void testToDto_withValidSession() {
        // Arrange
        Session session = new Session();
        session.setDescription("Test Description");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);
        session.setUsers(List.of(user1, user2));

        // Act
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assert
        assertNotNull(sessionDto);
        assertEquals("Test Description", sessionDto.getDescription());
        assertEquals(1L, sessionDto.getTeacher_id());
        assertEquals(2, sessionDto.getUsers().size());
        assertTrue(sessionDto.getUsers().contains(2L));
        assertTrue(sessionDto.getUsers().contains(3L));
    }

    @Test
    void testToDto_withNullUsers() {
        // Arrange
        Session session = new Session();
        session.setDescription("Test Description");

        // Act
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assert
        assertNotNull(sessionDto);
        assertEquals("Test Description", sessionDto.getDescription());
        assertNull(sessionDto.getTeacher_id());
        assertTrue(sessionDto.getUsers().isEmpty());
    }
}
