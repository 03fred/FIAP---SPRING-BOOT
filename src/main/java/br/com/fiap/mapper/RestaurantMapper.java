package br.com.fiap.mapper;

import java.util.stream.Collectors;

import br.com.fiap.domain.entities.Restaurant;
import br.com.fiap.infrastructure.persistence.restaurants.JpaRestaurantEntity;

public class RestaurantMapper {

	 public static JpaRestaurantEntity toEntity(Restaurant restaurant) {
	        return new JpaRestaurantEntity(
	                restaurant.getId(),
	                restaurant.getName(),
	                restaurant.getAddress(),
	                restaurant.getTypeKitchen(),
	                restaurant.getOpeningTime(),
	                restaurant.getClosingTime(),
	                UserMapper.toEntity(restaurant.getRestaurantOwner()),
	                restaurant.getMenus() != null
	                        ? restaurant.getMenus().stream()
	                            .map(MenuMapper::toEntity)
	                            .collect(Collectors.toList())
	                        : null
	        );
	    }

	    public static Restaurant toDomain(JpaRestaurantEntity entity) {
	        return new Restaurant(
	                entity.getId(),
	                entity.getName(),
	                entity.getAddress(),
	                entity.getTypeKitchen(),
	                entity.getOpeningTime(),
	                entity.getClosingTime(),
	                UserMapper.toDomain(entity.getRestaurantOwner()),
	                entity.getMenus() != null
	                        ? entity.getMenus().stream()
	                            .map(MenuMapper::toDomain)
	                            .collect(Collectors.toList())
	                        : null
	        );
	    }
}
