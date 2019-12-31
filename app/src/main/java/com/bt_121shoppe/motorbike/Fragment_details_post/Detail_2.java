package com.bt_121shoppe.motorbike.Fragment_details_post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.RentViewModel;
import com.bt_121shoppe.motorbike.models.SaleViewModel;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.google.gson.internal.$Gson$Types.arrayOf;

public class Detail_2 extends Fragment {

    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_address;
    private CircleImageView cr_img;
    private TextView username;
    private int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private GoogleMap mMap;
    Double latitude= (double) 0;
    Double longtitude= (double) 0;

    public static final String TAG = "2 Fragement";
    PostViewModel postDetail = new PostViewModel();

    private int pt=0;
    private int postId = 0;

    SharedPreferences prefer;
    private String name,pass,Encode;
    String basic_Encode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_detail_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_email = view.findViewById(R.id.tv_email);
        tv_address = view.findViewById(R.id.address);

        cr_img = view.findViewById(R.id.cr_img);
        username = view.findViewById(R.id.us_name);

        tv_phone.setText("PHONE");
        tv_email.setText("EMAIL");
        tv_address.setText("ADDRESS");


        //basic
        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        basic_Encode = "Basic "+CommonFunction.getEncodedString(name,pass);
        pt = getActivity().getIntent().getIntExtra("postt",0);
        postId = getActivity().getIntent().getIntExtra("ID",0);
        detail_fragment_2(Encode);
    }

    private void getUserProfile(int id,String encode){

        String URL_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/users/"+id;
        MediaType MEDIA_TYPE=MediaType.parse("application/json");
        OkHttpClient client= new  OkHttpClient();
        Request request= new Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                //.header("Authorization",encode)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Gson gson = new  Gson();

            }
        });
    }

//    private void onMapReady(GoogleMap googleMap) {
//        // Creating a marker
//        MarkerOptions markerOptions = new MarkerOptions();
//        // Setting the position for the marker
//        markerOptions.position(new LatLng(latitude,longtitude));
//        if (googleMap != null) {
//            mMap = googleMap;
//        }
//        LatLng current_location = new  LatLng(latitude, longtitude);
//        mMap.animateCamera(CameraUpdateFactory.zoomIn());
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(5f), 2000, null);
//        CameraPosition cameraPosition = new  CameraPosition.Builder()
//                .target(current_location)
//                .zoom(14f)
//                .bearing(90f)
//                .tilt(30f)
//                .build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        mMap.addMarker(markerOptions);
//    }

    private void detail_fragment_2(String encode){
        String url;
        Request request;
        String auth = "Basic" + encode;
        if (pt==1){
            url = ConsumeAPI.BASE_URL + "postbyuser/" + postId;
            request = new  Request.Builder()
                    .url(url)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    .header("Authorization",auth)
                    .build();
        }
        else {
            url = ConsumeAPI.BASE_URL + "detailposts/" + postId;
            request = new  Request.Builder()
                    .url(url)
                    .header("Accept","application/json")
                    .header("Content-Type", "application/json")
                    .build();
        }

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Request",mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.d(TAG+"3333",mMessage);
                Gson json = new Gson();
                try {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            postDetail = json.fromJson(mMessage, PostViewModel.class);
                            Log.e(TAG, "D" + mMessage);
                            tv_email.setText(postDetail.getContact_email());
                            username.setText(postDetail.getMachine_code());
                            String contact_phone = postDetail.getContact_phone();
                            tv_phone.setText(contact_phone);

                            //address
                            String addr = postDetail.getContact_address();
                            if (addr.isEmpty()) {

                            } else {
                                String[] splitAddr = (addr.split(","));
                                latitude = Double.valueOf(splitAddr[0]);
                                longtitude = Double.valueOf(splitAddr[1]);
                                try {
                                    Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addresses = geo.getFromLocation(latitude, longtitude, 1);
                                    if (addresses.isEmpty()) {
                                        tv_address.setText("Waiting for Location");
                                    } else {
                                        if (addresses.size() > 0) {
                                            tv_address.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch(JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
