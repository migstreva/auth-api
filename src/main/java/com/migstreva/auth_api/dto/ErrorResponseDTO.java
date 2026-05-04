package com.migstreva.auth_api.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponseDTO(
        int status,
        String message,
        List<FieldErrorDTO> errors
)
{
    public static ErrorResponseDTO conflict(String message){
        return new ErrorResponseDTO(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ErrorResponseDTO notFound(String message){
        return new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), message, List.of());
    }

    public static ErrorResponseDTO unauthorized(String message){
        return new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), message, List.of());
    }

    public static ErrorResponseDTO forbidden(String message){
        return new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), message, List.of());
    }
}
