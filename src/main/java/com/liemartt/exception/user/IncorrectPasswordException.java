package com.liemartt.exception.user;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException() {
    }
    
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
