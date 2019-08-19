package com.bt_121shoppe.lucky_app.Login_Register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.lucky_app.Activity.Home;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.Setting.ChangePassword;
import com.bt_121shoppe.lucky_app.models.ChangepassModel;
import com.bt_121shoppe.lucky_app.models.User;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG=ResetPasswordActivity.class.getSimpleName();
    private Intent intent;
    private String username,password,encodeAuth,firebasePassword;
    private Button btnSubmit;
    private EditText etPassword,etConfirmPassword;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgress;
    private SharedPreferences prefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        intent=getIntent();
        username=intent.getStringExtra("phonenumber");
        password=intent.getStringExtra("password");

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.progress_waiting));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                setFirebaseUserPassword(user.getPassword());
                encodeAuth= "Basic "+ CommonFunction.getEncodedString(username,user.getPassword());
                Log.d(TAG,"Password in "+ encodeAuth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Log.d(TAG,"RUN RESULT "+username+" "+password);
        btnSubmit=findViewById(R.id.submit);
        etPassword=findViewById(R.id.etNewpass);
        etConfirmPassword=findViewById(R.id.etComfirmpass);

        TextView close=findViewById(R.id.tvBack);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent=new Intent(ResetPasswordActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etPassword.getText().toString().trim().isEmpty() && !etConfirmPassword.getText().toString().isEmpty()) {
                    mProgress.show();
                    if (etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
                        postResetPassword(etPassword.getText().toString().trim(), etConfirmPassword.getText().toString().trim());
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                }
            }
        });

    }

    private void postResetPassword(String password1,String password2){

        String url= ConsumeAPI.BASE_URL+"resetpassword/";
        OkHttpClient client=new OkHttpClient();
        JSONObject object=new JSONObject();
        try{
            object.put("new_password1",password1);
            object.put("new_password2",password2);
        }catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(ConsumeAPI.MEDIA_TYPE, object.toString());

        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                setFirebaseUserPassword(user.getPassword());
                encodeAuth= "Basic "+ CommonFunction.getEncodedString(username,user.getPassword());
                Request request = new Request.Builder()
                        .url(url)
                        .put(body)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .header("Authorization",encodeAuth)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String st = response.body().string();
                        Log.d("Result",st);
                        /*update firebase password*/
                        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("password",password1);
                        databaseReference.updateChildren(hashMap);

                        Gson gson = new Gson();
                        ChangepassModel model = new ChangepassModel();
                        try{
                            model=gson.fromJson(st,ChangepassModel.class);
                            if (model!=null){
                                int statusCode = model.getStatus();
                                if (statusCode==201){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
                                            alertDialog.setTitle(getString(R.string.reset_password_title));
                                            alertDialog.setMessage("Your password has been reset.");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            SharedPreferences.Editor editor = prefer.edit();
                                                            editor.clear();
                                                            editor.commit();
                                                            startActivity(new Intent(ResetPasswordActivity.this, Home.class));
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                    });

                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),st,Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
                                        alertDialog.setTitle(getString(R.string.reset_password_title));
                                        alertDialog.setMessage("Your password has been error while changing.");
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setFirebaseUserPassword(String password){
        firebasePassword=password;
    }

}
