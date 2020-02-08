package com.bt_121shoppe.motorbike.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.bt_121shoppe.motorbike.Login_Register.Register;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.stores.CreateShop;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.utils.ImageUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class FragmentMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private Button back,pin;
    private EditText et_search;
    private Uri bitmapImage1,bitmapImage2,bitmapImage3,bitmapImage4,bitmapImage5,bitmapImage6;
    private String intent_edit,road,location1,address,date,re_password,password,email,phone,phone1,phone2,gender,username,wing_account,wing_number,register_verify;
    private Uri image,photo,bitmap;
    private GoogleMap mMap;
    private double latitude,longtitude;
    private int user_group,process_type,modelID,brandID,categoryID,yearID,typeID;
    private int shopId,category_post, seekbar_price,seekbar_rearr,seekbar_screww, seekbar_engine, seekbar_head,assembly,seekbar_accessorie,seekbar_consolee,seekbar_pump,whole_ink;
    private String cat,edit,name,register,shopName,number_wing,account_wing,addresses,phone_number,phone_number1,phone_number2,mProfile,post;
    private String edit_store,color,location_post, year, model,discount_price,price,brand,category,post_type,condition,email_post,address_post,phone_number1_post,phone_number2_post,phone_number3_post,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        pin = findViewById(R.id.pin);
        back = findViewById(R.id.bt_back);
        et_search = findViewById(R.id.et_search);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_Account);
        mapFragment.getMapAsync(FragmentMap.this::onMapReady);
        get_location(true);

        Intent intent = getIntent();
        intent_edit        = intent.getStringExtra("edit_store");
        bitmap             = intent.getParcelableExtra("image");
        edit_store         = intent.getStringExtra("edit");
        shopId             = intent.getIntExtra("shopId",0);
        edit               = intent.getStringExtra("edit");
        mProfile           = intent.getStringExtra("Profile");
        user_group         = intent.getIntExtra("group",0);
        address            = intent.getStringExtra("address");
        date               = intent.getStringExtra("date");
        re_password        = intent.getStringExtra("re_password");
        password           = intent.getStringExtra("password");
        email              = intent.getStringExtra("email");
        phone              = intent.getStringExtra("phone");
        phone1             = intent.getStringExtra("phone1");
        phone2             = intent.getStringExtra("phone2");
        gender             = intent.getStringExtra("gender");
        username           = intent.getStringExtra("username");
        image              = intent.getParcelableExtra("image");
        wing_account       = intent.getStringExtra("wing_account_name");
        wing_number        = intent.getStringExtra("wing_account_number");
        register_verify    = intent.getStringExtra("Register_verify");
        register           = intent.getStringExtra("register");
        shopName           = intent.getStringExtra("shop");
        number_wing        = intent.getStringExtra("wing_number");
        account_wing       = intent.getStringExtra("wing_account");
        photo              = intent.getParcelableExtra("photo");
        addresses          = intent.getStringExtra("addresses");
        phone_number       = intent.getStringExtra("phone_number");
        phone_number1      = intent.getStringExtra("phone_number1");
        phone_number2      = intent.getStringExtra("phone_number2");
        post               = intent.getStringExtra("post");
        price              = intent.getStringExtra("price");
        model              = intent.getStringExtra("model");
        brand              = intent.getStringExtra("brand");
        condition          = intent.getStringExtra("condition");
        description        = intent.getStringExtra("description");
        category           = intent.getStringExtra("category");
        post_type          = intent.getStringExtra("post_type");
        email_post         = intent.getStringExtra("email_post");
        address_post       = intent.getStringExtra("address_post");
        phone_number1_post = intent.getStringExtra("phone_number1_post");
        phone_number2_post = intent.getStringExtra("phone_number2_post");
        phone_number3_post = intent.getStringExtra("phone_number3_post");
        discount_price     = intent.getStringExtra("discount_amount");
        seekbar_price      = intent.getIntExtra("discount_percent",0);
        year               = intent.getStringExtra("year");
        whole_ink          = intent.getIntExtra("whole_ink",0);
        seekbar_rearr      = intent.getIntExtra("rear",0);
        seekbar_screww     = intent.getIntExtra("screw",0);
        seekbar_pump       = intent.getIntExtra("pumps",0);
        seekbar_engine     = intent.getIntExtra("right_engine",0);
        seekbar_head       = intent.getIntExtra("engine_head",0);
        assembly           = intent.getIntExtra("assembly",0);
        seekbar_consolee   = intent.getIntExtra("console",0);
        seekbar_accessorie = intent.getIntExtra("accessories",0);
        process_type       = intent.getIntExtra("process_type",0);
        category_post      = intent.getIntExtra("category_post",0);
        name               = intent.getStringExtra("name_post");
        color              = intent.getStringExtra("color");
        cat                = intent.getStringExtra("cat");
        bitmapImage1       = intent.getParcelableExtra("image1");
        bitmapImage2       = intent.getParcelableExtra("image2");
        bitmapImage3       = intent.getParcelableExtra("image3");
        bitmapImage4       = intent.getParcelableExtra("image4");
        bitmapImage5       = intent.getParcelableExtra("image5");
        bitmapImage6       = intent.getParcelableExtra("image6");
        modelID            = intent.getIntExtra("modelID",0);
        brandID            = intent.getIntExtra("brandID",0);
        categoryID         = intent.getIntExtra("categoryID",0);
        typeID             = intent.getIntExtra("typeID",0);
        yearID             = intent.getIntExtra("yearID",0);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (register != null){
                    if (mProfile != null) {
                        startActivity(new Intent(FragmentMap.this, CreateShop.class));
                    }else if (edit != null){
                        startActivity(new Intent(FragmentMap.this,Register.class));
                    }
                }else if (post != null){
                    startActivity(new Intent(FragmentMap.this,Camera.class));
                }else
                    finish();
            }
        });
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (register != null){
                    if (register.equals("register")) {
                        Intent intent = new Intent(FragmentMap.this, Register.class);
                        intent.putExtra("user_group", user_group);
                        intent.putExtra("edit",edit);
                        intent.putExtra("Profile",mProfile);
                        intent.putExtra("edit_profile",register);
                        intent.putExtra("Register_verify", register_verify);
                        intent.putExtra("road", et_search.getText().toString());
                        intent.putExtra("location", location1);
                        intent.putExtra("address", address);
                        intent.putExtra("date", date);
                        intent.putExtra("re_password", re_password);
                        intent.putExtra("password", password);
                        intent.putExtra("email", email);
                        intent.putExtra("phone", phone);
                        intent.putExtra("phone1", phone1);
                        intent.putExtra("phone2", phone2);
                        intent.putExtra("gender", gender);
                        intent.putExtra("username", username);
                        intent.putExtra("image", image);
                        intent.putExtra("wing_account_number", wing_number);
                        intent.putExtra("wing_account_name", wing_account);
                        startActivity(intent);
                    }
                } else if (post != null){
                    if (post.equals("post")){
                        Intent intent = new Intent(FragmentMap.this, Camera.class);
                        intent.putExtra("road", et_search.getText().toString());
                        intent.putExtra("post",post);
                        intent.putExtra("location", location1);
                        intent.putExtra("name_post",name);
                        intent.putExtra("process_type",process_type);
                        intent.putExtra("price",price);
                        intent.putExtra("post_type",post_type);
                        intent.putExtra("category",category);
                        intent.putExtra("brand",brand);
                        intent.putExtra("model",model);
                        intent.putExtra("year",year);
                        intent.putExtra("condition",condition);
                        intent.putExtra("description",description);
                        intent.putExtra("email_post",email_post);
                        intent.putExtra("address_post",address_post);
                        intent.putExtra("phone_number1_post",phone_number1_post);
                        intent.putExtra("phone_number2_post",phone_number2_post);
                        intent.putExtra("phone_number3_post",phone_number3_post);
                        intent.putExtra("discount_percent",seekbar_price);
                        intent.putExtra("discount_amount",discount_price);
                        intent.putExtra("whole_ink",whole_ink);
                        intent.putExtra("rear",seekbar_rearr);
                        intent.putExtra("screw",seekbar_screww);
                        intent.putExtra("pumps",seekbar_pump);
                        intent.putExtra("right_engine",seekbar_engine);
                        intent.putExtra("engine_head",seekbar_head);
                        intent.putExtra("assembly",assembly);
                        intent.putExtra("console",seekbar_consolee);
                        intent.putExtra("accessories",seekbar_accessorie);
                        intent.putExtra("category_post",category_post);
                        intent.putExtra("cat",cat);
                        intent.putExtra("color",color);
                        intent.putExtra("image1",bitmapImage1);
                        intent.putExtra("image2",bitmapImage2);
                        intent.putExtra("image3",bitmapImage3);
                        intent.putExtra("image4",bitmapImage4);
                        intent.putExtra("image5",bitmapImage5);
                        intent.putExtra("image6",bitmapImage6);
                        intent.putExtra("modelID",modelID);
                        intent.putExtra("brandID",brandID);
                        intent.putExtra("yearID",yearID);
                        intent.putExtra("categoryID",categoryID);
                        intent.putExtra("typeID",typeID);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(FragmentMap.this, CreateShop.class);
                    intent.putExtra("edit",edit_store);
                    intent.putExtra("shopId",shopId);
                    intent.putExtra("edit_store",intent_edit);
                    intent.putExtra("user_group", user_group);
                    intent.putExtra("Register_verify", register_verify);
                    intent.putExtra("road", et_search.getText().toString());
                    intent.putExtra("location", location1);
                    intent.putExtra("addresses", addresses);
                    intent.putExtra("phone_number", phone_number);
                    intent.putExtra("phone_number1",phone_number1);
                    intent.putExtra("phone_number2",phone_number2);
                    intent.putExtra("shop", shopName);
                    intent.putExtra("photo", photo);
                    intent.putExtra("wing_number", number_wing);
                    intent.putExtra("wing_name", account_wing);
                    intent.putExtra("image",bitmap);
                    startActivity(intent);
                }
            }
        });
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Seach_Address();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (register != null){
            if (mProfile != null) {
                startActivity(new Intent(FragmentMap.this, CreateShop.class));
            }else if (edit != null){
                startActivity(new Intent(FragmentMap.this,Register.class));
            }
        }else if (post != null){
            startActivity(new Intent(FragmentMap.this,Camera.class));
        }else
            finish();
    }

    private void Seach_Address(){
        String loca = et_search.getText().toString();
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
                location1 = latitude+","+longtitude;
                Log.d("LATITUDE and LONGTITUDE",location1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map));

        mMap = googleMap;
        LatLng current_location = new LatLng(latitude, longtitude);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5),2000,null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current_location)
                .zoom(15)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longtitude)));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map));
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                latitude = latLng.latitude;
                longtitude = latLng.longitude;
                location1 = latitude+","+longtitude;
                Log.e("Location 1", ""+location1);

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
                        String road = addressList.get(0).getAddressLine(0);
                        if (road != null) {
                            if (road.length() > 30) {
                                String loca = road.substring(0,30) + "...";
                                et_search.setText(loca);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }
    private void get_location(boolean isCurrent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocation(isCurrent);
        }
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
                    location1 = latitude+","+longtitude;
                }

                try{
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addressList = null;
                    addressList = geocoder.getFromLocation(latitude,longtitude,1);
                    String road = addressList.get(0).getAddressLine(0);
                    if (road != null) {
                        if (road.length() > 30) {
                            String loca = road.substring(0,30) + "...";
                            et_search.setText(loca);
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
