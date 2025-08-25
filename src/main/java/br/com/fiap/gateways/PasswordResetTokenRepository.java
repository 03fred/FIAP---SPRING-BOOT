package br.com.fiap.gateways;

import br.com.fiap.infrastructure.persistence.passwordResetToken.JpaPasswordResetTokenEntity;

public interface PasswordResetTokenRepository {
	 JpaPasswordResetTokenEntity findByToken(String token);

}