package com.bt_121shoppe.motorbike.Login_Register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmMobileNumberActivity extends AppCompatActivity {

    private static final String TAG=ConfirmMobileNumberActivity.class.getSimpleName();
    private TextView back;
    private MaterialEditText edphonenumber;
    private Button button_sumit;

    private String facebooktokenkey,facebookid,facebookname,imageurl,gender,birth;
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
        gender=intent.getStringExtra("gender");
        birth=intent.getStringExtra("birthday");

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
//        final String username;
        try {
            result = CommonFunction.doGetRequest(url);
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            JSONObject obj=new JSONObject(result);
            int count=obj.getInt("count");
            String user = obj.getString("username");
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
                intent.putExtra("gender",gender);
                intent.putExtra("birthday",birth);
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

    private void registerWithFBRequest(String username,String password,String group,String firstname,String facebookid,String gender,String birthdate){
        String url=ConsumeAPI.BASE_URL+"api/v1/users/";
        OkHttpClient client=new OkHttpClient();
        JSONObject postdata=new JSONObject();
        JSONObject post_body=new JSONObject();
        try{
            postdata.put("username", username);
            postdata.put("password", password);
            postdata.put("first_name",firstname);
            postdata.put("last_name",facebookid);
            post_body.put("gender",gender);
            if (!birthdate.isEmpty() && birthdate != null)
                if (Build.VERSION.SDK_INT >= 26) {
                    //post_body.put("date_of_birth",convertDateofBirth(birth));
                }
            post_body.put("telephone", username);
            post_body.put("group",group);
            postdata.put("profile", post_body);
            postdata.put("groups",new JSONArray("[\"1\"]"));
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestBody body=RequestBody.create(ConsumeAPI.MEDIA_TYPE,postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.d(TAG,mMessage);
                //convertUser(mMessage);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                mProgress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "failure Response:" + mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}
