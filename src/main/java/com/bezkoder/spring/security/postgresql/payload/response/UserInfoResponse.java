package com.bezkoder.spring.security.postgresql.payload.response;

public class UserInfoResponse {
    private String firstName;
    private String lastName;
    private String country;
    private String company;
    private String position;

    private Integer step;
    public UserInfoResponse(String firstName, String lastName, String country, String company, String position, Integer step) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.company = company;
        this.position = position;
        this.step = step;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}
