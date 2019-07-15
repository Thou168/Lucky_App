package com.bt_121shoppe.lucky_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Api.User;
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.utils.FileCompressor;
import com.bt_121shoppe.lucky_app.utils.ImageUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Camera extends AppCompatActivity {

    private static final String TAG = "Response";
    static final int REQUEST_TAKE_PHOTO_1=1;
    static final int REQUEST_TAKE_PHOTO_2=2;
    static final int REQUEST_TAKE_PHOTO_3=3;
    static final int REQUEST_TAKE_PHOTO_4=4;
    static final int REQUEST_GALLERY_PHOTO_1=5;
    static final int REQUEST_GALLERY_PHOTO_2=6;
    static final int REQUEST_GALLERY_PHOTO_3=7;
    static final int REQUEST_GALLERY_PHOTO_4=8;
    private int REQUEST_TAKE_PHOTO_NUM=0;
    private int REQUEST_CHOOSE_PHOTO_NUM=0;
    File mPhotoFile;
    FileCompressor mCompressor;
    private RelativeLayout relatve_discount;

    private EditText etTitle,etDescription,etPrice,etDiscount_amount,etName,etPhone1,etPhone2,etPhone3,etEmail;
    private ImageView icPostType,icCategory,icType_elec,icBrand,icModel,icYears,icCondition,icColor,icRent,icDiscount_type,
            icTitile,icVincode,icMachineconde,icDescription,icPrice,icDiscount_amount,icName,icEmail,icPhone1,icPhone2,icPhone3;
    private ImageButton addPhone2,addPhone1;
    private TextInputLayout tilPhone2,tilPhone3;
    private MaterialSpinner tvPostType,tvCategory, tvType_elec,tvBrand,tvModel,tvYear,tvCondition,tvColor,tvDiscount_type;
    private Button submit_post;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private String name,pass,Encode;
    private int pk;
    private ArrayAdapter<String> brands,models;
    private ArrayAdapter<Integer> ID_category,ID_brands,ID_type,ID_year,ID_model;
    private List<String> list_category = new ArrayList<>();
    private List<String> list_type = new ArrayList<>();
    private List<String> list_brand = new ArrayList<>();
    private List<String> list_model = new ArrayList<>();
    private List<String> list_year= new ArrayList<>();
    private List<Integer> list_id_category = new ArrayList<>();
    private List<Integer> list_id_type = new ArrayList<>();
    private List<Integer> list_id_brands = new ArrayList<>();
    private List<Integer> list_id_model = new ArrayList<>();
    private List<Integer> list_id_year = new ArrayList<>();
    String id_cate, id_brand,id_model,id_year,id_type;
    int cate,brand,model,year,type;
    SharedPreferences prefer,pre_id;
    private Bitmap bitmapImage1,bitmapImage2,bitmapImage3,bitmapImage4;
    String test;
    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        Log.d("Pk",""+pk);
        ButterKnife.bind(this);
        mCompressor = new FileCompressor(this);
   //     Log.d(TAG,"time"+Instant.now().toString());
        TextView back = (TextView)findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                        if (prefer.contains("token")||prefer.contains("id")) {
                            startActivity(new Intent(getApplicationContext(), Account.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(), UserAccount.class));
                        }
                        break;
                }
                return false;
            }
        });

        pre_id = getSharedPreferences("id",MODE_PRIVATE);
        Variable_Field();
        initialUserInformation(pk,Encode);
        TextChange();

        DropDown();
        Call_category(Encode);
        Call_Type(Encode);

//      Call_Brand(Encode);

//        Call_Model(Encode);
        Call_years(Encode);



        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_1;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_1;
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_2;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_2;
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_3;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_3;
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_4;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_4;
            }
        });


        submit_post = (Button) findViewById(R.id.btnSubmitPost);
        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostData(Encode);
            }
        });

    } // create

    private void initialUserInformation(int pk, String encode) {
        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        MediaType MEDIA_TYPE=MediaType.parse("application/json");
        Log.d(TAG,"tt"+url);
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
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                Gson gson = new Gson();
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            User converJsonJava = new User();
                            converJsonJava = gson.fromJson(respon,User.class);

                            etPhone1.setText(converJsonJava.getUsername());

                        }
                    });
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void PostData(String encode) {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "";
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        JSONObject sale = new JSONObject();
        try {

            String postType = tvPostType.getSelectedItem().toString().toLowerCase();


            post.put("title",etTitle.getText().toString().toLowerCase());
            post.put("category", cate );
            post.put("status", 1);
            post.put("condition",tvCondition.getSelectedItem().toString().toLowerCase() );

            if (postType.equals("buy")) {
                post.put("discount", "0");
                post.put("discount_type","amount");
            }else {
                post.put("discount_type", tvDiscount_type.getSelectedItem().toString().toLowerCase() );
                post.put("discount",etDiscount_amount.getText().toString());
            }
            //post.put("discount", 0);
            post.put("user",pk );
            if(bitmapImage1==null) {
                post.put("front_image_path", "");
                post.put("front_image_base64", "");
            }
            else {
                post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                post.put("front_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
            }
            if(bitmapImage2==null){
                post.put("right_image_path", "");
                post.put("right_image_base64", "");
            }else{
                post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage2)));
                post.put("right_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage2)));
            }
            if(bitmapImage3==null){
                post.put("left_image_path", "");
                post.put("left_image_base64", "");
            }else{
                post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage3)));
                post.put("left_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage3)));
            }
            if(bitmapImage4==null){
                post.put("back_image_path", "");
                post.put("back_image_base64", "");
            }else{
                post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage4)));
                post.put("back_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage4)));
            }
            //Instant.now().toString()
            post.put("created", "");
            post.put("created_by", pk);
            post.put("modified", Instant.now().toString());
            post.put("modified_by", null);
            post.put("approved_date", null);
            post.put("approved_by", null);
            post.put("rejected_date", null);
            post.put("rejected_by",null);
            post.put("rejected_comments", "");
            post.put("year", year); //year
            post.put("modeling", model);
            //post.put("modeling", 1);
            post.put("description", etDescription.getText().toString().toLowerCase());
            //post.put("cost", etPrice.getText().toString().toLowerCase());
            post.put("cost",etPrice.getText().toString());
            post.put("post_type",tvPostType.getSelectedItem().toString().toLowerCase() );

            //post.put("contact_phone", etPhone1.getText().toString().toLowerCase());

            post.put("vin_code", "");
            post.put("machine_code", "");
            post.put("type", type);
            post.put("contact_phone", etPhone1.getText().toString());
            post.put("contact_email", etEmail.getText().toString().toLowerCase() );
            post.put("contact_address", "");
            post.put("color", tvColor.getSelectedItem().toString().toLowerCase());


            switch (postType){
                case "sell":
                    url=ConsumeAPI.BASE_URL+"postsale/";
                    //Log.d("URL","URL"+url);
                    sale.put("sale_status", 3);
                    sale.put("record_status",1);
                    sale.put("sold_date", null);
                    //sale.put("price", etPrice.getText().toString().toLowerCase());
                    //sale.put("total_price", etPrice.getText().toString().toLowerCase());
                    sale.put("price", etPrice.getText().toString());
                    sale.put("total_price",etPrice.getText().toString());
                    post.put("sale_post",new JSONArray("["+sale+"]"));

                    break;
                case "rent":
                    url = ConsumeAPI.BASE_URL+"postrent/";
                    JSONObject rent=new JSONObject();
                    rent.put("rent_status",3);
                    rent.put("record_status",1);
                    rent.put("rent_type","month");
                    rent.put("price",etPrice.getText().toString().toLowerCase());
                    rent.put("total_price",etPrice.getText().toString().toLowerCase());
                    rent.put("rent_date",null);
                    rent.put("return_date",null);
                    rent.put("rent_count_number",0);
                    post.put("rent_post",new JSONArray("["+rent+"]"));
                    break;
                case "buy":
                    url = ConsumeAPI.BASE_URL+"api/v1/postbuys/";
                    JSONObject buy=new JSONObject();
                    buy.put("buy_status",3);
                    buy.put("record_status",1);
                    post.put("buy_post",new JSONArray("["+buy+"]"));
                    break;
            }
            Log.d(TAG,post.toString());
            RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
            String auth = "Basic " + encode;
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization",auth)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    Log.d("Failure:",mMessage );

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG,"TTTT"+response.body().string());
             //       String message = response.body().string();
              //      Log.d("Responseqqq", message);
                    startActivity(new Intent(getApplicationContext(),Account.class));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                       //     Log.d("Responseqqq", message);
                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
    } //postdata

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
                        list_id_category.add(id);
                        list_category.add(name);
                        ID_category = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_id_category);
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


                                id_cate = String.valueOf(ID_category.getItem(i));
                                Log.d("ID", id_cate);

                                cate = Integer.parseInt(id_cate);

                            if (cate==1){
                                icType_elec.setVisibility(View.VISIBLE);
                                tvType_elec.setVisibility(View.VISIBLE);
                            }else if (cate == 2){
                                icType_elec.setVisibility(View.GONE);
                                tvType_elec.setVisibility(View.GONE);
                                type = 3;
                                Log.d("Typd id", String.valueOf(type));
                            }
                            icCategory.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            tvBrand.setSelection(-1);
                            icBrand.setImageResource(R.drawable.icon_null);
                           Call_Brand(Encode,id_cate);

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
                        list_id_type.add(id);
                        list_type.add(name);
                        ID_type = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_id_type);
                        final ArrayAdapter<String> type = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_type);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvType_elec.setAdapter(type);
                            }
                        });
                    }
                    tvType_elec.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                            id_type = String.valueOf(ID_type.getItem(i));
                            icType_elec.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            type = Integer.parseInt(id_type);

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
    } // type

    private void Call_Brand(String encode, String id_cate){
        int t = Integer.parseInt(id_cate);
        Log.d("Category id:",""+ this.id_cate);

        list_id_brands = new ArrayList<>();
        list_brand=new ArrayList<>();
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
                        int cate = object.getInt("category");
                    if (cate==t){
                            int id = object.getInt("id");
                            String name = object.getString("brand_name");
                            list_id_brands.add(id);
                            list_brand.add(name);
                            ID_brands = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_id_brands);
                            brands = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list_brand);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvBrand.setAdapter(brands);
                                }
                            });
                        }

                    }

                    tvBrand.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                           // brands.clear();
                                id_brand = String.valueOf(ID_brands.getItem(i));
                                Log.d("brand id",id_brand);
                                brand = Integer.parseInt(id_brand);
                                icBrand.setImageResource(R.drawable.ic_check_circle_black_24dp);
                                tvModel.setSelection(-1);
                                icModel.setImageResource(R.drawable.icon_null);
                                Call_Model(encode, id_brand);
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
    }// brand

    private void Call_Model(String encode,String id_bran){
        list_id_model = new ArrayList<>();
        list_model = new ArrayList<>();
        int b = Integer.parseInt(id_bran);
        Log.d("brand id:",""+ b);
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
                        int Brand = object.getInt("brand");
                        if (Brand==b) {
                            String name = object.getString("modeling_name");
                            list_id_model.add(id);
                            list_model.add(name);
                            ID_model = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_id_model);
                            models = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list_model);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvModel.setAdapter(models);
                                }
                            });
                        }
                    }

                    tvModel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                           id_model = String.valueOf(ID_model.getItem(i));
                           model = Integer.parseInt(id_model);
                      //     models.clear();
                           icModel.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        }

                        @Override
                        public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

                        }
                    });

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
                    Log.d(TAG,respon);
                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("year");
                        list_id_year.add(id);
                        list_year.add(name);
                        ID_year = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_id_year);
                        final ArrayAdapter<String> years = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,list_year);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvYear.setAdapter(years);
                            }
                        });

                    }

                    tvYear.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                            id_year = String.valueOf(ID_year.getItem(i));
                            year = Integer.parseInt(id_year);
                            icYears.setImageResource(R.drawable.ic_check_circle_black_24dp);
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
    } // years

    private void DropDown() {
        final String[] posttype = getResources().getStringArray(R.array.posty_type);
        ArrayAdapter<String> post = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, posttype);
        tvPostType.setAdapter(post);

        tvPostType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String st = post.getItem(i);

                if (st.equals("Buy")){
                    relatve_discount.setVisibility(View.GONE);

                }else {
                    relatve_discount.setVisibility(View.VISIBLE);
                }

                icPostType.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        String[] conditions = getResources().getStringArray(R.array.condition);
        ArrayAdapter<String> condition = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,conditions);
        tvCondition.setAdapter(condition);
        tvCondition.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                icCondition.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        String[] colors = getResources().getStringArray(R.array.color);
        ArrayAdapter<String> color = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,colors);
        tvColor.setAdapter(color);
        tvColor.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                icColor.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });


        String[] discount_type = getResources().getStringArray(R.array.discount_type);
        ArrayAdapter<String> discountType = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,discount_type);
        tvDiscount_type.setAdapter(discountType);
        tvDiscount_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String d = discountType.getItem(i);
                if (d.equals("Amount")){
                    etDiscount_amount.setHint("Discount Amount");
                }else if (d.equals("Percentage")){
                    etDiscount_amount.setHint("Discount Percentage");
                }

                icDiscount_amount.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

    }

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }
    private void Variable_Field() {

        relatve_discount = (RelativeLayout)findViewById(R.id.relative_discount);
//textview ///////
        tvPostType = (MaterialSpinner)findViewById(R.id.tvPostType);
        tvCategory = (MaterialSpinner) findViewById(R.id.tvCategory);
        tvType_elec= (MaterialSpinner) findViewById(R.id.tvType_elec);
        tvBrand    = (MaterialSpinner) findViewById(R.id.tvBrand);
        tvModel    = (MaterialSpinner) findViewById(R.id.tvModel);
        tvYear     = (MaterialSpinner) findViewById(R.id.tvYears);
        tvCondition= (MaterialSpinner) findViewById(R.id.tvCondition);
        tvColor    = (MaterialSpinner) findViewById(R.id.tvColor);
        tvDiscount_type = (MaterialSpinner)findViewById(R.id.tvDisType);
// edit text ////
        etTitle           = (EditText)findViewById(R.id.etTitle );
        etDescription     = (EditText)findViewById(R.id.etDescription );
        etPrice           = (EditText)findViewById(R.id.etPrice );
        etDiscount_amount = (EditText)findViewById(R.id.etDisAmount );
        etName            = (EditText)findViewById(R.id.etName );
        etPhone1          = (EditText)findViewById(R.id.etphone1 );

        etEmail           = (EditText)findViewById(R.id.etEmail );

// til phone

// add phone button


//// icon  ////////
        icPostType   = (ImageView) findViewById(R.id.imgPostType);
        icCategory   = (ImageView) findViewById(R.id. imgCategory);
        icType_elec  = (ImageView) findViewById(R.id.imgType_elec );
        icBrand      = (ImageView) findViewById(R.id. imgBrand);
        icModel      = (ImageView) findViewById(R.id. imgModel);
        icYears      = (ImageView) findViewById(R.id. imgYear);
        icCondition  = (ImageView) findViewById(R.id. imgCondition);
        icColor      = (ImageView) findViewById(R.id. imgColor);
        icTitile     = (ImageView) findViewById(R.id. imgTitle);
        icDescription= (ImageView) findViewById(R.id. imgDescription);
        icPrice      = (ImageView) findViewById(R.id. imgPrice);
        icName       = (ImageView) findViewById(R.id. imgName);
        icEmail      = (ImageView) findViewById(R.id. imgEmail);
        icPhone1     = (ImageView) findViewById(R.id. imgPhone1);

        icDiscount_amount = (ImageView) findViewById(R.id. imgDisAmount);
        icDiscount_type   = (ImageView) findViewById(R.id.imgDisType );

        imageView1=(ImageView) findViewById(R.id.Picture1);
        imageView2=(ImageView) findViewById(R.id.Picture2);
        imageView3=(ImageView) findViewById(R.id.Picture3);
        imageView4=(ImageView) findViewById(R.id.Picture4);
    }

    private void TextChange(){
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icTitile.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icTitile.setImageResource(R.drawable.ic_error_black_24dp);
                } else icTitile.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Camera.this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                requestStoragePermission(true);
            } else if (items[item].equals("Choose from Library")) {
                requestStoragePermission(false);
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }


    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        switch (REQUEST_TAKE_PHOTO_NUM){
            case REQUEST_TAKE_PHOTO_1:
                Toast toast=Toast.makeText(getApplicationContext(),"ថតពីមុខ",Toast.LENGTH_SHORT);
                //toast.setMargin(50,50);
                toast.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(25);
                toast.show();
                break;
            case REQUEST_TAKE_PHOTO_2:
                Toast toast1=Toast.makeText(getApplicationContext(),"ថតផ្នែកខាងស្ដាំ",Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group1 = (ViewGroup) toast1.getView();
                TextView messageTextView1 = (TextView) group1.getChildAt(0);
                messageTextView1.setTextSize(25);
                toast1.show();
                break;
            case REQUEST_TAKE_PHOTO_3:
                Toast toast2=Toast.makeText(getApplicationContext(),"ថតផ្នែកខាងឆ្វេង",Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group2 = (ViewGroup) toast2.getView();
                TextView messageTextView2 = (TextView) group2.getChildAt(0);
                messageTextView2.setTextSize(25);
                toast2.show();
                break;
            case REQUEST_TAKE_PHOTO_4:
                Toast toast3= Toast.makeText(getApplicationContext(),"ថតពីក្រោយ",Toast.LENGTH_SHORT);
                toast3.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group3 = (ViewGroup) toast3.getView();
                TextView messageTextView3 = (TextView) group3.getChildAt(0);
                messageTextView3.setTextSize(25);
                toast3.show();
                break;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        this.getPackageName() + ".provider",
                        photoFile);
                //BuildConfig.APPLICATION_ID
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_NUM);
            }
        }
    }

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_CHOOSE_PHOTO_NUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO_1) {
                try {
                    //Uri filePath=data.getData();
                    //bitmapImage1=MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage1= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView1);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_2;

                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_2) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage2= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView2);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_3;
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_3) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage3= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView3);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_4;
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_4) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage4= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView4);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_1) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage1=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView1);

            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_2) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage2=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView2);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_3) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage3=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView3);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_4) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage4=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(imageView4);

            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        //Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}


