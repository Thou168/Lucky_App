package com.bt_121shoppe.motorbike.useraccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.Startup.StartUp;
import com.bt_121shoppe.motorbike.consumeapi.CONSTANT;
import com.bt_121shoppe.motorbike.consumeapi.LoginModel;
import com.bt_121shoppe.motorbike.consumeapi.UserModel;
import com.google.gson.Gson;
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

public class LoginActivity extends AppCompatActivity {

    EditText edUsername,edPassword;
    Button btnLogin;
    ProgressDialog mProgress;
    TextView txErrorMsg;
    SharedPreferences preferences;

    final String TAG=LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
        edUsername=(EditText) findViewById(R.id.ed_username);
        edPassword=(EditText) findViewById(R.id.ed_password);
        btnLogin=(Button) findViewById(R.id.btn_login);
        txErrorMsg=(TextView)findViewById(R.id.txErrorMessage);
        */
        preferences=getSharedPreferences("Register",MODE_PRIVATE);

        mProgress=new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edUsername.getText().toString();
                String password=edPassword.getText().toString();

                try {
                    submitPostRequest(username, password);
                }catch (IOException e){
                    e.printStackTrace();
                    mProgress.dismiss();
                }
            }
        });
    }

    private void submitPostRequest(String username,String password) throws IOException{
        String apiEndPoint= CONSTANT.BASE_URL+"api/v1/rest-auth/login/";
        OkHttpClient client=new OkHttpClient();
        JSONObject postdata=new JSONObject();
        try{
            postdata.put("username",username);
            postdata.put("password",password);

        } catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(CONSTANT.MEDIA_TYPE,postdata.toString());
        Request request=new Request.Builder()
                .url(apiEndPoint)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                jsonResultConverting(mMessage);
                Log.e(TAG, mMessage);
            }
        });
    }

    private void jsonResultConverting(String result){
        Gson gson=new Gson();
        try{
            LoginModel loginModel=new LoginModel();
            loginModel=gson.fromJson(result,LoginModel.class);
            final String token=loginModel.getToken();
            final UserModel user=loginModel.getUser();
            final int pk=user.getPk();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(token!=null){
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("token",token);
                        editor.putString("name",edUsername.getText().toString());
                        editor.putString("pass",edPassword.getText().toString());
                        editor.putInt("pk",pk);
                        editor.commit();

                        mProgress.dismiss();
                        Intent intent=new Intent(LoginActivity.this, StartUp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        txErrorMsg.setText(getString(R.string.login_fail));
                        mProgress.dismiss();
                    }
                }
            });
        }catch (JsonParseException e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txErrorMsg.setText(getString(R.string.login_fail));
                    mProgress.dismiss();
                }
            });
        }
    }
}
