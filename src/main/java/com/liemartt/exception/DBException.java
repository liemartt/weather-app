package com.liemartt.exception;

public class DBException extends RuntimeException {
    public DBException() {
        super("Service is unavailable now");
    }
    
}
