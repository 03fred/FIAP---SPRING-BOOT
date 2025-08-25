package br.com.fiap.gateways;

import java.util.List;
import java.util.Optional;

import br.com.fiap.domain.entities.User;


public interface RoleRepository {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByLogin(String login);
    
    boolean existsByEmail(String email);
    
    boolean existsByLogin(String login);
    
    User save(User user);
    
    Optional<User> findById(Long id);

	List<User> findAll();
    
	void deleteById(Long id);
}