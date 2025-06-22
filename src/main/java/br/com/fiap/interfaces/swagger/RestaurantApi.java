package br.com.fiap.interfaces.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.RestauranteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Restaurante")
public interface RestaurantApi {

	@PostMapping
	@Operation(summary = "Cadastrar novo restaurante",
			description = "Recebe os dados de um novo restaurante e o salva no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
	})
	ResponseEntity<Void> save(@Valid @RequestBody RestauranteDTO restauranteDTO, Long codigoProprietario);
	
	@PutMapping("/{id}")
	@Operation(summary = "Atualizar restaurante",
			description = "Atualiza os dados de um restaurante existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "restaurante atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
			@ApiResponse(responseCode = "404", description = "restaurante não encontrado")
	})
	ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody RestauranteDTO restauranteDTO);
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir restaurante",
			description = "Exclui um restaurante existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "restaurante não encontrado")
	})
	public ResponseEntity<Void> delete(@PathVariable("id") Long id);
	
}