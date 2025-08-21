package br.com.fiap.application.useCases;

public interface EmailService {

	 void sendResetToken(String toEmail, String token);
}
