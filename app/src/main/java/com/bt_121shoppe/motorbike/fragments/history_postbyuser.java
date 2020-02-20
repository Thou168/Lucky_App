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
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class history_postbyuser extends Fragment {
    RecyclerView recyclerView;
    ListView list;
    SharedPreferences prefer;
    private String name,pass,Encode;
    String basic_Encode;
    private List<Item> listData = new ArrayList<>();
    Adapter_historybyuser mAdapter;
    ProgressBar progressBar;
    TextView no_result;

    TextView date_text;
    String show_date;
    public history_postbyuser(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        date_text=view.findViewById(R.id.txt_date);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

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
                listData = response.body().getresults();

                if (listData.size()==0){
                    date_text.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    no_result.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);

                //date history
//                for (int i = 0;i<listData.size();i++) {
//                    date_text.setVisibility(View.VISIBLE);
//                    Log.d("MODIFIED", listData.get(i).getModified());
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date1,date2;
//                    try {
//                        date1 = sdf.parse(String.valueOf(listData.get(i).getModified()));
//                        date2 = sdf.parse(String.valueOf(listData.get(i).getModified()));
//                        if (date1 == date2){
//
//                        }
//                        if (i!=0) {
//                            Log.d("MODIFIELD 1111111111", String.valueOf(date1));
//                            date_text.setText(String.valueOf(date1));
//                        }else {
//                            Log.d("MODIFIELD 2222222222", String.valueOf(date1));
//                            date_text.setText(String.valueOf(date1));
//                        }
//
//                        if (listData.get(i).getModified()==null){
//                            date_text.setVisibility(View.GONE);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
                //end

                mAdapter = new Adapter_historybyuser(listData,getContext());
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
