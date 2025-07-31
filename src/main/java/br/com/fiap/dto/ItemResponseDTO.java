package br.com.fiap.dto;

public record ItemResponseDTO(
		String name,
		String description,
		String availability,
		String price,
		String photo) {
}
