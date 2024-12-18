package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TeacherControllerIntegrationTest {
    @MockBean
    private TeacherRepository teacherRepository; // Mock du repository

    @MockBean
    private TeacherMapper teacherMapper; // Mock du mapper

    @Autowired
    private TeacherService teacherService; // Service réel

    @Autowired
    private TeacherController teacherController; // Contrôleur réel

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
    }



    @Test
    void findById_withValidId_returnsTeacher() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Act
        ResponseEntity<?> response = teacherController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherMapper, times(1)).toDto(teacher);
    }

    @Test
    void findById_withInvalidId_returnsNotFound() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = teacherController.findById("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherRepository, times(1)).findById(1L);
        verifyNoInteractions(teacherMapper); // Aucun appel au mapper
    }

}
