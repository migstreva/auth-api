package com.migstreva.auth_api.controller;

import com.migstreva.auth_api.dto.UserRegisterDTO;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.mapper.UserMapper;
import com.migstreva.auth_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController implements GenericController{

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody @Valid UserRegisterDTO dto) {
        User user = mapper.toEntity(dto);
        service.save(user);
        URI location = generateHeaderLocation(user.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findAll() {
        return service.findAll();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@RequestParam String username ) {
        service.deleteByUsername(username);
    }
}
