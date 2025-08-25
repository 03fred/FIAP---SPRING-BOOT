package br.com.fiap.mapper;

import br.com.fiap.domain.entities.Role;
import br.com.fiap.infrastructure.persistence.role.JpaRoleEntity;

public class RoleMapper {

	public static JpaRoleEntity toEntity(Role role) {
        if (role == null) return null;
        return new JpaRoleEntity(role.getId(), role.getName());
    }

    public static Role toDomain(JpaRoleEntity entity) {
        if (entity == null) return null;
        return new Role(entity.getId(), entity.getName());
    }
}
