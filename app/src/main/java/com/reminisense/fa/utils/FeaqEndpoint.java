package com.reminisense.fa.utils;

import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.RestResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 */
public interface FeaqEndpoint {

    @POST("/register/asset")
    Call<RestResult> register (@Body Asset asset);




}
