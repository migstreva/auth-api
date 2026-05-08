package com.migstreva.auth_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migstreva.auth_api.dto.LoginRequestDTO;
import com.migstreva.auth_api.entity.Role;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class AuthControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

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

        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("johndoe@email.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setDob(LocalDate.of(2005, 2, 1));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Test
    void login_shouldReturn200AndToken_whenCredentialsAreValid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("johndoe", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void login_shouldReturn200AndToken_whenEmailIsUsedInsteadOfUsername() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("johndoe@email.com", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_shouldReturn401_whenPasswordIsWrong() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("johndoe", "wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturn401_whenUserDoesNotExist() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("unknown", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturn422_whenUsernameOrEmailIsBlank() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].field").value("usernameOrEmail"));
    }

    @Test
    void login_shouldReturn422_whenPasswordIsBlank() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("johndoe", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].field").value("password"));
    }
}