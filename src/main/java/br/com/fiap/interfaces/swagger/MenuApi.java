package br.com.fiap.interfaces.swagger;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Cardápio")
public interface MenuApi {


	@Operation(summary = "Cadastrar novo cardápio",
			description = "Recebe os dados de um novo cardápio e o salva no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cardápio cadastrado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
	})
	ResponseEntity<Void> save(@Valid @RequestBody MenuDTO menuDTO, Long codigoProprietario);

	@Operation(summary = "Atualizar cardápio",
			description = "Atualiza os dados de um cardápio existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cardápio atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
			@ApiResponse(responseCode = "404", description = "Cardápio não encontrado")
	})
	ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody MenuDTO menuDTO);

	@Operation(summary = "Excluir cardápio",
			description = "Exclui um cardápio existente com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cardápio excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Cardápio não encontrado")
	})
	public ResponseEntity<Void> delete(@PathVariable("id") Long id);

	@Operation(summary = "Listar cardápio", description = "Lista todos os cardápios cadastrados no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cardápios listados com sucesso", content = @Content(schema = @Schema(implementation = MenuResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno ao listar cardápios", content = @Content(schema = @Schema(implementation = Map.class))) })
	@Parameter(in = ParameterIn.QUERY, description = "Página atual", name = "page", schema = @Schema(type = "integer", defaultValue = "0"))
	@Parameter(in = ParameterIn.QUERY, description = "Número total de itens por página", name = "size", schema = @Schema(type = "integer", defaultValue = "10"))
	@Parameter(in = ParameterIn.QUERY, description = "Ordenação no formato (asc|desc). " + "Por padrão é asc. "
			+ "Multiplas ordenações são suportadas.", name = "sort", array = @ArraySchema(schema = @Schema(type = "string", example = "name,asc")), required = false)
	ResponseEntity<PaginatedResponseDTO<MenuResponseDTO>> getAllMenus (@Parameter(hidden = true) Pageable pageable);

	@Operation(summary = "Buscar cardápio por ID", description = "Retorna os dados de um cardápio específico com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cardápio encontrado com sucesso", content = @Content(schema = @Schema(implementation = MenuResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Cardápio não encontrado", content = @Content(schema = @Schema(implementation = Map.class))) })
	ResponseEntity<MenuResponseDTO> findById(@PathVariable Long id);

}