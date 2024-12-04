package com.homestat.registration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationRequestTest {

    @Test
    void testAllFieldsAreSetCorrectly() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password123";
        String role = "USER";

        // Act
        RegistrationRequest request = new RegistrationRequest(firstName, lastName, email, password, role);

        // Assert
        assertEquals(firstName, request.firstName());
        assertEquals(lastName, request.lastName());
        assertEquals(email, request.email());
        assertEquals(password, request.password());
        assertEquals(role, request.role());
    }

    @Test
    void testToString() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("John", "Doe", "john.doe@example.com", "password123", "USER");

        // Act
        String requestString = request.toString();

        // Assert
        assertNotNull(requestString);
        assertTrue(requestString.contains("John"));
        assertTrue(requestString.contains("Doe"));
        assertTrue(requestString.contains("john.doe@example.com"));
        assertTrue(requestString.contains("password123"));
        assertTrue(requestString.contains("USER"));
    }

    @Test
    void testEquality() {
        // Arrange
        RegistrationRequest request1 = new RegistrationRequest("John", "Doe", "john.doe@example.com", "password123", "USER");
        RegistrationRequest request2 = new RegistrationRequest("John", "Doe", "john.doe@example.com", "password123", "USER");

        // Act & Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testInequality() {
        // Arrange
        RegistrationRequest request1 = new RegistrationRequest("John", "Doe", "john.doe@example.com", "password123", "USER");
        RegistrationRequest request2 = new RegistrationRequest("Jane", "Smith", "jane.smith@example.com", "password456", "ADMIN");

        // Act & Assert
        assertNotEquals(request1, request2);
    }
}
