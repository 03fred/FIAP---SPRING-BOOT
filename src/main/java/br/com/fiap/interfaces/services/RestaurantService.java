package br.com.fiap.interfaces.services;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
	
	void save(RestaurantDTO restaurantDTO, Long ownerId);
	
	RestaurantResponseDTO getRestaurantById(Long id);

	void update(RestaurantDTO restaurantDTO, Long id);

	void delete(Long id);

	PaginatedResponseDTO<RestaurantResponseDTO> getAllRestaurants(Pageable pageable);
}
