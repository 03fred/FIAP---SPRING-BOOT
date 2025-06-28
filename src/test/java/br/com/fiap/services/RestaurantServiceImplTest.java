package br.com.fiap.services;

import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.User;
import br.com.fiap.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        User owner = TestDataFactory.createUser(1L);
        RestaurantDTO restaurantDTO = TestDataFactory.createRestaurantDTO();

         when(userService.getUser(owner.getId())).thenReturn(Optional.of(owner));

         //Act
        restaurantServiceImpl.save(restaurantDTO, owner.getId());

        //Assert
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    public void shouldThrowExceptionWhenOwnerIsNotFound() {
        User owner = TestDataFactory.createUser(1L);
        RestaurantDTO restaurantDTO = TestDataFactory.createRestaurantDTO();

        when(userService.getUser(owner.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> restaurantServiceImpl.save(restaurantDTO, owner.getId())
        );

        verify(restaurantRepository, never()).save(any());
    }
}
