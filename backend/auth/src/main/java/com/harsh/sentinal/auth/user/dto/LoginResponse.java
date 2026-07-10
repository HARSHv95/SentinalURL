package com.harsh.sentinal.auth.user.dto;

public record LoginResponse(
    String accessToken,
    String tokenType,
    Long expiresIn,
    UserInfoLoginResponse user
) {}
