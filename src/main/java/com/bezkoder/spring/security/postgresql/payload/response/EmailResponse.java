package com.bezkoder.spring.security.postgresql.payload.response;

public class EmailResponse {
    private String email;
    private String status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmailResponse(String email, String status) {
        this.email = email;
        this.status = status;
    }
}
