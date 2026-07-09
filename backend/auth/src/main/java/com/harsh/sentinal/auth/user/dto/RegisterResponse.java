package com.harsh.sentinal.auth.user.dto;

import com.harsh.sentinal.auth.common.enums.Role;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(

        UUID id,

        String firstName,

        String lastName,

        String email,

        Role role,

        Instant createdAt

) {}