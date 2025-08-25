package br.com.fiap.infrastructure.persistence.passwordResetToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.fiap.gateways.PasswordResetTokenRepository;

@Repository
public class JpaPasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {
    
    @Autowired
    private JpaPasswordResetTokenRepository jpaRepo;
    
    public JpaPasswordResetTokenRepositoryImpl(JpaPasswordResetTokenRepository jpaRepo) {
    	this.jpaRepo = jpaRepo;
    }

	@Override
	public JpaPasswordResetTokenEntity findByToken(String token) {
		JpaPasswordResetTokenEntity resetToken = jpaRepo.findByToken(token)
	            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
		
		return resetToken;
	}

    
}