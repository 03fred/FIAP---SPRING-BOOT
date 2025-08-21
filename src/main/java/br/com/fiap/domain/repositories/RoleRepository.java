package br.com.fiap.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.domain.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

 	Optional<Role> findByName(String name);
}
