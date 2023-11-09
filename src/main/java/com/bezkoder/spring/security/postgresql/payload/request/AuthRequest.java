package com.bezkoder.spring.security.postgresql.payload.request;

public class AuthRequest {
    private String code;
    private String refresh_token;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
