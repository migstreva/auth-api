package com.migstreva.auth_api.security;

import com.migstreva.auth_api.entity.Role;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("johndoe");
        user.setPassword("encoded_password");
        user.setRole(Role.USER);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        when(userService.findByUsernameOrEmail("johndoe")).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("johndoe");

        assertThat(result.getUsername()).isEqualTo("johndoe");
        assertThat(result.getPassword()).isEqualTo("encoded_password");
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        when(userService.findByUsernameOrEmail("inexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("inexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
}
