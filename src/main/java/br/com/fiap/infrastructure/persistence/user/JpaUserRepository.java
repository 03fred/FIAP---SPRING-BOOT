package br.com.fiap.infrastructure.persistence.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Long> {

	Optional<JpaUserEntity> findByEmail(String email);

	Optional<JpaUserEntity> findByLogin(String login);

	boolean existsByEmail(String email);

	boolean existsByLogin(String login);
	
	 Page<JpaUserEntity> findAll(Pageable pageable);
}