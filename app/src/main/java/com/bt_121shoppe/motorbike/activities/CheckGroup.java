package com.bt_121shoppe.motorbike.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckGroup extends AppCompatActivity {
    static SharedPreferences prefer;
    int group;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefer = getSharedPreferences("RegisterActivity", MODE_PRIVATE);
    }

    public int getGroup(int userId, Context context){
        String result = "";
        int g = 0;
        try{
            result= CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/users/"+userId);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        try{
            JSONObject obj=new JSONObject(result);
            group = obj.getJSONObject("profile").getInt("group");
            g = group;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return g;
    }
}
