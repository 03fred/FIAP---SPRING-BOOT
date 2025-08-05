package br.com.fiap.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import br.com.fiap.factory.AddressFactory;
import br.com.fiap.interfaces.repositories.MenuRepository;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.repositories.RoleRepository;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.Menu;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.Role;
import br.com.fiap.model.User;

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@ActiveProfiles("test")
@DataJpaTest
class MenuRepositoryTest {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Test
	void testFindByRestaurantId() {
		

		Role ownerRole = new Role();
		ownerRole.setName("RESTAURANT_OWNER");
		roleRepository.save(ownerRole);
		
		User owner =  new User();
		owner.setLogin("owner_" + UUID.randomUUID());
		owner.setEmail("owner@owner.com");
		owner.setPassword("owner123");
		owner.setName("Owner");
		owner.setAddress(AddressFactory.getMockAddress(owner));
		owner.getUserTypesRoles().add(ownerRole);
		owner = userRepository.save(owner);

		Restaurant restaurant = new Restaurant();
		restaurant.setName("Nome do restaurante");
		restaurant.setOpeningTime(LocalTime.of(9, 0));
		restaurant.setClosingTime(LocalTime.of(22, 0));
		restaurant.setRestaurantOwner(owner);
		restaurant.setAddress("rua teste");
		restaurant.setTypeKitchen("ITALIAN"); 
		restaurantRepository.save(restaurant);
		
		Menu menu1 = new Menu();
		menu1.setTitle("Menu 1");
		menu1.setRestaurant(restaurant);
		menuRepository.save(menu1);

		Menu menu2 = new Menu();
		menu2.setTitle("Menu 2");
		menu2.setRestaurant(restaurant);
		menuRepository.save(menu2);


		List<Menu> menus = menuRepository.findByRestaurantId(restaurant.getId());

		assertNotNull(menus);
		assertEquals(2, menus.size());
	}
}
