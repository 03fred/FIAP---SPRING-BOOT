package br.com.fiap.interfaces.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

    Optional<Restaurant> findById(Long id);
    
    Page<Restaurant> findAllByRestaurantOwnerId(Long ownerId, Pageable pageable);
    
    Optional<Restaurant> findByIdAndRestaurantOwnerId(Long restaurantId, Long userId);
}
