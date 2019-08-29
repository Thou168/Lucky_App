package com.bt_121shoppe.lucky_app.Api.api;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

//    private static final String BASE_URL = "http://103.205.26.103:8000";
    private static Retrofit retrofit = null;
    static OkHttpClient httpClient=new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    public static Retrofit getClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConsumeAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient).build();
        }
        return retrofit;
    }
}
