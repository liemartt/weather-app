package com.liemartt.exception.user;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException() {
        super("Username already exists");
    }
    
    public UsernameExistsException(String username) {
        super("Username '" + username + "' already exists");
    }
}
