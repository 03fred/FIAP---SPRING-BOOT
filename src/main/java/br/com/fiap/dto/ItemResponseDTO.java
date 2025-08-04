package br.com.fiap.dto;

import java.math.BigDecimal;

public record ItemResponseDTO(
		String name,
		String description,
		Boolean availability,
		BigDecimal price,
		String photo) {
}
