package com.harsh.sentinal.auth.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (

    @NotBlank(message = "First name is required!!")
    @Size(max = 100)
    String firstName,

    @NotBlank(message = "Last name is required!!")
    @Size(max = 100)
    String lastName,

    @NotBlank(message = "Email Id is required!!")
    @Email(message = "Invalid Email Address!!")
    String emailId,

    @NotBlank(message = "Password is required!!")
    @Size(min = 8 , max = 100)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must contain uppercase, lowercase, digit and special character"
    )
    String password
) {}
