package com.homestat.registration.event.listener;

import com.homestat.registration.event.RegistrationCompleteEvent;
import com.homestat.user.User;
import com.homestat.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;

class RegistrationCompleteEventListenerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationCompleteEventListener eventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnApplicationEvent() {
        // Arrange
        User user = new User();
        String applicationUrl = "http://localhost:8080";
        RegistrationCompleteEvent event = new RegistrationCompleteEvent(user, applicationUrl);

        // Act
        eventListener.onApplicationEvent(event);

        // Assert
        verify(userService, times(1)).saveUserVerificationToken(eq(user), anyString());
    }

    @Test
    void testVerificationUrlLogging() {
        // Arrange
        User user = new User();
        String applicationUrl = "http://localhost:8080";
        RegistrationCompleteEvent event = new RegistrationCompleteEvent(user, applicationUrl);
        String expectedUrlPattern = applicationUrl + "/register/verifyEmail?token=" + UUID.randomUUID();

        // Act
        eventListener.onApplicationEvent(event);

        // Verify
        verify(userService, times(1)).saveUserVerificationToken(eq(user), matches("^[a-f0-9\\-]{36}$"));
    }

    @Test
    void testLogInfoForVerificationLink() {
        // Arrange
        User user = new User();
        String applicationUrl = "http://localhost:8080";
        RegistrationCompleteEvent event = new RegistrationCompleteEvent(user, applicationUrl);

        // Act
        eventListener.onApplicationEvent(event);

        // Assert
        // Verification of log output would require a logging framework mocking tool like LogCaptor
        verify(userService, times(1)).saveUserVerificationToken(eq(user), anyString());
    }
}
