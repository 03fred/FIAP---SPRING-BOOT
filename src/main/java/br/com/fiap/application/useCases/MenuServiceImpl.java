package br.com.fiap.application.useCases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.fiap.dto.ItemDTO;
import br.com.fiap.dto.ItemMenuDTO;
import br.com.fiap.dto.MenuCreateDTO;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.exceptions.UnauthorizedException;
import br.com.fiap.domain.gateways.ItemDatabaseGateway;
import br.com.fiap.domain.gateways.MenuDatabaseGateway;
import br.com.fiap.domain.gateways.RestaurantDatabaseGateway;
import br.com.fiap.application.useCases.MenuService;
import br.com.fiap.domain.entities.AuthenticatedUser;
import br.com.fiap.domain.entities.Item;
import br.com.fiap.domain.entities.Menu;
import br.com.fiap.domain.entities.Restaurant;

@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuDatabaseGateway menuDatabaseGateway;
    
    @Autowired
    private ItemDatabaseGateway itemDatabaseGateway;

    @Autowired
    private RestaurantDatabaseGateway restaurantDatabaseGateway;

	@Override
	public MenuDTO create(MenuCreateDTO dto) {
	    
    	Long restaurantId = getRestaurantId(dto.restaurantId());
	
        Restaurant restaurant = restaurantDatabaseGateway.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        Menu menu = new Menu();
        menu.setTitle(dto.title());
        menu.setRestaurant(restaurant);

        Menu saved = menuDatabaseGateway.save(menu);
        return new MenuDTO(saved.getId(), saved.getTitle(), saved.getRestaurant().getId());
    }

    @Override
    public List<MenuResponseDTO> findAll() {
        return menuDatabaseGateway.findAll().stream()
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
        Menu menu = menuDatabaseGateway.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        return new MenuResponseDTO(menu.getId(), menu.getTitle(), menu.getRestaurant().getId(), getItens(menu));
    }

    @Override
    public MenuDTO update(Long id, MenuCreateDTO dto) {

        Menu menu = menuDatabaseGateway.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu não encontrado"));

        Long restaurantId = getRestaurantId(menu.getRestaurant().getId());
        
        if(!restaurantId.equals(menu.getRestaurant().getId())) {
			throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
        }

        Restaurant restaurant = restaurantDatabaseGateway.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        menu.setTitle(dto.title());
        menu.setRestaurant(restaurant);

        Menu updated = menuDatabaseGateway.save(menu);
        return new MenuDTO(updated.getId(), updated.getTitle(), updated.getRestaurant().getId());
    }

    @Override
    public void delete(Long id) {
        Menu menu = menuDatabaseGateway.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        
        Long restaurantId = getRestaurantId(menu.getRestaurant().getId());
        
        if(!restaurantId.equals(menu.getRestaurant().getId())) {
			throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
        }
        
        menuDatabaseGateway.delete(menu);
    }
    
    private Long getRestaurantId(Long id) {
    	AuthenticatedUser auth = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    return auth.hasRoleAdmin() ? id : auth.getRestaurantId();
    }
    
    public void addItemToMenu(ItemMenuDTO dto) {
    	
		Menu menu = menuDatabaseGateway.findById(dto.menuId())
				.orElseThrow(() -> new ResourceNotFoundException("Menu não encontrado"));

		Item item = itemDatabaseGateway.findById(dto.itemId())
				.orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));

		Long restaurantId = getRestaurantId(menu.getRestaurant().getId());

		if (!restaurantId.equals(menu.getRestaurant().getId())) {
			throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
		}
		
		menu.getItems().add(item);
		menuDatabaseGateway.save(menu);
	}
    
    public void removeItemFromMenu(ItemMenuDTO dto) {

        Menu menu = menuDatabaseGateway.findById(dto.menuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu não encontrado"));

        Item item = itemDatabaseGateway.findById(dto.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));

        Long restaurantId = getRestaurantId(menu.getRestaurant().getId());

        if (!restaurantId.equals(menu.getRestaurant().getId())) {
            throw new UnauthorizedException("Esse menu não pertence ao usuário logado.");
        }
       
        menu.getItems().remove(item);
        menuDatabaseGateway.save(menu);
    }


}
