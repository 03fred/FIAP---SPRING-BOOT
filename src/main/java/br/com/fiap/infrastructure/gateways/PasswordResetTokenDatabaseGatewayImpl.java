package br.com.fiap.infrastructure.gateways;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import br.com.fiap.domain.entities.PasswordResetToken;
import br.com.fiap.domain.gateways.PasswordResetTokenDatabaseGateway;
import br.com.fiap.domain.repositories.PasswordResetTokenRepository;

/**
 * Implementation of PasswordResetTokenDatabaseGateway using Spring Data JPA
 */
@Component
public class PasswordResetTokenDatabaseGatewayImpl implements PasswordResetTokenDatabaseGateway {
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Override
    public PasswordResetToken save(PasswordResetToken entity) {
        return passwordResetTokenRepository.save(entity);
    }
    
    @Override
    public Optional<PasswordResetToken> findById(Long id) {
        return passwordResetTokenRepository.findById(id);
    }
    
    @Override
    public List<PasswordResetToken> findAll() {
        return passwordResetTokenRepository.findAll();
    }
    
    @Override
    public Page<PasswordResetToken> findAll(Pageable pageable) {
        return passwordResetTokenRepository.findAll(pageable);
    }
    
    @Override
    public void deleteById(Long id) {
        passwordResetTokenRepository.deleteById(id);
    }
    
    @Override
    public void delete(PasswordResetToken entity) {
        passwordResetTokenRepository.delete(entity);
    }
    
    @Override
    public boolean existsById(Long id) {
        return passwordResetTokenRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return passwordResetTokenRepository.count();
    }
    
    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
    
    @Override
    public void deleteExpiredTokens() {
       // passwordResetTokenRepository.deleteExpiredTokens();
    }
    
    @Override
    public void deleteByUserId(Long userId) {
      //  passwordResetTokenRepository.deleteByUserId(userId);
    }
}