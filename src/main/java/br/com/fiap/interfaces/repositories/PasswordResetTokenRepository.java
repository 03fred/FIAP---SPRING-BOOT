package br.com.fiap.interfaces.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	  Optional<PasswordResetToken> findByToken(String token);

}
