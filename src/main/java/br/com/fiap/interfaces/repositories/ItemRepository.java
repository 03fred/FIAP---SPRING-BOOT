package br.com.fiap.interfaces.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

    Optional<Item> findById(Long id);
    
    Page<Item> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}
