package br.com.fiap.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.fiap.domain.entities.Item;
import br.com.fiap.domain.entities.Menu;
import br.com.fiap.domain.entities.Restaurant;
import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.infrastructure.persistence.item.JpaItemEntity;
import br.com.fiap.infrastructure.persistence.menu.JpaMenuEntity;
import br.com.fiap.infrastructure.persistence.restaurants.JpaRestaurantEntity;

public class MenuMapper {

    public static JpaMenuEntity toEntity(Menu menu) {
        if (menu == null) return null;

        JpaRestaurantEntity restaurantEntity = RestaurantMapper.toEntity(menu.getRestaurant());

        Set<JpaItemEntity> itemEntities = menu.getItems() != null
                ? menu.getItems().stream().map(ItemMapper::toEntity).collect(Collectors.toSet())
                : Set.of();

        return new JpaMenuEntity(
                menu.getId(),
                menu.getTitle(),
                restaurantEntity,
                itemEntities
        );
    }

    public static Menu toDomain(JpaMenuEntity entity) {
        if (entity == null) return null;

        Restaurant restaurant = RestaurantMapper.toDomain(entity.getRestaurant());

        Set<Item> items = entity.getItems() != null
                ? entity.getItems().stream().map(ItemMapper::toDomain).collect(Collectors.toSet())
                : Set.of();

        return new Menu(
                entity.getId(),
                entity.getTitle(),
                restaurant,
                items
        );
    }
    
    public static MenuResponseDTO toDTO(Menu menu) {
        if (menu == null) return null;

        List<ItemDTO> itemDTOs = menu.getItems() != null
                ? menu.getItems().stream()
                    .map(MenuMapper::toItemDTO)
                    .collect(Collectors.toList())
                : List.of();

        return new MenuResponseDTO(
                menu.getId(),
                menu.getTitle(),
                menu.getRestaurant() != null ? menu.getRestaurant().getId() : null,
                itemDTOs
        );
    }
    
    private static ItemDTO toItemDTO(Item item) {
        if (item == null) return null;

        return new ItemDTO(
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAvailability(),
                item.getPhoto()
        );
    }
}