package com.liemartt.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException() {
        super("City not found");
    }
    
    public LocationNotFoundException(String message) {
        super(message);
    }
}
