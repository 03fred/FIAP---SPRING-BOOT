package br.com.fiap.infrastructure.persistence.item;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaItemRepository extends JpaRepository<JpaItemEntity, Long> {

     Optional<JpaItemEntity> findById(Long id);
    
    Page<JpaItemEntity> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}