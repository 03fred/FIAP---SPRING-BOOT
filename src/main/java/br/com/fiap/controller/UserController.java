package br.com.fiap.controller;

import java.util.List;
import java.util.Map;

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

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.interfaces.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	@Operation(summary = "Listar usuários", description = "Lista todos os usuários cadastrados no sistema.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso"),
			@ApiResponse(responseCode = "500", description = "Erro interno ao listar usuários") })

	@Parameter(in = ParameterIn.QUERY, description = "Página atual", name = "page", schema = @Schema(type = "integer", defaultValue = "0"))
	@Parameter(in = ParameterIn.QUERY, description = "Número total de itens por página", name = "size", schema = @Schema(type = "integer", defaultValue = "10"))
	@Parameter(in = ParameterIn.QUERY, description = "Ordenação no formato (asc|desc). "
			+ "Por padrão é asc. "
			+ "Multiplas ordenações são suportadas.", name = "sort", array = @ArraySchema(schema = @Schema(type = "string", example = "name,asc")),
			required = false)

	public ResponseEntity<PaginatedResponseDTO<UserResponseDTO>> getAllUsers(@Parameter(hidden = true) Pageable pageable) {
		PaginatedResponseDTO<UserResponseDTO> userPage = userService.getAllUsers(pageable);
		return ResponseEntity.ok(userPage);
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

	@GetMapping
	@Operation(summary = "Buscar todos os usuários",
			description = "Retorna uma lista com todos os usuários cadastrados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<List<UserDTO>> findAll() {
		List<UserDTO> users = userService.findAll();
		return ResponseEntity.ok(users);
	}

}
