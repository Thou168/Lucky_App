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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_historyloan;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Loadbyuser_history extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences prefer;
    private String name,pass,Encode;
    String basic_Encode;
    private List<Item_loan> listData;
    Adapter_historyloan mAdapter;
    ProgressBar progressBar;
    TextView no_result;

    TextView txt_date;
    public Loadbyuser_history(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        txt_date = view.findViewById(R.id.txt_date);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);

        basic_Encode = "Basic "+getEncodedString(name,pass);
        getloanbyuser_history();

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        no_result = view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
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
    private void getloanbyuser_history(){
        Service apiService = Client.getClient().create(Service.class);
        Call<AllResponse> call = apiService.getloanhistory(basic_Encode);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                listData = response.body().getresults();
                if (listData.size()==0){
                    txt_date.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }

                //date history
//                for (int i = 0;i<listData.size();i++) {
//                    txt_date.setVisibility(View.VISIBLE);
//                    Log.d("MODIFIED", listData.get(i).getModified());
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date1;
//                    try {
//                        date1 = sdf.parse(listData.get(i).getModified());
//                        Log.d("MODIFIELD DATETETETETE1", String.valueOf(date1));
//                        txt_date.setText(String.valueOf(date1));
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
                //end

                progressBar.setVisibility(View.GONE);
                mAdapter = new Adapter_historyloan(listData,getContext());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

}
