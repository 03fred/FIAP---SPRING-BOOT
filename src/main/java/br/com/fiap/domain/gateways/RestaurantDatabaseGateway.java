package br.com.fiap.domain.gateways;

import java.util.Optional;
import br.com.fiap.domain.entities.Restaurant;

/**
 * Database gateway interface for Restaurant entity operations
 */
public interface RestaurantDatabaseGateway extends DatabaseGateway<Restaurant, Long> {
    
    Optional<Restaurant> findByIdAndRestaurantOwnerId(Long restaurantId, Long ownerId);
    
    Optional<Restaurant> findByName(String name);
    
    boolean existsByName(String name);
}