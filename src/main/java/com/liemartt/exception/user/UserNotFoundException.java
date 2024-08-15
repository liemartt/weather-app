package com.liemartt.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
}
