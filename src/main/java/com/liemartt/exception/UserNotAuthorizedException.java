package com.liemartt.exception;

public class UserNotAuthorizedException extends RuntimeException{
    public UserNotAuthorizedException() {
    }
    
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
