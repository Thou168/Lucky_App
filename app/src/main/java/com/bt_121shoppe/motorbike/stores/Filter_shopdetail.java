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


public class Filter_shopdetail extends AppCompatActivity implements BottomChooseFilterBrand.ItemClickListener, BottomChooseYear.ItemClickListener {

    private static final String TAG= Filter_shopdetail.class.getSimpleName();
    private TextView btnCategory,btnBrand,btnyear ,btnType,submit_filter;
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
    private int cate=0,year_fil=0,type=0;
    private LinearLayout rela_cate,rela_brand,rela_year,rela_type;
    private RangeSeekBar rangeBar;
    private SharedPreferences prefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_shopdetail);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        locale();
        getCategory();
        tv_result = findViewById(R.id.tv_result);
        tv_result.setOnClickListener(v -> finish());
        rela_cate  = findViewById(R.id.linea_cate);
        rela_brand = findViewById(R.id.linea_brand);
        rela_year  = findViewById(R.id.linea_year);
        rela_type = findViewById(R.id.linea_type);
        btnCategory = findViewById(R.id.search_category);
        btnBrand = findViewById(R.id.search_brand);
        btnyear = findViewById(R.id.search_year);
        btnType = findViewById(R.id.search_type);
        submit_filter = findViewById(R.id.btnSubmit_filter);

        Intent getTitle = getIntent();
        stTitle = getTitle.getStringExtra("title");
        stType=getTitle.getStringExtra("post_type");
        mMinPrice=getTitle.getIntExtra("min_price",0);
        mMaxPrice=getTitle.getIntExtra("max_price",20000);

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");

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

        rela_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetYear(v);
            }
        });

// add filter condition by samang 27/08
        typeListItems = getResources().getStringArray(R.array.filter_type);
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
