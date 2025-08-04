package br.com.fiap.services;

import static br.com.fiap.factory.RestaurantFactory.createRestaurant;
import static br.com.fiap.factory.RestaurantFactory.createRestaurantDTO;
import static br.com.fiap.factory.UserFactory.createUserMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.factory.ItemFactory;
import br.com.fiap.interfaces.repositories.ItemRepository;
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.Item;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.enums.EnumUserType;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

	@Mock
	private ItemRepository ItemRepository;

	@Mock
	private RestaurantService restaurantService;

	@InjectMocks
	private ItemServiceImpl ItemServiceImpl;

	private AutoCloseable openMocks;

	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		setupSecurityContext("testuser", "ADMIN");
	}

	private void setupSecurityContext(String username, String role) {
		Set<Role> userTypesRoles = new HashSet<>();
		userTypesRoles.add(new Role(EnumUserType.ADMIN.toString()));

		AuthenticatedUser authenticatedUser = new AuthenticatedUser(1L, username, role, 1L, userTypesRoles);

		Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null,
				authenticatedUser.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	public void shouldSaveMenuWithOwnerId() {

		Item item = ItemFactory.createItem(1L);
		ItemDTO itemDTO = ItemFactory.createMenuDTO();
		when(restaurantService.getRestaurant(item.getId())).thenReturn(createRestaurant());
		ItemServiceImpl.save(itemDTO, item.getId());
		verify(ItemRepository, times(1)).save(any(Item.class));
	}

	@Test
	public void shouldReturnMenuById() {
		Item item = ItemFactory.createItem(1L);
		when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));
		ItemResponseDTO result = ItemServiceImpl.getMenuById(1L);
		assertEquals(item.getName(), result.name());
		assertEquals(item.getDescription(), result.description());
		assertEquals(item.getAvailability(), result.availability());
		assertEquals(item.getPrice(), result.price());
		assertEquals(item.getPhoto(), result.photo());
	}

	@Test
	public void shouldThrowWhenMenuNotFoundById() {
		when(ItemRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> ItemServiceImpl.getMenuById(1L));
	}

	@Test
	public void shouldUpdateMenu() {
		Item item = ItemFactory.createItem(1L);
		ItemDTO itemDTO = ItemFactory.createMenuDTO();

		when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));

		ItemServiceImpl.update(itemDTO, 1L);

		verify(ItemRepository, times(1)).save(item);
		assertEquals(itemDTO.name(), item.getName());
		assertEquals(itemDTO.description(), item.getDescription());
		assertEquals(itemDTO.availability(), item.getAvailability());
		assertEquals(itemDTO.price(), item.getPrice());
		assertEquals(itemDTO.photo(), item.getPhoto());
	}

	@Test
	public void shouldThrowWhenUpdatingNonexistentMenu() {
		ItemDTO itemDTO = ItemFactory.createMenuDTO();
		when(ItemRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> ItemServiceImpl.update(itemDTO, 1L));
	}

	@Test
	public void shouldDeleteMenu() {
		Item item = ItemFactory.createItem(1L);
		when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));

		ItemServiceImpl.delete(1L);

		verify(ItemRepository, times(1)).delete(item);
	}

	@Test
	public void shouldThrowWhenDeletingNonexistentMenu() {
		when(ItemRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> ItemServiceImpl.delete(1L));
	}

	@Test
	public void shouldListAllMenus() {

		Long authenticatedRestaurantId = 1L;

		Item item = ItemFactory.createItem(1L);
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1l);

		item.setRestaurant(restaurant);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Item> itemPage = new PageImpl<>(List.of(item));
		when(ItemRepository.findAllByRestaurantId(authenticatedRestaurantId, pageable)).thenReturn(itemPage);

		PaginatedResponseDTO<ItemResponseDTO> response = ItemServiceImpl.getAllMenu(pageable); // Era ItemServiceImpl

		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		assertEquals(1, response.getContent().size());
		assertEquals(item.getName(), response.getContent().get(0).name());

		verify(ItemRepository).findAllByRestaurantId(authenticatedRestaurantId, pageable);
	}

	@Test
	void testAllArgsConstructorAndGetters() {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1L);
		restaurant.setName("Restaurante X");

		Item item = new Item(ItemFactory.createMenuDTO(), restaurant);

		assertEquals("Hamburguer", item.getName());
		assertEquals(restaurant, item.getRestaurant());
	}

	@Test
	void testEqualsAndHashCode() {
		Restaurant restaurant = createRestaurant();
		Item item1 = new Item(ItemFactory.createMenuDTO(), restaurant);
		Item item2 = new Item(ItemFactory.createMenuDTO(), restaurant);
		assertEquals(item1, item2);
		assertEquals(item2.hashCode(), item2.hashCode());
	}

	@Test
	void testToString() {
		Restaurant restaurant = new Restaurant(createRestaurantDTO(), createUserMock());
		Item item = new Item(ItemFactory.createMenuDTO(), restaurant);
		assertTrue(item.toString().contains("Item"));
		assertTrue(item.toString().contains("Lanche"));
	}

}