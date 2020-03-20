package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter_for_shop.Adapter_store_post;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_post;
import com.bt_121shoppe.motorbike.Api.responses.APIStorePostResponse;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.StorePostViewModel;
import com.bt_121shoppe.motorbike.stores.CreateShop;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;

public class List_store_post extends Fragment {
    private RecyclerView recyclerView;
    private SharedPreferences prefer;
    private String name,pass,Encode;
    private List<Item> listData;
    private Adapter_store_post mAdapter;
    private ProgressBar progressBar;
    private TextView no_result;
    private TextView tv_back,tv_dealer,tv_location,tv_phone,tv_rate,tv_viewcount;
    private CircleImageView img_user;
    private int pk,shopId;
    private String basic_Encode;
    public  List_store_post(){}
//    private String storeName,storeLocation,storeImage;
    private TextView btn_edit;
    private List<StorePostViewModel> postListItems;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_post_list, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            shopId = bundle.getInt("shopId", 0);
        }

        tv_dealer   = view.findViewById(R.id.tv_dealer);
        tv_location = view.findViewById(R.id.location_store);
        tv_phone    = view.findViewById(R.id.phone);
        img_user    = view.findViewById(R.id.img_user);
        btn_edit    = view.findViewById(R.id.btn_edit);
        tv_rate=view.findViewById(R.id.rate);
        tv_viewcount=view.findViewById(R.id.view);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CreateShop.class);
                i.putExtra("edit_store","edit");
                i.putExtra("shopId",shopId);
                startActivity(i);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_Encode = "Basic "+getEncodedString(name,pass);
        Log.e("TAG","Shop id na ja :"+shopId);
        getShop_Detail(shopId);
//        getShop_Info(pk,Encode);
        //getListStore(pk,Encode);
        getListStore(shopId);

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        no_result = view.findViewById(R.id.text);
        return view;
    }
    private void getListStore(int userId,String encode){
        Service api = Client.getClient().create(Service.class);
        Call<AllResponse> modelCall = api.getPostbyuser(basic_Encode);
        modelCall.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if (!response.isSuccessful()){
                    //Log.d("TAG","55"+response.code()+": "+response.errorBody());
                }
                listData = response.body().getresults();

                if (listData.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
                for (int i=0;i<listData.size();i++){
                    //if(listData.get(i).getStatus()==4){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                        long date = 0;
                        try {
                            date = sdf.parse(listData.get(i).getApproved_date()).getTime()+(15000*60*60*24);
                            Long now = System.currentTimeMillis();
                            Long valida = now;
                            CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);

                            if (date <= valida){
                                //Log.d("555555555555","You are win"+(int)listData.get(i).getId());
                                change_status_post ch = new change_status_post(2);
                                Movetohistory((int) listData.get(i).getId(),ch,basic_Encode);
                            }
                        } catch (ParseException e) { e.printStackTrace(); }
                    //}
                }
                progressBar.setVisibility(View.GONE);
                //Adapter_store_post mAdapter=new Adapter_store_post(listData);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {

            }
        });


    }
    private void getListStore(int shopId){
        Service api=Client.getClient().create(Service.class);
        //Call<APIStorePostResponse> model=api.GetStorePost(shopId);
        Call<APIStorePostResponse> model=api.GetStoreActivePost(shopId);
        model.enqueue(new Callback<APIStorePostResponse>() {
            @Override
            public void onResponse(Call<APIStorePostResponse> call, Response<APIStorePostResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("TAG","55"+response.code()+": "+response.errorBody());
                }

                postListItems=response.body().getResults();
                int count=response.body().getCount();
                if(count==0)
                {
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    for(int i=0;i<postListItems.size();i++){
                        StorePostViewModel item=postListItems.get(i);
                        Log.e("SL","Post ID "+item.getPost()+" Shop ID "+item.getShop());
                    }
                    Adapter_store_post mAdapter=new Adapter_store_post(postListItems);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<APIStorePostResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }
    private void Movetohistory(int id, change_status_post ch, String basic_Encode){
        Service api = Client.getClient().create(Service.class);
        Call call = api.getMovehistory(id,ch,basic_Encode);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()){
                    try {
                        Log.d("12121thou",response.errorBody().string());
                    } catch (IOException e) { e.printStackTrace(); }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("ONFAilure",t.getMessage());
            }
        });
    }
//    private void getShop_Info(int pk, String encode) {
//        Service api = Client.getClient().create(Service.class);
//        retrofit2.Call<User_Detail> call = api.getDetailUser(pk,encode);
//        call.enqueue(new retrofit2.Callback<User_Detail>() {
//            @Override
//            public void onResponse(retrofit2.Call<User_Detail> call, retrofit2.Response<User_Detail> response) {
//                if (response.isSuccessful()) {
//                    String stphone = response.body().getProfile().getTelephone();
//                    tv_phone.setText(method(stphone));
//                }
//            }
//            @Override
//            public void onFailure(retrofit2.Call<User_Detail> call, Throwable t) {
//
//            }
//        });
//    }
    private void getShop_Detail(int id) {
        Service api = Client.getClient().create(Service.class);
        retrofit2.Call<ShopViewModel> call = api.getDealerShop(id);
        call.enqueue(new retrofit2.Callback<ShopViewModel>() {
            @Override
            public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response) {
                if (response.isSuccessful()) {
                    tv_dealer.setText(response.body().getShop_name());
                    tv_location.setText(response.body().getShop_address());
                    tv_rate.setText(String.valueOf(response.body().getShop_rate()));
                    tv_viewcount.setText(String.valueOf(response.body().getShop_view()));
                    String stphone = response.body().getShop_phonenumber();
                    tv_phone.setText(method(stphone));
                    String image = response.body().getShop_image();
                    Glide.with(List_store_post.this).asBitmap().load(image).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            img_user.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

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
}
