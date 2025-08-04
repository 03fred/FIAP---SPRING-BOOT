package br.com.fiap.mapper;

import br.com.fiap.dto.AddressDTO;
import br.com.fiap.model.Address;
import br.com.fiap.model.User;

public class AddressMapper {

    public static AddressDTO toDTO(Address address) {
        if (address == null) return null;

        return new AddressDTO(
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );
    }

    public static Address toEntity(AddressDTO dto, User user) {
        if (dto == null) return null;

        return new Address(
                null,
                dto.street(),
                dto.number(),
                dto.neighborhood(),
                dto.city(),
                dto.state(),
                dto.zipCode(),
                user
        );
    }

    public static boolean isEmpty(AddressDTO dto) {
        return dto == null || (
                (dto.city() == null || dto.city().isBlank()) &&
                        (dto.state() == null || dto.state().isBlank()) &&
                        (dto.street() == null || dto.street().isBlank()) &&
                        (dto.neighborhood() == null || dto.neighborhood().isBlank()) &&
                        (dto.zipCode() == null || dto.zipCode().isBlank())
        );
    }


}
