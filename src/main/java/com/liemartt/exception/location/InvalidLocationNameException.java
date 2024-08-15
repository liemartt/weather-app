package com.liemartt.exception.location;

public class InvalidLocationNameException extends RuntimeException{
    public InvalidLocationNameException() {
        super("Invalid location name");
    }
    
    public InvalidLocationNameException(String message) {
        super(message);
    }
}
