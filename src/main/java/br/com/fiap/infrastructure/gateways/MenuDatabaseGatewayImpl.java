package br.com.fiap.infrastructure.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import br.com.fiap.domain.entities.Menu;
import br.com.fiap.domain.gateways.MenuDatabaseGateway;
import br.com.fiap.domain.repositories.MenuRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of MenuDatabaseGateway using Spring Data JPA
 */
@Component
public class MenuDatabaseGatewayImpl implements MenuDatabaseGateway {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public Menu save(Menu entity) {
        return menuRepository.save(entity);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Page<Menu> findAll(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public void delete(Menu entity) {
        menuRepository.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return menuRepository.existsById(id);
    }

    @Override
    public long count() {
        return menuRepository.count();
    }

    @Override
    public List<Menu> findByRestaurantId(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }
}