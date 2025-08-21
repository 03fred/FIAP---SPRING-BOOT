package br.com.fiap.application.useCases;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.exceptions.BusinessException;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.exceptions.UnauthorizedException;
import br.com.fiap.domain.gateways.ItemDatabaseGateway;
import br.com.fiap.application.useCases.ItemService;
import br.com.fiap.application.useCases.RestaurantService;
import br.com.fiap.domain.entities.AuthenticatedUser;
import br.com.fiap.domain.entities.Item;
import br.com.fiap.domain.entities.Restaurant;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemDatabaseGateway itemDatabaseGateway;

	@Autowired
	private RestaurantService restaurantService;

	@Override
	public void save(ItemDTO itemDTO, Long restaurantId) {
		Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
		this.save(restaurant, itemDTO);
	}

	@Override
	public void save(ItemDTO itemDTO) {
		AuthenticatedUser userAuth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (Objects.isNull(userAuth.getRestaurantId())) {
			throw new BusinessException("Restaurante não existe no token de acesso. Favor renove suas credenciais e tente novamente.");
		}

		Restaurant restaurant = restaurantService.getRestaurant(userAuth.getRestaurantId());
		this.save(restaurant, itemDTO);
	}

	private void save(Restaurant restaurant, ItemDTO itemDTO) {
		if (restaurant != null) {
			Item item = new Item(itemDTO, restaurant);
			itemDatabaseGateway.save(item);
		}
	}

	@Override
	public ItemResponseDTO getItemById(Long id) {
		Item item = itemDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o id: " + id));

		return new ItemResponseDTO(
				item.getName(),
				item.getDescription(),
				item.getAvailability(),
				item.getPrice(),
				item.getPhoto()
		);
	}

	@Override
	public void update(ItemDTO itemDTO, Long id) {
		Item item = itemDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o id: " + id));

		checkPermissionItemUpdateDelete(item);

		item.setName(itemDTO.name());
		item.setDescription(itemDTO.description());
		item.setAvailability(itemDTO.availability());
		item.setPrice(itemDTO.price());
		item.setPhoto(itemDTO.photo());

		itemDatabaseGateway.save(item);
	}

	private void checkPermissionItemUpdateDelete(Item item) {
		AuthenticatedUser userAuth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!userAuth.hasRoleAdmin() && !item.getRestaurant().getRestaurantOwner().getId().equals(userAuth.getId())) {
			throw new UnauthorizedException("Este item não pertence ao restaurante do usuário logado.");
		}
	}

	@Override
	public void delete(Long id) {
		Item item = itemDatabaseGateway.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o id: " + id));

		checkPermissionItemUpdateDelete(item);

		itemDatabaseGateway.delete(item);
	}

	@Override
	public PaginatedResponseDTO<ItemResponseDTO> getAllItems(Pageable pageable) {
		AuthenticatedUser userAuth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Item> itemPage = itemDatabaseGateway.findAllByRestaurantId(userAuth.getRestaurantId(), pageable);

		return buildPaginatedResponse(itemPage);
	}

	@Override
	public PaginatedResponseDTO<ItemResponseDTO> getAllItemsByRestaurant(Pageable pageable, Long restaurantId) {
		Page<Item> itemPage = itemDatabaseGateway.findAllByRestaurantId(restaurantId, pageable);
		return buildPaginatedResponse(itemPage);
	}

	private PaginatedResponseDTO<ItemResponseDTO> buildPaginatedResponse(Page<Item> itemPage) {
		List<ItemResponseDTO> items = itemPage.getContent().stream()
				.map(item -> new ItemResponseDTO(
						item.getName(),
						item.getDescription(),
						item.getAvailability(),
						item.getPrice(),
						item.getPhoto()))
				.collect(Collectors.toList());

		return new PaginatedResponseDTO<>(
				items,
				itemPage.getTotalElements(),
				itemPage.getNumber(),
				itemPage.getSize()
		);
	}
}
