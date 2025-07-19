package br.com.fiap.interfaces.repositories;

import br.com.fiap.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

    Optional<Restaurant> findById(Long id);
}
