package br.com.fiap.infrastructure.persistence.passwordResetToken;

import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.PasswordResetToken;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.gateways.PasswordResetTokenRepository;
import br.com.fiap.mapper.PasswordResetTokenMapper;


@Repository
public class JpaPasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private JpaPasswordResetTokenRepository jpaRepo;

    JpaPasswordResetTokenRepositoryImpl(JpaPasswordResetTokenRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }


	@Override
	public PasswordResetToken findByToken(String token) {
		JpaPasswordResetTokenEntity resetToken = jpaRepo.findByToken(token)
				   .orElseThrow(() -> new ResourceNotFoundException("Token Invalido"));
		return PasswordResetTokenMapper.toDomain(resetToken);
	}

	
	@Override
	public PasswordResetToken save(PasswordResetToken passwordResetToken) {
		var entity = jpaRepo.save(PasswordResetTokenMapper.toEntity(passwordResetToken));
		return PasswordResetTokenMapper.toDomain(entity);

	}
    
}