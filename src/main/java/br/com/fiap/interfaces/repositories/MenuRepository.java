package br.com.fiap.interfaces.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>{

    Optional<Menu> findById(Long id);
    
    Page<Menu> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}
