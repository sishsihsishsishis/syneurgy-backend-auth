package com.bezkoder.spring.security.postgresql.dto;

import java.math.BigInteger;

public class ComponentWithUsersDto {
    private BigInteger component_id;
    private String component_name;
    private String users;

    public ComponentWithUsersDto(BigInteger component_id, String component_name, String users) {
        this.component_id = component_id;
        this.component_name = component_name;
        this.users = users;
    }

    public BigInteger getComponent_id() {
        return component_id;
    }

    public void setComponent_id(BigInteger component_id) {
        this.component_id = component_id;
    }

    public String getComponent_name() {
        return component_name;
    }

    public void setComponent_name(String component_name) {
        this.component_name = component_name;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
