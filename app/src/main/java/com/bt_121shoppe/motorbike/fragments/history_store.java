package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_history_store;
import com.bt_121shoppe.motorbike.Api.api.adapter_for_shop.Adapter_store_post_history;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class history_store extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences prefer;
    private String name,pass,Encode;
    private String basic_Encode;
    private List<ShopViewModel> listData;
    Adapter_history_store mAdapter;
    ProgressBar progressBar;
    TextView no_result;
    public history_store(){}
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
        getStore_history();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        no_result = view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }
    private void getStore_history(){
        Service apiService1 = Client.getClient().create(Service.class);
        Call<AllResponse> call1 = apiService1.getpostbyhistory(basic_Encode);
        call1.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if(response.isSuccessful()) {
                    List<Item> listData = response.body().getresults();

                    if (listData.size() == 0) {
                        no_result.setVisibility(View.VISIBLE);
                    }else {
                        no_result.setVisibility(View.GONE);
                        Adapter_store_post_history mAdapter = new Adapter_store_post_history(listData, getContext());
                        recyclerView.setAdapter(mAdapter);
                    }
                }
                else{
                    no_result.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {

            }
        });
    }

}
