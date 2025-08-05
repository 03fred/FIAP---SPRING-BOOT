package br.com.fiap.services;

import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.factory.RestaurantFactory;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static br.com.fiap.factory.RestaurantFactory.createRestaurant;
import static br.com.fiap.factory.UserFactory.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RestaurantServiceImpl restaurantServiceImpl;

    @Test
    public void shouldSaveRestaurantWithOwnerId() {
        //Arrange
        User owner = createUser(1L);
        RestaurantDTO restaurantDTO = RestaurantFactory.createRestaurantDTO();

         when(userService.getUser(owner.getId())).thenReturn(Optional.of(owner));

         //Act
        restaurantServiceImpl.save(restaurantDTO, owner.getId());

        //Assert
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    public void shouldThrowExceptionWhenOwnerIsNotFound() {
        User owner = createUser(1L);
        RestaurantDTO restaurantDTO = RestaurantFactory.createRestaurantDTO();

        when(userService.getUser(owner.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> restaurantServiceImpl.save(restaurantDTO, owner.getId())
        );

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    public void shouldReturnRestaurantWhenFoundById(){
        Long restaurantId = 1L;

        Restaurant restaurant = createRestaurant();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        RestaurantResponseDTO response = restaurantServiceImpl.getRestaurantById(restaurantId);

        assertEquals("Ana", response.name());
    }

    @Test
    public void shouldThrowExceptionWhenNotFoundById(){
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> restaurantServiceImpl.getRestaurantById(restaurantId));

        assertEquals("Restaurant with " + restaurantId + " was not found", exception.getMessage());
    }

    @Test
    public void shouldUpdateRestaurantByRestaurantId(){
        Restaurant restaurant = createRestaurant();

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        RestaurantDTO restaurantDTO = new RestaurantDTO(
                "Maria",
                "Address 2",
                "Italian",
                LocalTime.of(17, 0),
                LocalTime.of(23, 0)

        );

        restaurantServiceImpl.update(restaurantDTO, restaurant.getId());

        verify(restaurantRepository).save(argThat(saved ->
                saved.getName().equals("Maria") &&
                saved.getTypeKitchen().equals("Italian"))
        );
    }

    @Test
    public void shouldThrowExceptionWhenRestaurantIdIsNotFound(){
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> restaurantServiceImpl.getRestaurantById(restaurantId));

        assertEquals("Restaurant with " + restaurantId + " was not found", exception.getMessage());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteRestaurantByRestaurantId(){
        Long restaurantId = 1L;
        Restaurant restaurant = createRestaurant();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        restaurantServiceImpl.delete(restaurantId);

        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    public void shouldThrowExceptionWhenIdIsNotFound(){
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> restaurantServiceImpl.delete(restaurantId));

        assertEquals("Restaurant with " + restaurantId + " was not found", exception.getMessage());
    }

    @Test
    public void shouldListAllRestaurants(){
        Restaurant restaurant = createRestaurant();

        List<Restaurant> restaurants = List.of(restaurant);
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);

        Pageable pageable = PageRequest.of(0, 10);

        when(restaurantRepository.findAll(pageable)).thenReturn(restaurantPage);

        PaginatedResponseDTO<RestaurantResponseDTO> responseDTO = restaurantServiceImpl.getAllRestaurants(pageable);

        assertEquals(restaurantPage.getTotalElements(), responseDTO.getTotalElements());
        assertEquals("Ana", responseDTO.getContent().get(0).name());

        verify(restaurantRepository).findAll(pageable);
    }

    @Test
    public void shouldReturnRestaurantById() {
        Restaurant restaurant = createRestaurant(); // método de factory utilitária
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantServiceImpl.getRestaurant(restaurant.getId());

        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
    }
}
