package com.example.SportyRest.repository;

import com.example.SportyRest.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    void deleteByExpirationTimeBefore(LocalDateTime now);

    VerificationCode findByEmail(String Email);

    void deleteByEmail(String email);

}
