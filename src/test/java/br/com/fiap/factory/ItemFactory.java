package br.com.fiap.factory;

import java.math.BigDecimal;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemResponseDTO;
import br.com.fiap.model.Item;

public class ItemFactory {

	public static Item createItem(Long id) {
		Item item = new Item();
		item.setId(id);
		return item;
	}

	public static ItemDTO createMenuDTO() {
		return new ItemDTO("Hamburguer", "Lanche", new BigDecimal("39.90"), true, "image/photo");
	}

	public static ItemResponseDTO createMenuResponseDTO() {
		return new ItemResponseDTO("Hamburguer", "Lanche", true, new BigDecimal("39.90"), "image/photo");
	}
}
