package br.com.fiap.domain.gateways;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.fiap.domain.entities.Item;

public interface ItemDatabaseGateway extends DatabaseGateway<Item, Long> {
    Page<Item> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}