package com.reminisense.fa.models;

/**
 * Created by USER on 5/23/2016.
 */
public class User {

    private int userId;
    private int companyId;
    private int authorities;
    private String firstName;
    private String lastName;
    private String position;
    private String description;
    private String email;
    //TODO userImageUrls
    //private String userImageUrls;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getAuthorities() {
        return authorities;
    }

    public void setAuthorities(int userLevel) {
        this.authorities = userLevel;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*
    public String getUserImageUrls() {
        return userImageUrls;
    }

    public void setUserImageUrls(String userImageUrls) {
        this.userImageUrls = userImageUrls;
    }
    */
}

