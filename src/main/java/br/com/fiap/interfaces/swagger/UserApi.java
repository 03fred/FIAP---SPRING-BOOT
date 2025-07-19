package br.com.fiap.interfaces.swagger;


import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.PasswordUpdateDTO;
import br.com.fiap.dto.UserDTO;
import br.com.fiap.dto.UserPartialUpdateDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.dto.UserUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuários")
public interface UserApi {

	@Operation(summary = "Cadastrar novo usuário", description = "Recebe os dados de um novo usuário e o salva no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos.", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserDTO userDto);

	@Operation(summary = "Atualizar usuários", description = "Atualiza os dados de um usuário existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO);


	@Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<Map<String, String>> update(@Valid @RequestBody UserDTO userDto);

	
	@Operation(summary = "Excluir usuário", description = "Exclui um usuário existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content(schema = @Schema(implementation = Map.class))), // Even
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<Map<String, String>> delete(@PathVariable("id") Long id);

	@Operation(summary = "Listar usuários", description = "Lista todos os usuários cadastrados no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuários listados com sucesso", content = @Content(schema = @Schema(implementation = PaginatedResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno ao listar usuários", content = @Content(schema = @Schema(implementation = Map.class))) })
	@Parameter(in = ParameterIn.QUERY, description = "Página atual", name = "page", schema = @Schema(type = "integer", defaultValue = "0"))
	@Parameter(in = ParameterIn.QUERY, description = "Número total de itens por página", name = "size", schema = @Schema(type = "integer", defaultValue = "10"))
	@Parameter(in = ParameterIn.QUERY, description = "Ordenação no formato (asc|desc). " + "Por padrão é asc. "
			+ "Multiplas ordenações são suportadas.", name = "sort", array = @ArraySchema(schema = @Schema(type = "string", example = "name,asc")), required = false)
	ResponseEntity<PaginatedResponseDTO<UserResponseDTO>> getAllUsers(@Parameter(hidden = true) Pageable pageable);

	@Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<UserResponseDTO> findById(@PathVariable Long id);

	@Operation(summary = "Atualização parcial de dados do usuário",
			description = "Atualiza um ou mais campos específicos de um usuário existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Campo atualizado com sucesso", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou campo não permitido", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = Map.class)))
	})
	ResponseEntity<Map<String, String>> updatePartial(
			@PathVariable("id") Long id,
			@RequestBody UserPartialUpdateDTO dto);

	@Operation(summary = "Atualizar senha do usuário",
			description = "Altera a senha do usuário mediante validação da senha atual e confirmação da nova senha.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Senha atual incorreta ou confirmação inválida", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = Map.class)))
	})
	ResponseEntity<Map<String, String>> updatePassword(
			@PathVariable("id") Long id,
			@Valid @RequestBody PasswordUpdateDTO dto);
}


