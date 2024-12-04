package com.homestat.registration.token;

import com.homestat.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationTokenTests {

    @Mock
    private VerificationTokenRespository tokenRepository;

    @InjectMocks
    private VerificationToken testToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTokenInitialization() {
        // Arrange
        User user = new User();
        String tokenValue = "test-token";

        // Act
        VerificationToken token = new VerificationToken(tokenValue, user);

        // Assert
        assertEquals(tokenValue, token.getToken());
        assertEquals(user, token.getUser());
        assertNotNull(token.getExpirationTime());
        assertTrue(token.getExpirationTime().after(new Date()));
    }

    @Test
    void testTokenExpirationCalculation() {
        // Arrange
        User user = new User();
        String tokenValue = "test-token";
        Date beforeCreation = new Date();

        // Act
        VerificationToken token = new VerificationToken(tokenValue, user);
        Date expirationTime = token.getExpirationTime();

        // Assert
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beforeCreation);
        calendar.add(Calendar.MINUTE, 5); // EXPIRATION_TIME is 5 minutes
        assertEquals(calendar.getTime().getTime(), expirationTime.getTime(), 5000); // Allow slight time deviation
    }

    @Test
    void testFindByToken() {
        // Arrange
        String tokenValue = "test-token";
        VerificationToken expectedToken = new VerificationToken(tokenValue, new User());
        when(tokenRepository.findByToken(tokenValue)).thenReturn(expectedToken);

        // Act
        VerificationToken actualToken = tokenRepository.findByToken(tokenValue);

        // Assert
        assertEquals(expectedToken, actualToken);
        verify(tokenRepository, times(1)).findByToken(tokenValue);
    }

    @Test
    void testTokenPersistence() {
        // Arrange
        VerificationToken token = new VerificationToken("test-token", new User());

        // Act
        tokenRepository.save(token);

        // Assert
        verify(tokenRepository, times(1)).save(token);
    }

    @Test
    void testTokenEntityMapping() {
        // Arrange
        User user = new User();
        String tokenValue = "test-token";
        VerificationToken token = new VerificationToken(tokenValue, user);

        // Act
        token.setId(1L);

        // Assert
        assertEquals(1L, token.getId());
        assertEquals(tokenValue, token.getToken());
        assertEquals(user, token.getUser());
        assertNotNull(token.getExpirationTime());
    }
}
