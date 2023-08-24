package com.bezkoder.spring.security.postgresql.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bezkoder.spring.security.postgresql.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    private Integer step;
    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;
    private String country;
    private String company;
    private String position;

    private String countryCode;

    private String photo;
    private String answers;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password, Integer step,
                           Collection<? extends GrantedAuthority> authorities, String firstName, String lastName, String countryCode, String country, String company, String position, String photo, String answers) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.step = step;
        this.authorities = authorities;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryCode = countryCode;
        this.country = country;
        this.company = company;
        this.position = position;
        this.photo = photo;
        this.answers = answers;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail().toLowerCase(),
                user.getPassword(),
                user.getStep(),
                authorities,
                user.getFirstName(),
                user.getLastName(),
                user.getCountryCode(),
                user.getCountry(),
                user.getCompany(),
                user.getPosition(),
                user.getPhoto(),
                user.getAnswers()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    public Integer getStep() {
        return step;
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
