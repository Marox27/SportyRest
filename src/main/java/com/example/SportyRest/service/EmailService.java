package com.example.SportyRest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.juli.logging.Log;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("sportyhub.soporte@gmail.com"); // Asegúrate de usar un correo válido
        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String text/*, String attachmentPath*/)  {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // `true` para indicar que es multipart

            String imageUrl = "https://res.cloudinary.com/dkl7y8jew/image/upload/v1738254408/SportyHub_Logo.png";
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText("<html><body><p>" + text + "</p><img src='" + imageUrl + "' width='200'/></body></html>", true); // `true` para permitir HTML en el mensaje
            helper.setFrom("sportyhub.soporte@gmail.com");

            mailSender.send(message);
        }catch (MessagingException e){
            System.err.println("Error al enviar el correo con imagen adjunta.");
            e.printStackTrace();
        }
    }
}
