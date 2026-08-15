package com.harsh.sentinal.auth.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Email Address Required!!")
        @Email(message = "Invalid Email Address!!")
        String emailId,

        @NotBlank(message = "Password is Required!!")
        @Size(min = 8, max = 100)
        String password
) {}
