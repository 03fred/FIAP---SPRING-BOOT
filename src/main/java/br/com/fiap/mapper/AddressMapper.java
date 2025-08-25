package br.com.fiap.mapper;

import br.com.fiap.domain.entities.Address;
import br.com.fiap.infrastructure.persistence.address.JpaAddressEntity;

public class AddressMapper {

    public static JpaAddressEntity toEntity(Address address) {
        if (address == null) return null;
        return new JpaAddressEntity(
            address.getId(),
            address.getStreet(),
            address.getNumber(),
            address.getNeighborhood(),
            address.getCity(),
            address.getState(),
            address.getZipCode(),
            UserMapper.toEntity(address.getUser())
        );
    }

    public static Address toDomain(JpaAddressEntity entity) {
        if (entity == null) return null;
        return new Address(
            entity.getId(),
            entity.getStreet(),
            entity.getNumber(),
            entity.getNeighborhood(),
            entity.getCity(),
            entity.getState(),
            entity.getZipCode(),
            UserMapper.toDomain(entity.getUser())
        );
    }
}
