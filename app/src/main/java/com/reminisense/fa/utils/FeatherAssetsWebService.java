package com.reminisense.fa.utils;

import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.models.TransactRequest;
import com.reminisense.fa.models.VerifyRequest;
import com.reminisense.fa.models.VerifyResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Retrofit interface definitions to communicate with our API.
 */
public interface FeatherAssetsWebService {

    @POST("/register/asset")
    Call<RestResult> register (@Body Asset asset);

    @POST("/verify")
    Call<VerifyResult> verify (@Body VerifyRequest request);

    @POST
    Call<RestResult> transact (@Body TransactRequest request);

}
