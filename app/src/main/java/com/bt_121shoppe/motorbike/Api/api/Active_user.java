package com.bt_121shoppe.motorbike.Api.api;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.Activity.Home;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.UserProfileModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Active_user extends AppCompatActivity {
    static SharedPreferences prefer;
    boolean isActive;
    String isActiveResult="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefer = getSharedPreferences("RegisterActivity", MODE_PRIVATE);
    }

    public String isUserActive(int userId, Context context){
        String result="";
        String re = "";

        try{
            result= CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/users/"+userId);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        try{
            JSONObject obj=new JSONObject(result);
             isActive=obj.getBoolean("is_active");
             re = String.valueOf(isActive);
        }catch (JSONException e){
            e.printStackTrace();
        }
        //Log.d("ACCOUNT","return is:"+ String.valueOf(re));
//        Service apiService=Client.getClient().create(Service.class);
//        Call<UserResponseModel> call=apiService.getUserProfile(userId);
//        call.enqueue(new Callback<UserResponseModel>() {
//            @Override
//            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
//                if(response.isSuccessful()){
//                    isActiveResult=String.valueOf(response.body().isIs_active());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserResponseModel> call, Throwable t) {
//
//            }
//        });
        return re;
    }

    public void clear_session(Context context){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_deactive);
        dialog.setCancelable(false);
        Button deactived = dialog.findViewById(R.id.btn_dialog_deactive);
        deactived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences("RegisterActivity",MODE_PRIVATE).edit().clear().commit();
                context.startActivity(new Intent(context, Home.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public static boolean isUserActive(Context context,int userId){
        String response="";
        try{
            response= CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/users/"+userId);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        try{
            JSONObject obj=new JSONObject(response);
            return obj.getBoolean("is_active");

        }catch (JSONException e){
            e.printStackTrace();
        }
        return true;
    }
    public static void clearSession(Context context){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_deactive);
        dialog.setCancelable(false);
        Button deactived = dialog.findViewById(R.id.btn_dialog_deactive);
        deactived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences("RegisterActivity",MODE_PRIVATE).edit().clear().commit();
                context.startActivity(new Intent(context, Home.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
