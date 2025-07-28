package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record UserForgotPasswordDTO(
		@NotNull(message = "O identificador (e-mail ou login) é obrigatório")
		String identificator
){}

