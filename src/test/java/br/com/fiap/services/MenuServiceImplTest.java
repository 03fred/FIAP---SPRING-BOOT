package br.com.fiap.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.fiap.dto.ItemMenuDTO;
import br.com.fiap.dto.MenuCreateDTO;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.exceptions.UnauthorizedException;
import br.com.fiap.interfaces.repositories.ItemRepository;
import br.com.fiap.interfaces.repositories.MenuRepository;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.Item;
import br.com.fiap.model.Menu;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.enums.EnumUserType;
import jakarta.persistence.EntityNotFoundException;

class MenuServiceImplTest {

	@InjectMocks
	private MenuServiceImpl menuService;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	private AutoCloseable openMocks;


	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		setupSecurityContext("testuser", EnumUserType.ADMIN.toString(), 1L);
	}

	private void setupSecurityContext(String username, String role, Long restaurantId) {
		Set<Role> userTypesRoles = new HashSet<>();
		userTypesRoles.add(new Role(role));

		AuthenticatedUser authenticatedUser = new AuthenticatedUser(1L, username, role, restaurantId, userTypesRoles);

		Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null,
				authenticatedUser.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	@Test
	void shouldCreateMenu() {
		MenuCreateDTO dto = new MenuCreateDTO("Menu Test", 1L);
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1L);

		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> {
			Menu m = invocation.getArgument(0);
			m.setId(1L);
			return m;
		});

		MenuDTO result = menuService.create(dto);

		assertEquals("Menu Test", result.title());
		assertEquals(1L, result.restaurantId());
		verify(menuRepository, times(1)).save(any(Menu.class));
	}

	@Test
	void shouldFindAllMenus() {
		Menu menu = new Menu();
		menu.setId(1L);
		menu.setTitle("Lunch");
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1L);
		menu.setRestaurant(restaurant);
		
		when(menuRepository.findAll()).thenReturn(List.of(menu));

		List<MenuResponseDTO> all = menuService.findAll();

		assertEquals(1, all.size());
		assertEquals("Lunch", all.get(0).title());
	}

	@Test
	void shouldThrowWhenMenuNotFound() {
		when(menuRepository.findById(999L)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> menuService.findById(999L));
	}

	@Test
	void shouldUpdateMenu() {
		MenuCreateDTO dto = new MenuCreateDTO("New Title", 1L);

		Restaurant restaurant = new Restaurant();
		restaurant.setId(1L);

		Menu menu = new Menu();
		menu.setId(1L);
		menu.setRestaurant(restaurant);

		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRepository.save(any(Menu.class))).thenAnswer(i -> i.getArgument(0));

		MenuDTO updated = menuService.update(1L, dto);

		assertEquals("New Title", updated.title());
	}

	@Test
	void shouldDeleteMenu() {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1L);

		Menu menu = new Menu();
		menu.setId(1L);
		menu.setRestaurant(restaurant);

		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

		assertDoesNotThrow(() -> menuService.delete(1L));
		verify(menuRepository).delete(menu);
	}

	@Test
	void shouldThrowWhenUnauthorizedOnDelete() {
		setupSecurityContext("testuser_restaurantowner", EnumUserType.RESTAURANT_OWNER.toString(), 1L);
		
		Restaurant restaurant = new Restaurant();
		restaurant.setId(2L); // Not same as user's restaurantId

		Menu menu = new Menu();
		menu.setId(1L);
		menu.setRestaurant(restaurant);

		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

		assertThrows(UnauthorizedException.class, () -> menuService.delete(1L));
	}

	@Test
	void shouldAddItemToMenu() {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1L);

		Menu menu = new Menu();
		menu.setId(1L);
		menu.setRestaurant(restaurant);


		Item item = new Item();
		item.setId(1L);
		item.setRestaurant(restaurant);

		ItemMenuDTO dto = new ItemMenuDTO(1L, 1L);

		when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
		when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

		assertDoesNotThrow(() -> menuService.addItemToMenu(dto));
		assertEquals(1, menu.getItems().size());
		verify(menuRepository).save(menu);
	}
}
