package com.migstreva.auth_api.dto;

import com.migstreva.auth_api.entity.Role;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRegister(
        @NotBlank(message = "Mandatory field")
        @Size(min = 2, max = 100, message = "Must be between {min} and {max} characters long")
        String username,
        @NotBlank(message = "Mandatory field")
        @Email(message = "Invalid email")
        @Size(max = 150, message = "Must be {max} characters long")
        String email,
        @NotNull(message = "Mandatory field")
        @Past(message = "Date of birth must be in the past")
        LocalDate dob,
        @NotBlank(message = "Mandatory field")
        @Size(min = 8, max = 64, message = "Must be between {min} and {max} characters long")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "Password must contain at least one letter and one number"
        )
        String password,
        @NotNull(message = "Mandatory field")
        Role role
) {
}
