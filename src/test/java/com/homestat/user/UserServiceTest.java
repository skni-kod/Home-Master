package com.homestat.user;

import com.homestat.exception.TokenNotExistsException;
import com.homestat.exception.UserAlreadyExistsException;
import com.homestat.registration.RegistrationRequest;
import com.homestat.registration.token.VerificationToken;
import com.homestat.registration.token.VerificationTokenRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getUsers();

        // Assert
        assertEquals(users.size(), result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testRegisterUserSuccess() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("John", "Doe", "john.doe@example.com", "password", "USER");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.registerUser(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.email(), result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("John", "Doe", "john.doe@example.com", "password", "USER");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindByEmailUserExists() {
        // Arrange
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testFindByEmailUserNotExists() {
        // Arrange
        String email = "john.doe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveUserVerificationToken() {
        // Arrange
        User user = new User();
        String token = "sampleToken";

        // Act
        userService.saveUserVerificationToken(user, token);

        // Assert
        verify(tokenRespository, times(1)).save(any(VerificationToken.class));
    }

    @Test
    void testValidateTokenSuccess() {
        // Arrange
        String tokenValue = "validToken";
        User user = new User();
        VerificationToken token = new VerificationToken(tokenValue, user);
        when(tokenRespository.findByToken(tokenValue)).thenReturn(token);

        // Act
        String result = userService.validateToken(tokenValue);

        // Assert
        assertEquals("valid", result);
        assertTrue(user.isEnabled());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testValidateTokenNullOrEmpty() {
        // Act & Assert
        assertThrows(TokenNotExistsException.class, () -> userService.validateToken(null));
        assertThrows(TokenNotExistsException.class, () -> userService.validateToken(""));
    }

    @Test
    void testDeleteTokenSuccess() {
        // Arrange
        String tokenValue = "validToken";
        VerificationToken token = new VerificationToken();
        when(tokenRespository.findByToken(tokenValue)).thenReturn(token);

        // Act
        String result = userService.deleteToken(tokenValue);

        // Assert
        assertEquals("Token deleted", result);
        verify(tokenRespository, times(1)).delete(token);
    }

    @Test
    void testDeleteTokenNullOrEmpty() {
        // Act & Assert
        assertThrows(TokenNotExistsException.class, () -> userService.deleteToken(null));
        assertThrows(TokenNotExistsException.class, () -> userService.deleteToken(""));
    }
}
