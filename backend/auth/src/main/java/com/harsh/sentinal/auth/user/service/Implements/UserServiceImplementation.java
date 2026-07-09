package com.harsh.sentinal.auth.user.service.Implements;

import com.harsh.sentinal.auth.user.dto.LoginRequest;
import com.harsh.sentinal.auth.user.dto.RegisterRequest;
import com.harsh.sentinal.auth.user.dto.RegisterResponse;
import com.harsh.sentinal.auth.common.enums.Role;
import com.harsh.sentinal.auth.user.entity.User;
import com.harsh.sentinal.auth.user.exception.IncorrectPasswordException;
import com.harsh.sentinal.auth.user.exception.UserExistsException;
import com.harsh.sentinal.auth.user.exception.UserNotFoundException;
import com.harsh.sentinal.auth.user.repository.UserRepo;
import com.harsh.sentinal.auth.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepo userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public RegisterResponse registerUser(RegisterRequest userInfo){
        try{
            User checkifUserExists = userRepo.findByEmail(userInfo.emailId()).orElse(null);

            if(checkifUserExists != null){
                throw new UserExistsException();
            }
            String hashedPassword = encoder.encode(userInfo.password());
            User newUser = new User();
            newUser.setFirst_name(userInfo.firstName());
            newUser.setLast_name(userInfo.lastName());
            newUser.setEmail(userInfo.emailId());
            newUser.setPassword(hashedPassword);
            newUser.setRole(Role.USER);

            userRepo.save(newUser);

            return new RegisterResponse(newUser.getId(), newUser.getFirst_name(), newUser.getLast_name(), newUser.getEmail(), newUser.getRole(), newUser.getCreated_at());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> loginUser(LoginRequest loginInfo){
        User checkIfUserExists = userRepo.findByEmail(loginInfo.emailId()).orElse(null);

        if(checkIfUserExists == null){
            throw new UserNotFoundException();
        }

        boolean checkIfPasswordMatches = encoder.matches(loginInfo.password(), checkIfUserExists.getPassword());

        if(!checkIfPasswordMatches){
            throw new IncorrectPasswordException();
        }

        return new ResponseEntity<>("Login Successfull!!", HttpStatus.ACCEPTED);
    }
}
