package com.homestat.registration.event.listener;

import com.homestat.registration.event.RegistrationCompleteEvent;
import com.homestat.user.User;
import com.homestat.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor

public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User theUser;
        //1. Get newly registered user
        theUser = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser,verificationToken);
        //4. Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        //TODO: 5. Send the email
        log.info("Click the link to verify your registration: {} ",url);
    }
    //TODO: sendVerificationEmail method
}
