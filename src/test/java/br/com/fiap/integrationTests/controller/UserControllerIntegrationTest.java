package br.com.fiap.integrationTests.controller;

import br.com.fiap.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void shouldCreateUser() throws Exception {
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

}
