package br.com.fiap.dto;

import jakarta.validation.constraints.NotNull;

public record ItemMenuDTO(
		@NotNull(message = "O item precisa ser informado")
		Long itemId,
		@NotNull(message = "O menu precisa ser informado")
		Long menuId
		) {}