package com.bt_121shoppe.lucky_app.Fragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.lucky_app.Activity.Camera;
import com.bt_121shoppe.lucky_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class contact_user2 extends Intent_data implements OnMapReadyCallback {
    private TextView tvPhone,tvEmail,tvAddress;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double latitude,longtitude;
    private MapView mapView;
    private GoogleMap mMap;
    private String latlng;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_contact_user2, container, false);
        tvPhone = view.findViewById(R.id.phone);
        tvEmail = view.findViewById(R.id.email);
        tvAddress = view.findViewById(R.id.address_contact);
//        String phone = getActivity().getIntent().getExtras().getString("Phone");
        String name = getActivity().getIntent().getExtras().getString("Name");
        String email = getActivity().getIntent().getExtras().getString("Email");
        String addr  = getActivity().getIntent().getExtras().getString("map");
        tvPhone.setText(name);
        if (getActivity().getIntent().getExtras().getString("Email")==null){
            tvEmail.setText("Null");
        }else {
            tvEmail.setText(getActivity().getIntent().getExtras().getString("Email"));
        }

        if (addr.isEmpty()){
            Toast.makeText(getContext(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();
        }else {
            String[] splitAddr = addr.split(",");
            latitude = Double.valueOf(splitAddr[0]);
            longtitude = Double.valueOf(splitAddr[1]);
            get_location(latitude,longtitude);
//            mapView = (MapView)view.findViewById(R.id.map_contact);
//            mapView.onCreate(savedInstanceState);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_contact);
            mapFragment.getMapAsync(this);

//            mapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//                    if (googleMap!=null){
//                        mMap = googleMap;
//                    }
//                    LatLng current_location = new LatLng(latitude, longtitude);
//                    //      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(old,10));
//                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(5),2000,null);
//                    CameraPosition cameraPosition = new CameraPosition.Builder()
//                            .target(current_location)
//                            .zoom(18)
//                            .bearing(90)
//                            .tilt(30)
//                            .build();
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longtitude)));
//                }
//            });
        }
        return view;
    }

    private void get_location(double latitude, double longtitude) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocation(latitude,longtitude);
        }
    }

    private void getLocation(double latitude, double longtitude) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location!=null){

                try{
                    Geocoder geocoder = new Geocoder(getActivity());
                    List<Address> addressList = null;
                    addressList = geocoder.getFromLocation(latitude, longtitude,1);
//                    String country = addressList.get(0).getCountryName();
//                    String city    = addressList.get(0).getLocality();
                    String road = addressList.get(0).getAddressLine(0);

                    tvAddress.setText( road );
                }catch (IOException e){
                    e.printStackTrace();
                }


            }else {
                Toast.makeText(getContext(),"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap!=null){
            mMap = googleMap;
        }
        LatLng current_location = new LatLng(latitude, longtitude);
        //      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(old,10));
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
    }
}
