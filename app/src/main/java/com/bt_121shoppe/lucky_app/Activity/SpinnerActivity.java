package com.bt_121shoppe.lucky_app.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpinnerActivity extends AppCompatActivity {

    private Spinner spinner1,spinner2;
    private Button btnSubmit;

    private List<String> IdCategories,categories;
    String[] listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        //spinner2=(Spinner)findViewById(R.id.spinner2);
        //addItemOnSpinner2();

        initialCategoryAPI();
        Button mButton=(Button) findViewById(R.id.button);
        //listItems=new String[20];
        listItems=getResources().getStringArray(R.array.location);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SpinnerActivity.this);
                mBuilder.setTitle("Choose an item");
                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //mResult.setText(listItems[i]);
                        mButton.setText(listItems[i]);
                        Toast.makeText(SpinnerActivity.this,listItems[i],Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SpinnerActivity.this,String.valueOf(spinner2.getSelectedItemPosition()),Toast.LENGTH_LONG).show();
            }
        });
/*
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SpinnerActivity.this,String.valueOf(parent.getSelectedItemPosition()),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
    }

    private void addItemOnSpinner2(){
        List<String> list=new ArrayList<>();
        list.add("List 1");
        list.add("List 2");
        list.add("List 3");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    private void initialCategoryAPI(){
        IdCategories=new ArrayList<>();
        categories=new ArrayList<>();
        String url= ConsumeAPI.BASE_URL+"api/v1/categories/";
        Log.d("Initial Category",url);
        OkHttpClient client = new OkHttpClient();
        //String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                //.header("Authorization",auth)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("failure Category","E "+ e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{
                    Log.d("Initial Category",respon);

                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }
}
