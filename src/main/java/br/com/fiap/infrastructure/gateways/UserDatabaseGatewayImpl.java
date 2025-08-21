package br.com.fiap.infrastructure.gateways;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import br.com.fiap.domain.entities.User;
import br.com.fiap.domain.gateways.UserDatabaseGateway;
import br.com.fiap.domain.repositories.UserRepository;

/**
 * Implementation of UserDatabaseGateway using Spring Data JPA
 */
@Component
public class UserDatabaseGatewayImpl implements UserDatabaseGateway {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }
    
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return userRepository.count();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}