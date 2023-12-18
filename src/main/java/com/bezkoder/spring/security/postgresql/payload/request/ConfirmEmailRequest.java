package com.bezkoder.spring.security.postgresql.payload.request;

public class ConfirmEmailRequest {
    private String email;
    private String token;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
