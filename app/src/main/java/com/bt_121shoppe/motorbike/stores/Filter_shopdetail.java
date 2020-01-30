package com.bt_121shoppe.motorbike.stores;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.searches.SearchTypeActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.example.roman.thesimplerangebar.SimpleRangeBar;
import com.example.roman.thesimplerangebar.SimpleRangeBarOnChangeListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Filter_shopdetail extends AppCompatActivity {


//   private  TextView btnCategory,btnBrand,btnyear ,btnCondition,submit_filter,tv_result,tv_done;
//   private  int cate=0,brand=0,model=0,year=0,type=0;
//   private  String stTitle="",stCategory="",stBrand="",stYear="",stCondition,st="";
//   private ImageView icCategory_fil,icBrand_fil,icYear_fil,icCondition_fil;
//   private  String [] cateListItems,brandListItems,yearListItems,conditionListItems,categoryItemkg,brandItemkh,yearlistItemkh;
//   private  int [] cateIDlist,brandIDlist,yearIDlist;
//   private LinearLayout rela_cate,rela_brand,rela_year,rela_condition;

    private static final String TAG= Filter_shopdetail.class.getSimpleName();

   private  TextView btnCategory,btnBrand,btnyear ,btnType,submit_filter;
    ImageView tv_result,tv_done;
    private  int cate=0,brand=0,model_fil=0,year_fil=0,type=0; //model & year
    private  String stTitle="",stCategory="",stBrand="",stYear="",stType,st="";
    double dbPrice = 0.0;
//    private ImageView icCategory_fil,icBrand_fil,icYear_fil,icType_fil;
    private  String [] cateListItems,brandListItems,yearListItems,typeListItems,categoryItemkg,brandItemkh,yearlistItemkh;
    private  int [] cateIDlist,brandIDlist,yearIDlist;
    private LinearLayout rela_cate,rela_brand,rela_year,rela_type;

    private SimpleRangeBar simpleRangeBar;
    private TextView minText;
    private TextView maxText;
    private int mMinPrice = 0,mMaxPrice = 20000;
    private String stPriceMin,stPriceMax;
    private Integer postId = 0;
    //for bottomsheetdialog

    private int index=3,indexB=6,indexY=23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_shopdetail);
        //seekbar
        simpleRangeBar = findViewById(R.id.rangeBar);
        minText = findViewById(R.id.text_min);
        maxText = findViewById(R.id.text_max);
        postId = getIntent().getIntExtra("ID",0);
        dbPrice = getIntent().getDoubleExtra("price",0);
        simpleRangeBar.setOnSimpleRangeBarChangeListener(new SimpleRangeBarOnChangeListener() {
            @Override
            public void leftThumbValueChanged(long l) {
                mMinPrice = Integer.valueOf(String.valueOf(l));
                minText.setText(String.valueOf(mMinPrice));
            }

            @Override
            public void rightThumbValueChanged(long l) {
                mMaxPrice = Integer.valueOf(String.valueOf(l));
                maxText.setText(String.valueOf(mMaxPrice));
            }
        });
        simpleRangeBar.setRanges(mMinPrice,mMaxPrice);
        simpleRangeBar.setThumbValues(mMinPrice, mMaxPrice);
        simpleRangeBar.setRangeBarColor(getResources().getColor(R.color.seekbar_range));
        simpleRangeBar.setThumbColor(getResources().getColor(R.color.colorPrimary));
        simpleRangeBar.setRangeColor(getResources().getColor(R.color.colorPrimary));
        simpleRangeBar.setThumbColorPressed(getResources().getColor(R.color.colorPrimary));

        locale();
        tv_result = findViewById(R.id.tv_result);
        tv_result.setOnClickListener(v -> finish());
//        tv_done  = findViewById(R.id.tv_done);
        rela_cate  = findViewById(R.id.linea_cate);
        rela_brand = findViewById(R.id.linea_brand);
        rela_year  = findViewById(R.id.linea_year);
        rela_type = findViewById(R.id.linea_type);
        btnCategory = findViewById(R.id.search_category);
        btnBrand = findViewById(R.id.search_brand);
        btnyear = findViewById(R.id.search_year);
        btnType = findViewById(R.id.search_type);
        submit_filter = findViewById(R.id.btnSubmit_filter);
//        icCategory_fil = findViewById(R.id.icCategory_sr);
//        icBrand_fil    = findViewById(R.id.icBrand_sr);
//        icYear_fil     = findViewById(R.id.icYear_sr);
//        icType_fil = findViewById(R.id.icType_sr);

        Intent getTitle = getIntent();
        stTitle = getTitle.getStringExtra("title");

        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefer.getString("My_Lang", "");

        getCategory();
        getBrand();
        getYear();

        rela_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_cate,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter_shopdetail.this);
                bottomSheetDialog.setContentView(dialogView);
//                bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                bottomSheetDialog.show();
                ImageView close = dialogView.findViewById(R.id.icon_close);
                RadioGroup group = dialogView.findViewById(R.id.radio_group);
                Button ok = dialogView.findViewById(R.id.btn_ok);
                close.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

                group.setOnCheckedChangeListener((group1, checkedId) -> {
                    View radioButton = group.findViewById(checkedId);
                    index = group.indexOfChild(radioButton);
                    btnCategory.setText(categoryItemkg[index]);
                    cate = cateIDlist[index];
                    if (language!=null) {
                        if (language.equals("km")) {
                            switch (cate) {
                                case 0:
                                    index=0;
                                    break;
                                case 1:
                                    index=1;
                                    break;
                                case 2:
                                    index=2;
                                    break;
                            }
                        } else if (language.equals("en")) {
                            switch (cate) {
                                case 0:
                                    index=0;
                                    break;
                                case 1:
                                    index=1;
                                    break;
                                case 2:
                                    index=2;
                                    break;
                            }
                        }
                    }
                });

                ok.setOnClickListener(v12 -> {
                    if (cate==0){
                        stCategory = String.valueOf(cate);
//                        icCategory_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getCategory();
                        bottomSheetDialog.dismiss();
                    }else if (cate==1){
                        stCategory = String.valueOf(cate);
//                        icCategory_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getCategory();
                        bottomSheetDialog.dismiss();
                    }else if (cate==2){
                        stCategory = String.valueOf(cate);
//                        icCategory_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getCategory();
                        bottomSheetDialog.dismiss();
                    }

                    if (stCategory!=null){
                        bottomSheetDialog.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(), "Please select item", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        rela_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_brand,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter_shopdetail.this);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                ImageView close = dialogView.findViewById(R.id.icon_close);
                RadioGroup group = dialogView.findViewById(R.id.radio_group);
                Button ok = dialogView.findViewById(R.id.btn_ok);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                group.setOnCheckedChangeListener((group1, checkedId) -> {
                    View radioButton = group.findViewById(checkedId);
                    indexB = group.indexOfChild(radioButton);
//                    btnBrand.setText(brandListItems[indexB]);
                    brand = brandIDlist[indexB];
                    if (language!=null) {
                        if (language.equals("km")) {
                            switch (brand) {
                                case 0:
                                    indexB=0;
                                    break;
                                case 1:
                                    indexB=1;
                                    break;
                                case 2:
                                    indexB=2;
                                    break;
                                case 3:
                                    indexB=3;
                                    break;
                                case 4:
                                    indexB=4;
                                    break;
                                case 5:
                                    indexB=5;
                                    break;
                            }
                        } else if (language.equals("en")) {
                            switch (brand) {
                                case 0:
                                    indexB=0;
                                    break;
                                case 1:
                                    indexB=1;
                                    break;
                                case 2:
                                    indexB=2;
                                    break;
                                case 3:
                                    indexB=3;
                                    break;
                                case 4:
                                    indexB=4;
                                    break;
                                case 5:
                                    indexB=5;
                                    break;
                            }
                        }
                    }
                });

                ok.setOnClickListener(v12 -> {
                    if (brand==0){
                        stBrand = String.valueOf(brand);
                        getBrand();
//                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }else if (brand==1){
                        stBrand = String.valueOf(brand);
                        getBrand();
//                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }else if (brand==2){
                        stBrand = String.valueOf(brand);
                        getBrand();
//                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }else if (brand==3){
                        stBrand = String.valueOf(brand);
                        getBrand();
//                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }else if (brand==4){
                        stBrand = String.valueOf(brand);
                        getBrand();
//                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }else if (brand==5){
                        stBrand = String.valueOf(brand);
                        getBrand();
//                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

        rela_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_year,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter_shopdetail.this);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                ImageView close = dialogView.findViewById(R.id.icon_close);
                RadioGroup group = dialogView.findViewById(R.id.radio_group);
                Button ok = dialogView.findViewById(R.id.btn_ok);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                group.setOnCheckedChangeListener((group1, checkedId) -> {
                    View radioButton = group.findViewById(checkedId);
                    indexY = group.indexOfChild(radioButton);
                    btnyear.setText(yearListItems[indexY]);
                    year_fil = yearIDlist[indexY];

                            if (year_fil == 0) {
                                stYear = "";
                            }
                            switch (year_fil) {
                                case 0:
                                    indexY=0;
                                    break;
                                case 1:
                                    indexY=1;
                                    break;
                                case 2:
                                    indexY=2;
                                    break;
                                case 3:
                                    indexY=3;
                                    break;
                                case 4:
                                    indexY=4;
                                    break;
                                case 5:
                                    indexY=5;
                                    break;
                                case 6:
                                    indexY=6;
                                    break;
                                case 7:
                                    indexY=7;
                                    break;
                                case 8:
                                    indexY=8;
                                    break;
                                case 9:
                                    indexY=9;
                                    break;
                                case 10:
                                    indexY=10;
                                    break;
                                case 11:
                                    indexY=11;
                                    break;
                                case 12:
                                    indexY=12;
                                    break;
                                case 13:
                                    indexY=13;
                                    break;
                                case 14:
                                    indexY=14;
                                    break;
                                case 15:
                                    indexY=15;
                                    break;
                                case 16:
                                    indexY=16;
                                    break;
                                case 17:
                                    indexY=17;
                                    break;
                                case 18:
                                    indexY=18;
                                    break;
                                case 19:
                                    indexY=19;
                                    break;
                                case 20:
                                    indexY=20;
                                    break;
                                case 21:
                                    indexY=21;
                                    break;
                                case 22:
                                    indexY=22;
                                    break;
                                case 23:
                                    indexY=23;
                                    break;
                                case 24:
                                    indexY=24;
                                    break;
                            }
                });

                ok.setOnClickListener(v12 -> {
                    if (indexY==0){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==1){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==2){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==3){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==4){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==5){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==6){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==7){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    } else if (indexY==8) {
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==9){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==10){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==11){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==12){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==13){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==14){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==15){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==16){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==17){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==18){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==19){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==20){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==21){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }else if (indexY==22){
                        stYear = String.valueOf(year_fil);
//                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        getYear();
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

// add filter condition by samang 27/08
        typeListItems = getResources().getStringArray(R.array.filter_condition);
        rela_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_type,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter_shopdetail.this);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                ImageView close = dialogView.findViewById(R.id.icon_close);
                RadioGroup group = dialogView.findViewById(R.id.radio_group);
                Button ok = dialogView.findViewById(R.id.btn_ok);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                group.setOnCheckedChangeListener((group1, checkedId) -> {
                    View radioButton = group.findViewById(checkedId);
                    index = group.indexOfChild(radioButton);
                    btnType.setText(typeListItems[index]);
                    type = index;
                    if (language!=null) {
                        if (language.equals("km")) {
                            switch (type) {
                                case 0:
                                    index=0;
                                    break;
                                case 1:
                                    index=1;
                                    break;
                                case 2:
                                    index=2;
                                    break;
                            }
                        } else if (language.equals("en")) {
                            switch (type) {
                                case 0:
                                    index=0;
                                    break;
                                case 1:
                                    index=1;
                                    break;
                                case 2:
                                    index=2;
                                    break;
                            }
                        }
                    }
                });

                ok.setOnClickListener(v12 -> {
                    if (type==0){
                        stType = "";
//                        icType_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }else if (type==1){
                        stType = "sell";
//                        icType_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }else if (type==2){
                        stType = "rent";
//                        icType_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

//        tv_done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Result();
//
//            }
//        });
        submit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Result();

            }
        });
    } // create

    private void Result(){
        Intent intent = new Intent();
        intent.putExtra("title_search",stTitle);
        intent.putExtra("category",stCategory);
        intent.putExtra("brand",stBrand);
        intent.putExtra("year",stYear);
        intent.putExtra("min_price",mMinPrice);
        intent.putExtra("max_price",mMaxPrice);
        setResult(2,intent);
        finish();

        Log.d("FIlter",stTitle+","+stCategory+","+stBrand+","+stYear);
    }

    private void getPosttype(String postType){
        String url = ConsumeAPI.BASE_URL+"relatedpost/?post_type=" + postType;
        String response = "";
        try {
            response = CommonFunction.doGetRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "response " + response);
        APIResponse apiResponse = new APIResponse();
        Gson gson = new Gson();
        apiResponse = gson.fromJson(response, APIResponse.class);
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
                    cateListItems = new String[jsonArray.length()+1];
                    categoryItemkg=new String[jsonArray.length()+1];
                    cateIDlist = new int[jsonArray.length()+1];
                    cateListItems[0] = "All";
                    categoryItemkg[0]=getString(R.string.all);

                    for (int i=1;i<=jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i-1);
                        int id = object.getInt("id");
                        String name = object.getString("cat_name");
                        String category=object.getString("cat_name_kh");
                        categoryItemkg[i]=category;
                        cateListItems[i] = name;
                        cateIDlist[i] = id;

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPrice(){
        String url;
        Request request;
        url = ConsumeAPI.BASE_URL + "relatedpost/?post_type=&category=2&modeling=&min_price=&max_price=&year=";
        request = new  Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Request",mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");


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
                        brandListItems = new String[jsonArray.length()+1];
                        brandIDlist = new int[jsonArray.length()+1];
                        brandItemkh=new String[jsonArray.length()+1];
                        brandListItems[0] = "ALL";
                        brandItemkh[0]=getString(R.string.all);
                        for (int i=1;i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i-1);
                            int id = object.getInt("id");
                            String brand = object.getString("brand_name");
                            String brandkh=object.getString("brand_name_as_kh");
                            brandItemkh[i]=brandkh;
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

                        brandListItems = new String[count+1];
                        brandItemkh=new String[count+1];
                        brandIDlist = new int[count+1];
                        brandListItems[0] = "All";
                        brandItemkh[0]=getString(R.string.all);
                        int ccount = 0;
                        for (int i=1;i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i-1);
                            int category = object.getInt("category");
                            if (cate==category) {
                                int id = object.getInt("id");
                                String name = object.getString("brand_name");
                                String namekh=object.getString("brand_name_as_kh");
                                brandItemkh[ccount+1]=namekh;
                                brandListItems[ccount+1] = name;
                                brandIDlist[ccount+1] = id;
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

                    yearListItems = new String[jsonArray.length()+1];
                    yearIDlist = new int[jsonArray.length()+1];
                    yearlistItemkh=new String[jsonArray.length()+1];
                    yearlistItemkh[0]=getString(R.string.all);
                    yearListItems[0] = "All";
                    for (int i=1;i<=jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i-1);
                        int id = object.getInt("id");
                        String name = object.getString("year");
                        yearlistItemkh[i]=name;
                        yearListItems[i] = name;
                        yearIDlist[i] = id;

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void language(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration confi =new  Configuration();
        confi.locale = locale;
        getBaseContext().getResources().updateConfiguration(confi, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    private void locale() {
        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefer.getString("My_Lang","");
        Log.d("language",language);
        language(language);
    }
}
