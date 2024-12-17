package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity_withValidUserDto() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setAdmin(true);
        userDto.setPassword("password");

        // Act
        User user = userMapper.toEntity(userDto);

        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertTrue(user.isAdmin());
    }

    @Test
    void testToEntity_withNullUserDto() {
        // Act
        User user = userMapper.toEntity((UserDto) null);

        // Assert
        assertNull(user);
    }

    @Test
    void testToDto_withValidUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        user.setAdmin(false);
        user.setPassword("password");

        // Act
        UserDto userDto = userMapper.toDto(user);

        // Assert
        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("Jane", userDto.getFirstName());
        assertEquals("Smith", userDto.getLastName());
        assertEquals("jane.smith@example.com", userDto.getEmail());
        assertFalse(userDto.isAdmin());
    }

    @Test
    void testToDto_withNullUser() {
        // Act
        UserDto userDto = userMapper.toDto((User) null);

        // Assert
        assertNull(userDto);
    }
}
