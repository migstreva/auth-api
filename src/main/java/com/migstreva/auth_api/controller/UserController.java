package com.migstreva.auth_api.controller;

import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController implements GenericController{

    private final UserService service;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody User user) {
        service.save(user);

        URI location = generateHeaderLocation(user.getId());
        return ResponseEntity.created(location).build();
    }
}
