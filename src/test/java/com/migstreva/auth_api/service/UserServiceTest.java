package com.migstreva.auth_api.service;

import com.migstreva.auth_api.entity.Role;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.exception.UserAlreadyExistsException;
import com.migstreva.auth_api.exception.UserNotFoundException;
import com.migstreva.auth_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserService}.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("johndoe");
        user.setEmail("johndoe@email.com");
        user.setPassword("password123");
        user.setDob(LocalDate.of(2005, 2, 1));
        user.setRole(Role.USER);
    }

    // -------------------------
    // save()
    // -------------------------

    @Test
    void save_shouldEncodePasswordAndSaveUser_whenUserDoesNotExist() {
        when(repository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())).thenReturn(false);
        when(encoder.encode("password123")).thenReturn("encoded_password");
        when(repository.save(user)).thenReturn(user);

        User saved = service.save(user);

        assertThat(saved.getPassword()).isEqualTo("encoded_password");
        verify(repository).save(user);
    }

    @Test
    void save_shouldThrowUserAlreadyExistsException_whenUsernameOrEmailAlreadyInUse() {
        when(repository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> service.save(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Username or email already in use");

        verify(repository, never()).save(any());
    }

    // -------------------------
    // findByUsernameOrEmail()
    // -------------------------

    @Test
    void findByUsernameOrEmail_shouldReturnUser_whenUserExists() {
        when(repository.findByUsernameOrEmail("johndoe", "johndoe")).thenReturn(Optional.of(user));

        Optional<User> result = service.findByUsernameOrEmail("johndoe");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("johndoe");
    }

    @Test
    void findByUsernameOrEmail_shouldReturnEmpty_whenUserDoesNotExist() {
        when(repository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        Optional<User> result = service.findByUsernameOrEmail("inexistent");

        assertThat(result).isEmpty();
    }

    // -------------------------
    // findAll()
    // -------------------------

    @Test
    void findAll_shouldReturnAllUsers() {
        User other = new User();
        other.setUsername("other");
        when(repository.findAll()).thenReturn(List.of(user, other));

        List<User> result = service.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoUsersExist() {
        when(repository.findAll()).thenReturn(List.of());

        List<User> result = service.findAll();

        assertThat(result).isEmpty();
    }

    // -------------------------
    // deleteByUsername()
    // -------------------------

    @Test
    void deleteByUsername_shouldDeleteUser_whenUserExists() {
        when(repository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        service.deleteByUsername("johndoe");

        verify(repository).delete(user);
    }

    @Test
    void deleteByUsername_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        when(repository.findByUsername("inexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteByUsername("inexistent"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Could not find user to delete");

        verify(repository, never()).delete(any());
    }

}
