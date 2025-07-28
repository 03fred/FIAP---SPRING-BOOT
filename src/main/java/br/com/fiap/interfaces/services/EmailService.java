package br.com.fiap.interfaces.services;

public interface EmailService {

	 void sendResetToken(String toEmail, String token);
}
