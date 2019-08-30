package com.bt_121shoppe.motorbike.Login_Register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SearchAccountActivity extends AppCompatActivity {

    private static final String TAG=SearchAccountActivity.class.getSimpleName();
    private ProgressDialog mProgress;
    TextView btnBack,textPhoneNumber;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_account);

        btnBack=findViewById(R.id.tvBack);
        textPhoneNumber=findViewById(R.id.phone_number);
        btnSubmit=findViewById(R.id.submit);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.progress_waiting));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber=textPhoneNumber.getText().toString().trim();
                if(!phonenumber.isEmpty()){
                    mProgress.show();
                    checkExistUser(phonenumber);
                }
            }
        });

    }
    private void checkExistUser(String phonenumber){
        String url= ConsumeAPI.BASE_URL+"api/v1/userfilter/?last_name=&username="+phonenumber;
        String result="";
        try {
            result = CommonFunction.doGetRequest(url);
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            JSONObject obj=new JSONObject(result);
            int count=obj.getInt("count");
            if(count==0){
                //user not found
                mProgress.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(SearchAccountActivity.this).create();
                //alertDialog.setTitle("Post ads");
                alertDialog.setMessage(getString(R.string.invalid_phone));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.button_close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }else{
//user found
                mProgress.dismiss();
                Intent intent=new Intent(SearchAccountActivity.this,VerifyMobileActivity.class);
                intent.putExtra("authType",5);
                intent.putExtra("phoneNumber",phonenumber);
                intent.putExtra("password",phonenumber);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
