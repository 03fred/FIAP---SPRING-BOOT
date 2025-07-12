package br.com.fiap.factory;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserUpdateDTO;
import br.com.fiap.model.User;
import br.com.fiap.model.enums.EnumType;

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
        user.setAddress("rua 1");
        user.setEnumType(EnumType.USER);

        return user;
    }

    public static UserDTO createUserDTOMock() {
        return new UserDTO("Ana", "ana@email.com", "ana123", "senha", "rua 1");
    }

    public static UserUpdateDTO UserUpdateDTOMock() {
        return new UserUpdateDTO("Ana Maria", "novo@email.com", "anaNovo", "rua 2");
    }
}
