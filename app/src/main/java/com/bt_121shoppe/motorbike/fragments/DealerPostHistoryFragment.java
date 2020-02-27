package com.bt_121shoppe.motorbike.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_historybyuser;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerPostHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    ListView list;
    SharedPreferences prefer;
    private String name,pass,Encode;
    String basic_Encode;
    private List<Item> listData;
    Adapter_historybyuser mAdapter;
    ProgressBar progressBar;
    TextView no_result;

    public DealerPostHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dealer_post_history, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        basic_Encode = "Basic "+getEncodedString(name,pass);
        gethistory();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        no_result = view.findViewById(R.id.text);
        return view;
    }
    private void gethistory(){
        Service apiService = Client.getClient().create(Service.class);
        Call<AllResponse> call = apiService.getpostbyhistory(basic_Encode);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getCount()==0){
                        progressBar.setVisibility(View.GONE);
                        no_result.setVisibility(View.VISIBLE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        listData=response.body().getresults();
                        mAdapter = new Adapter_historybyuser(listData,getContext());
                        recyclerView.setAdapter(mAdapter);
                    }

                }

//                listData=new ArrayList<>();
//                List<Item> results=response.body().getresults();
//                for(int i=0;i<results.size();i++){
//                    if(results.get(i).getDealer_shops().size()==0){
//                        listData.add(results.get(i));
//                    }
//                }
//                //listData = response.body().getresults();
//
//                if (listData.size()==0){
//                    progressBar.setVisibility(View.GONE);
//                    no_result.setVisibility(View.VISIBLE);
//                }
//                progressBar.setVisibility(View.GONE);
//                mAdapter = new Adapter_historybyuser(listData,getContext());
//                recyclerView.setAdapter(mAdapter);
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
