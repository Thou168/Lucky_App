package com.example.lucky_app.Api;

import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lucky_app.models.BrandViewModel;
import com.example.lucky_app.models.CategoryViewModel;
import com.example.lucky_app.models.ModelingViewModel;
import com.example.lucky_app.models.TypeViewModel;
import com.example.lucky_app.models.YearViewModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.google.gson.JsonObject;

public class CommonFunction {
    public static String getAuthenticationEncodeString(String username, String password){
        final String userAccount=String.format("%s:%s",username,password);
        return Base64.encodeToString(userAccount.getBytes(),Base64.NO_WRAP);
    }

    public static List<YearViewModel> getYearList(String username, String password){
        final List<YearViewModel> yearList = new ArrayList<YearViewModel>();
        String API_ENDPOINT= String.format("%s%s",ConsumeAPI.BASE_URL,"api/v1/years/");
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, API_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray=response.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        yearList.add(new YearViewModel(object.getInt("id"),object.getString("year")));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        return yearList;
    }

    public static List<CategoryViewModel> getCategoriesList(RequestQueue mQueue, String username, String password){
        //RequestQueue mQueue=new Volley.newRequestQueue(context);
        final List<CategoryViewModel> categories=new ArrayList<CategoryViewModel>();
        String API_ENDPOINT=String.format("%s%s",ConsumeAPI.BASE_URL,"api/v1/categories/");
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, API_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray=response.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        categories.add(new CategoryViewModel(object.getInt("id"),object.getString("cat_name"),object.getString("cat_name_kh"),object.getString("cat_description"),object.getString("cat_image_path"),object.getInt("record_status")));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        return categories;
    }

    public static List<TypeViewModel> getTypesList(String username, String password){
        final List<TypeViewModel> types=new ArrayList<TypeViewModel>();
        String API_ENDPOINT=String.format("%s%s",ConsumeAPI.BASE_URL,"v1/types/");
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, API_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray jsonArray=response.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject obj=jsonArray.getJSONObject(i);
                        types.add(new TypeViewModel(obj.getInt("id"),obj.getString("type"),obj.getString("type_kh"),obj.getString("record_status")));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        return types;
    }

    public static List<BrandViewModel> getBrandsList(String username, String password){
        final List<BrandViewModel> brands=new ArrayList<BrandViewModel>();
        String API_ENDPOINT=String.format("%s%s",ConsumeAPI.BASE_URL,"api/v1/brands/");
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, API_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray=response.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject obj=jsonArray.getJSONObject(i);
                        brands.add(new BrandViewModel(obj.getInt("id"),obj.getInt("category"),obj.getString("brand_name"),obj.getString("brand_name_kh"),obj.getString("description"),obj.getString("brand_image_path")));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        return brands;
    }

    public static List<ModelingViewModel> getModelsList(String username, String password){
        final List<ModelingViewModel> models=new ArrayList<>();
        String API_ENDPOINT=String.format("%s%s",ConsumeAPI.BASE_URL,"api/v1/models/");
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, API_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray=response.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject obj=jsonArray.getJSONObject(i);
                        models.add(new ModelingViewModel(obj.getInt("id"),obj.getInt("brand"),obj.getString("modeling_name"),obj.getString("modeling_name_kh"),obj.getString("description"),obj.getString("modeling_image_path"),obj.getInt("record_status")));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        return models;
    }

}
