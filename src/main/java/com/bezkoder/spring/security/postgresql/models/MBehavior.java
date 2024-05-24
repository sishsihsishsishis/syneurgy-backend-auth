package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.Embeddable;

@Embeddable
public class MBehavior {

    private int no;
    private String behavior;

    // Getters and Setters
    public int getNo() {
        return no;
    }

    public MBehavior() {}

    public MBehavior(int no, String behavior) {
        this.no = no;
        this.behavior = behavior;
    }


    public void setNo(int no) {
        this.no = no;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
