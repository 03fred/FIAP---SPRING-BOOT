package br.com.fiap.infrastructure.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import br.com.fiap.domain.entities.Item;
import br.com.fiap.domain.gateways.ItemDatabaseGateway;
import br.com.fiap.domain.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ItemDatabaseGatewayImpl implements ItemDatabaseGateway {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item save(Item entity) {
        return itemRepository.save(entity);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public void delete(Item entity) {
        itemRepository.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return itemRepository.existsById(id);
    }

    @Override
    public long count() {
        return itemRepository.count();
    }

    @Override
    public Page<Item> findAllByRestaurantId(Long restaurantId, Pageable pageable) {
        return itemRepository.findAllByRestaurantId(restaurantId, pageable);
    }
}