package com.liemartt.exception.location;

public class LocationExistsException extends RuntimeException {
    public LocationExistsException() {
    }
    
    public LocationExistsException(String message) {
        super(message);
    }
}
