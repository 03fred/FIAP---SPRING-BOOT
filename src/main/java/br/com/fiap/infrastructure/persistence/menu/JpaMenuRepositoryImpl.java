package br.com.fiap.infrastructure.persistence.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.Menu;
import br.com.fiap.exceptions.ResourceNotFoundException;
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

	@Override
	public Menu findById(Long id) {
		JpaMenuEntity entity = jpaRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Menu não encontrado"));
		return MenuMapper.toDomain(entity);
		
	}

	@Override
	public Menu save(Menu menu) {
		return MenuMapper.toDomain(jpaRepo.save(MenuMapper.toEntity(menu)));
	}

	@Override
	public void delete(Menu menu) {
		jpaRepo.deleteById(menu.getId());

	}
}