package br.com.fiap.infrastructure.persistence.menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<JpaMenuEntity, Long> {
	 Page<JpaMenuEntity> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}