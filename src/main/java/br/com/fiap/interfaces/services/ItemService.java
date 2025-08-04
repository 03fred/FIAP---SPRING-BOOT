package br.com.fiap.interfaces.services;

import org.springframework.data.domain.Pageable;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;

public interface ItemService {
	
	void save(ItemDTO menuDTO, Long codigoRestaurante);
	
	void save(ItemDTO menuDTO);

	ItemResponseDTO getMenuById(Long id);

	void update(ItemDTO menuDTO, Long id);

	void delete(Long id);

	PaginatedResponseDTO<ItemResponseDTO> getAllMenu(Pageable pageable);
	
	PaginatedResponseDTO<ItemResponseDTO> getAllMenu(Pageable pageable, Long restaurantId);
	
}
