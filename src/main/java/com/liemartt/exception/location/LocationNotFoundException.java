package com.liemartt.exception.location;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException() {
        super("City not found");
    }
    
    public LocationNotFoundException(String message) {
        super(message);
    }
}
