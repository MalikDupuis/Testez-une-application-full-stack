package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TeacherControllerTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherController teacherController;

    @Test
    void findById_withValidId_returnsTeacher() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        TeacherDto teacherDto = new TeacherDto(); // Mocked DTO object
        teacherDto.setId(id);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
        verify(teacherService, times(1)).findById(id);
        verify(teacherMapper, times(1)).toDto(teacher);
    }


    @Test
    void findAll_returnsListOfTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher());
        List<TeacherDto> teacherDtos = new ArrayList<>();
        TeacherDto teacher = new TeacherDto();
        teacherDtos.add(teacher);// Mocked DTOs

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDtos, response.getBody());
        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    }
}
