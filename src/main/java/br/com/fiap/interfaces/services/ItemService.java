package br.com.fiap.interfaces.services;

import org.springframework.data.domain.Pageable;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;

public interface ItemService {

	void save(ItemDTO itemDTO, Long restaurantId);

	void save(ItemDTO itemDTO);

	ItemResponseDTO getItemById(Long id);

	void update(ItemDTO itemDTO, Long id);

	void delete(Long id);

	PaginatedResponseDTO<ItemResponseDTO> getAllItems(Pageable pageable);

	PaginatedResponseDTO<ItemResponseDTO> getAllItemsByRestaurant(Pageable pageable, Long restaurantId);
	
}
