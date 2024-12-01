package com.homestat.user;

import com.homestat.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    //Method to return a list of all users
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    //Method to return an Optional<User> object containing user if they're found by email or Optional.empty() if no user with that email exists
    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    String deleteToken(String theToken);
}
