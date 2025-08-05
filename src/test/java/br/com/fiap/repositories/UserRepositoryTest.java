package br.com.fiap.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import br.com.fiap.dto.AddressDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.User;

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User createUser() {
    	AddressDTO addressDTO = new AddressDTO("Rua das Flores", "123", "Centro", "São Paulo", "SP", "01000-000");

		UserDTO userDTO = new UserDTO("user@email.com", "hashed-password-123", "João Silva", addressDTO, "User123");

		String passwordCrypto = "hashed-password-123";
		User user = new User(userDTO, passwordCrypto);
        return user;
    }

    @Test
    @DisplayName("Deve encontrar usuário por email")
    void shouldFindUserByEmail() {
        User user = createUser();
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("user@email.com");

        assertThat(result).isPresent();
        assertThat(result.get().getLogin()).isEqualTo("User123");
    }

    @Test
    @DisplayName("Deve encontrar usuário por login")
    void shouldFindUserByLogin() {
        User user = createUser();
        userRepository.save(user);

        Optional<User> result = userRepository.findByLogin("User123");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("user@email.com");
    }

    @Test
    @DisplayName("Deve verificar se email já existe")
    void shouldReturnTrueIfEmailExists() {
        User user = createUser();
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("user@email.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve verificar se login já existe")
    void shouldReturnTrueIfLoginExists() {
        User user = createUser();
        userRepository.save(user);

        boolean exists = userRepository.existsByLogin("User123");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false para email inexistente")
    void shouldReturnFalseIfEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("inexistente@email.com");
        assertFalse(exists);
    }
}
