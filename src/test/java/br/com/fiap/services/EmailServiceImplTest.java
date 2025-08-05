package br.com.fiap.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void shouldSendResetTokenEmail() {
        String toEmail = "user@example.com";
        String token = "reset-token-123";

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendResetToken(toEmail, token);

        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Recuperação de Senha", message.getSubject());
        assertEquals("Clique no link para redefinir sua senha: http://localhost:8081/auth/reset-password?token=" + token,
                message.getText());
    }
}