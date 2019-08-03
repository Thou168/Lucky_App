package com.bt_121shoppe.lucky_app.Login_Register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bt_121shoppe.lucky_app.Activity.Camera;
import com.bt_121shoppe.lucky_app.Activity.Home;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;
import com.facebook.login.LoginManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ConfirmMobileNumberActivity extends AppCompatActivity {

    private static final String TAG=ConfirmMobileNumberActivity.class.getSimpleName();
    private TextView back;
    private MaterialEditText edphonenumber;
    private Button button_sumit;

    private String facebooktokenkey,facebookid,facebookname,imageurl;
    private Intent intent;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mobile_number);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.progress_waiting));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        intent=getIntent();
        facebooktokenkey=intent.getStringExtra("facebooktokenkey");
        facebookid=intent.getStringExtra("facebookid");
        facebookname=intent.getStringExtra("facebookname");
        imageurl=intent.getStringExtra("imageurl");

        back=findViewById(R.id.tvBack_account);
        edphonenumber=findViewById(R.id.phone_number);
        button_sumit=findViewById(R.id.submit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConfirmMobileNumberActivity.this,UserAccount.class);
                finish();
                startActivity(intent);
            }
        });

        button_sumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber=edphonenumber.getText().toString().trim();
                if(phonenumber.isEmpty())
                    return;
                mProgress.show();
                checkExistUser(phonenumber);

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
                //user not exist
                mProgress.dismiss();
                Intent intent=new Intent(ConfirmMobileNumberActivity.this,VerifyMobileActivity.class);
                intent.putExtra("authType",3);
                intent.putExtra("phoneNumber",phonenumber);
                intent.putExtra("password",phonenumber);
                intent.putExtra("facebooktokenkey",facebooktokenkey);
                intent.putExtra("facebookid",facebookid);
                intent.putExtra("facebookname",facebookname);
                intent.putExtra("imageurl",imageurl);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                //user exist
                mProgress.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(ConfirmMobileNumberActivity.this).create();
                //alertDialog.setTitle("Post ads");
                alertDialog.setMessage(getString(R.string.phone_exist));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.button_close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}
