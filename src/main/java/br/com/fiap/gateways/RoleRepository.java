package br.com.fiap.gateways;

import br.com.fiap.domain.entities.Role;


public interface RoleRepository {
    Role save(Role role);
    void delete(Role role);
}