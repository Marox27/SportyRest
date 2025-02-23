package com.example.SportyRest.service;

import com.example.SportyRest.model.VerificationCode;
import com.example.SportyRest.repository.VerificationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationCodeGeneratorService {

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeGeneratorService(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    public VerificationCode generateCode(String email) {
        // Lo primero elimina códigos que ya estén expirados
        verificationCodeRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
        // Comprueba de si existe otro código asociado al email, si existe lo elimina.
        if (verificationCodeRepository.findByEmail(email) != null){
            verificationCodeRepository.deleteByEmail(email);
        }
        // Generar un código aleatorio de 6 dígitos
        String code = String.format("%06d", new Random().nextInt(999999));

        // Crear y guardar el código en la base de datos
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        verificationCodeRepository.save(verificationCode);

        return verificationCode;
    }

}

