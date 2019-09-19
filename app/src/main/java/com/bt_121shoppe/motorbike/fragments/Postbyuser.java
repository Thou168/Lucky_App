package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_postbyuser;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.util.ArrayList;
import java.util.List;

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
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("Response", String.valueOf(response.code()));
                }else {
                    listData = response.body().getresults();

                    if (listData.size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        no_result.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter = new Adapter_postbyuser(listData, getContext());
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

}
