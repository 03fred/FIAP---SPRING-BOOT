package br.com.fiap.services;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.MenuRepository;
import br.com.fiap.interfaces.services.MenuService;
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.model.Menu;
import br.com.fiap.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RestaurantService restaurantService;


	@Override
	public void save(MenuDTO menuDTO, Long codigoRestaurante) {
		Restaurant restaurant = restaurantService.getRestaurant(codigoRestaurante);
		if (restaurant != null) {
			Menu menu = new Menu(menuDTO, restaurant);
			menuRepository.save(menu);
		}
	}

	@Override
	public MenuResponseDTO getMenuById(Long id) {
		var menu = menuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cardápio não encontrado com o id: " + id));

		return new MenuResponseDTO(
				menu.getName(),
				menu.getDescription(),
				menu.getAvailability(),
				menu.getPrice(),
				menu.getPhoto()
		);
	}

	@Override
	public void update(MenuDTO menuDTO, Long id) {
		Menu menu = menuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cardápio não encontrado com o id: " + id));

		menu.setName(menuDTO.name());
		menu.setDescription(menuDTO.description());
		menu.setAvailability(menuDTO.availability());
		menu.setPrice(menuDTO.price());
		menu.setPhoto(menuDTO.photo());

		menuRepository.save(menu);
	}

	@Override
	public void delete(Long id) {
		Menu menu = menuRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Cardápio não encontrado com o id: " + id + "."));
		menuRepository.delete(menu);
	}

	@Override
	public PaginatedResponseDTO<MenuResponseDTO> getAllMenu(Pageable pageable) {
		Page<Menu> menuPage = menuRepository.findAll(pageable);
		List<MenuResponseDTO> menuResponseDTOS = menuPage.getContent().stream()
				.map(menu -> new MenuResponseDTO(menu.getName(), menu.getDescription(), menu.getAvailability(),
						menu.getPrice(), menu.getPhoto()))
				.collect(Collectors.toList());

		return new PaginatedResponseDTO<>(
				menuResponseDTOS,
				menuPage.getTotalElements(),
				menuPage.getNumber(),
				menuPage.getSize()
		);
	}
}
