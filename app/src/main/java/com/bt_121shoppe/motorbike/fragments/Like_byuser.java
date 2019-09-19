package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
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
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_Likebyuser;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.LikebyUser;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Like_byuser extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences prefer;
    private String name,pass,Encode;
    String basic_Encode;
    private List<LikebyUser> listData;
    private List<Item> items;
    Adapter_Likebyuser mAdapter;
    ProgressBar progressBar;
    TextView no_result;
    public Like_byuser(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Log.d("Pass and name","123456786"+name+","+pass);
        Encode = CommonFunction.getEncodedString(name,pass);

        basic_Encode = "Basic "+getEncodedString(name,pass);
        getLikebyuser();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        no_result = view.findViewById(R.id.text);
        return view;
    }
    private void getLikebyuser(){
        Service api = Client.getClient().create(Service.class);
        Call<AllResponse> call = api.getLikebyuser(basic_Encode);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
//                Log.d("Like",response.message());
                listData = response.body().getresults();

                if (listData.size()==0){
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
                mAdapter = new Adapter_Likebyuser(listData,getContext());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {

            }
        });
    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }
}
