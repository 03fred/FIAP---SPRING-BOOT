package br.com.fiap.mapper;

import br.com.fiap.domain.entities.Item;
import br.com.fiap.domain.entities.Restaurant;
import br.com.fiap.infrastructure.persistence.item.JpaItemEntity;
import br.com.fiap.infrastructure.persistence.restaurants.JpaRestaurantEntity;

public class ItemMapper {

    public static JpaItemEntity toEntity(Item item) {
        if (item == null) return null;

        JpaRestaurantEntity restaurantEntity = RestaurantMapper.toEntity(item.getRestaurant());

        return new JpaItemEntity(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAvailability(),
                item.getPhoto(),
                restaurantEntity
        );
    }

    public static Item toDomain(JpaItemEntity entity) {
        if (entity == null) return null;

        Restaurant restaurant = RestaurantMapper.toDomain(entity.getRestaurant());

        return new Item(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getAvailability(),
                entity.getPhoto(),
                restaurant
        );
    }
}
