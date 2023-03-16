package com.bezkoder.spring.security.postgresql.payload.response;

import com.bezkoder.spring.security.postgresql.models.Question;

import java.util.List;

public class InviteResponse {
    private String msg;
    private Integer step;

    public InviteResponse(String msg, Integer step) {
        this.msg = msg;
        this.step = step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getStep() {
        return step;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
