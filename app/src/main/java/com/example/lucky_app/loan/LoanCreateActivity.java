package com.example.lucky_app.loan;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lucky_app.Api.ConsumeAPI;
import com.example.lucky_app.R;
import com.example.lucky_app.models.LoanViewModel;
import com.example.lucky_app.utils.CommonFunction;
import com.example.lucky_app.utils.LoanCalculator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoanCreateActivity extends AppCompatActivity {

    private static final String TAG=LoanCreateActivity.class.getSimpleName();
    private SharedPreferences preferences;
    private String username,password,encodeAuth;
    private int pk;

    private Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_create);

        preferences=getSharedPreferences("Register",MODE_PRIVATE);
        username=preferences.getString("name","");
        password=preferences.getString("pass","");
        encodeAuth="Basic "+ CommonFunction.getEncodedString(username,password);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }
        Log.d(TAG,String.valueOf(LoanCalculator.getLoanMonthPayment(2340,1.5,12)));

        btSubmit=(Button) findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumeLoanCreateApi();
            }
        });

    }

    private void consumeLoanCreateApi(){
        String urlAPIEndpoint=ConsumeAPI.BASE_URL+"api/v1/loan/";

        OkHttpClient client=new OkHttpClient();
        JSONObject data=new JSONObject();
        try{
            data.put("loan_to",pk);
            data.put("loan_amount","2500");
            data.put("loan_interest_rate","1");
            data.put("loan_duration","24");
            data.put("loan_purpose","hi");
            data.put("loan_status",1);
            data.put("record_status",1);
            data.put("username","Bye");
            data.put("gender","female");
            data.put("age",20);
            data.put("job","Test");
            data.put("average_income","20000");
            data.put("average_expense","200");
            data.put("telephone","011308281");
            data.put("address","Wat Phnom");
            data.put("state_id",false);
            data.put("family_book",false);
            data.put("staff_id",false);
            data.put("house_plant",false);
            data.put("post",1);
            data.put("created_by",pk);

        }catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(ConsumeAPI.MEDIA_TYPE,data.toString());
        Request request=new Request.Builder()
                .url(urlAPIEndpoint)
                .post(body)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String result=e.getMessage().toString();
                Log.d(TAG,"Fail:"+result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                        alertDialog.setTitle("Loan");
                        alertDialog.setMessage("Loan request has been error while submiting.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Gson gson=new Gson();
                LoanViewModel loanObj=new LoanViewModel();
                try{
                    loanObj=gson.fromJson(result,LoanViewModel.class);
                    if(loanObj!=null){
                        int statusCode=loanObj.getStatus();
                        if(statusCode==201){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                                    alertDialog.setTitle("Loan");
                                    alertDialog.setMessage("Loan request has been successfully submitted.");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });

                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                                alertDialog.setTitle("Loan");
                                alertDialog.setMessage("Loan request has been error while submiting.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                    }
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
