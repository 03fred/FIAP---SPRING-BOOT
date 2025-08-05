package br.com.fiap.interfaces.swagger;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Item")
public interface ItemApi {

    @Operation(summary = "Cadastrar item (ADMIN)",
            description = "Permite que o administrador cadastre um item em qualquer restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "item cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    ResponseEntity<Void> saveByAdmin(@Valid @RequestBody ItemDTO ItemDTO,
                                     @PathVariable("restaurantId") Long restaurantId);

    @Operation(summary = "Cadastrar item (REST. OWNER)",
            description = "Permite ao dono do restaurante cadastrar items para seu próprio restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "item cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    ResponseEntity<Void> saveByOwner(@Valid @RequestBody ItemDTO ItemDTO);

    @Operation(summary = "Atualizar item",
            description = "Atualiza um item existente com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "item não encontrado")
    })
    ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id,
                                               @Valid @RequestBody ItemDTO ItemDTO);

    @Operation(summary = "Excluir item",
            description = "Exclui um item com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "item excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "item não encontrado")
    })
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

    @Operation(summary = "Listar todos os items do sistema (ADMIN)",
            description = "Lista todos os items cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "items listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao listar items")
    })
    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Página atual", schema = @Schema(type = "integer", defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Itens por página", schema = @Schema(type = "integer", defaultValue = "10"))
    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Ordenação", array = @ArraySchema(schema = @Schema(type = "string", example = "name,asc")))
    ResponseEntity<PaginatedResponseDTO<ItemResponseDTO>> getAllMenus(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Listar items por restaurante (USER)",
            description = "Lista os items de um restaurante específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "items listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    ResponseEntity<PaginatedResponseDTO<ItemResponseDTO>> getAllMenus(@PathVariable("id") Long id,
                                                                       @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Buscar item por ID",
            description = "Retorna os dados de um item específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "item encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "item não encontrado")
    })
    ResponseEntity<ItemResponseDTO> findById(@PathVariable Long id);
}
