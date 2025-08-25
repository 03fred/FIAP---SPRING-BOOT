package br.com.fiap.infrastructure.persistence.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.User;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.gateways.UserRepository;
import br.com.fiap.mapper.UserMapper;

@Repository
public class JpaUserRepositoryImpl implements UserRepository {
    
    @Autowired
    private JpaUserRepository jpaRepo;
    
    private JpaUserRepositoryImpl(JpaUserRepository jpaRepo)
    {
    	this.jpaRepo = jpaRepo;
    }
    JpaUserEntity user;
    
	@Override
	public User save(User entity) {
		user = jpaRepo.save(UserMapper.toEntity(entity));
		return UserMapper.toDomain(user);
	}

	@Override
	public User findByEmail(String email) {
		JpaUserEntity entity = jpaRepo.findByEmail(email)
				   .orElseThrow(() -> new ResourceNotFoundException("Token Invalido"));
		return UserMapper.toDomain(entity);
	}

	@Override
	public User findByLogin(String login) {
		JpaUserEntity entity = jpaRepo.findByLogin(login)
				   .orElseThrow(() -> new ResourceNotFoundException("Token Invalido"));
		return UserMapper.toDomain(entity);
	}

	@Override
	public User findById(Long id) {
		JpaUserEntity entity = jpaRepo.findById(id)
				   .orElseThrow(() -> new ResourceNotFoundException("Token Invalido"));
		return UserMapper.toDomain(entity);
	}

	@Override
	public List<User> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return jpaRepo.findAll(pageable).map(UserMapper::toDomain).toList();
	}

	@Override
	public void deleteById(User user) {
	   jpaRepo.deleteById(user.getId());
		
	}
    
}