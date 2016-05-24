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

    @POST("/api/register/asset")
    Call<RestResult> registerAsset (@Body Asset asset);

    @GET("/api/asset")
    Call<Asset> verify (@Path("tag") String tag);

    @POST("/login")
    Call<LoginResult> login (@Body LoginInfo loginInfo);
    /*
    test post below
     */
    @POST("/api/user/add")
    Call<RestResult> registerUser (@Body User user);

    /*@GET("/user")
    Call<User> getUserId (@Path("userId") String userId);
    */
}
