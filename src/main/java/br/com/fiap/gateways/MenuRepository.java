package br.com.fiap.gateways;

import java.util.List;

import br.com.fiap.domain.entities.Menu;


public interface MenuRepository {
    
	public List<Menu> findByRestaurantId(Long restaurantId, int page, int size);
}