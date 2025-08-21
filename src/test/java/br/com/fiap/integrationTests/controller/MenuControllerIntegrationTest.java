package br.com.fiap.integrationTests.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import br.com.fiap.application.useCases.MenuService;
import br.com.fiap.config.security.JwtTokenUtil;
import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemMenuDTO;
import br.com.fiap.dto.MenuCreateDTO;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.factory.AddressFactory;
import br.com.fiap.domain.repositories.ItemRepository;
import br.com.fiap.domain.repositories.MenuRepository;
import br.com.fiap.domain.repositories.RestaurantRepository;
import br.com.fiap.domain.repositories.RoleRepository;
import br.com.fiap.domain.repositories.UserRepository;
import br.com.fiap.domain.entities.Item;
import br.com.fiap.domain.entities.Menu;
import br.com.fiap.domain.entities.Restaurant;
import br.com.fiap.domain.entities.Role;
import br.com.fiap.domain.entities.User;
import br.com.fiap.domain.enums.EnumUserType;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@Transactional
class MenuControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MenuService menuService;

	@Autowired
	private ObjectMapper objectMapper;

	private MenuCreateDTO menuCreateDTO;
	private MenuDTO menuDTO;
	private MenuResponseDTO menuResponseDTO;
	private ItemMenuDTO itemMenuDTO;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@MockBean
	private JavaMailSender mailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private String jwtAdmin;

	private String jwtOwner;

	private Long restaurantId;

	Item item;

	private ItemDTO itemDTO;

	private User owner = new User();
	
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
		admin.setAddress(AddressFactory.getMockAddress(admin));
		admin.getUserTypesRoles().add(adminRole);
		userRepository.save(admin);

	
		owner.setLogin("owner_" + UUID.randomUUID());
		owner.setEmail("owner@owner.com");
		owner.setPassword(passwordEncoder.encode("owner123"));
		owner.setName("Owner");
		owner.setAddress(AddressFactory.getMockAddress(owner));
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

		itemDTO = new ItemDTO("Pizza Margherita", "Tradicional com molho de tomate e manjericão",
				new BigDecimal("39.90"), true, "https://exemplo.com/imagem/pizza.jpg");

		item = new Item(itemDTO, restaurant);
		item = itemRepository.save(item);

		menuDTO = new MenuDTO(restaurantId, "restauranteOwner", restaurantId);
		menuCreateDTO = new MenuCreateDTO("menu de terça", restaurantId);
	}

	@Test
	@WithMockUser(roles = "RESTAURANT_OWNER")
	void shouldCreateMenu() throws Exception {
		Mockito.when(menuService.create(any())).thenReturn(menuDTO);

		mockMvc.perform(post("/menus").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuCreateDTO)).header("Authorization", "Bearer " + jwtOwner))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(roles = "RESTAURANT_OWNER")
	void shouldCreateMenuItem() throws Exception {
		Mockito.when(menuService.create(any())).thenReturn(menuDTO);
		ItemMenuDTO itemMenuDTO = new ItemMenuDTO(item.getId(), menuDTO.id());

		mockMvc.perform(post("/menus/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(itemMenuDTO)).header("Authorization", "Bearer " + jwtOwner))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(roles = "RESTAURANT_OWNER")
	void shouldRemoveMenuItem() throws Exception {
		Mockito.when(menuService.create(any())).thenReturn(menuDTO);
		ItemMenuDTO itemMenuDTO = new ItemMenuDTO(item.getId(), menuDTO.id());
		mockMvc.perform(delete("/menus/item")
		        .content(objectMapper.writeValueAsString(itemMenuDTO))
		        .contentType(MediaType.APPLICATION_JSON) 
		        .header("Authorization", "Bearer " + jwtOwner))
		    .andExpect(status().isNoContent());

	}

	@Test
	@WithMockUser(username = "Owner", roles = { "RESTAURANT_OWNER" })
	@Transactional
	void testGetAllMenusOwner() throws Exception {
		MenuResponseDTO menu1 = new MenuResponseDTO(1L, "Menu 1", 1L, List.of());
		MenuResponseDTO menu2 = new MenuResponseDTO(2L, "Menu 2", 1L, List.of());

		List<MenuResponseDTO> menus = List.of(menu1, menu2);

		Mockito.when(menuService.findAll()).thenReturn(menus);

		mockMvc.perform(get("/menus").header("Authorization", "Bearer " + jwtOwner)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].restaurantId").value(1L));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@Transactional
	void testGetAllMenusByAdmin() throws Exception {
		mockMvc.perform(get("/menus").header("Authorization", "Bearer " + jwtAdmin)).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = "RESTAURANT_OWNER")
	void shouldReturnBadRequestWhenCreatingInvalidMenu() throws Exception {
	    MenuCreateDTO invalidMenu = new MenuCreateDTO("", restaurantId);

	    mockMvc.perform(post("/menus")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(invalidMenu))
	            .header("Authorization", "Bearer " + jwtOwner))
	        .andExpect(status().isBadRequest());
	}

	
	@Test
	@WithMockUser(roles = "RESTAURANT_OWNER")
	void shouldReturnNotFoundWhenRemovingNonexistentMenuItem() throws Exception {
	    Mockito.doThrow(new br.com.fiap.exceptions.ResourceNotFoundException("Item not found"))
	        .when(menuService).removeItemFromMenu(any());
	    
	    
	    ItemMenuDTO itemMenuDTO = new ItemMenuDTO(10L, 10L);

	    mockMvc.perform(delete("/menus/item")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(itemMenuDTO))
	            .header("Authorization", "Bearer " + jwtOwner))
	        .andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "RESTAURANT_OWNER")
	void shouldReturnEmptyListWhenNoMenusExist() throws Exception {
	    Mockito.when(menuService.findAll()).thenReturn(List.of());

	    mockMvc.perform(get("/menus")
	            .header("Authorization", "Bearer " + jwtOwner))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	@WithMockUser(roles = "USER")
	void shouldDenyAccessForUserRole() throws Exception {
	    mockMvc.perform(post("/menus")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(menuCreateDTO)))
	        .andExpect(status().isForbidden());
	}



}
