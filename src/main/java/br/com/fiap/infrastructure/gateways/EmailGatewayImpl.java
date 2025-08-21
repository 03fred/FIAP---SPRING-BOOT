package br.com.fiap.infrastructure.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import br.com.fiap.domain.gateways.EmailGateway;

@Component
public class EmailGatewayImpl implements EmailGateway {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Override
    public void sendPasswordResetToken(String toEmail, String token) {
        String subject = "Recuperação de Senha";
        String resetUrl = "http://localhost:8081/auth/reset-password?token=" + token;
        String message = "Clique no link para redefinir sua senha: " + resetUrl;
        
        sendNotificationEmail(toEmail, subject, message);
    }
    
    @Override
    public void sendNotificationEmail(String toEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);
        
        mailSender.send(email);
    }
}