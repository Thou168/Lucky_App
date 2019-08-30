package com.bt_121shoppe.motorbike.Buy_Sell_Rent.Sell;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_Rent_vehicle;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.R;
import com.custom.sliderimage.logic.SliderImage;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sell_eletronic extends PassSell2 {
    RecyclerView recyclerView;
    SharedPreferences prefer;
    private String name,pass;
    String basic_Encode;
    private List<Item> listData;
    Adapter_Rent_vehicle mAdapter;
    ProgressBar progressBar;
    TextView no_result;
    SliderImage sliderImage;
    Toolbar toolbar;
    String[] images = {"https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg",
            "https://i.redd.it/glin0nwndo501.jpg", "https://i.redd.it/obx4zydshg601.jpg"};
    public Sell_eletronic(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.color.color_sell);
        TextView back = view.findViewById(R.id.tv_back);
        back.setOnClickListener(v -> getActivity().finish());

        TextView title = view.findViewById(R.id.title);
        title.setText(BACK_SELL.getIntent().getStringExtra("Title"));

        sliderImage = view.findViewById(R.id.slider_vehicles);
        sliderImage.setItems(Arrays.asList(images));
        sliderImage.addTimerToSlide(3000);
        //  sliderImage.removeTimerSlide()
        sliderImage.getIndicator();


        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");

        basic_Encode = "Basic "+getEncodedString(name,pass);
        getRent_vihicle();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        no_result = view.findViewById(R.id.text);
        return view;
    }
    private void getRent_vihicle(){
        Service api = Client.getClient().create(Service.class);
        Call<AllResponse> call = api.getSell_eletronic();
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                listData = response.body().getresults();

                if (listData.size()== 0){
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
                mAdapter = new Adapter_Rent_vehicle(listData,getContext());
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
