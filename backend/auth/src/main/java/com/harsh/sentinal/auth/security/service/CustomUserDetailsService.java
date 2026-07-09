package com.harsh.sentinal.auth.security.service;

import com.harsh.sentinal.auth.security.principal.CustomUserDetails;
import com.harsh.sentinal.auth.user.entity.User;
import com.harsh.sentinal.auth.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User checkIfUserExists = userRepo.findByEmail(username).orElse(null);

        if(checkIfUserExists == null){
            throw new UsernameNotFoundException("Username Not Registered!!!");
        }

        return new CustomUserDetails(checkIfUserExists);
    }
}
