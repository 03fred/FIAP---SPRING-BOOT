package br.com.fiap.mapper;

import br.com.fiap.domain.entities.User;
import br.com.fiap.infrastructure.persistence.user.JpaUserEntity;

public class UserMapper {
    public static JpaUserEntity toEntity(User user) {
        return new JpaUserEntity(user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getPassword(), null);
    }

    public static User toDomain(JpaUserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(), null, null, null);
    }
}
