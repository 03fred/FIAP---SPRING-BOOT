package br.com.fiap.infrastructure.persistence.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.Item;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.gateways.ItemRepository;
import br.com.fiap.mapper.ItemMapper;

@Repository
public class JpaItemRepositoryImpl implements ItemRepository {

	@Autowired
	private JpaItemRepository jpaRepo;

	@Override
	public Item findById(Long id) {
		JpaItemEntity jpaItemEntity = jpaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o id: " + id));

		return ItemMapper.toDomain(jpaItemEntity);

	}

	@Override
	public Page<Item> findAllByRestaurantId(Long restaurantId, Pageable pageable) {
		return jpaRepo.findAllByRestaurantId(restaurantId, pageable).map(ItemMapper::toDomain);
	}

}