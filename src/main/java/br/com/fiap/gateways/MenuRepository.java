package br.com.fiap.gateways;

import java.util.List;

import br.com.fiap.domain.entities.Menu;

public interface MenuRepository {

	List<Menu> findByRestaurantId(Long restaurantId, int page, int size);

	Menu findById(Long id);

	Menu save(Menu menu);

	void delete(Menu menu);
}