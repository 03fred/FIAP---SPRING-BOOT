package br.com.fiap.infrastructure.persistence.restaurants;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.Restaurant;
import br.com.fiap.gateways.RestaurantRepository;
import br.com.fiap.mapper.RestaurantMapper;

@Repository
public class JpaRestaurantRepositoryImpl implements RestaurantRepository {

	private final JpaRestaurantRepository jpaRepo;

	public JpaRestaurantRepositoryImpl(JpaRestaurantRepository jpaRepo) {
		this.jpaRepo = jpaRepo;
	}

	@Override
	public Restaurant save(Restaurant restaurant) {
		JpaRestaurantEntity entity = RestaurantMapper.toEntity(restaurant);
		JpaRestaurantEntity saved = jpaRepo.save(entity);
		return RestaurantMapper.toDomain(saved);
	}

	@Override
	public Restaurant findById(Long id) {
		return jpaRepo.findById(id).map(RestaurantMapper::toDomain).orElse(null);
	}

	@Override
	public Restaurant findByIdAndRestaurantOwnerId(Long restaurantId, Long ownerId) {
		return jpaRepo.findByIdAndRestaurantOwnerId(restaurantId, ownerId).map(RestaurantMapper::toDomain).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepo.deleteById(id);
	}

	@Override
	public List<Restaurant> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return jpaRepo.findAll(pageable).map(RestaurantMapper::toDomain).toList();
	}

}