package com.homestat.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRespository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
