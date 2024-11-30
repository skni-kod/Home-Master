package com.homestat.user;

import com.homestat.exception.UserAlreadyExistsException;
import com.homestat.registration.RegistrationRequest;
import com.homestat.registration.token.VerificationToken;
import com.homestat.registration.token.VerificationTokenRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRespository tokenRespository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> users = userService.getUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("John", "Doe", "john@example.com", "password", "USER");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User registeredUser = userService.registerUser(request);

        // Assert
        assertNotNull(registeredUser);
        verify(userRepository, times(1)).findByEmail(request.email());
        verify(passwordEncoder, times(1)).encode(request.password());
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("John", capturedUser.getFirstName());
        assertEquals("Doe", capturedUser.getLastName());
        assertEquals("john@example.com", capturedUser.getEmail());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals("USER", capturedUser.getRole());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("John", "Doe", "john@example.com", "password", "USER");
        User existingUser = new User();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
        assertEquals("User with emailjohn@example.com already exists", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(request.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindByEmail_UserFound() {
        // Arrange
        String email = "john@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.findByEmail(email);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_UserNotFound() {
        // Arrange
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = userService.findByEmail(email);

        // Assert
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testSaveUserVerificationToken() {
        // Arrange
        User user = new User();
        String token = "verification-token";
        VerificationToken verificationToken = new VerificationToken(token, user);

        // Act
        userService.saveUserVerificationToken(user, token);

        // Assert
        ArgumentCaptor<VerificationToken> tokenCaptor = ArgumentCaptor.forClass(VerificationToken.class);
        verify(tokenRespository, times(1)).save(tokenCaptor.capture());
        VerificationToken capturedToken = tokenCaptor.getValue();
        assertEquals("verification-token", capturedToken.getToken());
        assertEquals(user, capturedToken.getUser());
    }
}
