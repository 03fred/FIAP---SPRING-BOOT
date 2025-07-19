package br.com.fiap.dto;

import jakarta.validation.constraints.NotBlank;

public record UserTypeDTO(

		@NotBlank(message = "O login é obrigatório") String login,

		@NotBlank(message = "O permissão é obrigatória") String role

) {}
