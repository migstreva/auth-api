package com.migstreva.auth_api.dto;

public record TokenResponseDTO(
        String token,
        String type,
        long expiresIn
) {
}
