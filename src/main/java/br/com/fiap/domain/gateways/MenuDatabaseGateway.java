package br.com.fiap.domain.gateways;

import java.util.List;
import br.com.fiap.domain.entities.Menu;

/**
 * Database gateway interface for Menu entity operations
 */
public interface MenuDatabaseGateway extends DatabaseGateway<Menu, Long> {
    
    List<Menu> findByRestaurantId(Long restaurantId);
}