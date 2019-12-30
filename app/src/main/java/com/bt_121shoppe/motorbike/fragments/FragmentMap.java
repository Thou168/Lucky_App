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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.bt_121shoppe.motorbike.Login_Register.Register;
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
    private String road,current_location,address,date,re_password,password,email,phone,gender,username,image,wing_account,wing_number,register_verify;
    private int user_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        pin = findViewById(R.id.pin);
        back = findViewById(R.id.bt_back);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_Account);
        mapFragment.getMapAsync(FragmentMap.this::onMapReady);
//        get_location(true);

        Intent intent = getIntent();
        user_group = intent.getIntExtra("group",0);
        address = intent.getStringExtra("address");
        date = intent.getStringExtra("date");
        re_password = intent.getStringExtra("re_password");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        gender = intent.getStringExtra("gender");
        username = intent.getStringExtra("username");
        image = intent.getStringExtra("image");
        Log.e("image Upload",""+image);
        wing_account = intent.getStringExtra("wing_account_name");
        wing_number = intent.getStringExtra("wing_account_number");
        register_verify = intent.getStringExtra("Register_verify");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragmentMap.this,Register.class);
                intent.putExtra("user_group",user_group);
                intent.putExtra("Register_verify", register_verify);
                intent.putExtra("road",road);
                intent.putExtra("location",current_location);
                intent.putExtra("address",address);
                intent.putExtra("date",date);
                intent.putExtra("re_password",re_password);
                intent.putExtra("password",password);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("gender",gender);
                intent.putExtra("username",username);
                intent.putExtra("image",image);
                intent.putExtra("wing_account_number",wing_number);
                intent.putExtra("wing_account_name",wing_account);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.group_1));
                mMap = googleMap;
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location!=null){
                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();
                    current_location = latitude+","+longtitude;
                    Log.e("latitude",""+current_location);
                    LatLng current_location = new LatLng(latitude, longtitude);
                    Log.e("Current",""+current_location);
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(5),2000,null);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(current_location)
                            .zoom(18)
                            .bearing(90)
                            .tilt(30)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longtitude)));

                    try{
                        Geocoder geocoder = new Geocoder(this);
                        List<Address> addressList = null;
                        addressList = geocoder.getFromLocation(latitude,longtitude,1);
                        road = addressList.get(0).getAddressLine(0);
                        Log.e("Name road",road);
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }else {
                    Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
                }
        }

    }
}
