package com.migstreva.auth_api.controller;

import com.migstreva.auth_api.dto.LoginRequestDTO;
import com.migstreva.auth_api.dto.TokenResponseDTO;
import com.migstreva.auth_api.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
//    private final PasswordEncoder passwordEncoder;
//
//    @GetMapping("/hash")
//    public String hash(@RequestParam String password) {
//        return passwordEncoder.encode(password);
//    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO request){

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(),
                        request.password()
                )
        );

        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        TokenResponseDTO token = jwtService.generateToken(username);

        return ResponseEntity.ok(token);
    }
}
