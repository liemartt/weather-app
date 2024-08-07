package com.liemartt.exception;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException() {
        super("Username not exists");
    }
}
