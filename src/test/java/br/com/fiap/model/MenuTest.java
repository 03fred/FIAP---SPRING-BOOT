package br.com.fiap.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

	@Test
	void shouldCreateMenuAndAssignValues() {
	
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1L);
		restaurant.setName("Restaurante Teste");

		Item item1 = new Item();
		item1.setId(1L);
		item1.setName("Item 1");

		Item item2 = new Item();
		item2.setId(2L);
		item2.setName("Item 2");

		Set<Item> items = new HashSet<>();
		items.add(item1);
		items.add(item2);


		Menu menu = new Menu();
		menu.setId(100L);
		menu.setTitle("Menu Executivo");
		menu.setRestaurant(restaurant);
		menu.setItems(items);


		assertEquals(100L, menu.getId());
		assertEquals("Menu Executivo", menu.getTitle());
		assertEquals(restaurant, menu.getRestaurant());
		assertEquals(2, menu.getItems().size());
		assertTrue(menu.getItems().contains(item1));
		assertTrue(menu.getItems().contains(item2));
	}
}
