package br.com.fiap.interfaces.swagger;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
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

@Tag(name = "Cardápio")
public interface MenuApi {

    @Operation(summary = "Cadastrar cardápio (ADMIN)",
            description = "Permite que o administrador cadastre um cardápio em qualquer restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cardápio cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    ResponseEntity<Void> saveByAdmin(@Valid @RequestBody MenuDTO menuDTO,
                                     @PathVariable("restaurantId") Long restaurantId);

    @Operation(summary = "Cadastrar cardápio (REST. OWNER)",
            description = "Permite ao dono do restaurante cadastrar cardápios para seu próprio restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cardápio cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    ResponseEntity<Void> saveByOwner(@Valid @RequestBody MenuDTO menuDTO);

    @Operation(summary = "Atualizar cardápio",
            description = "Atualiza um cardápio existente com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cardápio atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cardápio não encontrado")
    })
    ResponseEntity<Map<String, String>> update(@PathVariable("id") Long id,
                                               @Valid @RequestBody MenuDTO menuDTO);

    @Operation(summary = "Excluir cardápio",
            description = "Exclui um cardápio com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cardápio excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cardápio não encontrado")
    })
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

    @Operation(summary = "Listar todos os cardápios do sistema (ADMIN)",
            description = "Lista todos os cardápios cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cardápios listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao listar cardápios")
    })
    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Página atual", schema = @Schema(type = "integer", defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Itens por página", schema = @Schema(type = "integer", defaultValue = "10"))
    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Ordenação", array = @ArraySchema(schema = @Schema(type = "string", example = "name,asc")))
    ResponseEntity<PaginatedResponseDTO<MenuResponseDTO>> getAllMenus(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Listar cardápios por restaurante (USER)",
            description = "Lista os cardápios de um restaurante específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cardápios listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    ResponseEntity<PaginatedResponseDTO<MenuResponseDTO>> getAllMenus(@PathVariable("id") Long id,
                                                                       @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Buscar cardápio por ID",
            description = "Retorna os dados de um cardápio específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cardápio encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cardápio não encontrado")
    })
    ResponseEntity<MenuResponseDTO> findById(@PathVariable Long id);
}
