package com.liemartt.exception.user;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException() {
        super("Please log in");
    }
    
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
