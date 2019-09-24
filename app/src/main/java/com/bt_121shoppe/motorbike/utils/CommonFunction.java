package com.bt_121shoppe.motorbike.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.Api.User;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommonFunction {

    public static final int FILTERPOSTTYPE=1;
    public final static int FILTERCATEGORY=2;
    public static final int FILTERBRAND=3;
    public final static int FILTERYEAR=4;
    public static final int FILTERPRICERANGE=5;

    public enum ProcessType{
        FacebookRegister,
        FacebookLogin,
        MobileRegister,MobileLogin
    }

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

    public static void convertJSONUser(Context context,String response,String verify){
        Gson gson=new Gson();
        Convert_Json_Java convertJsonJava=new Convert_Json_Java();
        try{

        }catch (JsonParseException e){
            e.printStackTrace();
        }
    }

    // Generates a random int with n digits
    public static int generateRandomDigits(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new Random().nextInt(9 * m);
    }

    public static String generatePostSubTitle(int brandId,int modelId,int year,String colorEn,String colorKH){
        String postSubTitleEN="";
        String postSubTitleKH="";
        //get brand name
        try{
            String brandResponse=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/brands/"+brandId);
            try{
                JSONObject obj=new JSONObject(brandResponse);
                postSubTitleEN=postSubTitleEN+" "+obj.getString("brand_name");
                postSubTitleKH=postSubTitleKH+" "+obj.getString("brand_name_as_kh");
            }catch (JSONException je){
                je.printStackTrace();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        //get model name
        try{
            String modelResponse=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/models/"+modelId);
            try{
                JSONObject obj=new JSONObject(modelResponse);
                postSubTitleEN=postSubTitleEN+" "+obj.getString("modeling_name");
                postSubTitleKH=postSubTitleKH+" "+obj.getString("modeling_name_kh");
            }catch (JSONException je){
                je.printStackTrace();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

        //year
        try{
            String yearResponse=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/years/"+year);
            try{
                JSONObject obj=new JSONObject(yearResponse);
                postSubTitleEN=postSubTitleEN+" "+obj.getString("year");
                postSubTitleKH=postSubTitleKH+" "+obj.getString("year");
            }catch (JSONException je){
                je.printStackTrace();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

        postSubTitleEN=postSubTitleEN+" "+colorEn;
        postSubTitleKH=postSubTitleKH+" "+colorKH;

        return postSubTitleEN+","+postSubTitleKH;
    }

    public static String generatePostSubTitle(int modelId,int yearId,String color){
        String postSubTitleEN="";
        String postSubTitleKH="";
        String modelEN="";
        String modelKH="";
        String strColor="";
        String strColorKH="";
        int brandId=0;
        //get model name and brand id
        try{
            String modelResponse=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/models/"+modelId);
            try{
                JSONObject obj=new JSONObject(modelResponse);
                brandId=obj.getInt("brand");
                modelEN=obj.getString("modeling_name");
                modelKH=obj.getString("modeling_name_kh");
            }catch (JSONException je){
                je.printStackTrace();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        //get brand name
        try{
            String brandResponse=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/brands/"+brandId);
            try{
                JSONObject obj=new JSONObject(brandResponse);
                postSubTitleEN=postSubTitleEN+" "+obj.getString("brand_name")+" "+modelEN;
                postSubTitleKH=postSubTitleKH+" "+obj.getString("brand_name_as_kh")+" "+modelKH;
            }catch (JSONException je){
                je.printStackTrace();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        //get year
        try{
            String yearResponse=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/years/"+yearId);
            try{
                JSONObject obj=new JSONObject(yearResponse);
                postSubTitleEN=postSubTitleEN+" "+obj.getString("year");
                postSubTitleKH=postSubTitleKH+" "+obj.getString("year");
            }catch (JSONException je){
                je.printStackTrace();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        //get color
        switch (color){
            case "blue":
                strColor="Blue";
                strColorKH="ខៀវ";
                break;
            case "black":
                strColor="Black";
                strColorKH="ខ្មៅ";
                break;
            case "silver":
                strColor="Silver";
                strColorKH="ខៀវ";
                break;
            case "red":
                strColor="Red";
                strColorKH="ក្រហម";
                break;
            case "gray":
                strColor="Gray";
                strColorKH="ប្រផេះ";
                break;
            case "yellow":
                strColor="Yellow";
                strColorKH="លឿង";
                break;
            case "pink":
                strColor="Pink";
                strColorKH="ផ្កាឈូក";
                break;
            case "purple":
                strColor="Purple";
                strColorKH="ស្វាយ";
                break;
            case "orange":
                strColor="Orange";
                strColorKH="ទឹកក្រូច";
                break;
            case "green":
                strColor="Green";
                strColorKH="បៃតង";
                break;
        }
        postSubTitleEN=postSubTitleEN+" "+strColor;
        postSubTitleKH=postSubTitleKH+" "+strColorKH;
        return postSubTitleEN+","+postSubTitleKH;
    }

}
