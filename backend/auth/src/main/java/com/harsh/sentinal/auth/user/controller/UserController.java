package com.harsh.sentinal.auth.user.controller;

import com.harsh.sentinal.auth.security.principal.CustomUserDetails;
import com.harsh.sentinal.auth.security.service.AuthenticationService;
import com.harsh.sentinal.auth.security.service.CustomUserDetailsService;
import com.harsh.sentinal.auth.user.dto.*;
import com.harsh.sentinal.auth.user.entity.User;
import com.harsh.sentinal.auth.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me")
    public UserInfoLoginResponse me(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        User user = customUserDetails.getUser();
        return new UserInfoLoginResponse(user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getRole());
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userEndpoint() {
        return "Welcome User!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Welcome Admin!";
    }
}
