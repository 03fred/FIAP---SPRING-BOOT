package br.com.fiap.domain.entities;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Restaurant {
	
    private Long id;
    
    private String name;

    private String address;

    private String typeKitchen;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private User restaurantOwner;

    private List<Menu> menus;
}