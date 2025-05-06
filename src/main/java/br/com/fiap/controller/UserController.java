package br.com.fiap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.interfaces.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users", produces = {"application/json"})
@Tag(name = "Usuários")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping
	@Operation(summary = "Cadastrar novo usuário",
			description = "Recebe os dados de um novo usuário e o salva no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
	})
	public ResponseEntity<Void> save(@Valid @RequestBody UserDTO userDto) {
		this.userService.save(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Atualizar usuário",
			description = "Atualiza os dados de um usuário existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDto) {
		this.userService.update(userDto, id);
		return ResponseEntity.status(204).build();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir usuário",
			description = "Exclui um usuário existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
