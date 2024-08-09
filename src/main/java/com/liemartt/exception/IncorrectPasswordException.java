package com.liemartt.exception;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException() {
        super("Incorrect password");
    }
    
}
