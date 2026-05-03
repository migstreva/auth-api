package com.migstreva.auth_api.controller;

import com.migstreva.auth_api.dto.LoginRequest;
import com.migstreva.auth_api.dto.TokenResponse;
import com.migstreva.auth_api.entity.User;
import com.migstreva.auth_api.exception.InvalidCredentialsException;
import com.migstreva.auth_api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request){

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(),
                        request.password()
                )
        );

        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        TokenResponse token = jwtService.generateToken(username);

        return ResponseEntity.ok(token);
    }
}
