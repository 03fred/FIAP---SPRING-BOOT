package br.com.fiap.domain.gateways;

import java.util.Optional;
import br.com.fiap.domain.entities.User;

/**
 * Database gateway interface for User entity operations
 */
public interface UserDatabaseGateway extends DatabaseGateway<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByLogin(String login);
    
    boolean existsByEmail(String email);
    
    boolean existsByLogin(String login);
}