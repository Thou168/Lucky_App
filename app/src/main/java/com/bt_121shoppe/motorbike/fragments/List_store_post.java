package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter_for_shop.Adapter_store_post;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.stores.CreateShop;
import com.bumptech.glide.Glide;

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
    RecyclerView recyclerView;
    private SharedPreferences prefer;
    private String name,pass,Encode;
    private List<Item> listData;
    private Adapter_store_post mAdapter;
    private ProgressBar progressBar;
    private TextView no_result;
    private TextView tv_back,tv_dealer,tv_location,tv_phone;
    private CircleImageView img_user;
    private int pk;
    String basic_Encode;
    public List_store_post(){}
    String storeName,storeLocation,storeImage;
    TextView btn_edit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail_post_list, container, false);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            storeName=bundle.getString("storeName");
            storeLocation=bundle.getString("storeLocation");
            storeImage=bundle.getString("storeImage");
            Log.e("List",storeName);
        }
        tv_dealer = view.findViewById(R.id.tv_dealer);
        tv_location = view.findViewById(R.id.location_store);
        tv_phone = view.findViewById(R.id.phone);
        img_user = view.findViewById(R.id.img_user);
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CreateShop.class);
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

        tv_location.setText(storeLocation);
        tv_dealer.setText(storeName);
        Glide.with(view.getContext()).load(storeImage).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(img_user);

        getListStore(pk,Encode);

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
                    Log.d("TAG","55"+response.code()+": "+response.errorBody());
                }
                listData = response.body().getresults();
//                progressBar.setVisibility(View.GONE);
//                mAdapter = new Adapter_store_post(listData);
//                recyclerView.setAdapter(mAdapter);
                if (listData.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
                for (int i=0;i<listData.size();i++){
                    if(listData.get(i).getStatus()==4){
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

//                                SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//                                Date d = in.parse(listData.get(i).getApproved_date());
//                                SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
//                                String outDate = out.format(date);
//                                String valida1 = out.format(valida);
//
//                                Log.d("3232323date1", String.valueOf(outDate));
//                                Log.d("Validd", String.valueOf(valida1));
//                                Log.d("3232323date", String.valueOf(date));
//                                Log.d("validate ",String.valueOf(valida));
//                                Log.d("3232323", String.valueOf(ago));
                        } catch (ParseException e) { e.printStackTrace(); }
                    }
                }
                progressBar.setVisibility(View.GONE);
//                    mAdapter = new Adapter_postbyuser(listData, getContext());
                Adapter_store_post mAdapter=new Adapter_store_post(listData);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {

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
}
