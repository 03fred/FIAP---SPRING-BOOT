package br.com.fiap.mapper;

import br.com.fiap.domain.entities.PasswordResetToken;
import br.com.fiap.infrastructure.persistence.passwordResetToken.JpaPasswordResetTokenEntity;

public class PasswordResetTokenMapper {

    public static PasswordResetToken toDomain(JpaPasswordResetTokenEntity entity) {
        if (entity == null) return null;

        return new PasswordResetToken(
                entity.getId(),
                entity.getToken(),
                UserMapper.toDomain(entity.getUser()), 
                entity.getExpiration()
        );
    }

    public static JpaPasswordResetTokenEntity toEntity(PasswordResetToken domain) {
        if (domain == null) return null;

        JpaPasswordResetTokenEntity entity = new JpaPasswordResetTokenEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUser(UserMapper.toEntity(domain.getUser())); 
        entity.setExpiration(domain.getExpiration());

        return entity;
    }
}
