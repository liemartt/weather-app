package com.liemartt.exception.location;

public class LocationApiException extends RuntimeException{
    public LocationApiException() {
        super("Error fetching locations");
    }
    
    public LocationApiException(String message) {
        super(message);
    }
}
