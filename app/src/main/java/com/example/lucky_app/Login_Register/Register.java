package com.example.lucky_app.Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucky_app.Activity.Home;
import com.example.lucky_app.Api.ConsumeAPI;
import com.example.lucky_app.Api.Convert_Json_Java;
import com.example.lucky_app.R;
import com.example.lucky_app.Startup.StartUp;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    ImageButton btnFacebookLogin;
    private Button btnSubmit;
    private EditText editPhone,editComfirmPass,editPassword;
    private static final String TAG = "Response";
    TextView textView;
    private Context context;
    ProgressDialog mProgress;
    SharedPreferences prefer;
    String phone;
    String comfirm;
    String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editPhone = (EditText) findViewById(R.id.editPhone);
        editPassword = (EditText)findViewById(R.id.editPasswordRegister);
        editComfirmPass = (EditText)findViewById(R.id.editConfirm);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        prefer = getSharedPreferences("Register",MODE_PRIVATE);

        btnSubmit = (Button)findViewById(R.id.btnSub);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                if (editPassword.getText().toString().equals(editComfirmPass.getText().toString())) {
                    postRequest();
                }else {
                    Toast.makeText(v.getContext(),"Password incorrect",Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }
        });
    }//create

    private void postRequest() {
        phone = editPhone.getText().toString();
        pass= editPassword.getText().toString();
        comfirm = editPassword.getText().toString();


        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        String url =String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/users/");

        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        JSONObject post_body = new JSONObject();

        try {
            postdata.put("username", phone);
            postdata.put("password", comfirm);

            post_body.put("telephone", phone);

            postdata.put("profile", post_body);
            postdata.put("groups", new JSONArray("[\"1\"]"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

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
                Log.e(TAG, mMessage);
                converting(mMessage);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                mProgress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "failure Response:" + mMessage, Toast.LENGTH_SHORT).show();
                    }
                });

                //call.cancel();
            }
        });
    }

    private void converting(String mMessage) {
        Gson gson = new Gson();
        Convert_Json_Java convertJsonJava = new Convert_Json_Java();
        try{
            convertJsonJava = gson.fromJson(mMessage,Convert_Json_Java.class);
            int[] gg = convertJsonJava.getGroups();
            final int g=gg[0];
            Log.d(TAG, convertJsonJava.getUsername() + "\t" + convertJsonJava.getEmail() + "\t" + convertJsonJava.getId()+ "\t" + g+ "\t"  + convertJsonJava.getStatus());
            final int id = convertJsonJava.getId();


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (id!=0){
                        SharedPreferences.Editor editor =prefer.edit();
                        editor.putInt("id",id);
                        editor.putString("name",phone);
                        editor.putString("pass",pass);
                        editor.putString("groups", String.valueOf(g));
                        editor.commit();

                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),"Register Success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }else {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),"register failure",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (JsonParseException e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"register failure",Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
