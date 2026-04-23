package com.migstreva.auth_api.controller;

import com.migstreva.auth_api.dto.LoginRequest;
import com.migstreva.auth_api.dto.TokenResponse;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.exception.InvalidCredentialsException;
import com.migstreva.auth_api.service.JwtService;
import com.migstreva.auth_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request){

        User user = userService
                .findByUsernameOrEmail(request.usernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials!"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials!");
        }

        TokenResponse token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(token);
    }
}
