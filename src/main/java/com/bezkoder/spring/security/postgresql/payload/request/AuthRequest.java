package com.bezkoder.spring.security.postgresql.payload.request;

public class AuthRequest {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
