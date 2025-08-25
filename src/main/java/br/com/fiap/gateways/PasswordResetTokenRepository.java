package br.com.fiap.gateways;

import br.com.fiap.domain.entities.PasswordResetToken;

public interface PasswordResetTokenRepository {
	PasswordResetToken findByToken(String token);
	PasswordResetToken save(PasswordResetToken passwordResetToken);

}