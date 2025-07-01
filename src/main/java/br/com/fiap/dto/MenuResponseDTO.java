package br.com.fiap.dto;

public record MenuResponseDTO(
		String name,
		String description,
		String availability,
		String price,
		String photo) {
}
