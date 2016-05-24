package com.reminisense.fa.utils;

import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.LoginInfo;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.models.LoginResult;
import com.reminisense.fa.models.VerifyRequest;
import com.reminisense.fa.models.VerifyResult;
import com.reminisense.fa.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Retrofit interface definitions to communicate with our API.
 */
public interface FeatherAssetsWebService {

    @POST("/register/asset")
    Call<RestResult> register (@Body Asset asset);

    @POST("/verify")
    Call<VerifyResult> verify (@Body VerifyRequest request);

    @POST("/login")
    Call<LoginResult> login (@Body LoginInfo loginInfo);
    /*
    test post below
     */
    @POST("/register/user")
    Call<RestResult> register (@Body User user);

    @GET("/user")
    Call<User> getUserId (@Path("userId") String userId);

}
