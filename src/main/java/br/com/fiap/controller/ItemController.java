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

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.interfaces.services.ItemService;
import br.com.fiap.interfaces.swagger.ItemApi;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "itens", produces = {"application/json"})
public class ItemController implements ItemApi {

	@Autowired
	private ItemService itemService;

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/restaurant/{restaurantId}")
	public ResponseEntity<Void> saveByAdmin(@Valid @RequestBody ItemDTO menuDTO, @PathVariable Long restaurantId) {
	    this.itemService.save(menuDTO, restaurantId);
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@PostMapping("/owner")
	public ResponseEntity<Void> saveByOwner(@Valid @RequestBody ItemDTO menuDTO) {
	    this.itemService.save(menuDTO);
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override 
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")		
	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id, @Valid @RequestBody ItemDTO itemDTO) {
		itemService.update(itemDTO, id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}	
	
	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		itemService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
    @Override
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<PaginatedResponseDTO<ItemResponseDTO>> getAllMenus(@PathVariable("id") Long id, Pageable pageable) {
		PaginatedResponseDTO<ItemResponseDTO> restaurantPage = itemService.getAllMenu(pageable, id);
		return ResponseEntity.ok(restaurantPage);
	}

	@Override
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	@GetMapping
	public ResponseEntity<PaginatedResponseDTO<ItemResponseDTO>> getAllMenus(Pageable pageable) {
		PaginatedResponseDTO<ItemResponseDTO> restaurantPage = itemService.getAllMenu(pageable);
		return ResponseEntity.ok(restaurantPage);
	}
	
	@Override
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/{id}")
	public ResponseEntity<ItemResponseDTO> findById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(itemService.getMenuById(id));
	}
	
}
