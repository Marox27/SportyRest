package com.example.SportyRest.service;

import com.example.SportyRest.repository.VerificationCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VerificationCodeCleanupService {

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeCleanupService(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    @Transactional
    public void cleanExpiredCodes() {
        verificationCodeRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }
}
