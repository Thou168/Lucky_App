package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Item_API;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_postbyuser;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_post;
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.UserPostActiveAdapter;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Postbyuser extends Fragment {
    RecyclerView recyclerView;
    ListView list;
    ArrayList<Item_API> list_item;
    MyAdapter_list_grid_image ad_list;
    SharedPreferences prefer;
    private String name,pass,Encode;
    String basic_Encode;
    private List<Item> listData;
    Adapter_postbyuser mAdapter;
    ProgressBar progressBar;
    TextView no_result;
    int pk=0;

    public Postbyuser(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        Encode = CommonFunction.getEncodedString(name,pass);

        basic_Encode = "Basic "+getEncodedString(name,pass);
        getpostbyuser();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        no_result = view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        String[] items = getResources().getStringArray(R.array.tab_A);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }
    private void getpostbyuser(){
        Service apiService = Client.getClient().create(Service.class);
        Call<AllResponse> call = apiService.getPostbyuser(basic_Encode);
        //Call<AllResponse> call = apiService.getActivePostbyuser(pk);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("Response", String.valueOf(response.code()));
                }else {
                    //Log.e("Post",response.body().toString());
                    listData=response.body().getresults();
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
                                    Log.d("555555555555","You are win"+(int)listData.get(i).getId());
                                    change_status_post  ch = new change_status_post(2);
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
                    //mAdapter = new Adapter_postbyuser(listData, getContext());
                    UserPostActiveAdapter mAdapter=new UserPostActiveAdapter(listData);
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
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
