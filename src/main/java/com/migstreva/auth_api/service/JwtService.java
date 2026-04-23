package com.migstreva.auth_api.service;

import com.migstreva.auth_api.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "segredinho_segredinho_segredinho";

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public TokenResponse generateToken(String subject) {
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

        return new TokenResponse(token, "Bearer", expiresIn);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public boolean isTokenValid(String token, String subject) {
        final String extractedSubject = extractSubject(token);
        return extractedSubject.equals(subject) && !isTokenExpired(token);
    }
}
