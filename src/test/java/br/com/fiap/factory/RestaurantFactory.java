package br.com.fiap.factory;

import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.model.Restaurant;

import java.time.LocalTime;
import java.util.ArrayList;

import static br.com.fiap.factory.UserFactory.createUser;

public class RestaurantFactory {

    public static RestaurantDTO createRestaurantDTO() {
        return new RestaurantDTO(
                "Ana",
                "Address 1",
                "Fast Food",
                LocalTime.of(17, 0),
                LocalTime.of(23, 0));
    }

    public static Restaurant createRestaurant() {
        return new Restaurant(
                1L,
                "Ana",
                "Address 1",
                "Fast Food",
                LocalTime.of(17, 0),
                LocalTime.of(23, 0),
                createUser(1L),
                new ArrayList<>()
        );
    }

    public static RestaurantResponseDTO getRestaurantResponseDTO() {
        return new RestaurantResponseDTO(
                "food",
                "Av central",
                "Pasta",
                LocalTime.of(17, 0),
                LocalTime.of(23, 0)
        );
    }
}
