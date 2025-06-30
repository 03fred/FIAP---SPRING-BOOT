package br.com.fiap.controller;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.interfaces.swagger.RestaurantApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurant", produces = {"application/json"})
@Tag(name = "Restaurant")
public class RestaurantController implements RestaurantApi{

	@Autowired
	private RestaurantService restaurantService;
	
	@Override
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody RestaurantDTO restaurantDTO, @RequestParam Long ownerId) {
		System.out.println("Chamou o save! ownerId=" + ownerId);

		this.restaurantService.save(restaurantDTO, ownerId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Override
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody RestaurantDTO restaurantDTO) {
		this.restaurantService.update(restaurantDTO, id);
		return ResponseEntity.status(204).build();
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		restaurantService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping
	public ResponseEntity<PaginatedResponseDTO<RestaurantResponseDTO>> getAllRestaurants(Pageable pageable) {
		PaginatedResponseDTO<RestaurantResponseDTO> restaurantPage = restaurantService.getAllRestaurants(pageable);
		return ResponseEntity.ok(restaurantPage);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<RestaurantResponseDTO> findById(Long id) {
		return ResponseEntity.ok(restaurantService.getRestaurantById(id));
	}


}
