package com.bt_121shoppe.motorbike.Fragment_details_post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.detail_shop;
import com.bt_121shoppe.motorbike.Api.responses.APIStorePostResponse;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.CheckGroup;
import com.bt_121shoppe.motorbike.adapters.ShopAdapter;
import com.bt_121shoppe.motorbike.fragments.FragmentMap;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.RentViewModel;
import com.bt_121shoppe.motorbike.models.SaleViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.post.PostDetailMapActivity;
import com.bt_121shoppe.motorbike.stores.StoreDetailActivity;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
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
    private TextView user_shop;
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
    String postUsername,postUserId;
    ImageView mapImageView;
    private ShopAdapter customAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView listShop;
    private int pk = 0;
    private int g = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_detail_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_email = view.findViewById(R.id.tv_email);
        tv_address = view.findViewById(R.id.tv_address);

        cr_img = view.findViewById(R.id.cr_img);
        user_shop = view.findViewById(R.id.us_shop);
        mapImageView=view.findViewById(R.id.map_icon);

        listShop=view.findViewById(R.id.list_shop);

        tv_phone.setText("");
        tv_email.setText("");
        tv_address.setText("");
        //basic
        CheckGroup check = new CheckGroup();
        if (getActivity()!=null) {
            prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
            if (prefer.contains("token")) {
                pk = prefer.getInt("Pk",0);
            }else if (prefer.contains("id")) {
                pk = prefer.getInt("id", 0);
            }
            g = check.getGroup(pk,getContext());
        }
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
                try {
                    User user1= gson.fromJson(mMessage,User.class);
                    Log.d(TAG,"TAH"+mMessage);

                    if (getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommomAPIFunction.getUserProfileFB(getActivity(),cr_img,user1.getUsername());
//                                if (g!=3) {
                                    if (user1.getFirst_name() == null)
                                        postUsername = user1.getUsername();
                                    else
                                        postUsername = user1.getFirst_name();
                                    postUserId = user1.getUsername();
                                    user_shop.setText(postUsername);
//                                }else {
//                                    user_shop.setVisibility(View.GONE);
//                                    listShop.setVisibility(View.VISIBLE);
//                                }
                            }
                        });
                    }
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

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
                //Log.d(TAG+"3333",mMessage);
                Gson json = new Gson();
                try {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            postDetail = json.fromJson(mMessage, PostViewModel.class);
                            Log.e(TAG, "D" + mMessage);
                            Service api = Client.getClient().create(Service.class);
                            retrofit2.Call<APIStorePostResponse> call1=api.GetStorePostItembyPost(postId);
                            call1.enqueue(new retrofit2.Callback<APIStorePostResponse>() {
                                @Override
                                public void onResponse(retrofit2.Call<APIStorePostResponse> call, retrofit2.Response<APIStorePostResponse> response) {
                                    if(response.isSuccessful()){
                                        Log.e("TAG","Count Shop Post "+response.body().getCount());
                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<APIStorePostResponse> call, Throwable t) {

                                }
                            });

                            tv_email.setText(postDetail.getContact_email());

//                            username.setText(postDetail.getMachine_code());

                            int created_by = Integer.parseInt(postDetail.getCreated_by());
                            getUserProfile(created_by, auth);

                            //shop name

                            for (int i = 0; i < postDetail.getDealer_shops().size(); i++) {
                                if (postDetail.getDealer_shops().get(i).getRecord_status() == 1) {
                                    if (postDetail.getDealer_shops().size() > 0) {
                                        user_shop.setVisibility(View.GONE);
                                        listShop.setVisibility(View.VISIBLE);
                                        customAdapter = new ShopAdapter(getActivity(), postDetail.getDealer_shops());
                                        mLayoutManager = new LinearLayoutManager(getActivity());
                                        listShop.setLayoutManager(mLayoutManager);
                                        listShop.setAdapter(customAdapter);
                                    }
                                }else {
                                    listShop.setVisibility(View.GONE);
                                    user_shop.setVisibility(View.VISIBLE);
                                }
                            }
//                            Glide.with(getActivity()).load(postDetail.getCreated_by()).placeholder(R.mipmap.ic_launcher_round).centerCrop().into(cr_img);

                            String contact_phone = postDetail.getContact_phone();
                            String[] splitPhone = contact_phone.split(",");
                            if (splitPhone.length ==1){
                                tv_phone.setText(splitPhone[0]);
                            }else if (splitPhone.length==2){
                                tv_phone.setText(splitPhone[0]+"\n"+splitPhone[1]);
                            }else if (splitPhone.length==3){
                                tv_phone.setText(splitPhone[0]+"\n"+splitPhone[1]+"\n"+splitPhone[2]);
                            }

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
                                    String select_add = addresses.get(0).getAddressLine(0);
                                    if (addresses.isEmpty()) {
                                        tv_address.setText("no location");
                                    } else {
                                        addresses.size();
                                        tv_address.setText(select_add);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mapImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                                                (getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                                        }else{
                                            Intent intent=new Intent(getActivity(), PostDetailMapActivity.class);
                                            intent.putExtra("addresslatlong",addr);
                                            startActivity(intent);
                                        }
                                    }
                                });
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
