package br.com.fiap.infrastructure.persistence.passwordResetToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPasswordResetTokenRepository extends JpaRepository<JpaPasswordResetTokenEntity, Long> {
	Optional<JpaPasswordResetTokenEntity> findByToken(String token);
}