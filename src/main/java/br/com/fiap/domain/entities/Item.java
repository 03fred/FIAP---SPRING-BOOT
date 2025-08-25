package br.com.fiap.domain.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Item {
	private Long id;

	private String name;

	private String description;

	private BigDecimal price;

	private Boolean availability;

	private String photo;

	private Restaurant restaurant;

}