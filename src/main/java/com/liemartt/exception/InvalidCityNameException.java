package com.liemartt.exception;

public class InvalidCityNameException extends RuntimeException{
    public InvalidCityNameException() {
        super("Invalid city name");
    }
    
    public InvalidCityNameException(String message) {
        super(message);
    }
}
