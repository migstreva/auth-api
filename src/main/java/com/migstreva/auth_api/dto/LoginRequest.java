package com.migstreva.auth_api.dto;

public record LoginRequest(
        String usernameOrEmail,
        String password
) {
}
