package com.reminisense.fa.utils;

import com.reminisense.fa.models.User;
import com.reminisense.fa.models.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 *
 */
public interface FeaqEndpoint {

    @POST("/register/asset")
    Call<UserInfo> register (@Body User user);




}
