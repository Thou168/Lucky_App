package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_store_post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_store_post extends Fragment {
    RecyclerView recyclerView;
    private SharedPreferences prefer;
    private String name,pass,Encode;
    private List<ShopViewModel> listData;
    private Adapter_store_post mAdapter;
    private ProgressBar progressBar;
    private TextView no_result;
    private TextView tv_back,tv_dealer,tv_location,tv_phone;
    private CircleImageView img_user;
    private int pk;
    public List_store_post(){}
    String storeName,storeLocation,storeImage;
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
        Call<User> modelCall = api.getProfile1(userId,encode);
        modelCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.d("TAG","55"+response.code()+": "+response.errorBody());
                }
                listData = response.body().getShops();
                progressBar.setVisibility(View.GONE);
                mAdapter = new Adapter_store_post(listData);
                recyclerView.setAdapter(mAdapter);
                if (listData.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
