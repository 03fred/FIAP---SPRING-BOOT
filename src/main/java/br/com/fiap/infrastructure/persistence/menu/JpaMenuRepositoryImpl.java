package br.com.fiap.infrastructure.persistence.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.Menu;
import br.com.fiap.gateways.MenuRepository;
import br.com.fiap.mapper.MenuMapper;

@Repository
public class JpaMenuRepositoryImpl implements MenuRepository {

	@Autowired
	private JpaMenuRepository jpaRepo;
	
	@Override
	public List<Menu> findByRestaurantId(Long restaurantId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return jpaRepo.findAllByRestaurantId(restaurantId, pageable).map(MenuMapper::toDomain).toList();
	}
}
	/*
	@Override
	public PaginatedResponseDTO<MenuResponseDTO> findByRestaurantId(Long restaurantId, int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);

	    Page<MenuResponseDTO> menuPage = jpaRepo.findAllByRestaurantId(restaurantId, pageable)
	            .map(menu -> new MenuResponseDTO(
	                menu.getId(),
	                menu.getTitle(),
	                menu.getRestaurant().getId(),
	                menu.getItems().stream()
	                    .map(item -> new ItemDTO(
	                        item.getName(),
	                        item.getDescription(),
	                        item.getPrice(),
	                        item.getAvailability(),
	                        item.getPhoto()
	                    ))
	                    .toList()
	            ));

	        return new PaginatedResponseDTO<>(
	            menuPage.getContent(),
	            menuPage.getTotalElements(),
	            menuPage.getNumber(),
	            menuPage.getSize()
			);
		}

	}*/