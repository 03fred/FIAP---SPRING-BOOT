package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record UserAuthorizationDTO(
		@NotNull(message = "O identificador (e-mail ou login) é obrigatório")
		String identificador,
		@NotNull(message = "A senha é obrigatória")
		String password,
		Long restaurantId
){}

