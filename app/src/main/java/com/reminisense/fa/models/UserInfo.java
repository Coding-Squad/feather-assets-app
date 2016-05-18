package com.reminisense.fa.models;

/**
 * Created by USER on 5/18/2016.
 */
public class UserInfo {

    private int success;
    private User user;

    public int getSuccess(){
        return success;
    }

    public void setSuccess(int success){
        this.success = success;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
