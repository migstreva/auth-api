package com.migstreva.auth_api.service;

import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

}
