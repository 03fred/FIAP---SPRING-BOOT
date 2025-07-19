package br.com.fiap.interfaces.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

 	Optional<Role> findByName(String name);
}
