package com.yannick.radioo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    public static Retrofit getRetrofit(){
        return retrofit;
    }
}
