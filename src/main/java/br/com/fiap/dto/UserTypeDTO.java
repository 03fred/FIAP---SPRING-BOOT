package br.com.fiap.dto;

import jakarta.validation.constraints.NotBlank;

public record UserTypeDTO(

		@NotBlank(message = "O nome é obrigatório")
		String name
		
) {}
