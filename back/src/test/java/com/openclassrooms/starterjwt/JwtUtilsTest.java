package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void generateJwtToken_shouldReturnValidToken() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "testUser", "testUser", false, "vrzgrg");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertDoesNotThrow(() -> Jwts.parser().setSigningKey("openclassrooms").parseClaimsJws(token));
    }

    @Test
    void getUserNameFromJwtToken_shouldReturnUsername() {
        // Arrange
        String username = "testUser";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 140000))
                .signWith(SignatureAlgorithm.HS512, "openclassrooms")
                .compact();

        // Act
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateJwtToken_shouldReturnTrueForValidToken() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 140000))
                .signWith(SignatureAlgorithm.HS512, "openclassrooms\"")
                .compact();

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalseForExpiredToken() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000)) // 2 hours ago
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // Expired 1 hour ago
                .signWith(SignatureAlgorithm.HS512, "openClassRoom")
                .compact();

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalseForMalformedToken() {
        // Arrange
        String malformedToken = "thisIsNotAValidToken";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalseForInvalidSignature() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 140000))
                .signWith(SignatureAlgorithm.HS512, "wrongSecretKey")
                .compact();

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }
}
