package com.homestat.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail_UserExists() {
        // Arrange
        User user = new User(null, "Adam", "Brown", "adambrown@test.com", "password", "USER", false);
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("adambrown@test.com");

        // Assert
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals("Adam", foundUser.get().getFirstName(), "First name should match");
        assertEquals("Brown", foundUser.get().getLastName(), "Last name should match");
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent(), "No user should be found");
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User(null, "Eve", "Smith", "evesmith@test.com", "password", "ADMIN", true);

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser.getId(), "Saved user should have an ID");
        assertEquals("Eve", savedUser.getFirstName(), "First name should match");
        assertEquals("ADMIN", savedUser.getRole(), "Role should match");
        assertTrue(savedUser.isEnabled(), "User should be enabled");
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User user = new User(null, "Taylor", "Swift", "taylorswift@test.com", "password", "USER", false);
        User savedUser = userRepository.save(user);

        // Act
        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        // Assert
        assertFalse(deletedUser.isPresent(), "Deleted user should not be found");
    }
}
