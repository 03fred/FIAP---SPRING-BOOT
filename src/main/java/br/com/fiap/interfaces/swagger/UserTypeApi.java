package br.com.fiap.interfaces.swagger;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.UserTypeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Permissões")
public interface UserTypeApi {

	@Operation(summary = "Cadastrar uma nova permissão para o usuário", description = "Recebe os dados de um usuário e permissão e salva no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Permissão cadastrada com sucesso!", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos.", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<Map<String, String>> save(@Valid @RequestBody UserTypeDTO userType);
	
	@Operation(summary = "Excluir uma permissão para o usuario", description = "Recebe os dados de um usuário e permissão e salva no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Permissão removida com sucesso!", content = @Content(schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos.", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<Map<String, String>> delete(@Valid @RequestBody UserTypeDTO userType);

}