package br.com.fiap.factory;

import br.com.fiap.dto.AddressDTO;
import br.com.fiap.domain.entities.Address;
import br.com.fiap.domain.entities.User;

public class AddressFactory {

    public static AddressDTO getMockAddressDTO() {
        return new AddressDTO(
                "Rua Exemplo",
                "123",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000"
        );
    }

    public static Address getMockAddress(User user) {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setNumber("123");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setZipCode("01000-000");
        address.setUser(user);
        return address;
    }

    public static Address getMockAddress() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setNumber("123");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setZipCode("01000-000");
        return address;
    }

    public static AddressDTO getEmptyAddressDTO() {
        return new AddressDTO("", "", "", "", "", "");
    }

}
