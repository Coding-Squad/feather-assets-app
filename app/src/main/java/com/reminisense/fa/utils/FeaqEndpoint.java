package com.reminisense.fa.utils;

import com.reminisense.fa.assets.UserInfo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 *
 */
public interface FeaqEndpoint {

    @POST("/register/assets")
    Call<User> operation (@Body User user);




}
