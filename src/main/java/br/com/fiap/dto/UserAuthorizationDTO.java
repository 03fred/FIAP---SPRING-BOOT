package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record UserAuthorizationDTO(
		@NotNull(message = "O Email não pode ser null") 
		String email,
		@NotNull(message = "A senha não pode ser null") 
		String password) {
}
