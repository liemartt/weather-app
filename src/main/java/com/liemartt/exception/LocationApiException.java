package com.liemartt.exception;

public class LocationApiException extends RuntimeException{
    public LocationApiException() {
        super("Error fetching locations");
    }
    
    public LocationApiException(String message) {
        super(message);
    }
}
