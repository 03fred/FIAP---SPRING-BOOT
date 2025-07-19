package br.com.fiap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.interfaces.services.MenuService;
import br.com.fiap.interfaces.swagger.MenuApi;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/menu", produces = {"application/json"})
public class MenuController implements MenuApi {

	@Autowired
	private MenuService menuService;
	
	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody MenuDTO menuDTO, Long codigoRestaurante) {
		this.menuService.save(menuDTO, codigoRestaurante);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody MenuDTO menuDTO) {
		this.menuService.update(menuDTO, id);
		return ResponseEntity.status(204).build();
	}

	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		menuService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<PaginatedResponseDTO<MenuResponseDTO>> getAllMenus(Pageable pageable) {
		PaginatedResponseDTO<MenuResponseDTO> restaurantPage = menuService.getAllMenu(pageable);
		return ResponseEntity.ok(restaurantPage);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<MenuResponseDTO> findById(Long id) {
		return ResponseEntity.ok(menuService.getMenuById(id));
	}


}
