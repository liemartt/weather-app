package com.liemartt.exception;

public class LocationExistsException extends RuntimeException {
    public LocationExistsException() {
    }
    
    public LocationExistsException(String message) {
        super(message);
    }
}
