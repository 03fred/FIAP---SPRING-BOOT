package br.com.fiap.interfaces.services;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import org.springframework.data.domain.Pageable;

public interface MenuService {
	
	void save(MenuDTO menuDTO, Long codigoRestaurante);

	MenuResponseDTO getMenuById(Long id);

	void update(MenuDTO menuDTO, Long id);

	void delete(Long id);

	PaginatedResponseDTO<MenuResponseDTO> getAllMenu(Pageable pageable);
}
