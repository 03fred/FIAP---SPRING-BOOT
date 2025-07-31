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
import br.com.fiap.dto.ItemDTO;
import br.com.fiap.interfaces.repositories.ItemRepository;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Item;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@Transactional
public class ItemControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ItemRepository itemRepository;

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

	Item item;

	private ItemDTO itemDTO;

	@BeforeEach
	void setup() {
		itemRepository.deleteAll();
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

		itemDTO = new ItemDTO("Pizza Margherita", "Tradicional com molho de tomate e manjericão", "39.90", "true",
				"https://exemplo.com/imagem/pizza.jpg");

		item = new Item(itemDTO, restaurant);
		item = itemRepository.save(item); // agora temos o ID real
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }) // Explicit username and roles
	@Transactional
	void givenValidItemDTO_whenAdminCreatesItem_thenReturnCreated() throws Exception {
		mockMvc.perform(post("/itens/restaurant/" + restaurantId).header("Authorization", "Bearer " + jwtAdmin)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(itemDTO)))
				.andExpect(status().isCreated());

		List<Item> menus = itemRepository.findAll();
		assertEquals(2, menus.size());
		assertEquals("Pizza Margherita", menus.get(0).getName());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }) // Explicit username and roles
	@Transactional
	void testSaveByAdmin() throws Exception {
		mockMvc.perform(post("/itens/restaurant/" + restaurantId).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + jwtAdmin).content(objectMapper.writeValueAsString(itemDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testSaveByOwner() throws Exception {
		mockMvc.perform(post("/itens/owner").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + jwtOwner).content(objectMapper.writeValueAsString(itemDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testUpdateMenu() throws Exception {
		mockMvc.perform(put("/itens/update/" + item.getId()).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + jwtOwner).content(objectMapper.writeValueAsString(itemDTO)))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@Transactional
	void testDeleteMenu() throws Exception {
		mockMvc.perform(delete("/itens/" + item.getId()).header("Authorization", "Bearer " + jwtAdmin))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testGetAllMenusByRestaurantId() throws Exception {
		mockMvc.perform(get("/itens/restaurant/" + restaurantId).header("Authorization", "Bearer " + jwtOwner)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testGetAllMenusByOwner() throws Exception {
		mockMvc.perform(get("/itens").header("Authorization", "Bearer " + jwtOwner)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testFindById() throws Exception {
		mockMvc.perform(get("/itens/" + item.getId()).header("Authorization", "Bearer " + jwtOwner)).andExpect(status().isOk());
	}
}
