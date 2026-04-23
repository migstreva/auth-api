package com.migstreva.auth_api.repository;

import com.migstreva.auth_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail, String usernameOrEmail2);

    boolean existsByUsernameOrEmail(String username, String email);

}
