package com.harsh.sentinal.auth.user.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User Not Registered!!");
    }
}
