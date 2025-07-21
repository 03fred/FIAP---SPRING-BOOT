package br.com.fiap.integrationTests.controller;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.interfaces.repositories.UserRepository;
import br.com.fiap.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=test")
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private Long createdUserId;

    @BeforeEach
    public void setupTestUser() {
        User user = new User();
        user.setEmail("original@email.com");
        user.setPassword("originalPass123");
        user.setName("Original User");
        user.setAddress("123 Main St");
        user.setLogin("original_login");
        userRepository.save(user);
        User savedUser = userRepository.save(user);
        createdUserId = savedUser.getId();
    }


    @Test
    public void givenValidUserDTO_whenCreateUser_thenReturnsCreatedStatus() throws Exception {
        UserDTO userDto = new UserDTO(
                "ana@email.com",
                "password123",
                "Ana",
                "123 Main St",
                "ana_login");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(jsonRequest)
        )
        .andExpect(status().isCreated());
    }

    @Test
    public void givenInvalidUserDTO_whenCreateUser_thenReturnsBadRequestStatus() throws Exception {
        UserDTO userDto = new UserDTO(
                "invalid-email",
                "short",
                "",
                "123 Main St",
                "ana_login");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(jsonRequest)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void givenEmptyUserDTO_whenCreateUser_thenReturnsBadRequestStatus() throws Exception {
        UserDTO userDto = new UserDTO("", "", "", "", "");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(jsonRequest)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullUserDTO_whenCreateUser_thenReturnsBadRequestStatus() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(null);

        mockMvc.perform(
                post("/users/create")
                        .contentType("application/json")
                        .content(jsonRequest)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenValidUserDTO_whenUpdateUserWithAdmin_thenReturnsOkStatus() throws Exception {
        UserDTO userDto = new UserDTO(
                "ana@email.com",
                "password123",
                "Ana",
                "123 Main St",
                "ana_login");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/users/update/" + createdUserId)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
