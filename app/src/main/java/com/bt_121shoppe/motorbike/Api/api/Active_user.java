package com.bt_121shoppe.motorbike.Api.api;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.bt_121shoppe.motorbike.Activity.Home;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Active_user extends Application {
    static SharedPreferences prefer;
    String st;
    boolean isActive;
    protected void onCreate(Bundle savedInstanceState) {
        prefer = getSharedPreferences("Register",MODE_PRIVATE);

    }

    public  boolean isUserActive(int userId, Context context){
        String result="";
        try{
            result= CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/users/"+userId);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        try{
            //Log.d("Active","User "+result);
            JSONObject obj=new JSONObject(result);
             isActive=obj.getBoolean("is_active");
            if(!isActive){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("This Account is Deactive")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                context.getSharedPreferences("Register",MODE_PRIVATE).edit().clear().commit();
                                context.startActivity(new Intent(context, Home.class));
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return isActive;
    }
}
