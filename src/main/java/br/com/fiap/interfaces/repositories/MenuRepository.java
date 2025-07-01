package br.com.fiap.interfaces.repositories;

import br.com.fiap.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long>{

    Optional<Menu> findById(Long id);
}
