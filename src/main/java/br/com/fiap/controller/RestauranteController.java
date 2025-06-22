package br.com.fiap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.dto.RestauranteDTO;
import br.com.fiap.interfaces.services.RestauranteService;
import br.com.fiap.interfaces.swagger.RestaurantApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/restaurante", produces = {"application/json"})
@Tag(name = "Restaurante")
public class RestauranteController implements RestaurantApi{

	@Autowired
	private RestauranteService restauranteService;
	
	@Override 
	public ResponseEntity<Void> save(@Valid @RequestBody RestauranteDTO restauranteDTO, Long codigoProprietario) {
		this.restauranteService.save(restauranteDTO, codigoProprietario);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Override 
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody RestauranteDTO restauranteDTO) {
		this.restauranteService.update(restauranteDTO, id);
		return ResponseEntity.status(204).build();
	}

	@Override 
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		restauranteService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
