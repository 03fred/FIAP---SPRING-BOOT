package br.com.fiap.controller;

import static br.com.fiap.factory.MenuFactory.createMenuDTO;
import static br.com.fiap.factory.MenuFactory.createMenuResponseDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.interfaces.services.MenuService;

class MenuControllerTest {

	@Mock
	private MenuService menuService;

	@InjectMocks
	private MenuController menuController;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSaveMenuShouldReturnCreatedStatus() throws Exception {
		Long restaurantId = 1L;
		MenuDTO menuDTO = new MenuDTO("Menu Teste", "Descrição do Menu Teste", "12.50", "true",
				"http://example.com/photo.jpg");

		doNothing().when(menuService).save(any(MenuDTO.class), eq(restaurantId));

		mockMvc.perform(post("/menu/restaurant/{restaurantId}", restaurantId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuDTO))).andExpect(status().isCreated());

		verify(menuService, times(1)).save(any(MenuDTO.class), eq(restaurantId));
	}

	
	@Test
	void testUpdateMenu() throws Exception {
		MenuDTO menuDTO = createMenuDTO();
		Long menuId = 10L;

		doNothing().when(menuService).update(any(MenuDTO.class), eq(menuId));

		mockMvc.perform(put("/menu/{id}", menuId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(menuDTO))).andExpect(status().isNoContent());

		verify(menuService).update(any(MenuDTO.class), eq(menuId));
	}

	@Test
	void testDeleteMenu() throws Exception {
		Long menuId = 5L;

		doNothing().when(menuService).delete(menuId);

		mockMvc.perform(delete("/menu/{id}", menuId)).andExpect(status().isNoContent());

		verify(menuService).delete(menuId);
	}

	@Test
	void testFindById() throws Exception {
		Long menuId = 3L;
		MenuResponseDTO responseDTO = createMenuResponseDTO();

		when(menuService.getMenuById(menuId)).thenReturn(responseDTO);

		mockMvc.perform(get("/menu/{id}", menuId)).andExpect(status().isOk());

		verify(menuService).getMenuById(menuId);
	}

}