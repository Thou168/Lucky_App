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

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_Likebyuser;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_ListStore;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.Api.responses.APIShopResponse;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListStore extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences prefer;
    private String name,pass,Encode;
    private List<ShopViewModel> listData;
    Adapter_ListStore mAdapter;
    ProgressBar progressBar;
    TextView no_result;
    int pk;
    public ListStore(){}
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
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }

        getListStore(pk,Encode);

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        no_result = view.findViewById(R.id.text);
        return view;
    }
    private void getListStore(int userId,String encode){
        Service api = Client.getClient().create(Service.class);
        Call<User> modelCall = api.getProfile1(userId,encode);
        modelCall.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, retrofit2.Response<User> response) {
                if (!response.isSuccessful()){
                    Log.d("TAG","55"+response.code()+": "+response.errorBody());
                }
                listData = response.body().getShops();
                progressBar.setVisibility(View.GONE);
                mAdapter = new Adapter_ListStore(listData,getContext());
                recyclerView.setAdapter(mAdapter);
                if (listData.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {

            }
        });
    }
}
