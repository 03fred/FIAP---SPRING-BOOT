package br.com.fiap.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemMenuDTO;
import br.com.fiap.dto.MenuCreateDTO;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.exceptions.UnauthorizedException;
import br.com.fiap.interfaces.repositories.ItemRepository;
import br.com.fiap.interfaces.repositories.MenuRepository;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.services.MenuService;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.Item;
import br.com.fiap.model.Menu;
import br.com.fiap.model.Restaurant;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

	@Override
	public MenuDTO create(MenuCreateDTO dto) {
	    
    	Long restaurantId = getRestaurantId(dto.restaurantId());
	
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        Menu menu = new Menu();
        menu.setTitle(dto.title());
        menu.setRestaurant(restaurant);

        Menu saved = menuRepository.save(menu);
        return new MenuDTO(saved.getId(), saved.getTitle(), saved.getRestaurant().getId());
    }

    @Override
    public List<MenuResponseDTO> findAll() {
        return menuRepository.findAll().stream()
            .map(menu -> new MenuResponseDTO(menu.getId(), menu.getTitle(), menu.getRestaurant().getId(), getItens(menu)))
            .toList();
    }

    private ItemDTO mapToItemDTO(Item item) {
        return new ItemDTO(
            item.getName(),
            item.getDescription(),
            item.getPrice(),
            item.getAvailability(),
            item.getPhoto()
        );
    }
    private List<ItemDTO>  getItens(Menu menu) {
    	  return menu.getItems().stream()
    		        .map(this::mapToItemDTO)
    		        .toList();
    }
    
    @Override
    public MenuResponseDTO findById(Long id) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        return new MenuResponseDTO(menu.getId(), menu.getTitle(), menu.getRestaurant().getId(), getItens(menu));
    }

    @Override
    public MenuDTO update(Long id, MenuCreateDTO dto) {

        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu não encontrado"));

        Long restaurantId = getRestaurantId(menu.getRestaurant().getId());
        
        if(!restaurantId.equals(menu.getRestaurant().getId())) {
			throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        menu.setTitle(dto.title());
        menu.setRestaurant(restaurant);

        Menu updated = menuRepository.save(menu);
        return new MenuDTO(updated.getId(), updated.getTitle(), updated.getRestaurant().getId());
    }

    @Override
    public void delete(Long id) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        
        Long restaurantId = getRestaurantId(menu.getRestaurant().getId());
        
        if(!restaurantId.equals(menu.getRestaurant().getId())) {
			throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
        }
        
        menuRepository.delete(menu);
    }
    
    private Long getRestaurantId(Long id) {
    	AuthenticatedUser auth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    return auth.hasRoleAdmin() ? id : auth.getRestaurantId();
    }
    
    public void addItemToMenu(ItemMenuDTO dto) {
    	
		Menu menu = menuRepository.findById(dto.menuId())
				.orElseThrow(() -> new EntityNotFoundException("Menu não encontrado"));

		Item item = itemRepository.findById(dto.itemId())
				.orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));

		Long restaurantId = getRestaurantId(menu.getRestaurant().getId());

		if (!restaurantId.equals(menu.getRestaurant().getId())) {
			throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
		}

		restaurantId = getRestaurantId(item.getRestaurant().getId());

		if (!restaurantId.equals(menu.getRestaurant().getId())) {
			throw new UnauthorizedException("Esse item não pertence ao usuário logado.");
		}

		menu.getItems().add(item);
		menuRepository.save(menu);
	}

}
