package br.com.fiap.integrationTests.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.config.security.JwtTokenUtil;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.interfaces.repositories.MenuRepository;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Menu;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@Transactional
public class MenuControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private JavaMailSender mailSender;

	private String jwtAdmin;

	private String jwtOwner;

	private Long restaurantId;

	Menu menu;

	private MenuDTO menuDTO;

	@BeforeEach
	void setup() {
		menuRepository.deleteAll();
		restaurantRepository.deleteAll();
		userRepository.deleteAll();
		roleRepository.deleteAll();

		// Cria roles
		Role adminRole = new Role();
		adminRole.setName("ADMIN");
		roleRepository.save(adminRole);

		Role ownerRole = new Role();
		ownerRole.setName("RESTAURANT_OWNER");
		roleRepository.save(ownerRole);

		// Cria usuário ADMIN
		User admin = new User();
		admin.setLogin("admin_" + UUID.randomUUID());
		admin.setEmail("admin@admin.com");
		admin.setPassword(passwordEncoder.encode("admin123"));
		admin.setName("Admin");
		admin.setAddress("Admin Address");
		admin.getUserTypesRoles().add(adminRole);
		userRepository.save(admin);

		// Cria usuário OWNER
		User owner = new User();
		owner.setLogin("owner_" + UUID.randomUUID());
		owner.setEmail("owner@owner.com");
		owner.setPassword(passwordEncoder.encode("owner123"));
		owner.setName("Owner");
		owner.setAddress("Owner Address");
		owner.getUserTypesRoles().add(ownerRole);
		owner = userRepository.save(owner);

		// Cria restaurante para o OWNER
		Restaurant restaurant = new Restaurant();
		restaurant.setName("Restaurante Teste");
		restaurant.setAddress("Rua A");
		restaurant.setOpeningTime(LocalTime.of(10, 0));
		restaurant.setClosingTime(LocalTime.of(22, 0));
		restaurant.setTypeKitchen("ITALIANA");
		restaurant.setRestaurantOwner(owner);
		restaurantId = restaurantRepository.save(restaurant).getId();

		jwtAdmin = JwtTokenUtil.createToken(admin.getLogin(), restaurantId);

		jwtOwner = JwtTokenUtil.createToken(owner.getLogin(), restaurantId);

		menuDTO = new MenuDTO("Pizza Margherita", "Tradicional com molho de tomate e manjericão", "39.90", "true",
				"https://exemplo.com/imagem/pizza.jpg");

		menu = new Menu(menuDTO, restaurant);
		menu = menuRepository.save(menu); // agora temos o ID real
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }) // Explicit username and roles
	@Transactional
	void givenValidMenuDTO_whenAdminCreatesMenu_thenReturnCreated() throws Exception {
		mockMvc.perform(post("/menu/restaurant/" + restaurantId).header("Authorization", "Bearer " + jwtAdmin)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(menuDTO)))
				.andExpect(status().isCreated());

		List<Menu> menus = menuRepository.findAll();
		assertEquals(2, menus.size());
		assertEquals("Pizza Margherita", menus.get(0).getName());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }) // Explicit username and roles
	@Transactional
	void testSaveByAdmin() throws Exception {
		mockMvc.perform(post("/menu/restaurant/" + restaurantId).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + jwtAdmin).content(objectMapper.writeValueAsString(menuDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testSaveByOwner() throws Exception {
		mockMvc.perform(post("/menu/owner").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + jwtOwner).content(objectMapper.writeValueAsString(menuDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testUpdateMenu() throws Exception {
		mockMvc.perform(put("/menu/update/" + menu.getId()).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + jwtOwner).content(objectMapper.writeValueAsString(menuDTO)))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@Transactional
	void testDeleteMenu() throws Exception {
		mockMvc.perform(delete("/menu/" + menu.getId()).header("Authorization", "Bearer " + jwtAdmin))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testGetAllMenusByRestaurantId() throws Exception {
		mockMvc.perform(get("/menu/restaurant/" + restaurantId).header("Authorization", "Bearer " + jwtOwner)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testGetAllMenusByOwner() throws Exception {
		mockMvc.perform(get("/menu").header("Authorization", "Bearer " + jwtOwner)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testFindById() throws Exception {
		mockMvc.perform(get("/menu/" + menu.getId()).header("Authorization", "Bearer " + jwtOwner)).andExpect(status().isOk());
	}
}
