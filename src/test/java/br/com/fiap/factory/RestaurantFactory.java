package br.com.fiap.factory;

import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.model.Restaurant;

import java.util.ArrayList;

import static br.com.fiap.factory.UserFactory.createUser;

public class RestaurantFactory {

    public static RestaurantDTO createRestaurantDTO() {
        return new RestaurantDTO(
                "Ana",
                "Address 1",
                "Fast Food",
                "11 am");
    }

    public static Restaurant createRestaurant() {
        return new Restaurant(
                1L,
                "Ana",
                "Address 1",
                "Fast Food",
                "11 am",
                createUser(1L),
                new ArrayList<>()
        );
    }
}
