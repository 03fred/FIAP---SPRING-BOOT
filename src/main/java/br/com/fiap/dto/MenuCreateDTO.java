package br.com.fiap.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MenuCreateDTO(
		@NotNull(message = "O titulo precisa ser informado")
		@NotEmpty
		String title,
		Long restaurantId
		) {}