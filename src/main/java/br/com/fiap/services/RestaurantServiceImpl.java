package br.com.fiap.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.User;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private UserService userService;


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

		restaurant = getRestaurant(restaurantDTO, restaurant);
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
		return getPage(restaurantPage);
	}

	@Override
	public Restaurant getRestaurant(Long id) {
		var restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com o id: " + id));

		return restaurant;
	}

	@Override
	public void save(RestaurantDTO restaurantDTO) {
		AuthenticatedUser userAuth =  (AuthenticatedUser)   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User owner = userService.getUser(userAuth.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Owner with " + userAuth.getId() + " was not found"));

		Restaurant restaurant = new Restaurant(restaurantDTO, owner);
		restaurantRepository.save(restaurant);
	}
	

	private Restaurant getRestaurant(RestaurantDTO restaurantDTO, Restaurant restaurant) {
		restaurant.setAddress(restaurantDTO.adress());
		restaurant.setName(restaurantDTO.name());
		restaurant.setOpeningHours(restaurantDTO.openingHours());
		restaurant.setTypeKitchen(restaurantDTO.typeKitchen());
		return restaurant;
	}
	
	@Override
	public void delete() {
		AuthenticatedUser userAuth =  (AuthenticatedUser)   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Restaurant restaurant = restaurantRepository.findById(userAuth.getRestaurantId())
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant with " + userAuth.getRestaurantId() + " was not found"));
		restaurantRepository.delete(restaurant);
	}
	
	@Override
	public PaginatedResponseDTO<RestaurantResponseDTO> getRestaurants(Pageable pageable) {
		AuthenticatedUser userAuth =  (AuthenticatedUser)   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Restaurant> restaurantPage = restaurantRepository.findAllByRestaurantOwnerId(userAuth.getId(), pageable);
		return getPage(restaurantPage);
	}
	
	private PaginatedResponseDTO<RestaurantResponseDTO> getPage(Page<Restaurant> restaurantPage ) {
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

	@Override
	public Restaurant findByIdAndRestaurantOwnerId(Long restaurantId, Long userId) {
		var restaurant = restaurantRepository.findByIdAndRestaurantOwnerId(userId, restaurantId)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com o id: " + restaurantId));

		return restaurant;
	}
}
