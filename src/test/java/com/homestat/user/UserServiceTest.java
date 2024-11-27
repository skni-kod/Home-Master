package com.homestat.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Create a mock UserRepository
        userRepository = mock(UserRepository.class);

        // Inject the mock repository into the UserService
        userService = new UserService(userRepository);
    }

    @Test
    void testGetUsers_ReturnsListOfUsers() {
        // Arrange
        User user1 = new User(1L, "Adam", "Brown", "adambrown@test.com", "password", "USER", false);
        User user2 = new User(2L, "eve", "Smith", "evesmith@test.com", "password", "ADMIN", true);
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> users = userService.getUsers();

        // Assert
        assertNotNull(users, "The result should not be null");
        assertEquals(2, users.size(), "The size of the result should be 2");
        assertEquals("Adam", users.get(0).getFirstName(), "First user's name should match");
        assertEquals("ADMIN", users.get(1).getRole(), "Second user's role should match");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUsers_ReturnsEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        List<User> users = userService.getUsers();

        // Assert
        assertNotNull(users, "The result should not be null");
        assertTrue(users.isEmpty(), "The result should be an empty list");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByEmail_UserExists() {
        // Arrange
        String email = "adambrown@test.com";
        User mockUser = new User(1L, "Adam", "Brown", email, "password", "USER", false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> user = userService.findByEmail(email);

        // Assert
        assertTrue(user.isPresent(), "User should be present");
        assertEquals(email, user.get().getEmail(), "Email should match the searched email");
        assertEquals("Adam", user.get().getFirstName(), "First name should match");
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        // Arrange
        String email = "nonexistent@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> user = userService.findByEmail(email);

        // Assert
        assertFalse(user.isPresent(), "User should not be present");
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_NullEmail() {
        // Arrange
        when(userRepository.findByEmail(null)).thenThrow(IllegalArgumentException.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.findByEmail(null), "Searching with null email should throw exception");
        verify(userRepository, times(1)).findByEmail(null);
    }
}
