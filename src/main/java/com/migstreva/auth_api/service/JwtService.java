package com.migstreva.auth_api.service;

import com.migstreva.auth_api.dto.TokenResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public TokenResponseDTO generateToken(String subject) {
        long expirationMillis = 1000 * 60 * 60; // 1h
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        String token = Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getKey())
                .compact();

        long expiresIn = expirationMillis / 1000;

        return new TokenResponseDTO(token, "Bearer", expiresIn);
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
