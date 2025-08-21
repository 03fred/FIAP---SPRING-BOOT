package br.com.fiap.application.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.application.useCases.EmailService;
import br.com.fiap.domain.gateways.EmailGateway;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailGateway emailGateway;

	public void sendResetToken(String toEmail, String token) {
		emailGateway.sendPasswordResetToken(toEmail, token);
	}
}
