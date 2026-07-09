package com.harsh.sentinal.auth.user.controller;

import com.harsh.sentinal.auth.security.service.AuthenticationService;
import com.harsh.sentinal.auth.user.dto.LoginRequest;
import com.harsh.sentinal.auth.user.dto.LoginResponse;
import com.harsh.sentinal.auth.user.dto.RegisterRequest;
import com.harsh.sentinal.auth.user.dto.RegisterResponse;
import com.harsh.sentinal.auth.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest userInfo){
        return userService.registerUser(userInfo);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@Valid @RequestBody LoginRequest loginInfo){
        return authenticationService.loginUser(loginInfo);
    }
}
