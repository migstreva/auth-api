package com.migstreva.auth_api.service;

import com.migstreva.auth_api.exception.UserAlreadyExistsException;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.exception.UserNotFoundException;
import com.migstreva.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final SqsService sqsService;

    public User save(User user) {
        if (repository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())){
            throw new UserAlreadyExistsException("Username or email already in use");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        User saved = repository.save(user);
        sqsService.publishUserCreated(saved.getUsername(), saved.getEmail());

        return saved;
    }

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return repository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public void deleteByUsername(String username) {
        Optional<User> userOptional = repository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Could not find user to delete");
        }

        repository.delete(userOptional.get());
    }
}
