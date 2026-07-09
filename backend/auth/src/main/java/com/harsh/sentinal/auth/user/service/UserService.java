package com.harsh.sentinal.auth.user.service;

import com.harsh.sentinal.auth.user.dto.LoginRequest;
import com.harsh.sentinal.auth.user.dto.RegisterRequest;
import com.harsh.sentinal.auth.user.dto.RegisterResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public RegisterResponse registerUser(RegisterRequest userInfo);
}
