package com.harsh.sentinal.auth.user.exception;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(){
        super("Incorrect Password!!!");
    }
}
