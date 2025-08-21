package br.com.fiap.domain.gateways;

import java.util.Optional;
import br.com.fiap.domain.entities.PasswordResetToken;

/**
 * Database gateway interface for PasswordResetToken entity operations
 */
public interface PasswordResetTokenDatabaseGateway extends DatabaseGateway<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    void deleteExpiredTokens();
    
    void deleteByUserId(Long userId);
}