package com.bezkoder.spring.security.postgresql.payload.response;

import com.bezkoder.spring.security.postgresql.models.Question;

import java.util.List;

public class InviteResponse {
    private String msg;


    public InviteResponse(String msg) {
        this.msg = msg;

    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
