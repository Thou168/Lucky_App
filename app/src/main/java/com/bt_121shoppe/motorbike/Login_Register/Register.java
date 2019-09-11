package com.bt_121shoppe.motorbike.Login_Register;

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

import com.bt_121shoppe.motorbike.Activity.Home;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextView PhoneError,PasswordError,ComfirmPassError;
    private static final String TAG = "Response";
    TextView textView;
    private Context context;
    ProgressDialog mProgress;
    SharedPreferences prefer;
    String phone;
    String comfirm;
    String pass;
    private String register_verify;
    private int product_id,user_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editPhone = (EditText) findViewById(R.id.editPhone);
        editPassword = (EditText)findViewById(R.id.editPasswordRegister);
        editComfirmPass = findViewById(R.id.editConfirm);
        PhoneError = findViewById(R.id.tv_errorPhone);
        PasswordError = findViewById(R.id.tv_errorPassword);
        ComfirmPassError = findViewById(R.id.tv_errorComfirm);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        prefer = getSharedPreferences("Register",MODE_PRIVATE);

        Intent intent = getIntent();
        register_verify = intent.getStringExtra("Register_verify");
        product_id      = intent.getIntExtra("product_id",0);
        user_group = intent.getIntExtra("user_group",0);

        btnSubmit = (Button)findViewById(R.id.btnSub);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mProgress.show();
    // check validation by samang 10/09/19
                String Number_Phone = editPhone.getText().toString();
                String Password = editPassword.getText().toString();
                String ComfirmPass = editComfirmPass.getText().toString();
                    PhoneError.setText(""); PasswordError.setText("");ComfirmPassError.setText("");
                if (Number_Phone.length()<8 || Password.length()<4 || ComfirmPass.length()<4 || !Password.equals(ComfirmPass)){
                    if (Number_Phone.length()<8){
                        PhoneError.setText("Phone number is empty");
                    }else if (Password.length()<4){
                        PasswordError.setText("Password is empty");
                    }else if (ComfirmPass.length()<4){
                        ComfirmPassError.setText("Comfirm Password is empty");
                    }else if (!Password.equals(ComfirmPass)){
                        ComfirmPassError.setText("Comfirm Password is Invalid");
                    }
                }else if (user_group == 1){
                    if (CheckNumber(ComfirmPass)){
                        Intent intent = new Intent(Register.this, VerifyMobileActivity.class);
                        intent.putExtra("authType",1);
                        intent.putExtra("phoneNumber",editPhone.getText().toString());
                        intent.putExtra("password",comfirm = editPassword.getText().toString());
                        intent.putExtra("Register_verify",register_verify);
                        intent.putExtra("product_id",product_id);
                        intent.putExtra("user_group",user_group);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        PasswordError.setText("Password is only numbers");
                        ComfirmPassError.setText("Password is only numbers");
                    }
                }else {
                    Intent intent = new Intent(Register.this, VerifyMobileActivity.class);
                    intent.putExtra("authType",1);
                    intent.putExtra("phoneNumber",editPhone.getText().toString());
                    intent.putExtra("password",comfirm = editPassword.getText().toString());
                    intent.putExtra("Register_verify",register_verify);
                    intent.putExtra("product_id",product_id);
                    intent.putExtra("user_group",user_group);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }


            }
        });
    }//create


    private Boolean CheckNumber(String st){
        Boolean check = false;
        String no = "\\d*\\.?\\d+";
        CharSequence inputStr = st;
        Pattern pte = Pattern.compile(no,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pte.matcher(inputStr);

        if (matcher.matches()){
            check = true;
        }

        return check;

    }
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
