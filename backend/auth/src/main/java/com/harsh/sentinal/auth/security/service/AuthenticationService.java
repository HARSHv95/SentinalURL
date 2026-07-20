package com.harsh.sentinal.auth.security.service;

import com.harsh.sentinal.auth.security.jwt.JwtService;
import com.harsh.sentinal.auth.security.principal.CustomUserDetails;
import com.harsh.sentinal.auth.user.dto.LoginRequest;
import com.harsh.sentinal.auth.user.dto.LoginResponse;
import com.harsh.sentinal.auth.user.dto.UserInfoLoginResponse;
import com.harsh.sentinal.auth.user.entity.User;
import com.harsh.sentinal.auth.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${jwt.expiration}")
    private Long expiration;

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    public LoginResponse loginUser(LoginRequest loginInfo){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginInfo.emailId(),
                        loginInfo.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String authToken = jwtService.generateToken(userDetails.getUsername(), user.getRole().toString(), user.getId());

        return new LoginResponse(authToken, "Bearer", expiration, new UserInfoLoginResponse(user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getRole()));
    }
}
