package br.com.fiap.utils;

import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.User;

import java.util.ArrayList;

public class TestDataFactory {

    public static User createUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    public static RestaurantDTO createRestaurantDTO() {
        return new RestaurantDTO(
                "Ana",
                "Address 1",
                "Fast Food",
                "11 am");
    }

    public static Restaurant createRestaurant() {
        return new Restaurant(
                1L, // id
                "Ana", // name
                "Address 1", // address
                "Fast Food", // typeKitchen
                "11 am", // openingHours
                createUser(1L), // restaurantOwner
                new ArrayList<>() // menus
        );
    }
}
