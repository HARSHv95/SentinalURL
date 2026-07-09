package com.harsh.sentinal.auth.user.dto;

public record LoginResponse(
    String message,
    String authToken
) {}
