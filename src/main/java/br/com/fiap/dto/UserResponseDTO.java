package br.com.fiap.dto;

public record UserResponseDTO(
		Long id,
		String email,
		String name,
		String address) {
}
