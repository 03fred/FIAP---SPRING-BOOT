package br.com.fiap.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.exceptions.BusinessException;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.exceptions.UnauthorizedException;
import br.com.fiap.interfaces.repositories.MenuRepository;
import br.com.fiap.interfaces.services.MenuService;
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.Menu;
import br.com.fiap.model.Restaurant;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RestaurantService restaurantService;


	@Override
	public void save(MenuDTO menuDTO, Long restaurantId) {
		Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
		this.save(restaurant, menuDTO);
	}
	
	@Override
	public void save(MenuDTO menuDTO) {
		AuthenticatedUser userAuth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(Objects.isNull(userAuth.getRestaurantId())){
			throw new BusinessException("Restaurante não existe no token de acesso. Favor renove suas credenciais e tente novamente.");
		}
		
		Restaurant restaurant = restaurantService.getRestaurant(userAuth.getRestaurantId());
		this.save(restaurant, menuDTO);
	}

	private void save(Restaurant restaurant, MenuDTO menuDTO) {
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

		 checkPermissionMenuUpdateDelete(menu);
		
		menu.setName(menuDTO.name());
		menu.setDescription(menuDTO.description());
		menu.setAvailability(menuDTO.availability());
		menu.setPrice(menuDTO.price());
		menu.setPhoto(menuDTO.photo());

		menuRepository.save(menu);
	}
	
	private void checkPermissionMenuUpdateDelete(Menu menu) {
		AuthenticatedUser userAuth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		
		if (!userAuth.hasRoleAdmin() && !menu.getRestaurant().getRestaurantOwner().getId().equals(userAuth.getId())) {
			throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
		}
	}

	@Override
	public void delete(Long id) {
		Menu menu = menuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cardápio não encontrado com o id: " + id + "."));

		checkPermissionMenuUpdateDelete(menu);
		
		menuRepository.delete(menu);
	}

	@Override
	public PaginatedResponseDTO<MenuResponseDTO> getAllMenu(Pageable pageable) {
		AuthenticatedUser userAuth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Page<Menu> menuPage = menuRepository.findAllByRestaurantId(userAuth.getRestaurantId(), pageable);

		return getPage(menuPage);
	}
	
	@Override
	public PaginatedResponseDTO<MenuResponseDTO> getAllMenu(Pageable pageable, Long restaurantId) {
		Page<Menu> menuPage = menuRepository.findAll(pageable);
		return getPage(menuPage);
	}

	private PaginatedResponseDTO<MenuResponseDTO> getPage(Page<Menu> menuPage) {
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
