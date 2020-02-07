package com.bt_121shoppe.motorbike.loan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_list_draft;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.loan.model.Draft;
import com.bt_121shoppe.motorbike.loan.model.draft_Item;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Draft_loan extends AppCompatActivity {

    private TextView tv_back;
    private RecyclerView recyclerView;
    private SharedPreferences prefer;
    private String name,pass,Encode;
    private String basic_Encode;
    private List<draft_Item> listData;
    private Adapter_list_draft mAdapter;
    private int pk;
//    ProgressBar progressBar;
//    TextView no_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_darft);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prefer = getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        Log.d("Pk",""+pk);
        Encode = CommonFunction.getEncodedString(name,pass);

        basic_Encode = "Basic "+getEncodedString(name,pass);
        getLoan_draft();

//        progressBar = findViewById(R.id.progress_bar);
//        progressBar.setVisibility(View.VISIBLE);
//        no_result = findViewById(R.id.text);

        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }
    private void getLoan_draft(){
        Service apiService = Client.getClient().create(Service.class);
        Call<Draft> call = apiService.getList_draft(basic_Encode);
        call.enqueue(new Callback<Draft>() {
            @Override
            public void onResponse(Call<Draft> call, Response<Draft> response) {
                listData = response.body().getresults();
                Log.e("list",""+listData.size());
                if (listData.size()==0){
//                    progressBar.setVisibility(View.GONE);
//                    no_result.setVisibility(View.VISIBLE);
                }
//                progressBar.setVisibility(View.GONE);
                mAdapter = new Adapter_list_draft(getBaseContext(),listData);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onFailure(Call<Draft> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

}
