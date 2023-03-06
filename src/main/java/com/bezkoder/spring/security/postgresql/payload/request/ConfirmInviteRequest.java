package com.bezkoder.spring.security.postgresql.payload.request;

public class ConfirmInviteRequest {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
