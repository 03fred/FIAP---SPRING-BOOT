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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/restaurante", produces = {"application/json"})
@Tag(name = "Restaurante")
public class RestauranteController implements RestaurantApi{

	@Autowired
	private RestaurantService restaurantService;
	
	@Override
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody RestaurantDTO restaurantDTO, Long codigoProprietario) {
		this.restaurantService.save(restaurantDTO, codigoProprietario);
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
		PaginatedResponseDTO<RestaurantResponseDTO> restaurantPage = restaurantService.getAllUsers(pageable);
		return ResponseEntity.ok(restaurantPage);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<RestaurantResponseDTO> findById(Long id) {
		return ResponseEntity.ok(restaurantService.getRestaurantById(id));
	}


}
