package br.com.fiap.gateways;

import java.util.List;

import br.com.fiap.domain.entities.Restaurant;

public interface RestaurantRepository {

	Restaurant save(Restaurant restaurant);

	Restaurant findById(Long id);

	Restaurant findByIdAndRestaurantOwnerId(Long restaurantId, Long ownerId);

	void deleteById(Long id);

	public List<Restaurant> findAll(int page, int size);
}