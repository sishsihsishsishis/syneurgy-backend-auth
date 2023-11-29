package com.bezkoder.spring.security.postgresql.exception;

public class UserChallengeNotFoundException extends RuntimeException {

    public UserChallengeNotFoundException(String message) {
        super(message);
    }
}