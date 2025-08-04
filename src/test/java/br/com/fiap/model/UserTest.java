package br.com.fiap.model;

import br.com.fiap.dto.AddressDTO;
import br.com.fiap.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

	@Test
	void shouldCreateUserFromUserDTOAndPassword() {
		AddressDTO addressDTO = new AddressDTO("Rua das Flores", "123", "Centro", "São Paulo", "SP", "01000-000");

		UserDTO userDTO = new UserDTO("user@example.com", "hashed-password-123", "João Silva", addressDTO, "joaosilva");

		String passwordCrypto = "hashed-password-123";
		User user = new User(userDTO, passwordCrypto);

		assertEquals("user@example.com", user.getEmail());
		assertEquals("João Silva", user.getName());
		assertEquals("joaosilva", user.getLogin());
		assertEquals("hashed-password-123", user.getPassword());
		assertNotNull(user.getDtUpdateRow());

		Address address = user.getAddress();
		assertNotNull(address);
		assertEquals("Rua das Flores", address.getStreet());
		assertEquals("123", address.getNumber());
		assertEquals("Centro", address.getNeighborhood());
		assertEquals("São Paulo", address.getCity());
		assertEquals("SP", address.getState());
		assertEquals("01000-000", address.getZipCode());
		assertEquals(user, address.getUser());
	}

	@Test
	void shouldUpdateDtUpdateRowOnUpdate() {
		User user = new User();
		user.setDtUpdateRow(null);
		user.onUpdate();
		assertNotNull(user.getDtUpdateRow());
		assertTrue(user.getDtUpdateRow().before(new Date(System.currentTimeMillis() + 1000)));
	}

	@Test
	void shouldRemoveRoleFromUser() {
		Role admin = new Role();
		admin.setId(1L);
		admin.setName("ADMIN");

		Role userRole = new Role();
		userRole.setId(2L);
		userRole.setName("USER");

		User user = new User();
		user.getUserTypesRoles().add(admin);
		user.getUserTypesRoles().add(userRole);
		user.removeRole(admin);
		assertFalse(user.getUserTypesRoles().contains(admin));
		assertTrue(user.getUserTypesRoles().contains(userRole));
	}
}
