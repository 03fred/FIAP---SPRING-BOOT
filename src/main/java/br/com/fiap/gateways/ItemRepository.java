package br.com.fiap.gateways;

import java.util.List;

import br.com.fiap.domain.entities.Item;

public interface ItemRepository {

	Item findById(Long id);

	List<Item> findAllByRestaurantId(Long restaurantId, int page, int size);

	Item save(Item item);
	
	void delete(Item item);
}