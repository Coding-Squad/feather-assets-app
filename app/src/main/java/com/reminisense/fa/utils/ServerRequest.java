package com.reminisense.fa.utils;

import com.reminisense.fa.assets.UserInfo.User;

/**
 * Created by USER on 5/17/2016.
 */
public class ServerRequest {

    private String operation;
    private User user;

    public void setOperation(String operation){
        this.operation = operation;
    }

    public void setUser(User user){
        this.user = user;
    }
}
