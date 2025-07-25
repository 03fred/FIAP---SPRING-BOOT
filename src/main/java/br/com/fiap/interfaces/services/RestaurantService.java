package br.com.fiap.interfaces.services;

import org.springframework.data.domain.Pageable;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.model.Restaurant;

public interface RestaurantService {
	
	void save(RestaurantDTO restaurantDTO, Long ownerId);
	
	void save(RestaurantDTO restaurantDTO);
	
	RestaurantResponseDTO getRestaurantById(Long id);

	void update(RestaurantDTO restaurantDTO, Long id);

	void delete(Long id);

	PaginatedResponseDTO<RestaurantResponseDTO> getAllRestaurants(Pageable pageable);

	Restaurant getRestaurant(Long id);

	void delete();
	
	public PaginatedResponseDTO<RestaurantResponseDTO> getRestaurants(Pageable pageable);

    Restaurant findByIdAndRestaurantOwnerId(Long restaurantId, Long userId);
}
