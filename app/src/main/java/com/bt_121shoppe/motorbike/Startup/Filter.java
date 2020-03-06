package com.bt_121shoppe.motorbike.Startup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseBrand;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseFilterBrand;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseYear;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.example.roman.thesimplerangebar.SimpleRangeBar;
import com.example.roman.thesimplerangebar.SimpleRangeBarOnChangeListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.innovattic.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Filter extends AppCompatActivity implements BottomChooseFilterBrand.ItemClickListener,BottomChooseYear.ItemClickListener {

//   private  TextView btnCategory,btnBrand,btnyear ,btnCondition,submit_filter,tv_result,tv_done;
//   private  int cate=0,brand=0,model=0,year=0,type=0;
//   private  String stTitle="",stCategory="",stBrand="",stYear="",stCondition,st="";
//   private ImageView icCategory_fil,icBrand_fil,icYear_fil,icCondition_fil;
//   private  String [] cateListItems,brandListItems,yearListItems,conditionListItems,categoryItemkg,brandItemkh,yearlistItemkh;
//   private  int [] cateIDlist,brandIDlist,yearIDlist;
//   private LinearLayout rela_cate,rela_brand,rela_year,rela_condition;

    private TextView btnCategory,btnBrand,btnyear ,btnType,submit_filter;
    private ImageView tv_result,tv_done;
    private int cate=0,brand=0,model_fil=0,year_fil=0,type=0; //model & year
    private String stTitle="",stCategory="",stBrand="",stYear="",stType="",st="";
    double dbPrice = 0.0;
    private  String [] cateListItems,brandListItems,yearListItems,typeListItems,categoryItemkg,brandItemkh,yearlistItemkh;
    private  int [] cateIDlist,brandIDlist,yearIDlist;
    private LinearLayout rela_cate,rela_brand,rela_year,rela_type;
    private int seleectedBrandId=0;

//    private SimpleRangeBar simpleRangeBar;
    private RangeSeekBar rangeBar;
    private TextView minText;
    private TextView maxText;
    private int mMinPrice = 0,mMaxPrice = 20000;
    private String stPriceMin,stPriceMax;
    private Integer postId = 0;
    String currentLanguage;
    //for bottomsheetdialog

    private int index=3,indexB=6,indexY=23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");
        locale();
        typeListItems = getResources().getStringArray(R.array.filter_type);


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
        minText = findViewById(R.id.text_min);
        maxText = findViewById(R.id.text_max);
        rangeBar = findViewById(R.id.rangeBar);
        postId = getIntent().getIntExtra("ID",0);
        dbPrice = getIntent().getDoubleExtra("price",0);

        Intent getTitle = getIntent();
        stTitle = getTitle.getStringExtra("title");
        stType=getTitle.getStringExtra("post_type");
        mMinPrice=getTitle.getIntExtra("min_price",0);
        mMaxPrice=getTitle.getIntExtra("max_price",20000);

        if(stType!=null){
            if(stType.equals("sell"))
                btnType.setText(typeListItems[1]);
            else if(stType.equals("rent"))
                btnType.setText(typeListItems[2]);
            else
                btnType.setText(getString(R.string.all));
        }
        else
            btnType.setText(getString(R.string.all));


        //range seekbar
        minText.setText(String.valueOf(mMinPrice));
        maxText.setText(String.valueOf(mMaxPrice));
        rangeBar.setMinRange(mMinPrice);
        rangeBar.setMax(mMaxPrice);
        rangeBar.setMinThumbValue(mMinPrice);
        rangeBar.setMaxThumbValue(mMaxPrice);
        rangeBar.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {

            }

            @Override
            public void onValueChanged(int i, int i1) {
                mMinPrice=i;
                mMaxPrice=i1;
                minText.setText(String.valueOf(mMinPrice));
                maxText.setText(String.valueOf(mMaxPrice));
            }
        });
        rangeBar.getSeekBarChangeListener();


        rela_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_cate,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter.this);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
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
//                    btnCategory.setText(categoryItemkg[index]);
//                    cate = cateIDlist[index];
//                    if (language!=null) {
//                        if (language.equals("km")) {
//                            switch (cate) {
//                                case 0:
//                                    index=0;
//                                    break;
//                                case 1:
//                                    index=1;
//                                    break;
//                                case 2:
//                                    index=2;
//                                    break;
//                            }
//                        } else if (language.equals("en")) {
//                            switch (cate) {
//                                case 0:
//                                    index=0;
//                                    break;
//                                case 1:
//                                    index=1;
//                                    break;
//                                case 2:
//                                    index=2;
//                                    break;
//                            }
//                        }
//                    }
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
                    if (currentLanguage.equals("en")){
                        btnCategory.setText(cateListItems[index]);
                    }else {
                        btnCategory.setText(categoryItemkg[index]);
                    }
                    cate = cateIDlist[index];
                    Log.e("TAG","Select Category id "+cate);
                });
            }
        });

        rela_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomBrand(v);
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Filter.this);
//                mBuilder.setTitle(R.string.choose_brand);
//                if (language.equals("km")){
//                    mBuilder.setSingleChoiceItems(brandItemkh, -1, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int i) {
//                            btnBrand.setText(brandItemkh[i]);
//                            //icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                            brand = brandIDlist[i];
//                            if (brand == 0){
//                                stBrand = "";
//                            }else {
//                                stBrand = String.valueOf(brand);
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//                }else if (language.equals("en")){
//                    mBuilder.setSingleChoiceItems(brandListItems, -1, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int i) {
//                            btnBrand.setText(brandListItems[i]);
//                            //icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                            brand = brandIDlist[i];
//                            if (brand == 0){
//                                stBrand = "";
//                            }else {
//                                stBrand = String.valueOf(brand);
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//
//                }
//                AlertDialog mDialog = mBuilder.create();
//                mDialog.show();
            }
        });

//        rela_brand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_brand,null);
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter.this);
//                bottomSheetDialog.setCanceledOnTouchOutside(false);
//                bottomSheetDialog.setContentView(dialogView);
//                bottomSheetDialog.show();
//                ImageView close = dialogView.findViewById(R.id.icon_close);
//                RadioGroup group = dialogView.findViewById(R.id.radio_group);
//                Button ok = dialogView.findViewById(R.id.btn_ok);
//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bottomSheetDialog.dismiss();
//                    }
//                });
//
//                group.setOnCheckedChangeListener((group1, checkedId) -> {
//                    View radioButton = group.findViewById(checkedId);
//                    indexB = group.indexOfChild(radioButton);
////                    if (language!=null) {
////                        if (language.equals("km")) {
////                            switch (brand) {
////                                case 0:
////                                    indexB=0;
////                                    break;
////                                case 1:
////                                    indexB=1;
////                                    break;
////                                case 2:
////                                    indexB=2;
////                                    break;
////                                case 3:
////                                    indexB=3;
////                                    break;
////                                case 4:
////                                    indexB=4;
////                                    break;
////                                case 5:
////                                    indexB=5;
////                                    break;
////                            }
////                        } else if (language.equals("en")) {
////                            switch (brand) {
////                                case 0:
////                                    indexB=0;
////                                    break;
////                                case 1:
////                                    indexB=1;
////                                    break;
////                                case 2:
////                                    indexB=2;
////                                    break;
////                                case 3:
////                                    indexB=3;
////                                    break;
////                                case 4:
////                                    indexB=4;
////                                    break;
////                                case 5:
////                                    indexB=5;
////                                    break;
////                            }
////                        }
////                    }
//                });
//
//                ok.setOnClickListener(v12 -> {
//                    if (brand==0){
//                            stBrand = String.valueOf(brand);
//                        getBrand();
////                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                        bottomSheetDialog.dismiss();
//                    }else if (brand==1){
//                        stBrand = String.valueOf(brand);
//                        getBrand();
////                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                        bottomSheetDialog.dismiss();
//                    }else if (brand==2){
//                        stBrand = String.valueOf(brand);
//                        getBrand();
////                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                        bottomSheetDialog.dismiss();
//                    }else if (brand==3){
//                        stBrand = String.valueOf(brand);
//                        getBrand();
////                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                        bottomSheetDialog.dismiss();
//                    }else if (brand==4){
//                        stBrand = String.valueOf(brand);
//                        getBrand();
////                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                        bottomSheetDialog.dismiss();
//                    }else if (brand==5){
//                        stBrand = String.valueOf(brand);
//                        getBrand();
////                        icBrand_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                        bottomSheetDialog.dismiss();
//                    }
//                    if (currentLanguage.equals("en")) {
//                        btnBrand.setText(brandListItems[indexB]);
//                    }else {
//                        btnBrand.setText(brandItemkh[indexB]);
//                    }
//                    brand = brandIDlist[indexB];
//                });
//            }
//        });

//        rela_year.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Filter.this);
//                mBuilder.setTitle(R.string.choose_condition);
//                if (language.equals("km")){
//                    mBuilder.setSingleChoiceItems(yearlistItemkh, -1, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int i) {
//                            btnyear.setText(yearlistItemkh[i]);
//                            icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                            year_fil = yearIDlist[i];
//                            if (year_fil==0){
//                                stYear = "";
//                            }else {
//                                stYear = String.valueOf(year_fil);
//                            }
//
//                            Log.d("ID Year","is"+stYear);
//
//                            dialog.dismiss();
//                        }
//                    });
//                }else if (language.equals("en")){
//                    mBuilder.setSingleChoiceItems(yearListItems, -1, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int i) {
//                            btnyear.setText(yearListItems[i]);
//                            icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                            year_fil = yearIDlist[i];
//                            if (year_fil==0){
//                                stYear = "";
//                            }else {
//                                stYear = String.valueOf(year_fil);
//                            }
//
//                            Log.d("ID Year","is"+stYear);
//
//                            dialog.dismiss();
//                        }
//                    });
//                }
//                AlertDialog mDialog = mBuilder.create();
//                mDialog.show();
//            }
//        });

        rela_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomSheetYear(v);

//                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_year,null);
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter.this);
//                bottomSheetDialog.setCanceledOnTouchOutside(false);
//                bottomSheetDialog.setContentView(dialogView);
//                bottomSheetDialog.show();
//                ImageView close = dialogView.findViewById(R.id.icon_close);
//                RadioGroup group = dialogView.findViewById(R.id.radio_group);
//                Button ok = dialogView.findViewById(R.id.btn_ok);
//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bottomSheetDialog.dismiss();
//                    }
//                });
//
//                group.setOnCheckedChangeListener((group1, checkedId) -> {
//                    View radioButton = group.findViewById(checkedId);
//                    indexY = group.indexOfChild(radioButton);
//                });
//
//                ok.setOnClickListener(v12 -> {
//                    if (indexY==0){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==1){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==2){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==3){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==4){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==5){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==6){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==7){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    } else if (indexY==8) {
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==9){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==10){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==11){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==12){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==13){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==14){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==15){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==16){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==17){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==18){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==19){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==20){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==21){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }else if (indexY==22){
//                        stYear = String.valueOf(year_fil);
////                        icYear_fil.setImageResource(R.drawable.ic_check_circle_black_24dp);
////                        getYear();
//                        bottomSheetDialog.dismiss();
//                    }
//                    if (currentLanguage.equals("en")) {
//                        btnyear.setText(yearListItems[indexY]);
//                    }else btnyear.setText(yearlistItemkh[indexY]);
//                    year_fil = yearIDlist[indexY];
//                    getYear();
//                });
            }
        });

        // add filter condition by samang 27/08

        rela_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_type,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter.this);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
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
                });

                ok.setOnClickListener(v12 -> {
                    type = index;
                    if (type==0){
                        stType = "";
                        bottomSheetDialog.dismiss();
                    }else if (type==1){
                        stType="sell";
                        bottomSheetDialog.dismiss();
                    }else if (type==2){
                        stType="rent";
                        bottomSheetDialog.dismiss();
                    }
                    if (currentLanguage.equals("en")) {
                        btnType.setText(typeListItems[index]);
                    }else {
                        btnType.setText(typeListItems[index]);
                    }
                });
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
        Log.d("FIlter","Filter condition "+stType+stTitle+","+stCategory+","+stBrand+","+stYear+","+mMinPrice+","+mMaxPrice);
        Intent intent = new Intent();
        intent.putExtra("posttype",stType);
        intent.putExtra("title_search",stTitle);
        intent.putExtra("category",cate);
        intent.putExtra("brand",seleectedBrandId);
        intent.putExtra("year",year_fil);
        intent.putExtra("min_price",mMinPrice);
        intent.putExtra("max_price",mMaxPrice);
        setResult(2,intent);
        finish();


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
                    if (currentLanguage.equals("en")) {
                        cateListItems[0] = "All";
                    }else {
                        cateListItems[0] = "ទាំងអស់";
                    }
                    categoryItemkg[0]= "ទាំងអស់";

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
                        if (currentLanguage.equals("en")) {
                            brandListItems[0] = "All";
                        }else {
//                            brandListItems[0] = "ទាំងអស់";
                            brandItemkh[0]= "ទាំងអស់";
                        }

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
                        if (currentLanguage.equals("en")) {
                            brandListItems[0] = "All";
                        }else {
//                            brandListItems[0] = "ទាំងអស់";
                            brandItemkh[0]= "ទាំងអស់";
                        }

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

                    if (!currentLanguage.equals("en")) {
                        yearListItems[0] = "ទាំងអស់";
                    }else {
                        yearListItems[0] = "All";
                    }
                    for (int i=1;i<=jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i-1);
                        int id = object.getInt("id");
                        String name = object.getString("year");
                        yearlistItemkh[i] = name;
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

    private void showBottomBrand(View view){
        BottomChooseFilterBrand addBottomDialogFragement=BottomChooseFilterBrand.newInstance(cate);
        addBottomDialogFragement.show(getSupportFragmentManager(),BottomChooseFilterBrand.TAG);
    }
    private void showBottomSheetYear(View view){
        BottomChooseYear addBottomDialogFragment=BottomChooseYear.newInstance();
        addBottomDialogFragment.show(getSupportFragmentManager(),BottomChooseYear.TAG);
    }


    @Override
    public void onSetBrandName(String item) {
        btnBrand.setText(item);
    }

    @Override
    public void onSetSelectedBrandIndex(int id) {
        seleectedBrandId=id;
    }

    @Override
    public void onClickItem(String item) {
        btnyear.setText(item);
    }

    @Override
    public void AddIDyear(int id) {
        year_fil=id;
    }
}
