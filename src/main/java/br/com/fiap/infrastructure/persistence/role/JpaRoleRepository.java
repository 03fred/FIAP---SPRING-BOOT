package br.com.fiap.infrastructure.persistence.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<JpaRoleEntity, Long> {

	Optional<JpaRoleEntity> findByEmail(String email);

	Optional<JpaRoleEntity> findByLogin(String login);

	boolean existsByEmail(String email);

	boolean existsByLogin(String login);
}