package br.com.fiap.services;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public void save(RestaurantDTO restaurantDTO, Long ownerId) {
		User owner = userService.getUser(ownerId).orElseThrow(
						() -> new ResourceNotFoundException("Owner with " + ownerId + " was not found"));

		Restaurant restaurant = new Restaurant(restaurantDTO, owner);
		restaurantRepository.save(restaurant);
	}

	@Override
	public RestaurantResponseDTO getRestaurantById(Long id) {
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant with " + id + " was not found"));

		return new RestaurantResponseDTO(
				restaurant.getName(),
				restaurant.getAddress(),
				restaurant.getTypeKitchen(),
				restaurant.getOpeningHours()
		);
	}

	@Override
	public void update(RestaurantDTO restaurantDTO, Long id) {
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant with " + id + " was not found"));

		restaurant.setAddress(restaurantDTO.adress());
		restaurant.setName(restaurantDTO.name());
		restaurant.setOpeningHours(restaurantDTO.openingHours());
		restaurant.setTypeKitchen(restaurantDTO.typeKitchen());

		restaurantRepository.save(restaurant);
	}

	@Override
	public void delete(Long id) {
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Restaurant with " + id + " was not found"));
		restaurantRepository.delete(restaurant);
	}

	@Override
	public PaginatedResponseDTO<RestaurantResponseDTO> getAllRestaurants(Pageable pageable) {
		Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageable);

		List<RestaurantResponseDTO> restaurantResponseDTOS = restaurantPage.getContent().stream()
				.map(restaurant -> new RestaurantResponseDTO(
						restaurant.getName(),
						restaurant.getAddress(),
						restaurant.getOpeningHours(),
						restaurant.getTypeKitchen()))
				.collect(Collectors.toList());

		return new PaginatedResponseDTO<>(
				restaurantResponseDTOS,
				restaurantPage.getTotalElements(),
				restaurantPage.getNumber(),
				restaurantPage.getSize()
		);
	}
}
