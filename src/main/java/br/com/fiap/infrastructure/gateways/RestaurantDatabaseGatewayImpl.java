package br.com.fiap.infrastructure.gateways;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import br.com.fiap.domain.entities.Restaurant;
import br.com.fiap.domain.gateways.RestaurantDatabaseGateway;
import br.com.fiap.domain.repositories.RestaurantRepository;

/**
 * Implementation of RestaurantDatabaseGateway using Spring Data JPA
 */
@Component
public class RestaurantDatabaseGatewayImpl implements RestaurantDatabaseGateway {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Override
    public Restaurant save(Restaurant entity) {
        return restaurantRepository.save(entity);
    }
    
    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }
    
    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }
    
    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }
    
    @Override
    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }
    
    @Override
    public void delete(Restaurant entity) {
        restaurantRepository.delete(entity);
    }
    
    @Override
    public boolean existsById(Long id) {
        return restaurantRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return restaurantRepository.count();
    }
    
    @Override
    public Optional<Restaurant> findByIdAndRestaurantOwnerId(Long restaurantId, Long ownerId) {
        return restaurantRepository.findByIdAndRestaurantOwnerId(restaurantId, ownerId);
    }
    
    @Override
    public Optional<Restaurant> findByName(String name) {
        //return restaurantRepository.findByName(name);
        return null;
    }
    
    @Override
    public boolean existsByName(String name) {
        //return restaurantRepository.existsByName(name);
        return true;
    }
}