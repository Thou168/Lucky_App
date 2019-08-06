package com.bt_121shoppe.lucky_app.Startup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Api.api.AllResponse;
import com.bt_121shoppe.lucky_app.Api.api.Breand;
import com.bt_121shoppe.lucky_app.Api.api.Category;
import com.bt_121shoppe.lucky_app.Api.api.Client;
import com.bt_121shoppe.lucky_app.Api.api.Service;
import com.bt_121shoppe.lucky_app.Api.api.Year;
import com.bt_121shoppe.lucky_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Filter extends AppCompatActivity {


   private  TextView btnCategory,btnBrand,btnyear,submit_filter,tv_result,tv_done;
   private  int cate=0,brand=0,model=0,year=0,type=0;
   private  String stTitle="",stCategory="",stBrand="",stYear="",st="";
   private ImageView icCategory_fil,icBrand_fil,icYear_fil;
   private  String [] cateListItems,brandListItems,yearListItems;
   private  int [] cateIDlist,brandIDlist,yearIDlist;
   private LinearLayout rela_cate,rela_brand,rela_year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        tv_result = findViewById(R.id.tv_result);
        tv_result.setOnClickListener(v -> finish());
        tv_done  = findViewById(R.id.tv_done);
        rela_cate  = findViewById(R.id.linea_cate);
        rela_brand = findViewById(R.id.linea_brand);
        rela_year  = findViewById(R.id.linea_year);
        btnCategory = findViewById(R.id.search_category);
        btnBrand = findViewById(R.id.search_brand);
        btnyear = findViewById(R.id.search_year);
        submit_filter = findViewById(R.id.btnSubmit_filter);
        icCategory_fil = findViewById(R.id.icCategory_sr);
        icBrand_fil    = findViewById(R.id.icBrand_sr);
        icYear_fil     = findViewById(R.id.icYear_sr);

        Intent getTitle = getIntent();
        stTitle = getTitle.getStringExtra("title");

        getCategory();
        getBrand();
        getYear();
        rela_cate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder mBuilder = new AlertDialog.Builder(Filter.this);
               mBuilder.setTitle(R.string.choose_category);
               mBuilder.setSingleChoiceItems(cateListItems, -1, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int i) {
                       btnCategory.setText(cateListItems[i]);
                       icCategory_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                       cate = cateIDlist[i];
                        stCategory = String.valueOf(cate);
                       getBrand();
                       dialog.dismiss();
                   }
               });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
           }
       });

        rela_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Filter.this);
                mBuilder.setTitle(R.string.choose_brand);
                mBuilder.setSingleChoiceItems(brandListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        btnBrand.setText(brandListItems[i]);
                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        brand = brandIDlist[i];
                        stBrand = String.valueOf(brand);

                        dialog.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        rela_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Filter.this);
                mBuilder.setTitle(R.string.choose_year);
                mBuilder.setSingleChoiceItems(yearListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        btnyear.setText(yearListItems[i]);
                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        year = yearIDlist[i];
                        stYear = String.valueOf(year);

                        dialog.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result();

            }
        });
        submit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Result();

            }
        });
    } // create

    private void Result(){
//        Intent intent = new Intent(this,Search1.class);
//        intent.putExtra("title_search",stTitle);
//        intent.putExtra("category",stCategory);
//        intent.putExtra("brand",stBrand);
//        intent.putExtra("year",stYear);
//        startActivity(intent);
//        finish();

        Intent intent = new Intent();
        intent.putExtra("title_search",stTitle);
        intent.putExtra("category",stCategory);
        intent.putExtra("brand",stBrand);
        intent.putExtra("year",stYear);
        setResult(2,intent);
        finish();

        Log.d("FIlter",stTitle+","+stCategory+","+stBrand+","+stYear);
    }
    private void getCategory(){
        String url = ConsumeAPI.BASE_URL+"/api/v1/categories/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String f = e.getMessage();
                Log.d("Failure",f);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{

                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    cateListItems = new String[jsonArray.length()];
                    cateIDlist = new int[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("cat_name");
                        cateListItems[i] = name;
                        cateIDlist[i] = id;

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getBrand( ){
        String url = ConsumeAPI.BASE_URL+"/api/v1/brands/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String f = e.getMessage();
                Log.d("Failure",f);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{

                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    if (cate==0){
                        brandListItems = new String[jsonArray.length()];
                        brandIDlist = new int[jsonArray.length()];
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String brand = object.getString("brand_name");
                            brandListItems[i] = brand;
                            brandIDlist[i] = id;

                        }
                    }else {
                        int count = 0;
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            int category = object.getInt("category");
                            if (cate==category){
                                count++;
                            }
                        }

                        brandListItems = new String[count];
                        brandIDlist = new int[count];
                        int ccount = 0;
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            int category = object.getInt("category");
                            if (cate==category) {
                                int id = object.getInt("id");
                                String name = object.getString("brand_name");
                                brandListItems[ccount] = name;
                                brandIDlist[ccount] = id;
                                ccount++;
                            }
                        }
                    }



                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getYear(){
        String url = ConsumeAPI.BASE_URL+"/api/v1/years/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String f = e.getMessage();
                Log.d("Failure",f);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{

                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    yearListItems = new String[jsonArray.length()];
                    yearIDlist = new int[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("year");
                        yearListItems[i] = name;
                        yearIDlist[i] = id;

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
