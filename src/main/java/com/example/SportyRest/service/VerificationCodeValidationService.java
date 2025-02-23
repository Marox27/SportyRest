package com.example.SportyRest.service;

import com.example.SportyRest.model.VerificationCode;
import com.example.SportyRest.repository.VerificationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationCodeValidationService {

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeValidationService(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    public boolean validateCode(String inputCode, String email) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if (verificationCode != null && verificationCode.getCode().equals(inputCode)) {
            if (verificationCode.getExpirationTime().isAfter(LocalDateTime.now())) {
                // Código válido, eliminarlo para evitar reutilización
                verificationCodeRepository.delete(verificationCode);
                return true;
            } else {
                // Código expirado
                verificationCodeRepository.delete(verificationCode);
            }
        }
        return false; // Código inválido
    }
}

