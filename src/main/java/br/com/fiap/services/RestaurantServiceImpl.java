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
import java.util.Optional;
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
	public void save(RestaurantDTO restaurantDTO, Long codigoProprietario) {
		Optional<User> user = userService.getUser(Long.valueOf(codigoProprietario));
		if (user.isPresent()) {
			Restaurant restaurant = new Restaurant(restaurantDTO, user.get());
			restaurantRepository.save(restaurant);
		}
	}

	@Override
	public RestaurantResponseDTO getRestaurantById(Long id) {
		var restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com o id: " + id));

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
				.orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com o id: " + id));

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
						() -> new ResourceNotFoundException("Restaurante não encontrado com o id: " + id + "."));
		restaurantRepository.delete(restaurant);
	}

	@Override
	public PaginatedResponseDTO<RestaurantResponseDTO> getAllUsers(Pageable pageable) {
		Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageable);
		List<RestaurantResponseDTO> restaurantResponseDTOS = restaurantPage.getContent().stream()
				.map(restaurant -> new RestaurantResponseDTO(restaurant.getName(), restaurant.getAddress(), restaurant.getOpeningHours(),
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
