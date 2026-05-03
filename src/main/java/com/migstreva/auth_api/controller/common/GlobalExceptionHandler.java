package com.migstreva.auth_api.controller.common;

import com.migstreva.auth_api.dto.ErrorResponse;
import com.migstreva.auth_api.dto.FieldErrorDTO;
import com.migstreva.auth_api.exception.InvalidCredentialsException;
import com.migstreva.auth_api.exception.UserAlreadyExistsException;
import com.migstreva.auth_api.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<FieldErrorDTO> errorsList = fieldErrors
                .stream()
                .map(fe -> new FieldErrorDTO(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Validation error.",
                errorsList);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e){
        return ErrorResponse.conflict(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e){
        return ErrorResponse.notFound(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e){
        return ErrorResponse.unauthorized(e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse authorizationDeniedException(AuthorizationDeniedException e){
        return ErrorResponse.forbidden(e.getMessage());
    }

//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleUnhandledException(RuntimeException e){
//        return new ErrorResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "An unexpected error occurred. Please contact administration.",
//                List.of());
//    }
}
