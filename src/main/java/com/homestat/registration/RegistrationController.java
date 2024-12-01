package com.homestat.registration;

import com.homestat.registration.event.RegistrationCompleteEvent;
import com.homestat.registration.token.VerificationTokenRespository;
import com.homestat.user.User;
import com.homestat.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
@Tag(name = "Registration")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
//    private final VerificationTokenRespository tokenRespository;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        //1. create new user in database and return it
        User user = userService.registerUser(registrationRequest);
        //2. publish registration event once user is registered
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));

        return "Success! Please, check your email for verification link";
    }

    String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    //TODO: email verification
}
