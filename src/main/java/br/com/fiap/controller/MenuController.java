package br.com.fiap.controller;

import java.util.Map;

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
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/restaurant/{restaurantId}")
	public ResponseEntity<Void> saveByAdmin(@Valid @RequestBody MenuDTO menuDTO, @PathVariable Long restaurantId) {
	    this.menuService.save(menuDTO, restaurantId);
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@PostMapping("/owner")
	public ResponseEntity<Void> saveByOwner(@Valid @RequestBody MenuDTO menuDTO) {
	    this.menuService.save(menuDTO);
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override 
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")		
	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id, @Valid @RequestBody MenuDTO menuDTO) {
		menuService.update(menuDTO, id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}	
	
	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		menuService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
    @Override
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<PaginatedResponseDTO<MenuResponseDTO>> getAllMenus(@PathVariable("id") Long id, Pageable pageable) {
		PaginatedResponseDTO<MenuResponseDTO> restaurantPage = menuService.getAllMenu(pageable, id);
		return ResponseEntity.ok(restaurantPage);
	}

	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@GetMapping
	public ResponseEntity<PaginatedResponseDTO<MenuResponseDTO>> getAllMenus(Pageable pageable) {
		PaginatedResponseDTO<MenuResponseDTO> restaurantPage = menuService.getAllMenu(pageable);
		return ResponseEntity.ok(restaurantPage);
	}
	
	@Override
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/{id}")
	public ResponseEntity<MenuResponseDTO> findById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(menuService.getMenuById(id));
	}


}
