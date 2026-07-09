package com.harsh.sentinal.auth.user.controller;

import com.harsh.sentinal.auth.user.dto.LoginRequest;
import com.harsh.sentinal.auth.user.dto.RegisterRequest;
import com.harsh.sentinal.auth.user.dto.RegisterResponse;
import com.harsh.sentinal.auth.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody RegisterRequest userInfo){
        return userService.registerUser(userInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginInfo){
        return userService.loginUser(loginInfo);
    }
}
