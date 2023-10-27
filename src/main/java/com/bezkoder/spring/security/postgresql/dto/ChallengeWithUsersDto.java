package com.bezkoder.spring.security.postgresql.dto;

import java.math.BigInteger;

public class ChallengeWithUsersDto {
    private BigInteger challenge_id;
    private String challenge_name;
    private String challenge_description;
    private String users;

    public ChallengeWithUsersDto(BigInteger challenge_id, String challenge_name, String challenge_description, String users) {
        this.challenge_id = challenge_id;
        this.challenge_name = challenge_name;
        this.challenge_description = challenge_description;
        this.users = users;
    }

    public BigInteger getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(BigInteger challenge_id) {
        this.challenge_id = challenge_id;
    }

    public String getChallenge_name() {
        return challenge_name;
    }

    public void setChallenge_name(String challenge_name) {
        this.challenge_name = challenge_name;
    }

    public String getChallenge_description() {
        return challenge_description;
    }

    public void setChallenge_description(String challenge_description) {
        this.challenge_description = challenge_description;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}