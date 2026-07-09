package com.harsh.sentinal.auth.user.exception;

public class UserExistsException extends RuntimeException{
    public UserExistsException(){
        super("Username already Exists!!!");
    }
}
