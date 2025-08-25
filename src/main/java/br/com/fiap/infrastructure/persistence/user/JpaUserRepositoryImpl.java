package br.com.fiap.infrastructure.persistence.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.User;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.gateways.UserRepository;
import br.com.fiap.mapper.UserMapper;

@Repository
public class JpaUserRepositoryImpl implements UserRepository {
    
    @Autowired
    private JpaUserRepository jpaRepo;
    
    JpaUserEntity user;
    
	@Override
	public User save(User entity) {
		user = jpaRepo.save(UserMapper.toEntity(entity));
		return UserMapper.toDomain(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		 jpaRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));
		 return Optional.ofNullable(UserMapper.toDomain(user));
	}

	@Override
	public Optional<User> findByLogin(String login) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsByLogin(String login) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}
    
}