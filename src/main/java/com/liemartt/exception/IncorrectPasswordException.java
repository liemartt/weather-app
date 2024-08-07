package com.liemartt.exception;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
