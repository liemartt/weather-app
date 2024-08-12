package com.liemartt.exception;

public class InvalidLocationNameException extends RuntimeException{
    public InvalidLocationNameException() {
        super("Invalid city name");
    }
    
    public InvalidLocationNameException(String message) {
        super(message);
    }
}
