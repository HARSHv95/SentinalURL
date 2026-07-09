package com.harsh.sentinal.auth.security.service;

import com.harsh.sentinal.auth.security.jwt.JwtService;
import com.harsh.sentinal.auth.security.principal.CustomUserDetails;
import com.harsh.sentinal.auth.user.dto.LoginRequest;
import com.harsh.sentinal.auth.user.dto.LoginResponse;
import com.harsh.sentinal.auth.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

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

        String authToken = jwtService.generateToken(userDetails.getUsername(), userDetails.getAuthorities().toString());

        return new LoginResponse("Login Successfull", authToken);
    }
}
