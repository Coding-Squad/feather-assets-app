package com.reminisense.fa.utils;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {

    private FeatherAssetsWebService apiService;

    public RestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FeatherAssetsWebService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(FeatherAssetsWebService.class);
    }

    public FeatherAssetsWebService getApiService() {

        return apiService;
    }

}
