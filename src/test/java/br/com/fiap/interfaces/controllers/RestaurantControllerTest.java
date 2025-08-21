package br.com.fiap.interfaces.controllers;

import br.com.fiap.application.useCases.RestaurantService;
import br.com.fiap.dto.PaginatedResponseDTO;
import br.com.fiap.dto.RestaurantDTO;
import br.com.fiap.dto.RestaurantResponseDTO;
import br.com.fiap.factory.RestaurantFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static br.com.fiap.factory.RestaurantFactory.getRestaurantResponseDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantService restaurantService;

    private RestaurantDTO restaurantDTO;

    @BeforeEach
    void setUp() {
        restaurantDTO = RestaurantFactory.createRestaurantDTO();
    }

    @Test
    void testSaveWithOwnerId() {
        Long ownerId = 1L;

        ResponseEntity<Void> response = restaurantController.save(restaurantDTO, ownerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(restaurantService).save(restaurantDTO, ownerId);
    }

    @Test
    void testSaveWithoutOwnerId() {
        ResponseEntity<Void> response = restaurantController.save(restaurantDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(restaurantService).save(restaurantDTO);
    }

    @Test
    void testUpdate() {
        Long id = 1L;

        ResponseEntity<Void> response = restaurantController.update(id, restaurantDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService).update(restaurantDTO, id);
    }

    @Test
    void testDeleteWithId() {
        Long id = 1L;

        ResponseEntity<Void> response = restaurantController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService).delete(id);
    }

    @Test
    void testDeleteWithoutId() {
        ResponseEntity<Void> response = restaurantController.delete();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService).delete();
    }

    @Test
    void testGetAllRestaurants() {
        Pageable pageable = PageRequest.of(0, 10);
        PaginatedResponseDTO<RestaurantResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(new ArrayList<>(), 1L, 1,1);
        when(restaurantService.getAllRestaurants(pageable)).thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponseDTO<RestaurantResponseDTO>> response = restaurantController.getAllRestaurants(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paginatedResponse, response.getBody());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        var responseDTO = RestaurantFactory.createRestaurantDTO();
        when(restaurantService.getRestaurantById(id)).thenReturn(getRestaurantResponseDTO());

        ResponseEntity<RestaurantResponseDTO> response = restaurantController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetARestaurants() {
        Pageable pageable = PageRequest.of(0, 10);
        PaginatedResponseDTO<RestaurantResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(new ArrayList<>(), 1L, 1,1);
        when(restaurantService.getRestaurants(pageable)).thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponseDTO<RestaurantResponseDTO>> response = restaurantController.getARestaurants(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paginatedResponse, response.getBody());
    }

}