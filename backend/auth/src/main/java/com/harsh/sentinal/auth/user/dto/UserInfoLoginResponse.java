package com.harsh.sentinal.auth.user.dto;

import com.harsh.sentinal.auth.common.enums.Role;

public record UserInfoLoginResponse(
        String firstName,
        String lastName,
        String email,
        Role role
){}
