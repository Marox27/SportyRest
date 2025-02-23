package com.example.SportyRest.controller;

import com.example.SportyRest.service.VerificationCodeGeneratorService;
import com.example.SportyRest.service.VerificationCodeValidationService;
import com.example.SportyRest.service.EmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationCodeGeneratorService verificationCodeGeneratorService;
    private final VerificationCodeValidationService verificationCodeValidationService;
    private final EmailService emailService;

    public VerificationController(VerificationCodeGeneratorService verificationCodeGeneratorService,
                                  VerificationCodeValidationService verificationCodeValidationService,
                                  EmailService emailService) {
        this.verificationCodeGeneratorService = verificationCodeGeneratorService;
        this.verificationCodeValidationService = verificationCodeValidationService;
        this.emailService = emailService;
    }

    @PostMapping("/send-code")
    public String sendVerificationCode(@RequestParam String userEmail) {
        // Generar y guardar el código
        String code = verificationCodeGeneratorService.generateCode(userEmail).getCode();

        // Enviar el código al correo electrónico
        //String code = verificationCodeGeneratorService.generateCode(userEmail).getCode();
        String subject = "Código de verificación";
        String message = "Tu código de verificación es: " + code + ". Este código expirará en 5 minutos.";
        emailService.sendEmail(userEmail, subject, message);

        return "Código de verificación enviado a " + userEmail;
    }


    @PostMapping("/verify")
    public Boolean verifyCode(@RequestParam String email, @RequestParam String code) {
        return verificationCodeValidationService.validateCode(code, email);
    }
}
