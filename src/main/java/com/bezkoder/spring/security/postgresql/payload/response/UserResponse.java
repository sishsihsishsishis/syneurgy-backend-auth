package com.bezkoder.spring.security.postgresql.payload.response;

import java.util.List;

public class UserResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;

    private Integer step;
    private List<String> roles;

    private String firstName;
    private String lastName;
    private String country;
    private String company;
    private String position;

    private String countryCode;

    private String photo;
    private String answers;

    private Boolean isActive;

    private Boolean isEmailVerified;

    private Boolean isActiveForTeam;
    private long createdDate;

    public UserResponse(String accessToken, Long id, String username, String email, Integer step, List<String> roles, String firstName, String lastName, String countryCode, String country, String company, String position, String photo, String answers, boolean isActiveForTeam, boolean isActive, boolean isEmailVerified, long createdDate) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.step = step;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryCode = countryCode;
        this.country = country;
        this.company = company;
        this.position = position;
        this.photo = photo;
        this.answers = answers;
        this.isActiveForTeam = isActiveForTeam;
        this.isActive = isActive;
        this.isEmailVerified = isEmailVerified;
        this.createdDate = createdDate;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getAnswers() {
        return answers;
    }

    public void setActiveForTeam(Boolean activeForTeam) {
        isActiveForTeam = activeForTeam;
    }

    public Boolean getActiveForTeam() {
        return isActiveForTeam;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }
}

