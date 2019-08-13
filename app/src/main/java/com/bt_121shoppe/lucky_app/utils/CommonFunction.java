package com.bt_121shoppe.lucky_app.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.util.Log;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Api.User;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
                    //Breand obj=new Breand();
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

    public static String doGetRequestwithAuth(String url,String encode) throws IOException {
        com.squareup.okhttp.OkHttpClient client=new com.squareup.okhttp.OkHttpClient();
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
                //.header("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo1LCJ1c2VybmFtZSI6IjAxMTMwODI4MSIsImV4cCI6MTU2NTE0NTU3NSwiZW1haWwiOm51bGx9.OyTLaGXInAFsXTNfmxDIGLrTDpPnZF7nOlyNjCcywj8")
                .url(url)
                .build();

        com.squareup.okhttp.Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getFrontImageURL(String url){
        String[] splitURL=url.split("/");
        if(splitURL.length>0)
            return ConsumeAPI.IMAGE_STRING_PATH+splitURL[splitURL.length-1];
        return "";
    }

    public static String getAddressFromMap(Context context, double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder=new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            //Log.d("Address",addresses.toString());
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return city;
        }catch (IOException e){
            e.printStackTrace();
        }

        return "";
    }

}
