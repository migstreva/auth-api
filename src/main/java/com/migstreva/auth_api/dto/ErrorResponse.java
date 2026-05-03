package com.migstreva.auth_api.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        List<FieldErrorDTO> errors
)
{
    public static ErrorResponse conflict(String message){
        return new ErrorResponse(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ErrorResponse notFound(String message){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), message, List.of());
    }

    public static ErrorResponse unauthorized(String message){
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message, List.of());
    }

    public static ErrorResponse forbidden(String message){
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), message, List.of());
    }
}
