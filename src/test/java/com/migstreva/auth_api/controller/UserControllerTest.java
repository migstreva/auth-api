package com.migstreva.auth_api.controller;

import com.migstreva.auth_api.entity.Role;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.deleteAll();

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@email.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setDob(LocalDate.of(1990, 1, 1));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }

    // POST /users

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_shouldReturn201_whenAdminCreatesUser() throws Exception {
        String dto = "{\"username\":\"newuser\",\"email\":\"new@email.com\",\"dob\":\"2000-01-01\",\"password\":\"password123\",\"role\":\"USER\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dto))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void save_shouldReturn403_whenUserTriesToCreateUser() throws Exception {
        String dto = "{\"username\":\"newuser\",\"email\":\"new@email.com\",\"dob\":\"2000-01-01\",\"password\":\"password123\",\"role\":\"USER\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dto))
                .andExpect(status().isForbidden());
    }

    @Test
    void save_shouldReturn401_whenNotAuthenticated() throws Exception {
        String dto = "{\"username\":\"newuser\",\"email\":\"new@email.com\",\"dob\":\"2000-01-01\",\"password\":\"password123\",\"role\":\"USER\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dto))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_shouldReturn422_whenDtoIsInvalid() throws Exception {
        String dto = "{\"username\":\"a\",\"email\":\"invalidemail\",\"dob\":\"2000-01-01\",\"password\":\"s\",\"role\":\"USER\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dto))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_shouldReturn409_whenUsernameOrEmailAlreadyExists() throws Exception {
        String dto = "{\"username\":\"admin\",\"email\":\"admin@email.com\",\"dob\":\"2000-01-01\",\"password\":\"password123\",\"role\":\"USER\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dto))
                .andExpect(status().isConflict());
    }

    // GET /users

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_shouldReturn200AndList_whenAdmin() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findAll_shouldReturn403_whenUser() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void findAll_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    // DELETE /users

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_shouldReturn204_whenAdminDeletesExistingUser() throws Exception {
        mockMvc.perform(delete("/users")
                        .param("username", "admin"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_shouldReturn404_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/users")
                        .param("username", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void delete_shouldReturn403_whenUser() throws Exception {
        mockMvc.perform(delete("/users")
                        .param("username", "admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/users")
                        .param("username", "admin"))
                .andExpect(status().isUnauthorized());
    }
}