package br.com.fiap.interfaces.swagger;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Restaurantes")
public interface RestaurantApi {

    @Operation(summary = "Cadastrar restaurante (ADMIN)",
            description = "Permite que o administrador cadastre um restaurante para um dono de restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<Void> save(@Valid @RequestBody RestaurantDTO restaurantDTO, @PathVariable("ownerId") Long ownerId);

    @Operation(summary = "Atualizar restaurante (ADMIN)",
            description = "Permite que o administrador atualize os dados de um restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody RestaurantDTO restaurantDTO);

    @Operation(summary = "Excluir restaurante por ID (ADMIN)",
            description = "Permite que o administrador exclua um restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

    @Operation(summary = "Cadastrar restaurante (OWNER)",
            description = "Permite que o próprio dono cadastre seu restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<Void> save(@Valid @RequestBody RestaurantDTO restaurantDTO);

    @Operation(summary = "Excluir restaurante próprio (OWNER)",
            description = "Permite que o dono exclua seu próprio restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    ResponseEntity<Void> delete();

    @Operation(summary = "Listar restaurantes do dono (OWNER)",
            description = "Lista os restaurantes do dono autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes listados com sucesso")
    })
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(type = "integer", defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(type = "integer", defaultValue = "10"))
    @Parameter(in = ParameterIn.QUERY, name = "sort", array = @ArraySchema(schema = @Schema(type = "string", example = "name,asc")))
    ResponseEntity<PaginatedResponseDTO<RestaurantResponseDTO>> getARestaurants(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Listar todos os restaurantes (USER)",
            description = "Lista todos os restaurantes disponíveis para os usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes listados com sucesso")
    })
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(type = "integer", defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(type = "integer", defaultValue = "10"))
    @Parameter(in = ParameterIn.QUERY, name = "sort", array = @ArraySchema(schema = @Schema(type = "string", example = "name,asc")))
    ResponseEntity<PaginatedResponseDTO<RestaurantResponseDTO>> getAllRestaurants(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Buscar restaurante por ID (USER)",
            description = "Retorna os dados de um restaurante específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    ResponseEntity<RestaurantResponseDTO> findById(@PathVariable("ownerId") Long id);
}
