package com.migstreva.auth_api.service;

import com.migstreva.auth_api.dto.TokenResponseDTO;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        Field secretField = JwtService.class.getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, "dHgPkZh3GztB/9FtmVvHDXV5vx2WZ1kNfciRQGzNT20=");
    }

    @Test
    void generateToken_shouldReturnTokenWithCorrectSubject() {
        TokenResponseDTO response = jwtService.generateToken("johndoe");

        Claims claims = jwtService.parseToken(response.token());
        assertThat(claims.getSubject()).isEqualTo("johndoe");
    }

    @Test
    void generateToken_shouldReturnBearerType() {
        TokenResponseDTO response = jwtService.generateToken("johndoe");

        assertThat(response.type()).isEqualTo("Bearer");
    }

    @Test
    void generateToken_shouldReturnExpiresInOf3600Seconds() {
        TokenResponseDTO response = jwtService.generateToken("johndoe");

        assertThat(response.expiresIn()).isEqualTo(3600L);
    }

    @Test
    void generateToken_shouldReturnNonBlankToken() {
        TokenResponseDTO response = jwtService.generateToken("johndoe");

        assertThat(response.token()).isNotBlank();
    }
}

