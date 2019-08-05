package com.bt_121shoppe.lucky_app.Startup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bt_121shoppe.lucky_app.Api.api.AllResponse;
import com.bt_121shoppe.lucky_app.Api.api.Breand;
import com.bt_121shoppe.lucky_app.Api.api.Category;
import com.bt_121shoppe.lucky_app.Api.api.Client;
import com.bt_121shoppe.lucky_app.Api.api.Service;
import com.bt_121shoppe.lucky_app.Api.api.Year;
import com.bt_121shoppe.lucky_app.R;

import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Filter extends AppCompatActivity {

    TextView tv_result;
    Spinner sp_category;
    List<String> list_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        tv_result = findViewById(R.id.tv_result);
        tv_result.setOnClickListener(v -> finish());
        sp_category = findViewById(R.id.category);

        getBrand();
        list_category = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_category);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(arrayAdapter);
    }
    private void getBrand(){
        Service service = Client.getClient().create(Service.class);
        Call<AllResponse> call = service.getYear();
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                List<Year> items = response.body().getCategory();
                for (int i=0;i<items.size();i++){
                    Log.d("Brand",items.get(i).getYear());
//                    list_category.add(breand.get(i).getCat_name());
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                Log.d("Error ",t.getMessage());
            }
        });
    }
}
