package br.com.fiap.services;

import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.interfaces.repositories.RestaurantRepository;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.Restaurant;
import br.com.fiap.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
         Long ownerId = 1L;

         User owner = new User();
         owner.setId(ownerId);

         RestaurantDTO restaurantDTO = new RestaurantDTO(
                         "Ana",
                         "Address 1",
                         "Fast Food",
                         "11 am");

         when(userService.getUser(ownerId)).thenReturn(Optional.of(owner));

         //Act
        restaurantServiceImpl.save(restaurantDTO, owner.getId());

        //Assert
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));

    }
}
