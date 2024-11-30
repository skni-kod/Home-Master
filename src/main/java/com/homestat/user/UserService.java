package com.homestat.user;

import com.homestat.exception.UserAlreadyExistsException;
import com.homestat.registration.RegistrationRequest;
import com.homestat.registration.token.VerificationToken;
import com.homestat.registration.token.VerificationTokenRespository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final VerificationTokenRespository tokenRespository;
    private final PasswordEncoder passwordEncoder;

    // Method used to retrieve all users from the database
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        //1. Check if user with provided email already exists
        Optional<User> user = this.findByEmail(request.email());
        if(user.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email"+request.email()+" already exists");
        }
        //create new user
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        //encode password
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());

        //save new user to database
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // Optional<User> is a container of object User which either has a non-null value or is empty
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRespository.save(verificationToken);
    }
}
