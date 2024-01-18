package com.bezkoder.spring.security.postgresql.payload.request;

public class JWTRequest {
    public String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
