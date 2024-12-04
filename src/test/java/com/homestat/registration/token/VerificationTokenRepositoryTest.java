package com.homestat.registration.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.homestat.user.User;
import com.homestat.user.UserRepository; // Assuming you have a UserRepository to persist the User entity
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // This ensures that the database state is rolled back after each test
public class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRespository verificationTokenRespository;

    @Autowired
    private UserRepository userRepository;  // Assuming you have a UserRepository to persist the User entity

    private VerificationToken verificationToken;
    private User user;

    @BeforeEach
    public void setUp() {
        // Create and save a User entity first, as it's required by VerificationToken
        user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setRole("USER");
        // Save user in the repository
        userRepository.save(user);

        // Initialize a VerificationToken and associate it with the user
        verificationToken = new VerificationToken("validToken123", user);
        // Save the VerificationToken in the repository
        verificationTokenRespository.save(verificationToken);
    }

    @Test
    public void testFindByToken_ValidToken() {
        // Test the retrieval of a token by a valid token string
        VerificationToken foundToken = verificationTokenRespository.findByToken("validToken123");
        assertEquals("validToken123", foundToken.getToken());
        assertEquals(user.getId(), foundToken.getUser().getId());
    }

    @Test
    public void testFindByToken_InvalidToken() {
        // Test the retrieval of a token by an invalid token string
        VerificationToken foundToken = verificationTokenRespository.findByToken("invalidToken");
        assertNull(foundToken);
    }

    @Test
    public void testFindByToken_NullToken() {
        // Test behavior when the token is null
        VerificationToken foundToken = verificationTokenRespository.findByToken(null);
        assertNull(foundToken);
    }

    @Test
    public void testFindByToken_TokenNotPresent() {
        // Test behavior when the token is not present in the repository
        VerificationToken foundToken = verificationTokenRespository.findByToken("nonExistentToken");
        assertNull(foundToken);
    }
}
