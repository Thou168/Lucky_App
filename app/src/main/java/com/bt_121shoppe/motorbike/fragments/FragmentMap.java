package com.bt_121shoppe.motorbike.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.bt_121shoppe.motorbike.stores.CreateShop;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
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

    private GoogleMap mMap;
    private double latitude,longtitude;
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private Button back,pin;
    private EditText et_search;
    private String road,location1,address,date,re_password,password,email,phone,phone1,phone2,gender,username,image,wing_account,wing_number,register_verify;
    private int user_group;
    private String register,shopName,number_wing,account_wing,photo,addresses,phone_number,phone_number1,phone_number2,mProfile;

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
        mProfile = intent.getStringExtra("Profile");
        user_group = intent.getIntExtra("group",0);
        address = intent.getStringExtra("address");
        date = intent.getStringExtra("date");
        re_password = intent.getStringExtra("re_password");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        phone1 = intent.getStringExtra("phone1");
        phone2 = intent.getStringExtra("phone2");
        gender = intent.getStringExtra("gender");
        username = intent.getStringExtra("username");
        image = intent.getStringExtra("image");
        wing_account = intent.getStringExtra("wing_account_name");
        wing_number = intent.getStringExtra("wing_account_number");
        register_verify = intent.getStringExtra("Register_verify");
        register = intent.getStringExtra("register");
        shopName = intent.getStringExtra("shop");
        number_wing = intent.getStringExtra("wing_number");
        account_wing = intent.getStringExtra("wing_account");
        photo = intent.getStringExtra("photo");
        addresses = intent.getStringExtra("addresses");
        phone_number = intent.getStringExtra("phone_number");
        phone_number1 = intent.getStringExtra("phone_number1");
        phone_number2 = intent.getStringExtra("phone_number2");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (register == null){
                    startActivity(new Intent(FragmentMap.this,CreateShop.class));
                }else
                    finish();
            }
        });
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (register!=null) {
                    Intent intent = new Intent(FragmentMap.this, Register.class);
                    intent.putExtra("user_group", user_group);
                    intent.putExtra("Profile",mProfile);
                    intent.putExtra("Register_verify", register_verify);
                    intent.putExtra("road", et_search.getText().toString());
                    Log.e("Location",et_search.getText().toString());
                    intent.putExtra("location", location1);
                    Log.e("Current Location",""+location1);
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
                }else {
                    Intent intent = new Intent(FragmentMap.this, CreateShop.class);
                    intent.putExtra("user_group", user_group);
                    intent.putExtra("Register_verify", register_verify);
                    intent.putExtra("road", et_search.getText().toString());
                    intent.putExtra("location", location1);
                    Log.e("Current Location",""+location1);
                    intent.putExtra("addresses", addresses);
                    intent.putExtra("phone_number", phone_number);
                    intent.putExtra("phone_number1",phone_number1);
                    intent.putExtra("phone_number2",phone_number2);
                    intent.putExtra("shop", shopName);
                    intent.putExtra("photo", phone);
                    intent.putExtra("wing_number", number_wing);
                    intent.putExtra("wing_name", account_wing);
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
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.group_1));

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
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.group_1));
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
