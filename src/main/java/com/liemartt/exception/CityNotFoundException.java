package com.liemartt.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException() {
        super("City not found");
    }
    
    public CityNotFoundException(String message) {
        super(message);
    }
}
