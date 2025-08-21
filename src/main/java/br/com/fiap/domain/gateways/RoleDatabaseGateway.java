package br.com.fiap.domain.gateways;

import java.util.Optional;
import br.com.fiap.domain.entities.Role;

public interface RoleDatabaseGateway extends DatabaseGateway<Role, Long> {
    Optional<Role> findByName(String name);
}