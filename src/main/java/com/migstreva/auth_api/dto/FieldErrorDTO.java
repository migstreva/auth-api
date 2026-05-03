package com.migstreva.auth_api.dto;

public record FieldErrorDTO(
        String field,
        String error
) {
}
