package com.bezkoder.spring.security.postgresql.exception;

public class UserComponentNotFoundException extends RuntimeException{
    public UserComponentNotFoundException(String message) {
        super(message);
    }
}
