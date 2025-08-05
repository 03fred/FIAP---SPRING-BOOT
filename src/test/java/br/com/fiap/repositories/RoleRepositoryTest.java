package br.com.fiap.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.model.Role;

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@DataJpaTest
class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	@DisplayName("should find role by name")
	void shouldFindByName() {
		Role role = new Role();
		role.setName("ADMIN");
		roleRepository.save(role);
		Optional<Role> result = roleRepository.findByName("ADMIN");
		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo("ADMIN");
	}

	@Test
    @DisplayName("should return empty if role name not found")
    void shouldReturnEmptyWhenNotFound() {
        Optional<Role> result = roleRepository.findByName("NOT_FOUND");
        assertThat(result).isEmpty();
    }
}