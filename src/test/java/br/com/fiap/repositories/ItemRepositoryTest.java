package br.com.fiap.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.factory.AddressFactory;
import br.com.fiap.domain.repositories.ItemRepository;
import br.com.fiap.domain.repositories.RestaurantRepository;
import br.com.fiap.domain.repositories.RoleRepository;
import br.com.fiap.domain.repositories.UserRepository;
import br.com.fiap.domain.entities.Item;
import br.com.fiap.domain.entities.Restaurant;
import br.com.fiap.domain.entities.Role;
import br.com.fiap.domain.entities.User;

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@DataJpaTest
class ItemRepositoryTest {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldFindItemById() {
		Restaurant restaurant = createRestaurant();
		ItemDTO itemDTO = new ItemDTO("Pizza Margherita", "Clássica pizza italiana", new BigDecimal(39.90), true,
				"pizza.jpg");
		Item item = new Item(itemDTO, restaurant);
		item = itemRepository.save(item);
		Optional<Item> result = itemRepository.findById(item.getId());
		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo("Pizza Margherita");
	}

	@Test
	void shouldFindAllItemsByRestaurantId() {
		Restaurant restaurant = createRestaurant();
		ItemDTO itemDTO = new ItemDTO("Pizza Margherita", "Clássica pizza italiana", new BigDecimal(39.90), true,
				"pizza.jpg");
		Item item1 = new Item(itemDTO, restaurant);
		itemRepository.save(item1);

		itemDTO = new ItemDTO("Pizza Mussarella", "Clássica pizza italiana", new BigDecimal(39.90), true, "pizza.jpg");
		Item item2 = new Item(itemDTO, restaurant);
		itemRepository.save(item2);
		Page<Item> result = itemRepository.findAllByRestaurantId(restaurant.getId(), PageRequest.of(0, 10));
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent()).extracting(Item::getName).containsExactlyInAnyOrder("Pizza Margherita", "Pizza Mussarella");
	}

	private Restaurant createRestaurant() {

		Restaurant restaurant = new Restaurant();
		Role ownerRole = new Role();
		ownerRole.setName("RESTAURANT_OWNER");
		roleRepository.save(ownerRole);

		User owner = new User();
		owner.setLogin("owner_" + UUID.randomUUID());
		owner.setEmail("owner@owner.com");
		owner.setPassword("owner123");
		owner.setName("Owner");
		owner.setAddress(AddressFactory.getMockAddress(owner));
		owner.getUserTypesRoles().add(ownerRole);
		owner = userRepository.save(owner);

		restaurant.setName("Nome do restaurante");
		restaurant.setOpeningTime(LocalTime.of(9, 0));
		restaurant.setClosingTime(LocalTime.of(22, 0));
		restaurant.setRestaurantOwner(owner);
		restaurant.setAddress("rua teste");
		restaurant.setTypeKitchen("ITALIAN");
		return restaurantRepository.save(restaurant);

	}
}
