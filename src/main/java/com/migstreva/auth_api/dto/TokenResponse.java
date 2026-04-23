package com.migstreva.auth_api.dto;

public record TokenResponse(
        String token,
        String type,
        long expiresIn
) {
}
