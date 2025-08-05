package br.com.fiap.factory;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserUpdateDTO;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class UserFactory {

    public static User createUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    public static User createUserMock() {
        User user = new User();
        user.setId(1L);
        user.setName("Ana");
        user.setEmail("ana@email.com");
        user.setLogin("anaLogin");
        user.setPassword("hashedSenha");  // Importante: senha deve bater com o when
        user.setAddress(AddressFactory.getMockAddress(user));
        return user;
    }

    public static UserDTO createUserDTOMock() {
        return new UserDTO("Ana", "ana@email.com", "ana123", AddressFactory.getMockAddressDTO(), "rua 1");
    }

    public static UserUpdateDTO UserUpdateDTOMock() {
        return new UserUpdateDTO("Ana Maria", "novo@email.com", AddressFactory.getMockAddressDTO(), "rua 2");
    }

    public static UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO(
                "john.doe@example.com",
                "john.doe@example.com",
                "John Doe",
                AddressFactory.getMockAddressDTO(),
                "johndoe"
        );
        return userDTO;
    }

    public static User getUser(Set<Role> roles, List<Restaurant> restaurants, Date updateDate) {
        User user = new User(
                1L,
                "jane.doe@example.com",
                "Jane Doe",
                "janedoe",
                "pass123",
                updateDate,
                AddressFactory.getMockAddress(),
                roles,
                restaurants
        );
        return user;
    }
}
