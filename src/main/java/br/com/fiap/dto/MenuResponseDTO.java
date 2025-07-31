package br.com.fiap.dto;

import java.util.List;

public record MenuResponseDTO(Long id, String title, Long restaurantId, List<ItemDTO> itens) {}