package br.com.fiap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.interfaces.services.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;




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
			@ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos.")
	})
	public ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserDTO userDto) {
		this.userService.save(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Usuário cadastrado com sucesso!"));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Atualizar usuário",
			description = "Atualiza os dados de um usuário existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	public ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDto) {
		userService.update(userDto, id);
		return ResponseEntity.ok(Map.of("mensagem", "Usuário atualizado com sucesso."));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir usuário",
			description = "Exclui um usuário existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	public ResponseEntity<Map<String, String>> delete(@PathVariable("id") Long id){
		userService.delete(id);
		return ResponseEntity.ok(Map.of("mensagem", "Usuário excluído com sucesso."));
	}

	@GetMapping
	@Operation(summary = "Listar usuários",
			description = "Lista todos os usuários cadastrados no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuários listados com sucesso"),
			@ApiResponse(responseCode = "500", description = "Erro interno ao listar usuários")
	})
	public ResponseEntity<List<UserResponseDTO>> list() {
		return ResponseEntity.ok(userService.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar usuário por ID",
			description = "Retorna os dados de um usuário específico com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

}
