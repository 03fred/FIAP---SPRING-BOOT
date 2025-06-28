package br.com.fiap.utils;

import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.model.User;

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
}
