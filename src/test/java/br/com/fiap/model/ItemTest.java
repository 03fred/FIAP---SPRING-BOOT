package br.com.fiap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.fiap.dto.ItemDTO;

class ItemTest {

    @Test
    void shouldCreateItemFromItemDTOAndRestaurant() {
       
        ItemDTO itemDTO = new ItemDTO("Pizza Margherita", "Clássica pizza italiana", new BigDecimal(39.90), true, "pizza.jpg");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Pizzaria Napoli");


        Item item = new Item(itemDTO, restaurant);

        assertEquals("Pizza Margherita", item.getName());
        assertEquals("Clássica pizza italiana", item.getDescription());
        assertEquals(true, item.getAvailability());
        assertEquals(new BigDecimal(39.90), item.getPrice());
        assertEquals("pizza.jpg", item.getPhoto());
        assertEquals(restaurant, item.getRestaurant());
    }
}
