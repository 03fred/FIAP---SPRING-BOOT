package br.com.fiap.services;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.MenuRepository;
import br.com.fiap.interfaces.services.RestaurantService;
import br.com.fiap.model.Menu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static br.com.fiap.factory.MenuFactory.createMenu;
import static br.com.fiap.factory.MenuFactory.createMenuDTO;
import static br.com.fiap.factory.RestaurantFactory.createRestaurant;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private MenuServiceImpl menuServiceImpl;

    @Test
    public void shouldSaveMenuWithOwnerId() {
        //Arrange
        Menu menu = createMenu(1L);
        MenuDTO menuDTO = createMenuDTO();
        when(restaurantService.getRestaurant(menu.getId())).thenReturn(createRestaurant());

        menuServiceImpl.save(menuDTO, menu.getId());

        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    public void shouldReturnMenuById() {
        Menu menu = createMenu(1L);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        MenuResponseDTO result = menuServiceImpl.getMenuById(1L);

        assertEquals(menu.getName(), result.name());
        assertEquals(menu.getDescription(), result.description());
        assertEquals(menu.getAvailability(), result.availability());
        assertEquals(menu.getPrice(), result.price());
        assertEquals(menu.getPhoto(), result.photo());
    }

    @Test
    public void shouldThrowWhenMenuNotFoundById() {
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuServiceImpl.getMenuById(1L));
    }

    @Test
    public void shouldUpdateMenu() {
        Menu menu = createMenu(1L);
        MenuDTO menuDTO = createMenuDTO();

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        menuServiceImpl.update(menuDTO, 1L);

        verify(menuRepository, times(1)).save(menu);
        assertEquals(menuDTO.name(), menu.getName());
        assertEquals(menuDTO.description(), menu.getDescription());
        assertEquals(menuDTO.availability(), menu.getAvailability());
        assertEquals(menuDTO.price(), menu.getPrice());
        assertEquals(menuDTO.photo(), menu.getPhoto());
    }

    @Test
    public void shouldThrowWhenUpdatingNonexistentMenu() {
        MenuDTO menuDTO = createMenuDTO();
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuServiceImpl.update(menuDTO, 1L));
    }

    @Test
    public void shouldDeleteMenu() {
        Menu menu = createMenu(1L);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        menuServiceImpl.delete(1L);

        verify(menuRepository, times(1)).delete(menu);
    }

    @Test
    public void shouldThrowWhenDeletingNonexistentMenu() {
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuServiceImpl.delete(1L));
    }

    @Test
    public void shouldListAllMenus() {
        Menu menu = createMenu(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Menu> menuPage = new PageImpl<>(List.of(menu));

        when(menuRepository.findAll(pageable)).thenReturn(menuPage);

        PaginatedResponseDTO<MenuResponseDTO> response = menuServiceImpl.getAllMenu(pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getContent().size());
        assertEquals(menu.getName(), response.getContent().get(0).name());

        verify(menuRepository).findAll(pageable);
    }

}