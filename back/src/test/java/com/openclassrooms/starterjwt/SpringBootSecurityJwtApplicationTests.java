package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class SpringBootSecurityJwtApplicationTests {

	@Autowired
	private TeacherController teacherController;

	@Autowired
	private TeacherService teacherService;


	@Test
	public void contextLoads() {
	}

	@Test
	void testFindAllLastNameAndFirstName() {
		// Arrange
		List<String> expectedFirstNames = Arrays.asList("Margot", "Helene");
		List<String> expectedLastNames = Arrays.asList("DELAHAYE", "THIERCELIN");

		// Act
		ResponseEntity<?> responseEntity = teacherController.findAll();
		@SuppressWarnings("unchecked")
		List<TeacherDto> actualTeachers = (List<TeacherDto>) responseEntity.getBody();

		// Extract actual first and last names
		List<String> actualFirstNames = actualTeachers.stream()
				.map(TeacherDto::getFirstName)
				.collect(Collectors.toList());
		List<String> actualLastNames = actualTeachers.stream()
				.map(TeacherDto::getLastName)
				.collect(Collectors.toList());

		// Assert
		assertEquals(expectedFirstNames, actualFirstNames, "First names do not match");
		assertEquals(expectedLastNames, actualLastNames, "Last names do not match");
	}

}
