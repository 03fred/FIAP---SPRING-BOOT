package br.com.fiap.controller;

import br.com.fiap.dto.RestauranteDTO;
import br.com.fiap.interfaces.services.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurante", produces = {"application/json"})
@Tag(name = "Restaurante")
public class RestauranteController {

	@Autowired
	private RestauranteService restauranteService;
	
	@PostMapping
	@Operation(summary = "Cadastrar novo restaurante",
			description = "Recebe os dados de um novo restaurante e o salva no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
	})
	public ResponseEntity<Void> save(@Valid @RequestBody RestauranteDTO restauranteDTO, Long codigoProprietario) {
		this.restauranteService.save(restauranteDTO, codigoProprietario);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Atualizar restaurante",
			description = "Atualiza os dados de um restaurante existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "restaurante atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
			@ApiResponse(responseCode = "404", description = "restaurante não encontrado")
	})
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody RestauranteDTO restauranteDTO) {
		this.restauranteService.update(restauranteDTO, id);
		return ResponseEntity.status(204).build();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir restaurante",
			description = "Exclui um restaurante existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "restaurante não encontrado")
	})
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		restauranteService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
