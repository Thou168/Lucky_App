package com.bt_121shoppe.lucky_app.utils;

import android.util.Base64;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Api.User;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommonFunction {
    public static User obj;
    public static String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        User user=new User();

        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }
    public static User getUser(String endcodeAuth,int pk){
        User user=new User();
        String URL_ENDPOINT= ConsumeAPI.BASE_URL+"api/v1/users/"+pk;
        MediaType MEDIA_TYPE= MediaType.parse("application/json");
        OkHttpClient client=new  OkHttpClient();
        Request request=new Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",endcodeAuth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage=response.body().toString();
                Gson gson=new Gson();
                try{
                    //User obj=new User();
                    obj=gson.fromJson(mMessage,User.class);
                    //user=obj;
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
        user=obj;
        return user;
    }

    public static String doGetRequest(String url) throws IOException {
        com.squareup.okhttp.OkHttpClient client=new com.squareup.okhttp.OkHttpClient();
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .url(url)
                .build();

        com.squareup.okhttp.Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
