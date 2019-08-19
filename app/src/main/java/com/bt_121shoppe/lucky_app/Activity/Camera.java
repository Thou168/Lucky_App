package com.bt_121shoppe.lucky_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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


import com.bt_121shoppe.lucky_app.firebases.FBPostCommonFunction;
import com.bt_121shoppe.lucky_app.models.CreatePostModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Api.User;
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount;
import com.bt_121shoppe.lucky_app.chats.ChatMainActivity;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.utils.FileCompressor;
import com.bt_121shoppe.lucky_app.utils.ImageUtil;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
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

public class Camera extends AppCompatActivity implements OnMapReadyCallback {


    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double latitude,longtitude;
    private GoogleMap mMap;
    private String latlng;
    SupportMapFragment mapFragment;
    private static final String TAG = Camera.class.getSimpleName();
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
            icTitile,icDescription,icPrice,icDiscount_amount,icName,icEmail,icPhone1,icAddress;
    private TextInputLayout input_title, input_price, input_des, input_dis, input_name, input_phone, input_email;
    private SearchView tvAddress;
    private Button submit_post;
    private EditText tvPostType,tvCondition,tvDiscount_type,tvColor,tvYear,tvCategory,tvType_elec,tvBrand,tvModel;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private String name,pass,Encode;
    private int pk;
    private ArrayAdapter<String> brands,models;
    private ArrayAdapter<Integer> ID_category,ID_brands,ID_type,ID_year,ID_model,id_brand_Model;
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
    private List<Integer> list_brand_model = new ArrayList<>();
    private  BottomNavigationView bnavigation;
    String id_cate, id_brand,id_model,id_year,id_type,strPostType,strCondition,strDiscountType,strColor;
    int idYear=0,process_type=0,post_type=0,category=0;
    int cate=0,brand=0,model=0,year=0,type=0;
    SharedPreferences prefer,pre_id;
    ProgressDialog mProgress;
    private Bitmap bitmapImage1,bitmapImage2,bitmapImage3,bitmapImage4;
    private  BitmapDrawable drawable1,drawable2,drawable3,drawable4;
    int edit_id,status;
    Bundle bundle;
    int mmodel=1;

    private String[] postTypeListItems,conditionListItems,modelListItemkh,discountTypeListItems,brandListItemkh,typeListItemkh,categoryListItemkh,colorListItems,yearListItems,categoryListItems,typeListItems,brandListItem,modelListItems;
    private int[] yearIdListItems,categoryIdListItems,typeIdListItems,brandIdListItems,modelIdListItems;
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

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");

        bnavigation = findViewById(R.id.bnaviga);
        bnavigation.getMenu().getItem(2).setChecked(true);
        bnavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.home:
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                    break;
                case R.id.notification:
                    if (prefer.contains("token")||prefer.contains("id")) {
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                    }else {
                        startActivity(new Intent(getApplicationContext(), UserAccount.class));
                    }
                    break;
                case R.id.camera:
                    if (prefer.contains("token")||prefer.contains("id")) {
                        startActivity(new Intent(getApplicationContext(), Camera.class));
                    }else {
                        startActivity(new Intent(getApplicationContext(), UserAccount.class));
                    }
                    break;
                case R.id.message:
                    if (prefer.contains("token")||prefer.contains("id")) {
                        startActivity(new Intent(getApplicationContext(), ChatMainActivity.class));
                    }else {
                        startActivity(new Intent(getApplicationContext(), UserAccount.class));
                    }
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
        });

        Toolbar toolbar=findViewById(R.id.toolbar);

        //Log.d("Edit_id:", String.valueOf(edit_id));
        pre_id = getSharedPreferences("id",MODE_PRIVATE);
        Variable_Field();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_post);
        DropDown();
        Call_category(Encode);
        Call_Type(Encode);
        Call_years(Encode);
        initialUserInformation(pk,Encode);

        TextChange();

        bundle = getIntent().getExtras();
        if (bundle!=null) {
            process_type=bundle.getInt("process_type",0);
            if(process_type==1){
                strPostType=bundle.getString("post_type");
                cate=bundle.getInt("category",0);

                if(strPostType.equals("buy")){
                    relatve_discount.setVisibility(View.GONE);
                    toolbar.setBackgroundColor(getColor(R.color.logo_orange));
                }else if(strPostType.equals("sell")){
                    toolbar.setBackgroundColor(getColor(R.color.logo_green));
                }else if(strPostType.equals("rent")){
                    toolbar.setBackgroundColor(getColor(R.color.logo_red));
                }

                tvPostType.setText(strPostType.substring(0,1).toUpperCase() + strPostType.substring(1));
                getCategegoryName(Encode,cate);
                Call_Brand(Encode,cate);
                if(cate==1){
                    icType_elec.setVisibility(View.VISIBLE);
                    tvType_elec.setVisibility(View.VISIBLE);
                }else{
                    type=3;
                    icType_elec.setVisibility(View.GONE);
                    tvType_elec.setVisibility(View.GONE);
                }
            }else {
                edit_id = bundle.getInt("id_product", 0);
                Log.d("Edit_id:", String.valueOf(edit_id));
            }
        }

        getData_Post(Encode,edit_id);
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_category));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(categoryListItemkh, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvCategory.setText(categoryListItemkh[i]);
                            cate = categoryIdListItems[i];
                            if (cate==1){
                                icType_elec.setVisibility(View.VISIBLE);
                                tvType_elec.setVisibility(View.VISIBLE);
                                Call_Type(Encode);
                            }else {
                                icType_elec.setVisibility(View.GONE);
                                tvType_elec.setVisibility(View.GONE);
                                type = 3;
                            }
                            icCategory.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            icBrand.setImageResource(R.drawable.icon_null);
                            Call_Brand(Encode,cate);
                            dialogInterface.dismiss();
                        }
                    });

                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(categoryListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvCategory.setText(categoryListItems[i]);
                            cate = categoryIdListItems[i];
                            if (cate==1){
                                icType_elec.setVisibility(View.VISIBLE);
                                tvType_elec.setVisibility(View.VISIBLE);
                                Call_Type(Encode);
                            }else {
                                icType_elec.setVisibility(View.GONE);
                                tvType_elec.setVisibility(View.GONE);
                                type = 3;
                            }
                            icCategory.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            icBrand.setImageResource(R.drawable.icon_null);
                            Call_Brand(Encode,cate);
                            dialogInterface.dismiss();
                        }
                    });
                }

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        tvType_elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_type));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(typeListItemkh, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvType_elec.setText(typeListItemkh[i]);
                            type = typeIdListItems[i];
                            icType_elec.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            dialogInterface.dismiss();
                        }
                    });
                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(typeListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvType_elec.setText(typeListItems[i]);
                            type = typeIdListItems[i];
                            icType_elec.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            dialogInterface.dismiss();
                        }
                    });
                }

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_brand));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(brandListItemkh, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvBrand.setText(brandListItemkh[i]);
                            brand = brandIdListItems[i];
                            icBrand.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            Call_Model(Encode, brand,null);
                            dialogInterface.dismiss();
                        }
                    });
                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(brandListItem, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvBrand.setText(brandListItem[i]);
                            brand = brandIdListItems[i];
                            icBrand.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            Call_Model(Encode, brand,null);
                            dialogInterface.dismiss();
                        }
                    });
                }

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        tvModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_model));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(modelListItemkh, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvModel.setText(modelListItemkh[i]);
                            model = modelIdListItems[i];
                            icModel.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            dialogInterface.dismiss();
                        }
                    });
                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(modelListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvModel.setText(modelListItems[i]);
                            model = modelIdListItems[i];
                            icModel.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            dialogInterface.dismiss();
                        }
                    });
                }

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_year));
                mBuilder.setSingleChoiceItems(yearListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvYear.setText(yearListItems[i]);
                        year = yearIdListItems[i];
                        icYears.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        //Toast.makeText(Camera.this,year,Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

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

        tvAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Seach_Address();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    icAddress.setImageResource(R.drawable.icon_null);
                }else {
                    icAddress.setImageResource(R.drawable.ic_check_circle_black_24dp);
                }
                return false;
            }
        });
        submit_post = (Button) findViewById(R.id.btnSubmitPost);
        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String stDis_amount,stDis_percent,stPrice;
                double dbDis_amount = 0 , dbDis_percent = 0, dbPrice ;
                    stPrice = etPrice.getText().toString();
                    if (stPrice == null || stPrice.isEmpty()){
                        dbPrice = 1;
                    }else {
                        dbPrice = Double.parseDouble(stPrice);
                    }

                if (strDiscountType == "amount"){
                    stDis_amount = etDiscount_amount.getText().toString();
                    dbDis_amount = Double.parseDouble(stDis_amount);
                }else {
                    dbDis_amount = 0;
                }
                 if(strDiscountType == "percent"){
                    stDis_percent = etDiscount_amount.getText().toString();
                    dbDis_percent = Double.parseDouble(stDis_percent);
                }else {
                     dbDis_percent = 0;
                 }

               if (etTitle.getText().toString().length()<3||tvPostType.getText().toString().length()==0||tvCategory.getText().toString().length()==0||
                   type==0 || tvBrand.getText().toString().length()==0 || tvModel.getText().toString().length()==0 || tvYear.getText().toString().length()==0
                   || etPrice.getText().toString().length()==0 || dbDis_percent >=100|| dbDis_amount >= dbPrice  || bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null
               ){

                   if (etTitle.getText().toString().length()<3) {
                       etTitle.requestFocus();
                       icTitile.setImageResource(R.drawable.ic_error_black_24dp);
                   }
                   //|| etPrice.getText().toString().length()==0 || dbDis_percent >=100|| dbDis_amount >= dbPrice  || bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null

                    if (dbDis_percent >= 100 || dbDis_amount >= dbPrice){
                        etDiscount_amount.requestFocus();
                        icDiscount_amount.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (etPrice.getText().toString().length()==0) {
                        etPrice.requestFocus();
                        icPrice.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (tvYear.getText().toString().length()==0) {
                        tvYear.setFocusable(true);
                        tvYear.setFocusableInTouchMode(true);
                        tvYear.requestFocus();
                        icYears.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (tvModel.getText().toString().length()==0) {
                        tvModel.setFocusable(true);
                        tvModel.setFocusableInTouchMode(true);
                        tvModel.requestFocus();
                        icModel.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (tvBrand.getText().toString().length()==0) {
                        tvBrand.setFocusable(true);
                        tvBrand.setFocusableInTouchMode(true);
                        tvBrand.requestFocus();
                        icBrand.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (tvType_elec.getText().toString().length()==0) {
                        tvType_elec.setFocusable(true);
                        tvType_elec.setFocusableInTouchMode(true);
                        tvType_elec.requestFocus();
                        icType_elec.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (tvCategory.getText().toString().length()==0) {
                        tvCategory.setFocusable(true);
                        tvCategory.setFocusableInTouchMode(true);
                        tvCategory.requestFocus();
                        icCategory.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (tvPostType.getText().toString().length()==0){
                        tvPostType.setFocusable(true);
                        tvPostType.setFocusableInTouchMode(true);
                        tvPostType.requestFocus();
                        icPostType.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    if (etTitle.getText().toString().length()<3) {
                        etTitle.requestFocus();
                        icTitile.setImageResource(R.drawable.ic_error_black_24dp);
                    }

                   if (dbDis_percent >= 100){
                       etDiscount_amount.requestFocus();
                       icDiscount_amount.setImageResource(R.drawable.ic_error_black_24dp);
                   }
                   if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                    AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                    alertDialog.setMessage(Camera.this.getString(R.string.missing_image));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

               }
                else if (bundle!=null) {
                    mProgress.show();
                    if(process_type==1)
                        PostData(Encode);
                    else
                        EditPost_Approve(Encode, edit_id);
                } else  {
                    mProgress.show();
                    PostData(Encode);
                }
            }
        });

    } // create

    private void getData_Post(String encode,int id){
        if (bundle!=null) {
            final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL, "postbyuser/", id);
            Log.d("Url", url);
            OkHttpClient client = new OkHttpClient();
            String auth = "Basic " + encode;
            Request request = new Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Log.d("Edit Response:", respon);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(respon);
                                String title = object.getString("title");
                                String description = object.getString("description");
                                int price = object.getInt("cost");
                                String search_title = object.getString("vin_code");
                                String name = object.getString("machine_code");
                                String discount = object.getString("discount");
                                String phone = object.getString("contact_phone");
                                String email = object.getString("contact_email");
                                strPostType = object.getString("post_type");
                                cate = object.getInt("category");
                                type = object.getInt("type");

                                if (cate == 2) {
                                    type = 3;
                                    icType_elec.setVisibility(View.GONE);
                                    tvType_elec.setVisibility(View.GONE);
                                } else {
                                    icType_elec.setVisibility(View.VISIBLE);
                                    tvType_elec.setVisibility(View.VISIBLE);
                                }

                                model = object.getInt("modeling");
                                mmodel = object.getInt("modeling");
                                year = object.getInt("year");

                                strCondition = object.getString("condition");
                                strColor = object.getString("color");
                                strDiscountType = object.getString("discount_type");


                                String addr = object.getString("contact_address");
                                if(!addr.isEmpty()) {

                                    String[] splitAddr = addr.split(",");
                                    latitude = Double.valueOf(splitAddr[0]);
                                    longtitude = Double.valueOf(splitAddr[1]);
                                    getLocation_edit(latitude, longtitude);
                                    tvAddress.setQuery(search_title,false);
                                    mapFragment.getMapAsync(Camera.this::onMapReady);
                                }


                                String fron = object.getString("front_image_path");
                                String back = object.getString("back_image_path");
                                String left = object.getString("left_image_path");
                                String right = object.getString("right_image_path");
                                List<String> list = new ArrayList<>();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        etTitle.setText(title);
                                        etDescription.setText(description);
                                        etPrice.setText(String.valueOf(price));
                                        etDiscount_amount.setText(discount);
                                        etName.setText(name);
                                        etPhone1.setText(phone);
                                        etEmail.setText(email);


                                        String postType = strPostType.substring(0,1).toUpperCase() + strPostType.substring(1);
                                        String condition= strCondition.substring(0,1).toUpperCase() + strCondition.substring(1);
                                        String discountType= strDiscountType.substring(0,1).toUpperCase() + strDiscountType.substring(1);
                                        String color= strColor.substring(0,1).toUpperCase() + strColor.substring(1);
                                        if (postType.equals("Sell")){
                                            tvPostType.setText(R.string.sel);
                                        }else if (postType.equals("Rent")){
                                            tvPostType.setText(R.string.ren);
                                        }else if (postType.equals("Buy")){
                                            tvPostType.setText(R.string.bu);
                                        }

                                        if (condition.equals("New")){
                                            tvCondition.setText(R.string.newl);
                                        }else if (condition.equals("Used")){
                                            tvCondition.setText(R.string.used);
                                        }

                                        if (discountType.equals("Amount")){
                                            tvDiscount_type.setText(R.string.amount);
                                        }else if (discountType.equals("Percent")){
                                            tvDiscount_type.setText(R.string.percent);
                                        }

                                        if (color.equals("Blue")){
                                            tvColor.setText(R.string.blue);
                                        }else if (color.equals("Black")){
                                            tvColor.setText(R.string.black);
                                        }else if (color.equals("Silver")){
                                            tvColor.setText(R.string.silver);
                                        }else if (color.equals("Red")){
                                            tvColor.setText(R.string.red);
                                        }else if (color.equals("Gray")){
                                            tvColor.setText(R.string.gray);
                                        }else if (color.equals("Yellow")){
                                            tvColor.setText(R.string.yellow);
                                        }else if (color.equals("Pink")){
                                            tvColor.setText(R.string.pink);
                                        }else if (color.equals("Purple")){
                                            tvColor.setText(R.string.purple);
                                        }else if (color.equals("Orange")){
                                            tvColor.setText(R.string.orange);
                                        }else if (color.equals("Green")){
                                            tvColor.setText(R.string.green);
                                        }

                                        Call_Brand(Encode,cate);
                                        //Call_Model(Encode,brand);
                                        getCategegoryName(Encode,cate);
                                        getTypeName(Encode,type);
                                        getYearName(Encode,year);
                                        getModelName(Encode,model);
                                        //Log.d(TAG,"Brand_id"+ brand);

                                        Glide.with(Camera.this).asBitmap().load(fron).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView1.setImageBitmap(resource);
                                                bitmapImage1 = resource;
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(back).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView2.setImageBitmap(resource);
                                                bitmapImage2 = resource;
                                                Log.d("BITMAP","2:"+bitmapImage2);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(left).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView3.setImageBitmap(resource);
                                                bitmapImage3 = resource;
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(right).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView4.setImageBitmap(resource);
                                                bitmapImage4 = resource;
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });



//                                        bitmapImage1 = ((BitmapDrawable) imageView1.getDrawable()).getBitmap();
                                        Log.d("sfasdfasf","1:"+bitmapImage1+"2:"+bitmapImage2+"3:"+bitmapImage3+"4:"+bitmapImage4);


//                                        byte[] decodedString1 = Base64.decode(fron, Base64.DEFAULT);
//                                        bitmapImage1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
//                                        imageView1.setImageBitmap(bitmapImage1);
//                                        byte[] decodedString2 = Base64.decode(back, Base64.DEFAULT);
//                                        bitmapImage2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
//                                        imageView2.setImageBitmap(bitmapImage2);
//                                        byte[] decodedString3 = Base64.decode(left, Base64.DEFAULT);
//                                        bitmapImage3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
//                                        imageView3.setImageBitmap(bitmapImage3);
//                                        byte[] decodedString4 = Base64.decode(right, Base64.DEFAULT);
//                                        bitmapImage4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
//                                        imageView4.setImageBitmap(bitmapImage4);

                                    }
                                });
                                model=object.getInt("modeling");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } //
                        }
                    });
                    //Log.d("Edit", respon);
                }
            });
            model=mmodel;
        }  //getextra
    }

    private void initialUserInformation(int pk, String encode) {
        if (bundle==null) {
            final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL, "api/v1/users/", pk);
            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            //Log.d(TAG,"tt"+url);
            OkHttpClient client = new OkHttpClient();

            String auth = "Basic " + encode;
            Request request = new Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Gson gson = new Gson();
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                User converJsonJava = new User();
                                converJsonJava = gson.fromJson(respon, User.class);

                            etName.setText(converJsonJava.getFirst_name());
                            etPhone1.setText(converJsonJava.getUsername());
                            String addr = converJsonJava.getProfile().getAddress();
                            if(addr.isEmpty()){
                                    get_location(true);
                                    mapFragment.getMapAsync(Camera.this::onMapReady);
                            }else {
                                String[] splitAddr = addr.split(",");
                                latitude = Double.valueOf(splitAddr[0]);
                                longtitude = Double.valueOf(splitAddr[1]);
                                get_location(false);

                                    if (converJsonJava.getProfile().getResponsible_officer()!=null){
                                        String search_title = converJsonJava.getProfile().getResponsible_officer();
                                        tvAddress.setQuery(search_title,false);
                                    }
                                    mapFragment.getMapAsync(Camera.this::onMapReady);

                                }
                            }
                        });
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });

        } // end if
    }

    private void PostData(String encode) {

        if(strDiscountType==null || strDiscountType.isEmpty())
            strDiscountType="amount";
        if(strCondition==null || strCondition.isEmpty())
            strCondition="new";
        if(strColor==null || strColor.isEmpty())
            strColor="black";
        String str_dis=etDiscount_amount.getText().toString();
        if(str_dis==null || str_dis.isEmpty())
            str_dis="0";
        /*
        final ProgressDialog mProgress=new ProgressDialog(Camera.this);
        mProgress.setMessage("Sumitting");
        mProgress.show();
        */
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "";
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        JSONObject sale = new JSONObject();
        try {
            //String postType = tvPostType.getSelectedItem().toString().toLowerCase();
            post.put("title",etTitle.getText().toString().toLowerCase());
            post.put("category", cate);
            post.put("status", 1);
            post.put("condition",strCondition);

            if (strPostType.equals("buy")) {
                post.put("discount", "0");
                post.put("discount_type","amount");
            }else {
                post.put("discount_type", strDiscountType);
                post.put("discount",str_dis);
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
   //         post.put("modified", Instant.now().toString());
            post.put("modified_by", null);
            post.put("approved_date", null);
            post.put("approved_by", null);
            post.put("rejected_date", null);
            post.put("rejected_by",null);
            post.put("rejected_comments", "");
            post.put("year", year); //year
            post.put("modeling", model);
            post.put("description", etDescription.getText().toString().toLowerCase());
            post.put("cost",etPrice.getText().toString());
            post.put("post_type",strPostType);
            post.put("vin_code", tvAddress.getQuery().toString());
            post.put("machine_code", etName.getText().toString().toLowerCase());
            post.put("type", type);
            post.put("contact_phone", etPhone1.getText().toString());
            post.put("contact_email", etEmail.getText().toString().toLowerCase() );
            post.put("contact_address", latlng);
            post.put("color", strColor);

            switch (strPostType){
                case "លក់":
                case "sell":
                    url=ConsumeAPI.BASE_URL+"postsale/";
                    sale.put("sale_status", 3);
                    sale.put("record_status",1);
                    sale.put("sold_date", null);
                    sale.put("price", etPrice.getText().toString());
                    sale.put("total_price",etPrice.getText().toString());
                    post.put("sale_post",new JSONArray("["+sale+"]"));
                    break;

                case "ជួល":
                case "rent":
                    url = ConsumeAPI.BASE_URL+"postrent/";
                    JSONObject rent=new JSONObject();
                    rent.put("rent_status",3);
                    rent.put("record_status",1);
                    rent.put("rent_type","month");
                    rent.put("price",etPrice.getText().toString());
                    rent.put("total_price",etPrice.getText().toString());
                    rent.put("rent_date",null);
                    rent.put("return_date",null);
                    rent.put("rent_count_number",0);
                    post.put("rent_post",new JSONArray("["+rent+"]"));
                    break;

                case "ទិញ":
                case "buy":
                    url = ConsumeAPI.BASE_URL+"api/v1/postbuys/";
                    JSONObject buy=new JSONObject();
                    buy.put("buy_status",3);
                    buy.put("record_status",1);
                    post.put("buy_post",new JSONArray("["+buy+"]"));
                    break;
            }
            Log.d(TAG,post.toString());
            Log.d("URLLLLLLL",url);

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
                public void onResponse(Call call, Response response) throws IOException {
                   String respon = response.body().string();
                    //Log.d(TAG, "Post TTTT" + respon);
                    Gson gson = new Gson();
                    CreatePostModel createPostModel = new CreatePostModel();
                    try{
                        createPostModel = gson.fromJson(respon,CreatePostModel.class);
                        if (createPostModel!=null){
                            int id = createPostModel.getId();
                            if (id!=0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject obj = new JSONObject(respon);
                                            int pID=obj.getInt("id");
                                            String pTitle=obj.getString("title");
                                            String pType=obj.getString("post_type");
                                            String pCoverURL=obj.getString("front_image_path");
                                            String price=obj.getString("cost");
                                            String dicountPrice=obj.getString("discount");
                                            String dicountType=obj.getString("discount_type");
                                            String location=obj.getString("contact_address");
                                            String createdAt=obj.getString("created");
                                            int pStatus=obj.getInt("status");
                                            FBPostCommonFunction.SubmitPost(String.valueOf(pID),pTitle,pType,pCoverURL,price,dicountPrice,dicountType,location,createdAt,pStatus,pk);
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.post_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mProgress.dismiss();
                                                        startActivity(new Intent(Camera.this,Home.class));
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.post_fail_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        mProgress.dismiss();
                                        //Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }catch (JsonParseException e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                alertDialog.setTitle(getString(R.string.title_post));
                                alertDialog.setMessage(getString(R.string.post_fail_message));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });

                        mProgress.dismiss();
                    }

                }//
                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    Log.d("Failure:",mMessage );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Failure:",mMessage );
                            AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                            alertDialog.setTitle(getString(R.string.title_post));
                            alertDialog.setMessage(getString(R.string.post_fail_message));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });

                    mProgress.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                    alertDialog.setTitle(getString(R.string.title_post));
                    alertDialog.setMessage(getString(R.string.post_fail_message));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            mProgress.dismiss();
        }

    } //postdata

    private void EditPost_Approve(String encode,int edit_id) {


        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "";
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        JSONObject sale = new JSONObject();
        //Log.d(TAG,"Edit Model "+mmodel);
        if(strDiscountType==null || strDiscountType.isEmpty())
            strDiscountType="amount";
        if(strCondition==null || strCondition.isEmpty())
            strCondition="new";
        if(strColor==null || strColor.isEmpty())
            strColor="black";
        String str_dis=etDiscount_amount.getText().toString();
        if(str_dis==null || str_dis.isEmpty())
            str_dis="0";
        if(model==0)
            model=mmodel;
        try {
            post.put("title",etTitle.getText().toString().toLowerCase());
            post.put("category", cate );
            post.put("status", 3);
            post.put("condition",strCondition);

            if (strPostType.equals("buy")) {
                post.put("discount", "0");
                post.put("discount_type","amount");
            }else {
                post.put("discount_type", strDiscountType);
                post.put("discount",str_dis);
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
            //post.put("created", "");
            post.put("created_by", pk);
            if (android.os.Build.VERSION.SDK_INT >= 26){
                post.put("modified", Instant.now().toString());
            } else{
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                post.put("modified", currentDateTimeString);
                // do something for phones running an SDK before lollipop
            }
            post.put("modified_by", pk);
            post.put("approved_date", null);
            post.put("approved_by", null);
            post.put("rejected_date", null);
            post.put("rejected_by",null);
            post.put("rejected_comments", "");
            post.put("year", year); //year
            post.put("modeling", model);
            post.put("description", etDescription.getText().toString().toLowerCase());
            post.put("cost",etPrice.getText().toString());
            post.put("post_type",strPostType);
            post.put("vin_code", tvAddress.getQuery().toString().toLowerCase());
            post.put("machine_code", etName.getText().toString().toLowerCase());
            post.put("type", type);
            post.put("contact_phone", etPhone1.getText().toString());
            post.put("contact_email", etEmail.getText().toString().toLowerCase() );
            post.put("contact_address", latlng);
            post.put("color", strColor);

            switch (strPostType){
                case "លក់":
                case "sell":
                    url=ConsumeAPI.BASE_URL+"postsale/"+edit_id+"/";
                    //Log.d("URL","URL"+url);
                    sale.put("sale_status", 4);
                    sale.put("record_status",1);
                    sale.put("sold_date", null);
                    sale.put("price", etPrice.getText().toString());
                    sale.put("total_price",etPrice.getText().toString());
                    post.put("sale_post",new JSONArray("["+sale+"]"));
                    //post.put("rent_post",new JSONArray("[]"));
                    //post.put("buy_post",new JSONArray("[]"));
                    break;

                case "ជួល":
                case "rent":
                    url = ConsumeAPI.BASE_URL+"postrent/"+edit_id+"/";
                    JSONObject rent=new JSONObject();
                    rent.put("rent_status",4);
                    rent.put("record_status",1);
                    rent.put("rent_type","month");
                    rent.put("price",etPrice.getText().toString().toLowerCase());
                    rent.put("total_price",etPrice.getText().toString().toLowerCase());
                    rent.put("rent_date",null);
                    rent.put("return_date",null);
                    rent.put("rent_count_number",0);
                    post.put("rent_post",new JSONArray("["+rent+"]"));
                    break;

                case "ទិញ":
                case "buy":
                    url = ConsumeAPI.BASE_URL+"api/v1/postbuys/"+ edit_id + "/";
                    JSONObject buy=new JSONObject();
                    buy.put("buy_status",4);
                    buy.put("record_status",1);
                    post.put("buy_post",new JSONArray("["+buy+"]"));
                    break;
            }

            Log.d("URLLLLLLL",url);
            //url=ConsumeAPI.BASE_URL+"detailposts/"+edit_id+"/";
            Log.d(TAG,tvAddress.getQuery().toString()+","+etName.getText().toString());
            RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
            String auth = "Basic " + encode;
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization",auth)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Log.d(TAG, "Edit TTTT" + respon);
                    Gson gson = new Gson();
                    CreatePostModel createPostModel = new CreatePostModel();
                    try{
                        createPostModel = gson.fromJson(respon,CreatePostModel.class);
                        if (createPostModel!=null){
                            int id = createPostModel.getId();

                            if (id!=0 ){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //update to firebase
                                        try {
                                            JSONObject obj = new JSONObject(respon);
                                            int pID = obj.getInt("id");
                                            String pTitle = obj.getString("title");
                                            String pCoverURL = obj.getString("front_image_path");
                                            String price = obj.getString("cost");
                                            String dicountPrice = obj.getString("discount");
                                            String dicountType = obj.getString("discount_type");
                                            String location = obj.getString("contact_address");
                                            String createdAt = obj.getString("created");
                                            FBPostCommonFunction.modifiedPost(String.valueOf(pID), pTitle, pCoverURL, price, dicountPrice, dicountType, location, createdAt);
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.waiting_approval));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mProgress.dismiss();
                                                        startActivity(new Intent(Camera.this,Home.class));
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                });

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.post_fail_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        mProgress.dismiss();
                                    }
                                });
                            }
                        }
                    }catch (JsonParseException e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                alertDialog.setTitle(getString(R.string.title_post));
                                alertDialog.setMessage(getString(R.string.post_fail_message));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                        mProgress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    Log.d("Failure:",mMessage );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                            alertDialog.setTitle(getString(R.string.title_post));
                            alertDialog.setMessage(getString(R.string.post_fail_message));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                    mProgress.dismiss();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                    alertDialog.setTitle(getString(R.string.title_post));
                    alertDialog.setMessage(getString(R.string.post_fail_message));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            mProgress.dismiss();
        }
    } //edit post approve

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
                    categoryListItems=new String[jsonArray.length()];
                    categoryListItemkh=new String[jsonArray.length()];
                    categoryIdListItems=new int[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("cat_name");
                        String category=object.getString("cat_name_kh");
                        categoryListItemkh[i]=category;
                        categoryListItems[i]=name;
                        categoryIdListItems[i]=id;
                    }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            tvType_elec.setHint(getString(R.string.type));
                            tvType_elec.setText("");
                            icType_elec.setImageResource(R.drawable.icon_null);
                            type=0;
                            JSONObject jsonObject = new JSONObject(respon);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            typeListItems=new String[jsonArray.length()];
                            typeListItemkh=new String[jsonArray.length()];
                            typeIdListItems=new int[jsonArray.length()];
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                String name = object.getString("type");
                                String type=object.getString("type_kh");
                                typeListItemkh[i]=type;
                                typeListItems[i]=name;
                                typeIdListItems[i]=id;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    } // type

    private void Call_Brand(String encode, int id_cate){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            brand=0;
                            tvBrand.setText("");
                            tvBrand.setHint(getString(R.string.brand));
                            icBrand.setImageResource(R.drawable.icon_null);
                            JSONObject jsonObject = new JSONObject(respon);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            int count=0,ccount=0;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int cate = object.getInt("category");
                                if(id_cate==cate)
                                    count++;
                            }
                            brandIdListItems=new int[count];
                            brandListItem=new String[count];
                            brandListItemkh=new String[count];

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int cate = object.getInt("category");
                                if (cate==id_cate){
                                    int id = object.getInt("id");
                                    String name = object.getString("brand_name");
                                    String brand =object.getString("brand_name_as_kh");
                                    brandListItemkh[ccount]=brand;
                                    brandListItem[ccount]=name;
                                    brandIdListItems[ccount]=id;
                                    ccount++;
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }// brand

    private void Call_Model(String encode,int id_bran,String modelName){

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            model=0;
                            tvModel.setText("");
                            tvModel.setHint(getString(R.string.model));
                            JSONObject jsonObject = new JSONObject(respon);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            int count=0,ccount=0;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int Brand = object.getInt("brand");
                                if(Brand==id_bran)
                                    count++;
                            }
                            modelListItems=new String[count];
                            modelIdListItems=new int[count];
                            modelListItemkh=new String[count];
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                int Brand = object.getInt("brand");
                                if (Brand==id_bran) {
                                    String name = object.getString("modeling_name");
                                    String model=object.getString("modeling_name_kh");
                                    modelListItemkh[ccount]=model;
                                    modelListItems[ccount]=name;
                                    modelIdListItems[ccount]=id;
                                    ccount++;
                                }
                            }

                            if(modelName!=null && !modelName.isEmpty())
                                tvModel.setText(modelName);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
                });
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
                    int count=0;
                    yearListItems=new String[jsonArray.length()];
                    yearIdListItems=new int[jsonArray.length()];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int id = object.getInt("id");
                                    String name = object.getString("year");
                                    yearListItems[i]=name;
                                    yearIdListItems[i]=id;
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    } // years

    private void get_model(String encode , int model_id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/models/"+model_id);
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
                String respon_model = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(respon_model);
                            int brand_id = jsonObject.getInt("brand");
                            String name = jsonObject.getString("modeling_name");
                            List<String> listMN = new ArrayList<>();
                            listMN.add(name);
                            Log.d("Brand",brand_id+" Model: "+name );
                            ArrayAdapter<Integer> adapterM_id = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,model_id);
                            ArrayAdapter<String> adapterM_name = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listMN);
                            //tvModel.setAdapter(adapterM_name);
                            //tvModel.setSelection(0);

                            final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/brands/"+brand_id);
                            Log.d("Url brand",url);
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
                                    String respon_brand = response.body().string();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject object = new JSONObject(respon_brand);
                                                String brand_name = object.getString("brand_name");
                                                Log.d("brand name",brand_name);
                                                List<String> ListBn = new ArrayList<>();
                                                ListBn.add(brand_name);
                                                ArrayAdapter<Integer> adapterB_id = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,brand_id);
                                                ArrayAdapter<String> adapterB_name = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,ListBn);
                                                //tvBrand.setAdapter(adapterB_name);
                                                //tvBrand.setSelection(0);



                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getCategegoryName(String encode,int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/categories/"+id+"/");
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
                    SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                    String language = preferences.getString("My_Lang", "");
                    JSONObject jsonObject = new JSONObject(respon);
                    String catName = jsonObject.getString("cat_name");
                    String catNamekh=jsonObject.getString("cat_name_kh");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (language.equals("km")){
                                tvCategory.setText(catNamekh);
                            }else if (language.equals("en")) {
                                tvCategory.setText(catName);
                            }
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTypeName(String encode,int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/types/"+id+"/");
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                            String language = preferences.getString("My_Lang", "");
                            JSONObject jsonObject = new JSONObject(respon);
                            String typename=jsonObject.getString("type");
                            String typenamekh=jsonObject.getString("type_kh");
                            if (language.equals("km")){
                                tvType_elec.setText(typenamekh);
                            }else if (language.equals("en")){
                                tvType_elec.setText(typename);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void getBrandName(String encode, int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/brands/"+id+"/");
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
                                tvBrand.setText(brandnamekh);
                            }else if (language.equals("en")){
                                tvBrand.setText(brandname);
                            }
                            //Call_Model(Encode,id,);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void getModelName(String encode,int id){

        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/models/"+id+"/");
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
                                brand=brandId;
                                getBrandName(Encode,brand);
                                Call_Model(Encode,brand,namekh);
                            }else if (language.equals("en")){
                                brand=brandId;
                                getBrandName(Encode,brand);
                                Call_Model(Encode,brand,name);
                            }
                            //tvModel.setText(name);
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

    private void getYearName(String encode,int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/years/"+id+"/");
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
                    String yearname=jsonObject.getString("year");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvYear.setText(yearname);
                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void DropDown() {

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        postTypeListItems=getResources().getStringArray(R.array.posty_type);
        tvPostType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bundle != null) {

                } else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                    mBuilder.setTitle(getString(R.string.choose_type_post));
                    mBuilder.setSingleChoiceItems(postTypeListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvPostType.setText(postTypeListItems[i]);
                            //Toast.makeText(Camera.this,postTypeListItems[i],Toast.LENGTH_LONG).show();
                            icPostType.setImageResource(R.drawable.ic_check_circle_black_24dp);

                            switch (i) {
                                case 0:
                                    strPostType = "sell";
                                    relatve_discount.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    strPostType = "buy";
                                    relatve_discount.setVisibility(View.GONE);
                                    break;
                                case 2:
                                    strPostType = "rent";
                                    relatve_discount.setVisibility(View.VISIBLE);
                                    break;
                            }
                            dialogInterface.dismiss();
                            Log.d("Post_type:",strPostType);
                        }
                    });

                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }

            }
        });

        conditionListItems = getResources().getStringArray(R.array.condition);
        tvCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_condition));
                mBuilder.setSingleChoiceItems(conditionListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvCondition.setText(conditionListItems[i]);
                        icCondition.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        switch (i){
                            case 0:
                                strCondition="new";
                                break;
                            case 1:
                                strCondition="used";
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        colorListItems = getResources().getStringArray(R.array.color);
        tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_color));
                mBuilder.setSingleChoiceItems(colorListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvColor.setText(colorListItems[i]);
                        switch (i){
                            case 0:
                                strColor="blue";
                                break;
                            case 1:
                                strColor="black";
                                break;
                            case 2:
                                strColor="silver";
                                break;
                            case 3:
                                strColor="red";
                                break;
                            case 4:
                                strColor="gray";
                                break;
                            case 5:
                                strColor="yellow";
                                break;
                            case 6:
                                strColor="pink";
                                break;
                            case 7:
                                strColor="purple";
                                break;
                            case 8:
                                strColor="orange";
                                break;
                            case 9:
                                strColor="green";
                                break;
                        }
                        icColor.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        discountTypeListItems = getResources().getStringArray(R.array.discount_type);
        tvDiscount_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Camera.this);
                mBuilder.setTitle(getString(R.string.choose_discount));
                mBuilder.setSingleChoiceItems(discountTypeListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        tvDiscount_type.setText(discountTypeListItems[i]);
                        switch (i){
                            case 0:
                                //etDiscount_amount.setHint("Discount Amount");
                                strDiscountType="amount";
                                break;
                            case 1:
                                //etDiscount_amount.setHint("Discount Percentage");
                                strDiscountType="percent";
                                break;
                        }
                        icDiscount_type.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                            tvDiscount_type.setText("");
                            etDiscount_amount.setText("");

                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
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
        tvPostType = (EditText) findViewById(R.id.tvPostType);
        tvCategory = (EditText) findViewById(R.id.tvCategory);
        tvType_elec= (EditText) findViewById(R.id.tvType_elec);
        tvBrand    = (EditText) findViewById(R.id.tvBrand);
        tvModel    = (EditText) findViewById(R.id.tvModel);
        tvYear     = (EditText) findViewById(R.id.tvYears);
        tvCondition= (EditText) findViewById(R.id.tvCondition);
        tvColor    = (EditText) findViewById(R.id.tvColor);
        tvDiscount_type = (EditText) findViewById(R.id.tvDisType);
        tvAddress  = (SearchView) findViewById(R.id.tvAddress_post);
        // edit text ////
        etTitle           = (EditText)findViewById(R.id.etTitle );
        etDescription     = (EditText)findViewById(R.id.etDescription );
        etPrice           = (EditText)findViewById(R.id.etPrice );
        etDiscount_amount = (EditText)findViewById(R.id.etDisAmount );
        etName            = (EditText)findViewById(R.id.etName );
        etPhone1          = (EditText)findViewById(R.id.etphone1 );
        etEmail           = (EditText)findViewById(R.id.etEmail );
        //// icon  ////////
        icTitile     = (ImageView) findViewById(R.id.imgTitle);
        icPostType   = (ImageView) findViewById(R.id.imgPostType);
        icCategory   = (ImageView) findViewById(R.id. imgCategory);
        icType_elec  = (ImageView) findViewById(R.id.imgType_elec );
        icBrand      = (ImageView) findViewById(R.id. imgBrand);
        icModel      = (ImageView) findViewById(R.id. imgModel);
        icYears      = (ImageView) findViewById(R.id. imgYear);
        icCondition  = (ImageView) findViewById(R.id. imgCondition);
        icColor      = (ImageView) findViewById(R.id. imgColor);
        icDescription= (ImageView) findViewById(R.id. imgDescription);
        icAddress    = (ImageView) findViewById(R.id.imgAddress_post);
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

        input_title = (TextInputLayout)findViewById(R.id.tilTitle);
        input_price = (TextInputLayout)findViewById(R.id.tilPrice);
        input_des = (TextInputLayout)findViewById(R.id.tilDescription);
        input_dis = (TextInputLayout)findViewById(R.id.tilDisAmout);
        input_name = (TextInputLayout)findViewById(R.id.tilName);
        input_phone = (TextInputLayout)findViewById(R.id.tilPhone1);
        input_email = (TextInputLayout)findViewById(R.id.tilEmail);

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
                } else if (s.length() < 2) {
                    icTitile.setImageResource(R.drawable.ic_error_black_24dp);
                } else icTitile.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_title.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_title.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvPostType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icPostType.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icPostType.setImageResource(R.drawable.ic_error_black_24dp);
                } else icPostType.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icCategory.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icCategory.setImageResource(R.drawable.ic_error_black_24dp);
                } else icCategory.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvType_elec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icType_elec.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icType_elec.setImageResource(R.drawable.ic_error_black_24dp);
                } else icType_elec.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icBrand.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 2) {
                    icBrand.setImageResource(R.drawable.ic_error_black_24dp);
                } else icBrand.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icModel.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 2) {
                    icModel.setImageResource(R.drawable.ic_error_black_24dp);
                } else icModel.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icYears.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icYears.setImageResource(R.drawable.ic_error_black_24dp);
                } else icYears.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvCondition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icCondition.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icCondition.setImageResource(R.drawable.ic_error_black_24dp);
                } else icCondition.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icColor.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icColor.setImageResource(R.drawable.ic_error_black_24dp);
                } else icColor.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icPrice.setImageResource(R.drawable.icon_null);
                } else if (s.length() > 0) {
                    icPrice.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    input_price.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                    input_price.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icDescription.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icDescription.setImageResource(R.drawable.ic_error_black_24dp);
                } else icDescription.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_des.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_des.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvDiscount_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icDiscount_type.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icDiscount_type.setImageResource(R.drawable.ic_error_black_24dp);
                } else icDiscount_type.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etDiscount_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icDiscount_amount.setImageResource(R.drawable.icon_null);
                } else if (s.length() >0) {
                    icDiscount_amount.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    input_dis.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                    input_dis.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icName.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icName.setImageResource(R.drawable.ic_error_black_24dp);
                } else icName.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_name.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_name.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icEmail.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 3) {
                    icEmail.setImageResource(R.drawable.ic_error_black_24dp);
                } else icEmail.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_email.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_email.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icPhone1.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 8) {
                    icPhone1.setImageResource(R.drawable.ic_error_black_24dp);
                } else icPhone1.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//     tvAddress.addTextChangedListener(new TextWatcher() {
//         @Override
//         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//         }
//
//         @Override
//         public void onTextChanged(CharSequence s, int start, int before, int count) {
//             if (s.length() == 0) {
//                 icAddress.setImageResource(R.drawable.icon_null);
//             } else if (s.length() < 3) {
//                 icAddress.setImageResource(R.drawable.ic_error_black_24dp);
//             } else icAddress.setImageResource(R.drawable.ic_check_circle_black_24dp);
//         }
//
//         @Override
//         public void afterTextChanged(Editable s) {
//
//         }
//     });




    } // text change

    private void selectImage() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        final CharSequence[] items = {"Take Photo", "Choose from Library","Cancel"};
        final CharSequence[] itemkh = {"ថតរូប", "វិចិត្រសាល","បោះបង់"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Camera.this);
        if (language.equals("km")){
            builder.setItems(itemkh, (dialog, item) -> {
                if (itemkh[item].equals("ថតរូប")) {
                    requestStoragePermission(true);
                } else if (itemkh[item].equals("វិចិត្រសាល")) {
                    requestStoragePermission(false);
                } else if (itemkh[item].equals("បោះបង់")) {
                    dialog.dismiss();
                }
            });
        }else if (language.equals("en")){
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals("Take Photo")) {
                    requestStoragePermission(true);
                } else if (items[item].equals("Choose from Library")) {
                    requestStoragePermission(false);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            });
        }
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
        builder.setTitle(getString(R.string.permission));
        builder.setMessage(getString(R.string.setting_permission));
        builder.setPositiveButton(getString(R.string.go_setting), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
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

    private void Seach_Address(){
        String loca = tvAddress.getQuery().toString();
        List<Address> address_Search = null;
        if (loca!=null || !loca.equals("")){
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try{
                address_Search = geocoder.getFromLocationName(loca,1);
            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                LatLng ll = new LatLng(address_Search.get(0).getLatitude(),address_Search.get(0).getLongitude());
                mMap.addMarker(new MarkerOptions().position(ll));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(ll));
                List<Address> address_S = address_Search;
                latitude = address_S.get(0).getLatitude();
                longtitude = address_S.get(0).getLongitude();
                latlng = latitude+","+longtitude;
                Log.d("LATITUDE and LONGTITUDE",latlng);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void get_location(boolean isCurrent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocation(isCurrent);
        }
    }

    private void getLocation_edit(double latitude, double longtitude){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            try{
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location!=null){

                    try{
                        Geocoder geocoder = new Geocoder(this);
                        List<Address> addressList = null;
                        addressList = geocoder.getFromLocation(latitude, longtitude,1);
//                    String country = addressList.get(0).getCountryName();
//                    String city    = addressList.get(0).getLocality();
                        String road = addressList.get(0).getAddressLine(0);

                        tvAddress.setQuery( road , false );
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }else {
                    Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void getLocation(boolean isCurrent) {

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            try{
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location!=null){
                    if(isCurrent) {
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                    }
                    latlng = latitude+","+longtitude;
                    try{
                        Geocoder geocoder = new Geocoder(this);
                        List<Address> addressList = null;
                        addressList = geocoder.getFromLocation(latitude,longtitude,1);

                        String road = addressList.get(0).getAddressLine(0);

                        tvAddress.setQuery( road , false );
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }else {
                    Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }  //

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_map_pin_48px_3));
        mMap = googleMap;
        LatLng current_location = new LatLng(latitude, longtitude);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current_location)
                .zoom(18)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_map_pin_48px_3));
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                latitude = latLng.latitude;
                longtitude = latLng.longitude;

                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location!=null) {
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        List<Address> addressList = null;
                        addressList = geocoder.getFromLocation(latitude, longtitude, 1);
                        String country = addressList.get(0).getCountryName();
                        String city = addressList.get(0).getLocality();
                        String road = addressList.get(0).getAddressLine(0);

                        tvAddress.setQuery(road,false);
                        latlng = latitude+","+longtitude;
                        Log.d("LATITUDE",latitude+","+longtitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        bnavigation.getMenu().getItem(2).setChecked(true);
    }
}


