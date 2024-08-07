package com.liemartt.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
