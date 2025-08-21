package br.com.fiap.domain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.domain.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

    Optional<Item> findById(Long id);
    
    Page<Item> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}
