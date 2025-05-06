package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record UserResponseDTO(
		Long id,
		String email,
		String name,
		String address) {
}
