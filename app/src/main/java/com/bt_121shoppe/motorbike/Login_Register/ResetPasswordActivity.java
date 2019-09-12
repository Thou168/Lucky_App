package com.bt_121shoppe.motorbike.Login_Register;

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

import com.bt_121shoppe.motorbike.Activity.Home;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ChangepassModel;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
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
        /*
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
           */
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
                Log.d(TAG,user.getEmail());
                //setFirebaseUserPassword(user.getPassword());

                //check to get password
                if(user.getGroup().equals("1")){
                    String pwd=password.substring(0,3);
                    //Log.d(TAG,"password "+pwd);
                    encodeAuth= "Basic "+ CommonFunction.getEncodedString(username,pwd);
                }else{
                    encodeAuth= "Basic "+ CommonFunction.getEncodedString(username,password);
                }

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
                        String newPassword;
                        if(user.getGroup().equals("1")){
                            newPassword=password1+"__";
                        }
                        else
                            newPassword=password1;
                        /*update firebase password*/
                        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("password",newPassword);
                        databaseReference.updateChildren(hashMap);

                        //reset password for firebase user
                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential= EmailAuthProvider.getCredential(user.getEmail(),user.getPassword());
                        firebaseUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d(TAG,"Password updated");

                                                    }else{
                                                        Log.d(TAG,"Error password not update");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

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
