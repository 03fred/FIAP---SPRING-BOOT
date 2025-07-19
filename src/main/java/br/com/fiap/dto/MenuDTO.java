package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record MenuDTO(
		@NotNull(message = "O nome precisa ser informado")
		String name,

		@NotNull(message = "A descrição precisa ser informada")
		String description,

		@NotNull(message = "O preço precisa ser informado")
		String price,

		@NotNull(message = "Necessário informar a disponibilidade para pedir apenas no restaurante")
		String availability,

		@NotNull(message = "Necessário informar o caminho da imagem")
		String photo) {
}
