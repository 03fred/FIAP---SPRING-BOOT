package br.com.fiap.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import br.com.fiap.factory.AddressFactory;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@DataJpaTest
public class RestaurantRepositoryTest {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("should find restaurant by ID")
	void shouldFindById() {
		User owner = createAndSaveOwner();
		Restaurant restaurant = createAndSaveRestaurant(owner);

		Optional<Restaurant> result = restaurantRepository.findById(restaurant.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo("Nome do restaurante");
	}

	@Test
	@DisplayName("should find all restaurants by owner ID")
	void shouldFindAllByOwnerId() {
		User owner = createAndSaveOwner();
		createAndSaveRestaurant(owner);
		createAndSaveRestaurant(owner);

		Page<Restaurant> result = restaurantRepository.findAllByRestaurantOwnerId(owner.getId(), PageRequest.of(0, 10));

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
	}

	@Test
	@DisplayName("should find restaurant by ID and owner ID")
	void shouldFindByIdAndOwnerId() {
		User owner = createAndSaveOwner();
		Restaurant restaurant = createAndSaveRestaurant(owner);

		Optional<Restaurant> result = restaurantRepository.findByIdAndRestaurantOwnerId(restaurant.getId(),
				owner.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getRestaurantOwner().getId()).isEqualTo(owner.getId());
	}

	private User createAndSaveOwner() {
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
		return userRepository.save(owner);
	}

	private Restaurant createAndSaveRestaurant(User owner) {
		Restaurant restaurant = new Restaurant();
		restaurant.setName("Nome do restaurante");
		restaurant.setOpeningTime(LocalTime.of(9, 0));
		restaurant.setClosingTime(LocalTime.of(22, 0));
		restaurant.setRestaurantOwner(owner);
		restaurant.setAddress("rua teste");
		restaurant.setTypeKitchen("ITALIAN");
		return restaurantRepository.save(restaurant);
	}

}
