package com.liemartt.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
    
    public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' already exists");
    }
}
