package com.liemartt.exception;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException() {
        super("Please log in");
    }
    
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
