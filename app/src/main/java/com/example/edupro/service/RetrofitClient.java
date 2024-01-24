package com.example.edupro.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.1.16:5000";
    public static int TIME_OUT_DURATION = 60;

    public static ServerAPIService createService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(TIME_OUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_DURATION, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ServerAPIService.class);
    }
}
