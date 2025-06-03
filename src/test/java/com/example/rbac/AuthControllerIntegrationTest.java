package com.example.rbac;

import com.example.rbac.dto.UpdateUserRolesRequestDTO;
import com.example.rbac.model.User;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.JwtResponse;
import com.example.rbac.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Sql("classpath:scripts/insert-test-admin.sql")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUpdateUserRolesWithAdminAuthentication() throws Exception {
        // 1. Логін як admin
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("adminpassword");

        String jsonLogin = objectMapper.writeValueAsString(loginRequest);

        String response = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtResponse jwtResponse = objectMapper.readValue(response, JwtResponse.class);
        String token = jwtResponse.getAccessToken();

        // 2. Знаходимо Bobr
        User bobr = userRepository.findByUsername("Bobr").orElseThrow();

        // 3. Оновлюємо його ролі
        UpdateUserRolesRequestDTO dto = new UpdateUserRolesRequestDTO();
        dto.setRoleNames(Set.of("ROLE_ADMIN"));
        String jsonRolesUpdate = objectMapper.writeValueAsString(dto);


        mockMvc.perform(put("/api/users/" + bobr.getId() + "/roles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRolesUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles").value(org.hamcrest.Matchers.hasItem("ROLE_ADMIN")));

        // 4. Перевіряємо в БД
        User updatedUser = userRepository.findByUsername("Bobr").orElseThrow();
        boolean hasAdminRole = updatedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        assertThat(hasAdminRole).isTrue();
    }
}
