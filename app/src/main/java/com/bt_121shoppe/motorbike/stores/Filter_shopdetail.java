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
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseFilterBrand;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseModel;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseYear;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.Startup.Filter;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.searches.SearchTypeActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.example.roman.thesimplerangebar.SimpleRangeBar;
import com.example.roman.thesimplerangebar.SimpleRangeBarOnChangeListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.innovattic.rangeseekbar.RangeSeekBar;

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


public class Filter_shopdetail extends AppCompatActivity implements BottomChooseFilterBrand.ItemClickListener, BottomChooseYear.ItemClickListener,BottomChooseModel.ItemClickListener  {

    private static final String TAG= Filter_shopdetail.class.getSimpleName();
    private TextView btnCategory,btnBrand,btnyear ,btnType,submit_filter,tv_model;
    private TextView minText;
    private TextView maxText;
    private ImageView tv_result;
    private String stTitle="",stCategory="",stBrand="",stYear="",stType,st="";
    private String name,pass,Encode;
    private String currentLanguage;
    private String [] cateListItems,typeListItems,categoryItemkg;
    private int [] cateIDlist;
    private int seleectedBrandId=0;
    private int mMinPrice = 0,mMaxPrice = 20000;
    private int pk=0;
    private int index=3;
    private int cate=0,year_fil=0,type=0,model_fil;
    private LinearLayout rela_cate,rela_brand,rela_year,rela_type,rela_model;
    private RangeSeekBar rangeBar;
    private SharedPreferences prefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_shopdetail);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        typeListItems = getResources().getStringArray(R.array.filter_type);
//        locale();
        tv_result = findViewById(R.id.tv_result);
        tv_result.setOnClickListener(v -> finish());
        rela_cate  = findViewById(R.id.linea_cate);
        rela_brand = findViewById(R.id.linea_brand);
        rela_year  = findViewById(R.id.linea_year);
        rela_type = findViewById(R.id.linea_type);
        rela_model = findViewById(R.id.linea_model);
        btnCategory = findViewById(R.id.search_category);
        btnBrand = findViewById(R.id.search_brand);
        btnyear = findViewById(R.id.search_year);
        btnType = findViewById(R.id.search_type);
        tv_model = findViewById(R.id.search_model);
        submit_filter = findViewById(R.id.btnSubmit_filter);

        Intent getTitle = getIntent();
        stTitle = getTitle.getStringExtra("title");
        stType=getTitle.getStringExtra("post_type");
        mMinPrice=getTitle.getIntExtra("min_price",0);
        mMaxPrice=getTitle.getIntExtra("max_price",20000);
        String c = getTitle.getStringExtra("category");
        String m = getTitle.getStringExtra("model");
        String b = getTitle.getStringExtra("brand");
        String y = getTitle.getStringExtra("year");
        Log.e("TAG",""+c+","+b+","+y);
        if (c != null && b != null && y != null && m != null) {
            cate = Integer.parseInt(c);
            seleectedBrandId = Integer.parseInt(b);
            year_fil = Integer.parseInt(y);
            model_fil = Integer.parseInt(m);
        }
        Log.e("Filter"," "+stType+stTitle+","+cate+","+seleectedBrandId+","+year_fil+","+mMinPrice+","+mMaxPrice+","+model_fil);

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

        getCategory(cate);
        getYear(year_fil);
        getBrand(seleectedBrandId);
        getModel(model_fil);
        getCategory();

        //seekbar
        rangeBar = findViewById(R.id.rangeBar);
        minText = findViewById(R.id.text_min);
        maxText = findViewById(R.id.text_max);

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
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter_shopdetail.this);
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
            }
        });
        rela_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomModel(view);
            }
        });

        rela_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetYear(v);
            }
        });

// add filter condition by samang 27/08
        rela_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_for_type,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Filter_shopdetail.this);
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
                    if (currentLanguage.equals("en")) {
                        btnType.setText(typeListItems[index]);
                    }else {
                        btnType.setText(typeListItems[index]);
                    }
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

        submit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Result();
            }
        });
    }


    private void Result(){
        Log.d("FIlter","Filter condition "+stType+stTitle+","+stCategory+","+stBrand+","+stYear+","+mMinPrice+","+mMaxPrice);
        Intent intent = new Intent();
        intent.putExtra("posttype",stType);
        intent.putExtra("title_search",stTitle);
        intent.putExtra("category",cate);
        intent.putExtra("brand",seleectedBrandId);
        intent.putExtra("model",model_fil);
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
                        categoryItemkg[0] = "ទាំងអស់";
                    }

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
    private void getCategory(int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/categories/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{
                    SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                    String language = preferences.getString("My_Lang", "");
                    JSONObject jsonObject = new JSONObject(respon);
                    String catName = jsonObject.getString("cat_name");
                    String catNamekh=jsonObject.getString("cat_name_kh");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (language.equals("km")){
                                btnCategory.setText(catNamekh);
                            }else if (language.equals("en")) {
                                btnCategory.setText(catName);
                            }
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getBrand(int id ){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/brands/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Failure Error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                            String language = preferences.getString("My_Lang", "");
                            JSONObject jsonObject = new JSONObject(respon);
                            String brandname=jsonObject.getString("brand_name");
                            String brandnamekh=jsonObject.getString("brand_name_as_kh");
                            if (language.equals("km")){
                                btnBrand.setText(brandnamekh);
                            }else if (language.equals("en")){
                                btnBrand.setText(brandname);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
    private void getModel(int id){

        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/models/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                            String language = preferences.getString("My_Lang", "");
                            JSONObject jsonObject = new JSONObject(respon);
                            int brandId=jsonObject.getInt("brand");
                            String name=jsonObject.getString("modeling_name");
                            String namekh=jsonObject.getString("modeling_name_kh");
                            if (language.equals("km")){
                                tv_model.setText(namekh);
                            }else if (language.equals("en")){
                                tv_model.setText(name);
                            }
                            Log.d(TAG,"Brand from model "+name);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getYear(int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/years/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(respon);
                    String yearname=jsonObject.getString("year");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnyear.setText(yearname);
                        }
                    });

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
    public void showBottomModel(View view) {
        BottomChooseModel addPhotoBottomDialogFragment = BottomChooseModel.newInstance(seleectedBrandId);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseModel.TAG);
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

    @Override
    public void onClickItemModel(String item) {
        tv_model.setText(item);
    }

    @Override
    public void AddModelID(int id) {
        model_fil = id;
    }
}
