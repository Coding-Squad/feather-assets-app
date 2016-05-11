package com.reminisense.fa.utils;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {

    private static final String BASE_URL = "";
    private FeaqEndpoint apiService;

    public RestClient() {
//        OkHttpClient client = new OkHttpClient();
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(FeaqEndpoint.class);
    }

    public FeaqEndpoint getApiService() {
        return apiService;
    }

}
