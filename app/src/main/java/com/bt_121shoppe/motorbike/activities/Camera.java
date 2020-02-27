package com.bt_121shoppe.motorbike.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.adapters.CustomView;
import com.bt_121shoppe.motorbike.adapters.ColorAdapter;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Api.api.model.dealershop;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseCondition;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseYear;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseType;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseCategory;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseTypeCate;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseBrand;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseModel;
import com.bt_121shoppe.motorbike.Login_Register.Register;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.fragments.FragmentMap;
import com.bt_121shoppe.motorbike.loan.child.one;
import com.bt_121shoppe.motorbike.models.CreatePostModel;
import com.bt_121shoppe.motorbike.models.PostDealerShopViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.UserShopViewModel;
import com.bt_121shoppe.motorbike.stores.StoreListActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.responses.FunctionColor;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.FileCompressor;
import com.bt_121shoppe.motorbike.utils.ImageUtil;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Camera extends AppCompatActivity implements BottomChooseCondition.ItemClickListener,
        BottomChooseYear.ItemClickListener,BottomChooseBrand.ItemClickListener,BottomChooseModel.ItemClickListener,
        BottomChooseCategory.ItemClickListener, BottomChooseType.ItemClickListener,BottomChooseTypeCate.ItemClickListener{
    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = Camera.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO_1=1;
    private static final int REQUEST_TAKE_PHOTO_2=2;
    private static final int REQUEST_TAKE_PHOTO_3=3;
    private static final int REQUEST_TAKE_PHOTO_4=4;
    private static final int REQUEST_TAKE_PHOTO_5=9;
    private static final int REQUEST_TAKE_PHOTO_6=10;
    private static final int REQUEST_GALLERY_PHOTO_1=5;
    private static final int REQUEST_GALLERY_PHOTO_2=6;
    private static final int REQUEST_GALLERY_PHOTO_3=7;
    private static final int REQUEST_GALLERY_PHOTO_4=8;
    private static final int REQUEST_GALLERY_PHOTO_5=11;
    private static final int REQUEST_GALLERY_PHOTO_6=12;
    private int REQUEST_TAKE_PHOTO_NUM=0;
    private int REQUEST_CHOOSE_PHOTO_NUM=0;
    private EditText etDescription,etPrice,etDiscount_amount,etPhone1,etPhone2,etPhone3,etEmail,etAddress,etMap,etName;
    private EditText tvPostType,tvCondition,tvYear,tvCategory,tvType_cate,tvBrand,tvModel;
    private ImageButton btremove_pic1,btremove_pic2,btremove_pic3,btremove_pic4,btremove_pic5,btremove_pic6;
    private Button submit_post,bt_update;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageMap,imgAdd_color,cancel_color;
    private String user_phone,user_address;
    private String name,pass,Encode,road,name_post,post;
    private String cat,condition1,type_post,yearr,discount_price,price,modell,brandd,categoryy,post_typee,condition,email_post,address_post,phone_number1_post,phone_number2_post,phone_number3_post,description;
    private String login_verify,register_intent,strPostType,strCondition,strColor,strColorKH="";
    private String color,latlng;
    private List<String> arrayColor;
    private String user_name,user_email,user_address_name;
    private String edit_name,edit_email,edit_phone,edit_address,edit_address_name;
    private ArrayList<Integer> selectedColor;
    private int seekbar_price = 0,seekbar_rearr = 0,seekbar_screww = 0, seekbar_engine = 0, seekbar_head = 0,assembly = 0,seekbar_accessorie = 0,seekbar_consolee = 0,seekbar_pump = 0,whole_ink=0;
    private int pk,mmodel=1,edit_id,shopId=0;
    private int process_type=0,category=0;
    private int cate=0,brand=0,model=0,year=0,type=0;
    private int id_typeother;
    private int selectedIndex;
    private File mPhotoFile;
    private FileCompressor mCompressor;
    private SharedPreferences prefer,pre_id;
    private SupportMapFragment mapFragment;
    private ProgressDialog mProgress;
    private Bitmap bitmapImage1,bitmapImage2,bitmapImage3,bitmapImage4,bitmapImage5,bitmapImage6,default_bitmap;
    private Uri image1,image2,image3,image4,image5,image6;

    private Bundle bundle;
    private RelativeLayout layout_color;
    private RelativeLayout layout_phone1,layout_phone2;
    private LinearLayout layout_estimate;
    private GridView gridView;
    private LocationManager locationManager;
    private double latitude,longtitude;

    private SeekBar seekbar_pri_per,seekbar_rear,seekbar_screw,seekbar_pumps,seekbar_rigt_engine,seekbar_engine_head;
    private SeekBar seekbar_assmebly,seekbar_console,seekbar_accessories,seekbar_whole;

    private TextView tv_add,tv_add1,tv_cancel,tvType_elec,tv_name,title_dicount,show_ID;
    private TextView phone_alert,email_alert,address_alert,map_alert,name_alert;
    private TextView price_alert,dis_price_alert,post_type_alert,category_alert,type_alert,brand_alert,model_alert,year_alert,info_alert,conditon_alert;
    private TextView whole_alert,console_alert,assembly_alert,engine_head_alert,right_engine_alert,screw_alert,accessories_alert,rear_alert,pumps_alert;
    private TextView tvConsole,tvAssecssorie,tvWhole_int,tvRear,tvScrew,tvPumps,tvRight_engine,tvEngine_head,tvAssembly;
    private String[] shopListItems;
    private int[]    shopIdListItems;

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
        //ButterKnife.bind(this);
        //check active and deactive account by samang 2/09/19
        Active_user activeUser = new Active_user();
        String active;
        active = activeUser.isUserActive(pk,this);
        if (active.equals("false")){
            activeUser.clear_session(this);
        }
        // end
        mCompressor = new FileCompressor(this);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");

        Toolbar toolbar=findViewById(R.id.toolbar);
        pre_id = getSharedPreferences("id",MODE_PRIVATE);
        Variable_Field();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_post);
        DropDown();
        mSeekbar();
        initialUserInformation(pk,Encode);

        show_ID.setText(String.valueOf(CommonFunction.generateRandomDigits(9)));

        bundle = getIntent().getExtras();
        if (bundle!=null) {
            shopId=bundle.getInt("shopId",0);
            Log.e(TAG,"shop Id "+shopId);
            post               = bundle.getString("post");
            road               = bundle.getString("road");
            latlng             = bundle.getString("location");
            login_verify       = bundle.getString("Login_verify");
            register_intent    = bundle.getString("Register_verify");
            process_type       = bundle.getInt("process_type",0);
            price              = bundle.getString("price");
            modell             = bundle.getString("model");
            brandd             = bundle.getString("brand");
            condition          = bundle.getString("condition");
            description        = bundle.getString("description");
            categoryy          = bundle.getString("category");
            post_typee         = bundle.getString("post_type");
            email_post         = bundle.getString("email_post");
            address_post       = bundle.getString("address_post");
            phone_number1_post = bundle.getString("phone_number1_post");
            phone_number2_post = bundle.getString("phone_number2_post");
            phone_number3_post = bundle.getString("phone_number3_post");
            discount_price     = bundle.getString("discount_amount");
            seekbar_price      = bundle.getInt("discount_percent",0);
            yearr              = bundle.getString("year");
            whole_ink          = bundle.getInt("whole_ink",0);
            seekbar_rearr      = bundle.getInt("rear",0);
            seekbar_screww     = bundle.getInt("screw",0);
            seekbar_pump       = bundle.getInt("pumps",0);
            seekbar_engine     = bundle.getInt("right_engine",0);
            seekbar_head       = bundle.getInt("engine_head",0);
            assembly           = bundle.getInt("assembly",0);
            seekbar_consolee   = bundle.getInt("console",0);
            seekbar_accessorie = bundle.getInt("accessories",0);
            name_post          = bundle.getString("name_post");
            cat                = bundle.getString("cat");
            strColor           = bundle.getString("color");
            image1             = bundle.getParcelable("image1");
            image2             = bundle.getParcelable("image2");
            image3             = bundle.getParcelable("image3");
            image4             = bundle.getParcelable("image4");
            image5             = bundle.getParcelable("image5");
            image6             = bundle.getParcelable("image6");
            model              = bundle.getInt("modelID",0);
            brand              = bundle.getInt("brandID",0);
            category           = bundle.getInt("categoryID",0);
            type               = bundle.getInt("typeID",0);
            year               = bundle.getInt("yearID",0);
            if(process_type != 1 && process_type != 2 && process_type!=3){
                submit_post.setVisibility(View.VISIBLE);
                bt_update.setVisibility(View.GONE);
                category = bundle.getInt("category_post",0);
                strPostType = tvPostType.getText().toString();
                if(strPostType.equals("sell")){
                    tvPostType.setText(R.string.sel);
                    toolbar.setBackgroundColor(getColor(R.color.logo_green));
                }else if(strPostType.equals("rent")){
                    tvPostType.setText(R.string.ren);
                    toolbar.setBackgroundColor(getColor(R.color.logo_red));
                }
            }else{
                bt_update.setVisibility(View.VISIBLE);
                submit_post.setVisibility(View.GONE);
                tv_name.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                name_alert.setVisibility(View.VISIBLE);
                edit_id = bundle.getInt("id_product", 0);
                getData_Post(Encode,edit_id);
            }
            etName.setText(name_post);
            tvType_cate.setText(cat);
            etPrice.setText(price);
            tvYear.setText(yearr);
            tvModel.setText(modell);
            tvBrand.setText(brandd);
            tvCondition.setText(condition);
            etEmail.setText(email_post);
            etDescription.setText(description);
            tvCategory.setText(categoryy);
            tvPostType.setText(post_typee);
            etAddress.setText(address_post);
            etPhone1.setText(phone_number1_post);
            etPhone2.setText(phone_number2_post);
            etPhone3.setText(phone_number3_post);
            etDiscount_amount.setText(discount_price);
            seekbar_pri_per.setProgress(seekbar_price);
            seekbar_whole.setProgress(whole_ink);
            seekbar_engine_head.setProgress(seekbar_head);
            seekbar_rigt_engine.setProgress(seekbar_engine);
            seekbar_assmebly.setProgress(assembly);
            seekbar_console.setProgress(seekbar_consolee);
            seekbar_accessories.setProgress(seekbar_accessorie);
            seekbar_pumps.setProgress(seekbar_pump);
            seekbar_screw.setProgress(seekbar_screww);
            seekbar_rear.setProgress(seekbar_rearr);
            etMap.setText(road);
            setColor(FunctionColor.selectItemColor(strColor));

            if(category == 1){
                layout_estimate.setVisibility(View.GONE);
                tvType_elec.setVisibility(View.VISIBLE);
                tvType_cate.setVisibility(View.VISIBLE);
            }else {
                tvType_elec.setVisibility(View.GONE);
                tvType_cate.setVisibility(View.GONE);
                if (condition != null){
                    if (condition.equals("used") || condition.equals("ប្រើប្រាស់រួច")){
                        layout_estimate.setVisibility(View.VISIBLE);
                    }else if (condition.equals("new") || condition.equals("ថ្មី")){
                        layout_estimate.setVisibility(View.GONE);
                    }
                }
            }
            if (shopId>0){
                getInfo_store(pk,Encode);
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                getLocation(true);
            }

            Glide.with(Camera.this).asBitmap().load(image1).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageView1.setImageBitmap(resource);
                    imageView1.setScaleType(ImageView.ScaleType.CENTER);
                    bitmapImage1 = resource;
                    btremove_pic1.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            Glide.with(Camera.this).asBitmap().load(image2).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageView2.setImageBitmap(resource);
                    imageView2.setScaleType(ImageView.ScaleType.CENTER);
                    bitmapImage2 = resource;
                    btremove_pic2.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            Glide.with(Camera.this).asBitmap().load(image3).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageView3.setImageBitmap(resource);
                    imageView3.setScaleType(ImageView.ScaleType.CENTER);
                    bitmapImage3 = resource;
                    btremove_pic3.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            Glide.with(Camera.this).asBitmap().load(image4).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageView4.setImageBitmap(resource);
                    imageView4.setScaleType(ImageView.ScaleType.CENTER);
                    bitmapImage4 = resource;
                    btremove_pic4.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            Glide.with(Camera.this).asBitmap().load(image5).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageView5.setImageBitmap(resource);
                    imageView5.setScaleType(ImageView.ScaleType.CENTER);
                    bitmapImage5 = resource;
                    btremove_pic5.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            Glide.with(Camera.this).asBitmap().load(image6).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageView6.setImageBitmap(resource);
                    imageView6.setScaleType(ImageView.ScaleType.CENTER);
                    bitmapImage6 = resource;
                    btremove_pic6.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            getLocation(true);
        }

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

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_5;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_5;
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_6;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_6;
            }
        });
        //add by Raksmey
        btremove_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView1 != null){
                    imageView1.setImageResource(R.drawable.icon_camera);
                    bitmapImage1=null;
                    btremove_pic1.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView2 !=null){
                    imageView2.setImageResource(R.drawable.icon_camera);
                    bitmapImage2=null;
                    btremove_pic2.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView3 !=null){
                    imageView3.setImageResource(R.drawable.icon_camera);
                    bitmapImage3=null;
                    btremove_pic3.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView4 !=null){
                    imageView4.setImageResource(R.drawable.icon_camera);
                    bitmapImage4=null;
                    btremove_pic4.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView5 !=null){
                    imageView5.setImageResource(R.drawable.icon_camera);
                    bitmapImage5=null;
                    btremove_pic5.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView6 !=null){
                    imageView6.setImageResource(R.drawable.icon_camera);
                    bitmapImage6=null;
                    btremove_pic6.setVisibility(View.GONE);
                }
            }
        });
        tv_add.setVisibility(View.VISIBLE);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone1.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
            }
        });
        tv_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone2.setVisibility(View.VISIBLE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone1.setVisibility(View.GONE);
                layout_phone2.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_add.setVisibility(View.VISIBLE);
            }
        });
        TextView back = (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog builder = new android.app.AlertDialog.Builder(Camera.this).create();
                builder.setMessage(getString(R.string.back_message));
                builder.setCancelable(false);
                builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.back_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                });
                builder.setButton(Dialog.BUTTON_NEGATIVE,getString(R.string.back_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });

//End
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stDis_amount,stDis_percent,stPrice;
                double dbDis_amount = 0 , dbDis_percent = 0, dbPrice ;
                double ko = 0.0;
                stPrice = etPrice.getText().toString();

                if (stPrice == null || stPrice.isEmpty()){
                    dbPrice = 1;
                }else {
                    dbPrice = Double.parseDouble(stPrice);
                }
                stDis_amount = etDiscount_amount.getText().toString();
                if (stDis_amount == null || stDis_amount.isEmpty()){
                    dbDis_percent = 0;
                    dbDis_amount = 0;
                }
                int image_value ;
                String postType = tvPostType.getText().toString();
                if (postType.equals("Buy") || postType.equals("ទិញ")){
                    image_value = 1;
                }else if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                    image_value = 0;
                }else image_value = 1;

                if (tvPostType.getText().toString().length()==0||tvCategory.getText().toString().length()==0
                        || tvBrand.getText().toString().length()==0 || tvModel.getText().toString().length()==0 || tvYear.getText().toString().length()==0
                        || etPrice.getText().toString().length()==0 || etPhone1.getText().toString().length() < 9 || dbDis_percent >=100|| dbDis_amount >= dbPrice
                        ||  image_value == 0) {
                    if (etPhone1.getText().toString().length()<9){
                        phone_alert.setText(getString(R.string.invalid_phone));
                        phone_alert.setTextColor(getColor(R.color.red));
                        etPhone1.requestFocus();
                    }else phone_alert.setText("");
                    if (etPrice.getText().toString().length()==0) {
                        price_alert.setText(getString(R.string.invalid_price));
                        price_alert.setTextColor(getColor(R.color.red));
                        etPrice.requestFocus();
                    }else price_alert.setText("");
                    if (tvYear.getText().toString().length()==0) {
                        year_alert.setText(getString(R.string.invalid_year));
                        year_alert.setTextColor(getColor(R.color.red));
                        tvYear.requestFocus();
                    }else year_alert.setText("");
                    if (tvModel.getText().toString().length()==0) {
                        model_alert.setText(getString(R.string.invalid_model));
                        model_alert.setTextColor(getColor(R.color.red));
                        tvModel.requestFocus();
                    }else model_alert.setText("");
                    if (tvBrand.getText().toString().length()==0) {
                        brand_alert.setText(getString(R.string.invalid_brand));
                        brand_alert.setTextColor(getColor(R.color.red));
                        tvBrand.requestFocus();
                    }else brand_alert.setText("");

                    if (tvCategory.getText().toString().length()==0) {
                        category_alert.setText(getString(R.string.invalid_category));
                        category_alert.setTextColor(getColor(R.color.red));
                        tvCategory.requestFocus();
                    }else category_alert.setText("");
                    if (tvPostType.getText().toString().length()==0){
                        post_type_alert.setText(getString(R.string.invalid_post_type));
                        post_type_alert.setTextColor(getColor(R.color.red));
                        tvPostType.requestFocus();
                    }else post_type_alert.setText("");
                    if (tvCondition.getText().toString().length() == 0){
                        conditon_alert.setText(getString(R.string.invalid_condition));
                        conditon_alert.setTextColor(getColor(R.color.red));
                        tvCondition.requestFocus();
                    }else conditon_alert.setText("");
                    if (strColor.isEmpty() || strColor == null ){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_color));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    if (dbDis_percent >= 100){
                        etDiscount_amount.requestFocus();
                    }

                    if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_image));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                } else if (category == 1 && tvType_cate.getText().toString().length()==0){
                    tvType_cate.requestFocus();
                } else if (bundle!=null) {
                    mProgress.show();
                    EditPost_Approve(Encode, edit_id);
                } else  {
                    mProgress.show();
                    EditPost_Approve(Encode, edit_id);
                }
            }
        });
        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stDis_amount,stDis_percent,stPrice;
                double dbDis_amount = 0 , dbDis_percent = 0, dbPrice ;
                double ko = 0.0;
                stPrice = etPrice.getText().toString();

                if (stPrice == null || stPrice.isEmpty()){
                    dbPrice = 1;
                }else {
                    dbPrice = Double.parseDouble(stPrice);
                }
                stDis_amount = etDiscount_amount.getText().toString();
                if (stDis_amount == null || stDis_amount.isEmpty()){
                    dbDis_percent = 0;
                    dbDis_amount = 0;
                }
                int image_value ;
                String postType = tvPostType.getText().toString();
                if (postType.equals("Buy") || postType.equals("ទិញ")){
                    image_value = 1;
                }else if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                    image_value = 0;
                }else image_value = 1;

                if (tvPostType.getText().toString().length()==0||tvCategory.getText().toString().length()==0
                        || tvBrand.getText().toString().length()==0 || tvModel.getText().toString().length()==0 || tvYear.getText().toString().length()==0
                        || etPrice.getText().toString().length()==0 || etPhone1.getText().toString().length() < 9 || dbDis_percent >=100|| dbDis_amount >= dbPrice
                        ||  image_value == 0) {
                    if (etPhone1.getText().toString().length()<9){
                        phone_alert.setText(getString(R.string.invalid_phone));
                        phone_alert.setTextColor(getColor(R.color.red));
                        etPhone1.requestFocus();
                    }else phone_alert.setText("");
                    if (etPrice.getText().toString().length()==0) {
                        price_alert.setText(getString(R.string.invalid_price));
                        price_alert.setTextColor(getColor(R.color.red));
                        etPrice.requestFocus();
                    }else price_alert.setText("");
                    if (tvYear.getText().toString().length()==0) {
                        year_alert.setText(getString(R.string.invalid_year));
                        year_alert.setTextColor(getColor(R.color.red));
                        tvYear.requestFocus();
                    }else year_alert.setText("");
                    if (tvModel.getText().toString().length()==0) {
                        model_alert.setText(getString(R.string.invalid_model));
                        model_alert.setTextColor(getColor(R.color.red));
                        tvModel.requestFocus();
                    }else model_alert.setText("");
                    if (tvBrand.getText().toString().length()==0) {
                        brand_alert.setText(getString(R.string.invalid_brand));
                        brand_alert.setTextColor(getColor(R.color.red));
                        tvBrand.requestFocus();
                    }else brand_alert.setText("");

                    if (tvCategory.getText().toString().length()==0) {
                        category_alert.setText(getString(R.string.invalid_category));
                        category_alert.setTextColor(getColor(R.color.red));
                        tvCategory.requestFocus();
                    }else category_alert.setText("");
                    if (tvPostType.getText().toString().length()==0){
                        post_type_alert.setText(getString(R.string.invalid_post_type));
                        post_type_alert.setTextColor(getColor(R.color.red));
                        tvPostType.requestFocus();
                    }else post_type_alert.setText("");
                    if (tvCondition.getText().toString().length() == 0){
                        conditon_alert.setText(getString(R.string.invalid_condition));
                        conditon_alert.setTextColor(getColor(R.color.red));
                        tvCondition.requestFocus();
                    }else conditon_alert.setText("");

                    if (color == null ){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_color));
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    if (dbDis_percent >= 100){
                        etDiscount_amount.requestFocus();
                    }

                    if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_image));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                } else if (category == 1 && tvType_cate.getText().toString().length()==0){
                    tvType_cate.requestFocus();
                } else if (bundle!=null) {
                    mProgress.show();
                    if(process_type==0){
                        if (cate == 2){
                            type = Integer.parseInt(tvType_cate.getText().toString());
                        }
                        //Log.d("Type id brs", String.valueOf(type));
                        PostData(Encode);
                    }
                } else  {
                    mProgress.show();
                    PostData(Encode);
                }
            }
        });

    } // create

    private void dialog_dealer(){
        AlertDialog dialog = new AlertDialog.Builder(Camera.this).create();
        dialog.setTitle(R.string.for_loan_title);
        dialog.setMessage(getString(R.string.already_select_shop));
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(Camera.this).create();
        builder.setMessage(getString(R.string.back_message));
        builder.setCancelable(false);
        builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.back_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (register_intent!=null || login_verify!=null){
                    startActivity(new Intent(Camera.this,Home.class));
                }else if (process_type == 1){
                    startActivity(new Intent(Camera.this,Account.class));
                }else if (post != null){
                    startActivity(new Intent(Camera.this,Home.class));
                }else if (process_type == 2 ){
                    finish();
                }else if (process_type == 3){
                    finish();
                } else finish();
            }
        });
        builder.setButton(Dialog.BUTTON_NEGATIVE,getString(R.string.back_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    private void getData_Post(String encode, int id){
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
                                String description = object.getString("description");
                                int price = object.getInt("cost");
                                int id = object.getInt("user");
                                int post_code = object.getInt("post_code");
                                String search_title = object.getString("vin_code");
                                String name = object.getString("machine_code");
                                String discount = object.getString("discount");
                                String email = object.getString("contact_email");
                                String post_sub_title = object.getString("post_sub_title");
                                strPostType = object.getString("post_type");
                                category = object.getInt("category");
                                type = object.getInt("type");

                                int eta1 = object.getInt("used_eta1");
                                int eta2 = object.getInt("used_eta2");
                                int eta3 = object.getInt("used_eta3");
                                int eta4 = object.getInt("used_eta4");
                                int machine1 = object.getInt("used_machine1");
                                int machine2 = object.getInt("used_machine2");
                                int machine3 = object.getInt("used_machine3");
                                int machine4 = object.getInt("used_machine4");
                                int other1 = object.getInt("used_other1");

                                seekbar_whole.setProgress(eta1);
                                seekbar_rear.setProgress(eta2);
                                seekbar_screw.setProgress(eta3);
                                seekbar_pumps.setProgress(eta4);
                                seekbar_rigt_engine.setProgress(machine1);
                                seekbar_engine_head.setProgress(machine2);
                                seekbar_assmebly.setProgress(machine3);
                                seekbar_console.setProgress(machine4);
                                seekbar_accessories.setProgress(other1);

                                String phone = object.getString("contact_phone");

                                model = object.getInt("modeling");
                                mmodel = object.getInt("modeling");
                                year = object.getInt("year");

                                strCondition = object.getString("condition");
                                strColor = object.getString("multi_color_code");
                                Log.e("String Color",""+strColor);
                                setColor(FunctionColor.selectItemColor(strColor));

                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(getApplication(), Locale.getDefault());
                                String locat = object.getString("contact_address");
                                if (!locat.isEmpty()) {
                                    String add[] = locat.split(",");
                                    Double latetitude = Double.parseDouble(add[0]);
                                    Double longtitude = Double.parseDouble(add[1]);
                                    try {
                                        addresses = geocoder.getFromLocation(latetitude, longtitude, 1);
                                        String road = addresses.get(0).getAddressLine(0);
                                        if (road.length() > 30) {
                                            String loca = road.substring(0,30) + "...";
                                            if (road != null){
                                                if (road.length() > 30) {
                                                    String locatee = road.substring(0,30) + "...";
                                                    etMap.setText(locatee);
                                                }
                                            }else {
                                                etMap.setText(loca);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(!search_title.isEmpty()) {
                                    etAddress.setText(search_title);
                                }

                                String fron = object.getString("front_image_path");
                                String back = object.getString("back_image_path");
                                String left = object.getString("left_image_path");
                                String right = object.getString("right_image_path");
                                String extra1 = object.getString("extra_image1");
                                String extra2 = object.getString("extra_image2");

                                List<String> list = new ArrayList<>();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        show_ID.setText(String.valueOf(post_code));
                                        etDescription.setText(description);
                                        etPrice.setText(String.valueOf(price));
                                        etEmail.setText(email);
                                        String[] st_price = discount.split(".00");
                                        int dis = Integer.valueOf(st_price[0]);
                                        int discount_amount = (price*dis)/100;
                                        seekbar_pri_per.setProgress(dis);
                                        etDiscount_amount.setText(String.valueOf(discount_amount));

                                        String[] splitPhone = phone.split(",");
                                        if (splitPhone.length == 1){
                                            etPhone1.setText(String.valueOf(splitPhone[0]));
                                        }else if (splitPhone.length == 2){
                                            etPhone1.setText(String.valueOf(splitPhone[0]));
                                            etPhone2.setText(String.valueOf(splitPhone[1]));
                                        }else if (splitPhone.length == 3){
                                            etPhone1.setText(String.valueOf(splitPhone[0]));
                                            etPhone2.setText(String.valueOf(splitPhone[1]));
                                            etPhone3.setText(String.valueOf(splitPhone[2]));
                                        }

                                        String postType = strPostType.substring(0,1).toUpperCase() + strPostType.substring(1);

                                        if (postType.equals("Sell")){
                                            tvPostType.setText(R.string.sel);
                                            type_post = "sell";
                                        }else if (postType.equals("Rent")){
                                            tvPostType.setText(R.string.ren);
                                            type_post = "rent";
                                        }else if (postType.equals("Buy")){
                                            tvPostType.setText(R.string.bu);
                                        }

                                        String condition = strCondition.substring(0, 1).toUpperCase() + strCondition.substring(1);
                                        if (category == 2) {
                                            type = 3;
                                            tvType_elec.setVisibility(View.GONE);
                                            tvType_cate.setVisibility(View.GONE);
                                            if (condition.equals("New")){
                                                tvCondition.setText(R.string.newl);
                                                condition1 = "new";
                                            }else if (condition.equals("Used")){
                                                tvCondition.setText(R.string.used);
                                                condition1 = "used";
                                                layout_estimate.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            tvType_elec.setVisibility(View.VISIBLE);
                                            tvType_cate.setVisibility(View.VISIBLE);
                                            if (condition.equals("New")){
                                                tvCondition.setText(R.string.newl);
                                                condition1 = "new";
                                            }else if (condition.equals("Used")){
                                                tvCondition.setText(R.string.used);
                                                condition1 = "used";
                                                layout_estimate.setVisibility(View.GONE);
                                            }
                                        }

                                        Log.e("color",""+strColor);

                                        //Call_Model(Encode,brand);
                                        getCategegoryName(Encode,category);
                                        getTypeName(Encode,type);
                                        getYearName(Encode,year);
                                        getModelName(Encode,model);
                                        getUsername(id);
                                        //Log.d(TAG,"Brand_id"+ brand);

                                        Glide.with(Camera.this).asBitmap().load(fron).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView1.setImageBitmap(resource);
                                                imageView1.setScaleType(ImageView.ScaleType.CENTER);
                                                bitmapImage1 = resource;
                                                btremove_pic1.setVisibility(View.VISIBLE);
                                                image1 = Uri.parse(fron);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(back).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView2.setImageBitmap(resource);
                                                imageView2.setScaleType(ImageView.ScaleType.CENTER);
                                                bitmapImage2 = resource;
                                                Log.d("BITMAP","2:"+bitmapImage2);
                                                btremove_pic2.setVisibility(View.VISIBLE);
                                                image2 = Uri.parse(back);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(left).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView3.setImageBitmap(resource);
                                                imageView3.setScaleType(ImageView.ScaleType.CENTER);
                                                bitmapImage3 = resource;
                                                btremove_pic3.setVisibility(View.VISIBLE);
                                                image3 = Uri.parse(left);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(right).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView4.setImageBitmap(resource);
                                                imageView4.setScaleType(ImageView.ScaleType.CENTER);
                                                bitmapImage4 = resource;
                                                btremove_pic4.setVisibility(View.VISIBLE);
                                                image4 = Uri.parse(right);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(extra1).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView5.setImageBitmap(resource);
                                                imageView5.setScaleType(ImageView.ScaleType.CENTER);
                                                bitmapImage5 = resource;
                                                btremove_pic5.setVisibility(View.VISIBLE);
                                                image5 = Uri.parse(extra1);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(extra2).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView6.setImageBitmap(resource);
                                                imageView6.setScaleType(ImageView.ScaleType.CENTER);
                                                bitmapImage6 = resource;
                                                btremove_pic6.setVisibility(View.VISIBLE);
                                                image6 = Uri.parse(extra2);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });


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
    private void getUsername(int pk) {
        Service apiService = Client.getClient().create(Service.class);
        retrofit2.Call<UserResponseModel> call = apiService.getUserProfile(pk);
        call.enqueue(new retrofit2.Callback<UserResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<UserResponseModel> call, retrofit2.Response<UserResponseModel> response) {
                if (response.isSuccessful()) {
                    etName.setText(response.body().getFirst_name());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<UserResponseModel> call, Throwable t) {

            }
        });
    }
    private void getInfo_store(int pk,String encode){
        Service api=Client.getClient().create(Service.class);
        retrofit2.Call<ShopViewModel> call=api.getDealerShop(shopId);
        call.enqueue(new retrofit2.Callback<ShopViewModel>() {
            @Override
            public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response) {
                if(response.isSuccessful()){
                    etName.setText(response.body().getShop_name());
                    etAddress.setText(response.body().getShop_address());
                    Service api = Client.getClient().create(Service.class);
                    retrofit2.Call<User_Detail> call1 = api.getDetailUser(pk,encode);
                    call1.enqueue(new retrofit2.Callback<User_Detail>() {
                        @Override
                        public void onResponse(retrofit2.Call<User_Detail> call, retrofit2.Response<User_Detail> response) {
                            if (response.isSuccessful()) {
                                String stphone = response.body().getProfile().getTelephone();
                                etPhone1.setText(method(stphone));
                            }
                        }
                        @Override
                        public void onFailure(retrofit2.Call<User_Detail> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ShopViewModel> call, Throwable t) {

            }
        });
    }
    public String method(String str) {
        for (int i=0;i<str.length();i++){
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    private void initialUserInformation(int pk, String encode) {
//        if (bundle==null || bundle.isEmpty()) {
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
//                                User converJsonJava = new User();
                            if (bundle == null || bundle.isEmpty()) {
                                User converJsonJava = gson.fromJson(respon, User.class);
                                int g = converJsonJava.getProfile().getGroup();
                                id_typeother = g;
                                etEmail.setText(converJsonJava.getEmail());

                                if (converJsonJava.getProfile().getTelephone() != null) {
                                    user_phone = converJsonJava.getProfile().getTelephone();
                                    String[] splitPhone = user_phone.split(",");
                                    if (splitPhone.length == 1) {
                                        etPhone1.setText(String.valueOf(splitPhone[0]));
                                    }
                                    else if (splitPhone.length == 2){
                                        etPhone1.setText(String.valueOf(splitPhone[0]));
                                        etPhone2.setText(String.valueOf(splitPhone[1]));
                                    } else if (splitPhone.length == 3) {
                                        etPhone1.setText(String.valueOf(splitPhone[0]));
                                        etPhone2.setText(String.valueOf(splitPhone[1]));
                                        etPhone3.setText(String.valueOf(splitPhone[2]));
                                    }
                                }
                                user_address = converJsonJava.getProfile().getAddress();
                                if(user_address.isEmpty()){

                                }else {
//                                    String[] splitAddr = user_address.split(",");
//                                    latitude = Double.valueOf(splitAddr[0]);
//                                    longtitude = Double.valueOf(splitAddr[1]);
//                                    get_location(false);

//                                    if (converJsonJava.getProfile().getResponsible_officer()!=null){
//                                        user_address_name = converJsonJava.getProfile().getResponsible_officer();
//                                        tvAddress.setQuery(user_address_name,false);
//                                    }
//                                    mapFragment.getMapAsync(Camera.this::onMapReady);

                                }
                                //dealer shop section
                                if (g == 3) {
//                                        txtOther_main.setVisibility(View.VISIBLE);
//                                        relative_othermain.setVisibility(View.VISIBLE);

                                    /* dealer shop */
                                    //CommonFunction.initialDealer(converJsonJava.getShops());

                                    List<String> shopname = new ArrayList<>();
                                    if (converJsonJava.getShops().size() > 0) {

                                        shopListItems=new String[converJsonJava.getShops().size()];
                                        shopIdListItems=new int[converJsonJava.getShops().size()];

                                        for(int i=0;i<converJsonJava.getShops().size();i++){
                                            ShopViewModel shopViewModel = converJsonJava.getShops().get(i);
                                            shopListItems[i]=shopViewModel.getShop_name();
                                            shopIdListItems[i]=shopViewModel.getId();
                                        }

                                    }

                                    Log.e("TAG",shopListItems.toString());
                                }
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
    }

    private void PostData(String encode) {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "";
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        JSONObject sale = new JSONObject();
        String con = tvCondition.getText().toString().toLowerCase();
        try {

            post.put("category", category);
            post.put("status", 3);
            if (con.equals("used") || con.equals("ប្រើប្រាស់រួច")){
                condition1 = "used";
            }else if (con.equals("new") || con.equals("ថ្មី")){
                condition1 = "new";
            }
            post.put("condition", condition1);
            if (category == 2){
                post.put("type", 3);
                post.put("used_eta1", seekbar_whole.getProgress());
                post.put("used_eta2", seekbar_rear.getProgress());
                post.put("used_eta3", seekbar_screw.getProgress());
                post.put("used_eta4", seekbar_pumps.getProgress());
                post.put("used_machine1", seekbar_rigt_engine.getProgress());
                post.put("used_machine2", seekbar_engine_head.getProgress());
                post.put("used_machine3", seekbar_assmebly.getProgress());
                post.put("used_machine4", seekbar_console.getProgress());
                post.put("used_other1", seekbar_accessories.getProgress());
            }else {
                post.put("type", type);
                post.put("used_eta1", 0);
                post.put("used_eta2",0);
                post.put("used_eta3", 0);
                post.put("used_eta4", 0);
                post.put("used_machine1", 0);
                post.put("used_machine2", 0);
                post.put("used_machine3", 0);
                post.put("used_machine4", 0);
                post.put("used_other1", 0);
            }

            post.put("user",pk );

            strPostType = tvPostType.getText().toString().toLowerCase();
            Log.e("post type",""+strPostType);
            if (strPostType == "buy") {
                post.put("discount", "0");
                post.put("discount_type","percent");
// set default image for Buy when no select image by samang 5/9/19
                default_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_121);
                if (bitmapImage1 == null) {
                    post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                }
                if (bitmapImage2 == null) {
                    post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage2)));
                }
                if (bitmapImage3 == null) {
                    post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage3)));
                }
                if (bitmapImage4 == null) {
                    post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage4)));
                }
                if (bitmapImage5 == null) {
                    post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage5)));
                }
                if (bitmapImage6 == null) {
                    post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage6)));
                }

            }else {
                post.put("discount_type", null);
                post.put("discount", String.valueOf(seekbar_pri_per.getProgress()));

                if (bitmapImage1 == null) {
                    post.put("front_image_path", "");
                    post.put("front_image_base64", "");
                } else {
                    post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                    post.put("front_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                }
                if (bitmapImage2 == null) {
                    post.put("right_image_path", "");
                    post.put("right_image_base64", "");
                } else {
                    post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage2)));
                    post.put("right_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage2)));
                }
                if (bitmapImage3 == null) {
                    post.put("left_image_path", "");
                    post.put("left_image_base64", "");
                } else {
                    post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage3)));
                    post.put("left_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage3)));
                }
                if (bitmapImage4 == null) {
                    post.put("back_image_path", "");
                    post.put("back_image_base64", "");
                } else {
                    post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage4)));
                    post.put("back_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage4)));
                }
                // add 2 image by samang 26/08
                if (bitmapImage5 == null) {
                    post.put("extra_image1", null);
                    post.put("extra_image1_base64", "");
                } else {
                    post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage5)));
                    post.put("extra_image1_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage5)));
                }
                if (bitmapImage6 == null) {
                    post.put("extra_image2", null);
                    post.put("extra_image2_base64", "");
                } else {
                    post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage6)));
                    post.put("extra_image2_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage6)));
                }
            }
            //end add image
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
            post.put("modeling",model);
            post.put("description", etDescription.getText().toString());
            post.put("cost",etPrice.getText().toString());
            if (strPostType.equals("sell") || strPostType.equals("លក់")){
                type_post = "sell";
            }else if (strPostType.equals("rent") || strPostType.equals("ជួល")){
                type_post = "rent";
            }
            post.put("post_type",type_post);
//check empty field user for detail by samang 28/08
            if (etAddress.getText().toString().isEmpty() || etAddress.getText().toString() == null){
                post.put("vin_code", user_address_name);
            }else {
                post.put("vin_code", etAddress.getText().toString());
            }
            if (etPhone1.getText().toString().isEmpty() || etPhone1.getText().toString() == null){
                post.put("contact_phone",user_phone);
            }else {
                post.put("contact_phone", etPhone1.getText().toString()+","+etPhone2.getText().toString()+","+etPhone3.getText().toString());
            }
            if (etEmail.getText().toString().isEmpty() || etEmail.getText().toString() == null){
                post.put("contact_email", user_email);
            }else {
                post.put("contact_email", etEmail.getText().toString());
            }

            post.put("contact_address", latlng);
// end check

            //Log.e("color",""+strColor);
            post.put("color", "black");
            post.put("multi_color_code",strColor);

            //Log.e("item",""+year+category+model+brand+type);

            //enhance sep 19 2019
            post.put("post_code", show_ID.getText().toString());
            post.put("post_sub_title",CommonFunction.generatePostSubTitle(brand,model,year,strColor,strColorKH));
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

            Log.d(TAG,"Post Colorr " +strColor);

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
                    Log.d(TAG, "Post TTTT" + respon);
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
                                            int pID = obj.getInt("id");
                                            String pTitle=obj.getString("title");
                                            String pType=obj.getString("post_type");
                                            String pCoverURL=obj.getString("front_image_path");
                                            String price=obj.getString("cost");
                                            String dicountPrice=obj.getString("discount");
                                            String dicountType=obj.getString("discount_type");
                                            String location=obj.getString("contact_address");
                                            String createdAt=obj.getString("created");
                                            String postCode=obj.getString("post_code");
                                            String postSubTitle=obj.getString("post_sub_title");
                                            //String fcolor=obj.getString("color");
                                            String fcolor=obj.getString("multi_color_code");
//                                            String fcolor=obj.getString("color");
                                            String multicolor=obj.getString("multi_color_code");
                                            String eta1 = obj.getString("used_eta1");
                                            String eta2 = obj.getString("used_eta2");
                                            String eta3 = obj.getString("used_eta3");
                                            String eta4 = obj.getString("used_eta4");
                                            String machine1 = obj.getString("used_machine1");
                                            String machine2 = obj.getString("used_machine2");
                                            String machine3 = obj.getString("used_machine3");
                                            String machine4 = obj.getString("used_machine4");
                                            String other1 = obj.getString("used_other1");
                                            int fcategory=obj.getInt("category");
                                            int pStatus=obj.getInt("status");
                                            if(shopId>0){
                                                Log.e("TAH","RUN submit shop post.");
                                                String urlShopPost=ConsumeAPI.BASE_URL+"api/v1/postdealershop/";
                                                OkHttpClient clientPost=new OkHttpClient();
                                                JSONObject shopPostObj=new JSONObject();
                                                try{
                                                    shopPostObj.put("post",pID);
                                                    shopPostObj.put("shop",shopId);
                                                    shopPostObj.put("record_status",1);
                                                    RequestBody bodyShopPost=RequestBody.create(ConsumeAPI.MEDIA_TYPE,shopPostObj.toString());
                                                    String auth = "Basic " + encode;
                                                    Request request1 = new Request.Builder()
                                                            .url(urlShopPost)
                                                            .post(bodyShopPost)
                                                            .header("Accept","application/json")
                                                            .header("Content-Type","application/json")
                                                            .header("Authorization",auth)
                                                            .build();
                                                    clientPost.newCall(request1).enqueue(new Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {
                                                            String message = e.getMessage().toString();
                                                            Log.d("failure Response",message);
                                                            //mProgress.dismiss();
                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            String message = response.body().string();
                                                            Log.d(TAG,"Submit Shop for Post "+ message);
                                                        }
                                                    });
                                                }catch (JSONException je)
                                                {
                                                    je.printStackTrace();
                                                }
                                            }

                                            FBPostCommonFunction.SubmitPost(String.valueOf(pID),String.valueOf(fcategory),pType,pCoverURL,price,dicountPrice,dicountType,location,createdAt,pStatus,pk,postSubTitle,postCode,fcolor);
                                            //added on Feb 05 2020 For Dealer Post Product


                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setCancelable(false);
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.post_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mProgress.dismiss();
                                                        Intent intent = new Intent(Camera.this,Home.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
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
                                        alertDialog.setCancelable(false);
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
                                alertDialog.setCancelable(false);
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
                            alertDialog.setCancelable(false);
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
                    alertDialog.setCancelable(false);
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
        String con = tvCondition.getText().toString().toLowerCase();
        if(model==0)
            model=mmodel;
        try {
            if (con.equals("used") || con.equals("ប្រើប្រាស់រួច")){
                condition1 = "used";
            }else if (con.equals("new") || con.equals("ថ្មី")){
                condition1 = "new";
            }
            if (category == 2){
                post.put("type", 3);
                post.put("used_eta1", seekbar_whole.getProgress());
                post.put("used_eta2", seekbar_rear.getProgress());
                post.put("used_eta3", seekbar_screw.getProgress());
                post.put("used_eta4", seekbar_pumps.getProgress());
                post.put("used_machine1", seekbar_rigt_engine.getProgress());
                post.put("used_machine2", seekbar_engine_head.getProgress());
                post.put("used_machine3", seekbar_assmebly.getProgress());
                post.put("used_machine4", seekbar_console.getProgress());
                post.put("used_other1", seekbar_accessories.getProgress());
            }else {
                post.put("type", type);
                post.put("used_eta1", 0);
                post.put("used_eta2",0);
                post.put("used_eta3", 0);
                post.put("used_eta4", 0);
                post.put("used_machine1", 0);
                post.put("used_machine2", 0);
                post.put("used_machine3", 0);
                post.put("used_machine4", 0);
                post.put("used_other1", 0);
            }

            post.put("condition",condition1);
            post.put("category", category );
            post.put("status", 3);


            strPostType = tvPostType.getText().toString().toLowerCase();
            Log.e("post type",":"+strPostType);
            if (strPostType.equals("buy")) {
                post.put("discount", "0");
                post.put("discount_type","percent");
            }else {
                post.put("discount_type", null);
                post.put("discount", String.valueOf(seekbar_pri_per.getProgress()));
            }
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
// add 2 image by samang 26/08
            if(bitmapImage5==null){
                post.put("extra_image1", null);
//                post.put("other_image_base64", "");
            }else{
                post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage5)));
//                post.put("other_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage5)));
            }
            if(bitmapImage6==null){
                post.put("extra_image2", null);
//                post.put("others_image_base64", "");
            }else{
                post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage6)));
//                post.put("others_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage6)));
            }
            // end add image
            //Instant.now().toString()
            //post.put("created", "");
            post.put("created_by", pk);
            if (android.os.Build.VERSION.SDK_INT >= 26){
                post.put("modified", Instant.now().toString());
            } else{
//                fix DateTime with sdk < 26 by samang (26/08/2019)
//                  String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String currentDateTimeString = formatter.format(date);
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
            post.put("description", etDescription.getText().toString());
            post.put("cost",etPrice.getText().toString());
            if (strPostType.equals("sell") || strPostType.equals("លក់")){
                type_post = "sell";
            }else if (strPostType.equals("rent") || strPostType.equals("ជូល")){
                type_post = "rent";
            }
            post.put("post_type",type_post);
            post.put("vin_code", etAddress.getText().toString());
//check empty field user for detail by samang 28/08
            if (etAddress.getText().toString().isEmpty() ||etAddress.getText().toString() == null){
                post.put("vin_code", edit_address_name);
            }else {
                post.put("vin_code", etAddress.getText().toString());
            }
            if (etPhone1.getText().toString().isEmpty() || etPhone1.getText().toString() == null){
                post.put("contact_phone",edit_phone);
            }else {
                post.put("contact_phone", etPhone1.getText().toString() + "," + etPhone2.getText().toString() + "," + etPhone3.getText().toString());
            }
            if (etEmail.getText().toString().isEmpty() || etEmail.getText().toString() ==null){
                post.put("contact_email", edit_email);
            }else {
                post.put("contact_email", etEmail.getText().toString());
            }

            post.put("contact_address", latlng);
            //end check
            Log.e("color",strColor);
            post.put("color", "black");
            post.put("multi_color_code",strColor);
            post.put("post_sub_title",CommonFunction.generatePostSubTitle(brand,model,year,strColor,strColorKH));

            switch (strPostType){
                case "លក់":
                case "sell":
                    url=ConsumeAPI.BASE_URL+"postsale/"+edit_id+"/";
                    //Log.d("URL","URL"+url);
                    sale.put("sale_status", 3);
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

//                case "ទិញ":
//                case "buy":
//                    url = ConsumeAPI.BASE_URL+"api/v1/postbuys/"+ edit_id + "/";
//                    JSONObject buy=new JSONObject();
//                    buy.put("buy_status",3);
//                    buy.put("record_status",1);
//                    post.put("buy_post",new JSONArray("["+buy+"]"));
//                    break;
            }

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
                                            String postSubTitle=obj.getString("post_sub_title");

                                            //String fcolor=obj.getString("color");
                                            String fcolor=obj.getString("multi_color_code");
                                            String eta1 = obj.getString("used_eta1");
                                            String eta2 = obj.getString("used_eta2");
                                            String eta3 = obj.getString("used_eta3");
                                            String eta4 = obj.getString("used_eta4");
                                            String machine1 = obj.getString("used_machine1");
                                            String machine2 = obj.getString("used_machine2");
                                            String machine3 = obj.getString("used_machine3");
                                            String machine4 = obj.getString("used_machine4");
                                            String other1 = obj.getString("used_other1");
                                            String fcategory=String.valueOf(obj.getInt("category"));
                                            FBPostCommonFunction.modifiedPost(String.valueOf(pID), fcategory, pCoverURL, price, dicountPrice, dicountType, location, createdAt,postSubTitle,eta1,eta2,eta3,eta4,machine1,machine2,machine3,machine4,other1,fcolor);

                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setCancelable(false);
                                        alertDialog.setMessage(getString(R.string.waiting_approval));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mProgress.dismiss();
                                                        Intent intent = new Intent(Camera.this,Account.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
//                                                        finish();
//                                                        dialog.dismiss();
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
                                        alertDialog.setCancelable(false);
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
                                alertDialog.setCancelable(false);
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
                            alertDialog.setCancelable(false);
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
                    alertDialog.setCancelable(false);
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
                                tvType_cate.setText(typenamekh);
                            }else if (language.equals("en")){
                                tvType_cate.setText(typename);
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
                                tvModel.setText(namekh);
                                getBrandName(Encode,brand);
                            }else if (language.equals("en")){
                                brand=brandId;
                                tvModel.setText(name);
                                getBrandName(Encode,brand);
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
    private void setColor(int[] index){
        Log.e("Index",""+index.length);
        for (int i=0;i<index.length;i++) {
            gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
            final ColorAdapter adapter = new ColorAdapter(FunctionColor.itemcolor, getApplication());
            gridView.setAdapter(adapter);
            if (index.length == 2) {
                adapter.selectedPositions.add(index[0]);
                adapter.selectedPositions.add(index[1]);
                gridView.setOnItemClickListener((parent, v, position, id) -> {
                    selectedIndex = adapter.selectedPositions.indexOf(position);
                    if (selectedIndex > - 1) {
                        adapter.selectedPositions.remove(selectedIndex);
                        ((CustomView)v).display(false);
                        selectedColor.remove((Integer) parent.getItemAtPosition(position));
                    } else {
                        adapter.selectedPositions.add(position);
                        ((CustomView)v).display(true);
                        selectedColor.add((Integer) parent.getItemAtPosition(position));
                    }
                    if (selectedColor.size() > 2 ){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setMessage(Camera.this.getString(R.string.select_color));
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                (dialog, which) -> {
                                    selectedIndex = selectedColor.size()-1;
                                    if (selectedIndex > -1 ) {
                                        adapter.selectedPositions.remove(selectedIndex);
                                    }
                                    ((CustomView)v).display(false);
                                    selectedColor.remove((Integer) parent.getItemAtPosition(position));
                                    dialog.dismiss();
                                });
                        alertDialog.show();
                    }
                    arrayColor = new ArrayList<>();
                    if (selectedColor.size() > 0) {
                        for (int j = 0; j < selectedColor.size(); j++) {
                            if (j < selectedColor.size() - 1) {
                                color = String.valueOf(selectedColor.get(j));
                                Log.e("Select", "" + color);
                            } else {
                                color = String.valueOf(selectedColor.get(j));
                                Log.e("Select", "" + color);
                            }
                            arrayColor.add(color);
                        }
                    }
                    strColor = FunctionColor.setColor(arrayColor);
                    Log.e("color select:",""+strColor);
                });
            }else {
                adapter.selectedPositions.add(index[0]);
                gridView.setOnItemClickListener((parent, v, position, id) -> {
                    selectedIndex = adapter.selectedPositions.indexOf(position);
                    if (selectedIndex > - 1) {
                        adapter.selectedPositions.remove(selectedIndex);
                        ((CustomView)v).display(false);
                        selectedColor.remove((Integer) parent.getItemAtPosition(position));
                    } else {
                        adapter.selectedPositions.add(position);
                        ((CustomView)v).display(true);
                        selectedColor.add((Integer) parent.getItemAtPosition(position));
                    }
                    if (selectedColor.size() > 2 ){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setMessage(Camera.this.getString(R.string.select_color));
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                (dialog, which) -> {
                                    selectedIndex = selectedColor.size()-1;
                                    if (selectedIndex > -1 ) {
                                        adapter.selectedPositions.remove(selectedIndex);
                                    }
                                    ((CustomView)v).display(false);
                                    selectedColor.remove((Integer) parent.getItemAtPosition(position));
                                    dialog.dismiss();
                                });
                        alertDialog.show();
                    }
                    arrayColor = new ArrayList<>();
                    if (selectedColor.size() > 0) {
                        for (int j = 0; j < selectedColor.size(); j++) {
                            if (j < selectedColor.size() - 1) {
                                color = String.valueOf(selectedColor.get(j));
                            } else {
                                color = String.valueOf(selectedColor.get(j));
                            }
                            arrayColor.add(color);
                        }
                    }
                    strColor = FunctionColor.setColor(arrayColor);
                    Log.e("color select:",""+strColor);
                });
            }
        }
        selectedColor = FunctionColor.getItemColor(strColor);
    }
    private void DropDown() {

        selectedColor = new ArrayList<>();
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        final ColorAdapter adapter = new ColorAdapter(FunctionColor.itemcolor,getApplication());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, v, position, id) -> {
            selectedIndex = adapter.selectedPositions.indexOf(position);
            if (selectedIndex > - 1) {
                adapter.selectedPositions.remove(selectedIndex);
                ((CustomView)v).display(false);
                selectedColor.remove((Integer) parent.getItemAtPosition(position));
            } else {
                adapter.selectedPositions.add(position);
                ((CustomView)v).display(true);
                selectedColor.add((Integer) parent.getItemAtPosition(position));
            }
            Log.e("select index",""+selectedIndex);
            if (selectedColor.size() > 2 ){
                AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setMessage(Camera.this.getString(R.string.select_color));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                        (dialog, which) -> {
                            selectedIndex = selectedColor.size()-1;
                            if (selectedIndex > -1 ) {
                                adapter.selectedPositions.remove(selectedIndex);
                            }
                            ((CustomView)v).display(false);
                            selectedColor.remove((Integer) parent.getItemAtPosition(position));
                            dialog.dismiss();
                        });
                alertDialog.show();
            }
            arrayColor = new ArrayList<>();
            if (selectedColor.size() > 0) {
                for (int i = 0; i < selectedColor.size(); i++) {
                    if (i < selectedColor.size() - 1) {
                        color = String.valueOf(selectedColor.get(i));
                        Log.e("Select",""+color);
                    } else {
                        color = String.valueOf(selectedColor.get(i));
                        Log.e("Select",""+color);
                    }
                    arrayColor.add(color);
                }
            }
            Log.e("list color",""+arrayColor.size());
            strColor = FunctionColor.setColor(arrayColor);
            Log.e("color select:",""+strColor);
        });

        imgAdd_color.setVisibility(View.VISIBLE);
        imgAdd_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_color.setBackground(getDrawable(R.color.light_gray));
                layout_color.setVisibility(View.VISIBLE);
                cancel_color.setVisibility(View.VISIBLE);
                imgAdd_color.setVisibility(View.GONE);
            }
        });
        cancel_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_color.setVisibility(View.GONE);
                imgAdd_color.setVisibility(View.VISIBLE);
                cancel_color.setVisibility(View.GONE);
            }
        });

        tvCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomCondition(view);
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomYear(view);
            }
        });
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomCategory(view);
            }
        });
        tvType_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomTypeCate(view);
            }
        });

        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomBrand(view);
            }
        });

        tvModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomModel(view);
            }
        });
        tvPostType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomType(view);
            }
        });
        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(Camera.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                }else {
                    Intent intent = new Intent(Camera.this, FragmentMap.class);
                    intent.putExtra("post","post");
                    intent.putExtra("name_post",etName.getText().toString());
                    intent.putExtra("process_type",process_type);
                    intent.putExtra("price",etPrice.getText().toString());
                    intent.putExtra("post_type",tvPostType.getText().toString());
                    intent.putExtra("category",tvCategory.getText().toString());
                    intent.putExtra("brand",tvBrand.getText().toString());
                    intent.putExtra("model",tvModel.getText().toString());
                    intent.putExtra("year",tvYear.getText().toString());
                    intent.putExtra("condition",tvCondition.getText().toString());
                    intent.putExtra("description",etDescription.getText().toString());
                    intent.putExtra("email_post",etEmail.getText().toString());
                    intent.putExtra("address_post",etAddress.getText().toString());
                    intent.putExtra("phone_number1_post",etPhone1.getText().toString());
                    intent.putExtra("phone_number2_post",etPhone2.getText().toString());
                    intent.putExtra("phone_number3_post",etPhone3.getText().toString());
                    intent.putExtra("discount_percent",seekbar_price);
                    intent.putExtra("discount_amount",etDiscount_amount.getText().toString());
                    intent.putExtra("whole_ink",whole_ink);
                    intent.putExtra("rear",seekbar_rearr);
                    intent.putExtra("screw",seekbar_screww);
                    intent.putExtra("pumps",seekbar_pump);
                    intent.putExtra("right_engine",seekbar_engine);
                    intent.putExtra("engine_head",seekbar_head);
                    intent.putExtra("assembly",assembly);
                    intent.putExtra("console",seekbar_consolee);
                    intent.putExtra("accessories",seekbar_accessorie);
                    intent.putExtra("category_post",category);
                    intent.putExtra("cat",tvType_cate.getText().toString());
                    intent.putExtra("color",strColor);
                    intent.putExtra("image1",image1);
                    intent.putExtra("image2",image2);
                    intent.putExtra("image3",image3);
                    intent.putExtra("image4",image4);
                    intent.putExtra("image5",image5);
                    intent.putExtra("image6",image6);
                    intent.putExtra("modelID",model);
                    intent.putExtra("brandID",brand);
                    intent.putExtra("yearID",year);
                    intent.putExtra("categoryID",category);
                    intent.putExtra("typeID",type);
                    Log.e("color",""+color);
                    startActivity(intent);
                }
            }
        });
    }

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),Base64.NO_WRAP);
    }

    private void Variable_Field() {

        tvPostType        = (EditText) findViewById(R.id.tvPostType);
        tvCategory        = (EditText) findViewById(R.id.tvCategory);
        tvBrand           = (EditText) findViewById(R.id.tvBrand);
        tvModel           = (EditText) findViewById(R.id.tvModel);
        tvYear            = (EditText) findViewById(R.id.tvYears);
        tvCondition       = (EditText) findViewById(R.id.tvCondition);
        tvType_cate       = (EditText) findViewById(R.id.tvType);
        etName            = (EditText) findViewById(R.id.etName);
        etMap             = (EditText) findViewById(R.id.et_map);
        etAddress         = (EditText) findViewById(R.id.et_address);
        etDescription     = (EditText) findViewById(R.id.etDescription );
        etPrice           = (EditText) findViewById(R.id.etPrice );
        etDiscount_amount = (EditText) findViewById(R.id.etDisAmount );
        etPhone1          = (EditText) findViewById(R.id.etphone1 );
        etPhone2          = (EditText) findViewById(R.id.etphone2);
        etPhone3          = (EditText) findViewById(R.id.etphone3);
        etEmail           = (EditText) findViewById(R.id.etEmail );

        btremove_pic1    = (ImageButton) findViewById(R.id.remove_pic1);
        btremove_pic2    = (ImageButton) findViewById(R.id.remove_pic2);
        btremove_pic3    = (ImageButton) findViewById(R.id.remove_pic3);
        btremove_pic4    = (ImageButton) findViewById(R.id.remove_pic4);
        btremove_pic5    = (ImageButton) findViewById(R.id.remove_pic5);
        btremove_pic6    = (ImageButton) findViewById(R.id.remove_pic6);

        name_alert        = (TextView) findViewById(R.id.name_alert);
        pumps_alert       = (TextView) findViewById(R.id.pumps_alert);
        accessories_alert = (TextView) findViewById(R.id.accessories_alert);
        whole_alert       = (TextView) findViewById(R.id.whole_alert);
        assembly_alert    = (TextView) findViewById(R.id.assembly_alert);
        right_engine_alert= (TextView) findViewById(R.id.right_engine_alert);
        engine_head_alert = (TextView) findViewById(R.id.engine_head_alert);
        screw_alert       = (TextView) findViewById(R.id.screw_alert);
        rear_alert        = (TextView) findViewById(R.id.rear_alert);
        console_alert     = (TextView) findViewById(R.id.console_alert);
        tv_add            = (TextView) findViewById(R.id.tv_add);
        tv_add1           = (TextView) findViewById(R.id.tv_add1);
        tv_cancel         = (TextView) findViewById(R.id.tv_cancel);
        tvType_elec       = (TextView) findViewById(R.id.title_cate);
        tv_name           = (TextView) findViewById(R.id.title_name);
        show_ID           = (TextView) findViewById(R.id.show_id);
        title_dicount     = (TextView) findViewById(R.id.title_dis_pri);
        tvConsole         = (TextView) findViewById(R.id.edConsole);
        tvAssecssorie     = (TextView) findViewById(R.id.edAccessories);
        tvWhole_int       = (TextView) findViewById(R.id.edwhole_int);
        tvAssembly        = (TextView) findViewById(R.id.edMachine_Assembly);
        tvRight_engine    = (TextView) findViewById(R.id.edLeft_and_right_engine_counter);
        tvEngine_head     = (TextView) findViewById(R.id.edEngine_head);
        tvPumps           = (TextView) findViewById(R.id.edFront_and_rear_pumps);
        tvRear            = (TextView) findViewById(R.id.edfront_and_rear_wheel_sets);
        tvScrew           = (TextView) findViewById(R.id.edThe_whole_screw);
        price_alert       = (TextView) findViewById(R.id.price_alert);
        dis_price_alert   = (TextView) findViewById(R.id.dis_price_alert);
        post_type_alert   = (TextView) findViewById(R.id.post_type_alert);
        type_alert        = (TextView) findViewById(R.id.type_alert);
        category_alert    = (TextView) findViewById(R.id.category_alert);
        brand_alert       = (TextView) findViewById(R.id.brand_alert);
        model_alert       = (TextView) findViewById(R.id.model_alert);
        year_alert        = (TextView) findViewById(R.id.year_alert);
        conditon_alert    = (TextView) findViewById(R.id.condition_alert);
        info_alert        = (TextView) findViewById(R.id.info_alert);
        phone_alert       = (TextView) findViewById(R.id.phone_alert);
        map_alert         = (TextView) findViewById(R.id.map_alert);
        address_alert     = (TextView) findViewById(R.id.address_alert);
        email_alert       = (TextView) findViewById(R.id.email_alert);

        bt_update         = (Button) findViewById(R.id.btnupdate);
        submit_post       = (Button) findViewById(R.id.btnSubmitPost);

        seekbar_whole       = (SeekBar) findViewById(R.id.seekbar_whole);
        seekbar_pri_per     = (SeekBar) findViewById(R.id.seekbar_pri_per);
        seekbar_rear        = (SeekBar) findViewById(R.id.seekbar_rear);
        seekbar_screw       = (SeekBar) findViewById(R.id.seekbar_screw);
        seekbar_rigt_engine = (SeekBar) findViewById(R.id.seekbar_right_engine);
        seekbar_pumps       = (SeekBar) findViewById(R.id.seekbar_pumps);
        seekbar_assmebly    = (SeekBar) findViewById(R.id.seekbar_assembly);
        seekbar_accessories = (SeekBar) findViewById(R.id.seekbar_accessories);
        seekbar_console     = (SeekBar) findViewById(R.id.seekbar_console);
        seekbar_engine_head = (SeekBar) findViewById(R.id.seekbar_engine_head);

        layout_color      = (RelativeLayout) findViewById(R.id.add_color);
        layout_phone1     = (RelativeLayout) findViewById(R.id.layout_phone1);
        layout_phone2     = (RelativeLayout) findViewById(R.id.layout_phone2);
        layout_estimate   = (LinearLayout)   findViewById(R.id.layout_estimate);

        cancel_color = (ImageView) findViewById(R.id.cancel_color);
        imgAdd_color = (ImageView) findViewById(R.id.add_more_color);
        imageView1   = (ImageView) findViewById(R.id.Picture1);
        imageView2   = (ImageView) findViewById(R.id.Picture2);
        imageView3   = (ImageView) findViewById(R.id.Picture3);
        imageView4   = (ImageView) findViewById(R.id.Picture4);
        imageView5   = (ImageView) findViewById(R.id.Picture5);
        imageView6   = (ImageView) findViewById(R.id.Picture6);
        imageMap     = (ImageView) findViewById(R.id.map);

        gridView     = (GridView) findViewById(R.id.gridview);

    }

    private void selectImage() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(Camera.this);
        View sheetview = getLayoutInflater().inflate(R.layout.choose_image,null,false);
        bottomSheet.setCanceledOnTouchOutside(false);
        bottomSheet.setContentView(sheetview);
        bottomSheet.show();
        TextView tv_camera = sheetview.findViewById(R.id.tv_camera);
        TextView tv_gallery = sheetview.findViewById(R.id.tv_gallery);
        Button bt_cancel = sheetview.findViewById(R.id.btn_cancel);
        TextView tv_clear = sheetview.findViewById(R.id.bt_clear);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission(true);
                bottomSheet.dismiss();
            }
        });
        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission(false);
                bottomSheet.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.dismiss();
            }
        });
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.dismiss();
            }
        });

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
                            Log.e("TAG","It's allow permiision");
//                            if (isCamera) {
//                                dispatchTakePictureIntent();
////                                dispatchTakePictureIntentdealer();
//                            } else {
//                                dispatchGalleryIntent();
//                            }
                        }else {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //showSettingsDialog();
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
// add 2 image by samang 26/08
            case REQUEST_TAKE_PHOTO_5:
                Toast toast4= Toast.makeText(getApplicationContext(),"ផ្នែកផ្សេងទៀត",Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group4 = (ViewGroup) toast4.getView();
                TextView messageTextView4 = (TextView) group4.getChildAt(0);
                messageTextView4.setTextSize(25);
                toast4.show();
                break;

            case REQUEST_TAKE_PHOTO_6:
                Toast toast5= Toast.makeText(getApplicationContext(),"ផ្នែកផ្សេងទៀត",Toast.LENGTH_SHORT);
                toast5.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group5 = (ViewGroup) toast5.getView();
                TextView messageTextView5 = (TextView) group5.getChildAt(0);
                messageTextView5.setTextSize(25);
                toast5.show();
                break;
            // end
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
                image1 = Uri.fromFile(mPhotoFile);
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView1);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_2;
                btremove_pic1.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_2) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage2= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image2 = Uri.fromFile(mPhotoFile);
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView2);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_3;
                btremove_pic2.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_3) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage3= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image3 = Uri.fromFile(mPhotoFile);
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView3);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_4;
                btremove_pic3.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_4) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage4= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image4 = Uri.fromFile(mPhotoFile);
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView4);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_5;
                btremove_pic4.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            // add 2 image by samang 26/08
            else if (requestCode == REQUEST_TAKE_PHOTO_5){
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage5= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image5 = Uri.fromFile(mPhotoFile);
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView5);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_6;
                btremove_pic5.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_6){
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage6= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image6 = Uri.fromFile(mPhotoFile);
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView6);
                btremove_pic6.setVisibility(View.VISIBLE);
            }
//
            else if (requestCode == REQUEST_GALLERY_PHOTO_1) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage1=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image1 = data.getData();
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView1);
                btremove_pic1.setVisibility(View.VISIBLE);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_2) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage2=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image2 = data.getData();
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView2);
                btremove_pic2.setVisibility(View.VISIBLE);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_3) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage3=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image3 = data.getData();
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView3);
                btremove_pic3.setVisibility(View.VISIBLE);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_4) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage4=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image4 = data.getData();
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView4);
                btremove_pic4.setVisibility(View.VISIBLE);

            }
            // add 2 image by samang
            else if (requestCode == REQUEST_GALLERY_PHOTO_5){
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage5=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image5 = data.getData();
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView5);
                btremove_pic5.setVisibility(View.VISIBLE);

            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_6){
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage6=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image6 = data.getData();
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView6);
                btremove_pic6.setVisibility(View.VISIBLE);
            }
            // end
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
        builder.setCancelable(false);
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
    private void mSeekbar(){
        seekbar_pri_per.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_price = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.ds_pri_percent)+"(%d%%)", seekbar_price);
                title_dicount.setText(title);
                try {
                    double str_dis = 0, price1 = 0;
                    String cost = "";
                    cost = etPrice.getText().toString();
                    price1 = Double.valueOf(cost);
                    str_dis = (price1 * seekbar_price) / 100;
                    etDiscount_amount.setText(String.valueOf(str_dis));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_whole.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                whole_ink = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.whole_int)+"(%d%%)", whole_ink);
                tvWhole_int.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_rear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_rearr = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.Front_and_rear_pumps)+"(%d%%)", seekbar_rearr);
                tvRear.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_screw.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_screww = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.The_whole_screw)+"(%d%%)", seekbar_screww);
                tvScrew.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_rigt_engine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_engine = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.Left_and_right_engine_counter)+"(%d%%)", seekbar_engine);
                tvRight_engine.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_engine_head.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_head = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.Engine_head)+"(%d%%)", seekbar_head);
                tvEngine_head.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_pumps.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_pump = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.Front_and_rear_pumps)+"(%d%%)", seekbar_pump);
                tvPumps.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_console.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_consolee = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.Console)+"(%d%%)", seekbar_consolee);
                tvConsole.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_accessories.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_accessorie = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.Accessories)+"(%d%%)", seekbar_accessorie);
                tvAssecssorie.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbar_assmebly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                assembly = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format(getString(R.string.Machine_Assembly)+"(%d%%)", assembly);
                tvAssembly.setText(title);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void showBottomCondition(View view) {
        BottomChooseCondition addPhotoBottomDialogFragment = BottomChooseCondition.newInstance();
        addPhotoBottomDialogFragment.setCancelable(false);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseCondition.TAG);
    }
    public void showBottomYear(View view) {
        BottomChooseYear addPhotoBottomDialogFragment = BottomChooseYear.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseYear.TAG);
    }
    public void showBottomBrand(View view) {
        BottomChooseBrand addPhotoBottomDialogFragment = BottomChooseBrand.newInstance(category);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseBrand.TAG);
    }
    public void showBottomModel(View view) {
        BottomChooseModel addPhotoBottomDialogFragment = BottomChooseModel.newInstance(brand);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseModel.TAG);
    }
    public void showBottomCategory(View view) {
        BottomChooseCategory addPhotoBottomDialogFragment = BottomChooseCategory.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseCategory.TAG);
    }
    public void showBottomType(View view) {
        BottomChooseType addPhotoBottomDialogFragment = BottomChooseType.newInstance();
        addPhotoBottomDialogFragment.setCancelable(false);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseType.TAG);
    }
    public void showBottomTypeCate(View view) {
        BottomChooseTypeCate addPhotoBottomDialogFragment = BottomChooseTypeCate.newInstance();
        addPhotoBottomDialogFragment.setCancelable(false);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseTypeCate.TAG);
    }

    @Override
    public void onItemClick(String item) {
        tvCondition.setText(item);
        if (category == 2){
            if (item.equals("Used") || item.equals("ប្រើប្រាស់រួច")) {
                layout_estimate.setVisibility(View.VISIBLE);
            }else {
                layout_estimate.setVisibility(View.GONE);
            }
        }else {
            layout_estimate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickItem(String item) {
        tvYear.setText(item);
    }

    @Override
    public void AddIDyear(int id) {
        year = id;
        Log.e("year",""+year);
    }

    @Override
    public void onItem(String item) {
        tvBrand.setText(item);
    }

    @Override
    public void onItemTypeCate(String item) {
        tvType_cate.setText(item);
    }

    @Override
    public void AddIdTypeCate(int id) {
        type = id;
        Log.e("type",""+type);
    }

    @Override
    public void AddID(int id) {
       brand = id;
       Log.e("brand",""+brand);
    }

    @Override
    public void onClickItemModel(String item) {
        tvModel.setText(item);
    }

    @Override
    public void AddModelID(int id) {
        model = id;
        Log.e("model",""+model);
    }

    @Override
    public void onCLickItemType(String item) {
        tvCategory.setText(item);
    }

    @Override
    public void AddIdCategory(int id) {
        category = id;
        String con = tvCondition.getText().toString();
        if (id == 1){
            layout_estimate.setVisibility(View.GONE);
            tvType_elec.setVisibility(View.VISIBLE);
            tvType_cate.setVisibility(View.VISIBLE);
        }else {
            tvType_elec.setVisibility(View.GONE);
            tvType_cate.setVisibility(View.GONE);
            if (con.equals("Used") || con.equals("ប្រើប្រាស់រួច")) {
                layout_estimate.setVisibility(View.VISIBLE);
            }else {
                layout_estimate.setVisibility(View.GONE);
            }
        }
        Log.e("category",""+category);
    }

    @Override
    public void onItemType(String item) {
        tvPostType.setText(item);
    }

    private void getLocation(boolean isCurent) {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location!=null){
                if(isCurent) {
                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();
                    latlng = latitude+","+longtitude;
                }
                try{
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addressList = null;
                    addressList = geocoder.getFromLocation(latitude,longtitude,1);
                    String road = addressList.get(0).getAddressLine(0);
                    if (road != null) {
                        if (road.length() > 30) {
                            String loca = road.substring(0,30) + "...";
                            etMap.setText(loca);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }
}


