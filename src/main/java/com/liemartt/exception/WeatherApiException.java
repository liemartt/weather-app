package com.liemartt.exception;

public class WeatherApiException extends RuntimeException {
    public WeatherApiException() {
    }
    
    public WeatherApiException(String message) {
        super(message);
    }
}
