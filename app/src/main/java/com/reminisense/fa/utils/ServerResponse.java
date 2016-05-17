package com.reminisense.fa.utils;

import com.reminisense.fa.assets.UserInfo.User;

/**
 * Created by USER on 5/17/2016.
 */
public class ServerResponse {

    private String result;
    private String message;
    private User user;

    public String getResult(){
        return result;
    }

    public  String getMessage(){
        return message;
    }

    public User getUser(){
        return user;
    }
}
