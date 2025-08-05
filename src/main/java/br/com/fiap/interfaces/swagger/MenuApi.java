package br.com.fiap.interfaces.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.ItemMenuDTO;
import br.com.fiap.dto.MenuCreateDTO;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Menu")
public interface MenuApi {

	@Operation(summary = "Criar novo menu", description = "Cria um novo menu vinculado ao restaurante do dono autenticado.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Menu criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos") })
	ResponseEntity<MenuDTO> create(@Valid @RequestBody MenuCreateDTO dto);

	@Operation(summary = "Listar todos os menus", description = "Retorna a lista de menus disponíveis no sistema.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Menus retornados com sucesso"),
			@ApiResponse(responseCode = "500", description = "Erro interno") })
	ResponseEntity<List<MenuResponseDTO>> findAll();

	@Operation(summary = "Buscar menu por ID", description = "Retorna os detalhes de um menu específico.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Menu encontrado"),
			@ApiResponse(responseCode = "404", description = "Menu não encontrado") })
	ResponseEntity<MenuResponseDTO> findById(@PathVariable Long id);

	@Operation(summary = "Atualizar menu", description = "Atualiza as informações de um menu já existente.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Menu atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "404", description = "Menu não encontrado") })
	ResponseEntity<MenuDTO> update(@PathVariable Long id, @Valid @RequestBody MenuCreateDTO dto);

	@Operation(summary = "Deletar menu", description = "Remove um menu com base no ID informado.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Menu removido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Menu não encontrado") })
	ResponseEntity<Void> delete(@PathVariable Long id);

	@Operation(summary = "Adicionar item ao menu", description = "Adiciona um item existente a um menu existente, desde que pertençam ao mesmo restaurante.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Item adicionado ao menu com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "403", description = "Acesso não autorizado") })
	ResponseEntity<Void> addItensMenu(@Valid @RequestBody ItemMenuDTO dto);
	
	@Operation(summary = "Remover item do menu",
	        description = "Remove um item de um menu existente, desde que o usuário seja o dono do restaurante.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "204", description = "Item removido do menu com sucesso"),
	        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
	        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
	})
	@DeleteMapping("/item")
	@PreAuthorize("hasRole('RESTAURANT_OWNER')")
	ResponseEntity<Void> removeItensMenu(@Valid @RequestBody ItemMenuDTO dto);

}
