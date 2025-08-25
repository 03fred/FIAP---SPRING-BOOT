package br.com.fiap.infrastructure.persistence.restaurants;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRestaurantRepository extends JpaRepository<JpaRestaurantEntity, Long> {

	Optional<JpaRestaurantEntity> findById(Long id);

	Page<JpaRestaurantEntity> findAllByRestaurantOwnerId(Long ownerId, Pageable pageable);

	Optional<JpaRestaurantEntity> findByIdAndRestaurantOwnerId(Long restaurantId, Long userId);
}