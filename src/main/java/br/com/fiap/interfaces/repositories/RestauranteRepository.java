package br.com.fiap.interfaces.repositories;

import br.com.fiap.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long>{

    Optional<Restaurante> findById(Long id);
}
