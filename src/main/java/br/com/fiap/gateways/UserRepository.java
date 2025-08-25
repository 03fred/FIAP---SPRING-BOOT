package br.com.fiap.gateways;

import java.util.List;

import br.com.fiap.domain.entities.User;


public interface UserRepository {
    
    User findByEmail(String email);
    
    User findByLogin(String login);
    
    User save(User user);
    
    User findById(Long id);

	List<User> findAll(int page, int size);
    
    void deleteById(User user);
}