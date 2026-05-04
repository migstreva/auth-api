package com.migstreva.auth_api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Mandatory Field")
        String usernameOrEmail,
        @NotBlank(message = "Mandatory Field")
        String password
) {
}
