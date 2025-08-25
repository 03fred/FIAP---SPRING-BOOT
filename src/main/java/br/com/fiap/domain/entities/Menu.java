package br.com.fiap.domain.entities;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Menu {

    private Long id;
    private String title;
    private Restaurant restaurant;
    private Set<Item> items = new HashSet<>();

    public Menu() {}

    public Menu(Long id, String title, Restaurant restaurant, Set<Item> items) {
        this.id = id;
        this.title = title;
        this.restaurant = restaurant;
        this.items = items != null ? items : new HashSet<>();
    }
}