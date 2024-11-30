package com.homestat.registration;

import com.homestat.registration.event.RegistrationCompleteEvent;
import com.homestat.user.User;
import com.homestat.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest(
                "John",
                "Doe",
                "john@example.com",
                "password",
                "USER"
        );

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john@example.com");

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServerName()).thenReturn("localhost");
        when(httpServletRequest.getServerPort()).thenReturn(8080);
        when(httpServletRequest.getContextPath()).thenReturn("/app");

        when(userService.registerUser(request)).thenReturn(mockUser);

        ArgumentCaptor<RegistrationCompleteEvent> eventCaptor = ArgumentCaptor.forClass(RegistrationCompleteEvent.class);

        // Act
        String response = registrationController.registerUser(request, httpServletRequest);

        // Assert
        assertEquals("Success! Please, check your email for verification link", response);
        verify(userService, times(1)).registerUser(request);
        verify(publisher, times(1)).publishEvent(eventCaptor.capture());

        RegistrationCompleteEvent capturedEvent = eventCaptor.getValue();
        assertEquals(mockUser, capturedEvent.getUser());
        assertEquals("http://localhost:8080/app", capturedEvent.getApplicationUrl());
    }

    @Test
    void testApplicationUrl_CorrectFormat() {
        // Arrange
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServerName()).thenReturn("example.com");
        when(httpServletRequest.getServerPort()).thenReturn(80);
        when(httpServletRequest.getContextPath()).thenReturn("");

        // Act
        String url = registrationController.applicationUrl(httpServletRequest);

        // Assert
        assertEquals("http://example.com:80", url);
    }
}
