package com.example.lucky_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucky_app.Api.ConsumeAPI;
import com.example.lucky_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Camera extends AppCompatActivity {

    private static final String TAG = "Response";

    private EditText etTitle,etVinCode,etMachineCode,etDescription,etPrice,etDiscount_amount,etName,etPhone1,etPhone2,etPhone3,etEmail;
    private ImageView icPostType,icCategory,icType_elec,icBrand,icModel,icYears,icCondition,icColor,icRent,icDiscount_type,
            icTitile,icVincode,icMachineconde,icDescription,icPrice,icDiscount_amount,icName,icEmail,icPhone1,icPhone2,icPhone3;
    private MaterialSpinner tvPostType,tvCategory, tvType_elec,tvBrand,tvModel,tvYear,tvCondition,tvColor,tvRent,tvDiscount_type;
    private Button submit_post;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private String name,pass,Encode;
    private List<String> list_category = new ArrayList<>();
    private List<String> list_type = new ArrayList<>();
    private List<String> list_brand = new ArrayList<>();
    private List<String> list_model = new ArrayList<>();
    private List<String> list_year= new ArrayList<>();
    private List<Integer> id_category = new ArrayList<>();
    private List<Integer> id_type = new ArrayList<>();
    private List<Integer> id_brand = new ArrayList<>();
    private List<Integer> id_model = new ArrayList<>();
    private List<Integer> id_year = new ArrayList<>();
    int id_cate;
    SharedPreferences prefer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);

        BottomNavigationView bnavigation = findViewById(R.id.bnaviga);
        bnavigation.getMenu().getItem(2).setChecked(true);
        bnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                        break;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(),Notification.class));
                        break;
                    case R.id.camera:

                        break;
                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(),Message.class));
                        break;
                    case R.id.account :
                        startActivity(new Intent(getApplicationContext(),Account.class));
                        break;
                }
                return false;
            }
        });

        Variable_Field();
        DropDown();
        Call_category(Encode);
        Call_Type(Encode);
        Call_Brand(Encode);
        Call_Model(Encode);
        Call_years(Encode);




    } // create



    private void Call_category(String encode) {

        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/categories/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
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
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("cat_name");
                        id_category.add(id);
                        list_category.add(name);
                         ArrayAdapter<Integer> ID_category = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,id_category);
                        final ArrayAdapter<String> category = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_category);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvCategory.setAdapter(category);
                            }
                        });
                    }


                    tvCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {

                            if(tvCategory.getSelectedItemId()==1){
                                id_cate = 1;
                                Log.d("ID 1", String.valueOf(id_cate));
                            }else if (tvCategory.getSelectedItemId()==2){
                                id_cate = 2;
                                Log.d("ID 2", String.valueOf(id_cate));
                            }
                        }

                        @Override
                        public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }  // category

    private void Call_Type(String encode) {
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/types/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
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
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("type");
                        id_type.add(id);
                        list_type.add(name);
                        final ArrayAdapter<String> type = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_type);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvType_elec.setAdapter(type);
                            }
                        });

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    } // type

    private void Call_Brand(String encode){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/brands/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Failure Error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("category");
//                        int cate = Integer.parseInt(id_cate);
//                        if (id==cate) {
                            String name = object.getString("brand_name");
                            id_brand.add(id);
                            list_brand.add(name);
                            final ArrayAdapter<String> brands = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list_brand);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvBrand.setAdapter(brands);
                                }
                            });
                       // }else Toast.makeText(getApplicationContext(),"Something wrong",Toast.LENGTH_SHORT).show();
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }// brand

    private void Call_Model(String encode){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/models/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
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
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("modeling_name");
                        id_model.add(id);
                        list_model.add(name);
                        final ArrayAdapter<String> models = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_model);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvModel.setAdapter(models);
                            }
                        });

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("Exception",e.getMessage());
                }
            }
        });
    } // model

    private void Call_years(String encode){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/years/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
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
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("year");
                        id_year.add(id);
                        list_year.add(name);
                        final ArrayAdapter<String> years = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_year);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvYear.setAdapter(years);
                            }
                        });

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    } // years


    private void DropDown() {
        final String[] posttype = getResources().getStringArray(R.array.posty_type);
        ArrayAdapter<String> post = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, posttype);
        tvPostType.setAdapter(post);

        String[] conditions = getResources().getStringArray(R.array.condition);
        ArrayAdapter<String> condition = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,conditions);
        tvCondition.setAdapter(condition);

        String[] colors = getResources().getStringArray(R.array.color);
        ArrayAdapter<String> color = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,colors);
        tvColor.setAdapter(color);

        String[] rents = getResources().getStringArray(R.array.rent_type);
        ArrayAdapter<String> rent = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,rents);
        tvRent.setAdapter(rent);

        String[] discount_type = getResources().getStringArray(R.array.discount_type);
        ArrayAdapter<String> discountType = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,discount_type);
        tvDiscount_type.setAdapter(discountType);

    }


    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }
    private void Variable_Field() {
        //textview ///////
        tvPostType = (MaterialSpinner)findViewById(R.id.tvPostType);
        tvCategory = (MaterialSpinner) findViewById(R.id.tvCategory);
        tvType_elec= (MaterialSpinner) findViewById(R.id.tvType_elec);
        tvBrand    = (MaterialSpinner) findViewById(R.id.tvBrand);
        tvModel    = (MaterialSpinner) findViewById(R.id.tvModel);
        tvYear     = (MaterialSpinner) findViewById(R.id.tvYears);
        tvCondition= (MaterialSpinner) findViewById(R.id.tvCondition);
        tvColor    = (MaterialSpinner) findViewById(R.id.tvColor);
        tvRent     = (MaterialSpinner) findViewById(R.id.tvRent);
        tvDiscount_type = (MaterialSpinner)findViewById(R.id.tvDisType);
        // edit text ////
        etTitle           = (EditText)findViewById(R.id.etTitle );
        etVinCode         = (EditText)findViewById(R.id.etVinCode );
        etMachineCode     = (EditText)findViewById(R.id.etMachineCode );
        etDescription     = (EditText)findViewById(R.id.etDescription );
        etPrice           = (EditText)findViewById(R.id.etPrice );
        etDiscount_amount = (EditText)findViewById(R.id.etDisAmount );
        etName            = (EditText)findViewById(R.id.etName );
        etPhone1          = (EditText)findViewById(R.id.etphone1 );
        etPhone2          = (EditText)findViewById(R.id.etphone2 );
        etPhone3          = (EditText)findViewById(R.id.etphone3 );
        etEmail           = (EditText)findViewById(R.id.etEmail );
//// icon  ////////
        icPostType   = (ImageView) findViewById(R.id.imgPostType);
        icCategory   = (ImageView) findViewById(R.id. imgCategory);
        icType_elec  = (ImageView) findViewById(R.id.imgType_elec );
        icBrand      = (ImageView) findViewById(R.id. imgBrand);
        icModel      = (ImageView) findViewById(R.id. imgModel);
        icYears      = (ImageView) findViewById(R.id. imgYear);
        icCondition  = (ImageView) findViewById(R.id. imgCondition);
        icColor      = (ImageView) findViewById(R.id. imgColor);
        icRent       = (ImageView) findViewById(R.id. imgRent);
        icTitile     = (ImageView) findViewById(R.id. imgTitle);
        icVincode    = (ImageView) findViewById(R.id. imgVinCode);
        icMachineconde=(ImageView) findViewById(R.id. imgMachineCode);
        icDescription= (ImageView) findViewById(R.id. imgDescription);
        icPrice      = (ImageView) findViewById(R.id. imgPrice);
        icName       = (ImageView) findViewById(R.id. imgName);
        icEmail      = (ImageView) findViewById(R.id. imgEmail);
        icPhone1     = (ImageView) findViewById(R.id. imgPhone1);
        icPhone2     = (ImageView) findViewById(R.id. imgPhone2 );
        icPhone3     = (ImageView) findViewById(R.id. imgPhone3);
        icDiscount_amount = (ImageView) findViewById(R.id. imgDisAmount);
        icDiscount_type   = (ImageView) findViewById(R.id.imgDisType );

        imageView1=(ImageView) findViewById(R.id.Picture1);
        imageView2=(ImageView) findViewById(R.id.Picture2);
        imageView3=(ImageView) findViewById(R.id.Picture3);
        imageView4=(ImageView) findViewById(R.id.Picture4);
        imageView5=(ImageView) findViewById(R.id.Picture5);
    }
}
