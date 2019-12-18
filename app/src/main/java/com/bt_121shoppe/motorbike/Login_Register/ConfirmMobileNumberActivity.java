package com.bt_121shoppe.motorbike.Login_Register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private int usergroup;
    private String facebooktokenkey,facebookid,facebookname,imageurl,gender,birth,userEmail,verify;
    private Intent intent;
    private ProgressDialog mProgress;
    private SharedPreferences prefer;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String FCM_API="https://fcm.googleapis.com/fcm/send";
    private String serverKey="key=AAAAc-OYK_o:APA91bFUyiHEPdYUjVatqxaVzfLPwVcd090bMY5emPPh-ubQtu76mEDAdmthgR03jYwhClbDqy0lqbSr_HAAvD0vnTqigM16YH4x-Xr1TMb3q_sz9PLtjNLpfnLi6NdCI-v6dyX6-5jB";
    private String contentType = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mobile_number);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+ConsumeAPI.FB_Notification);

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
        usergroup=intent.getIntExtra("usergroup",0);
        verify=intent.getStringExtra("Register_verify");

        back=findViewById(R.id.tvBack_account);
        edphonenumber=findViewById(R.id.phone_number);
        button_sumit=findViewById(R.id.submit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConfirmMobileNumberActivity.this, UserAccountActivity.class);
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
            Log.d(TAG,result);
            JSONObject obj=new JSONObject(result);
            int count=obj.getInt("count");
            //String user = obj.getString("username");
            if(count==0){
                //user not exist
                //mProgress.dismiss();
                /* old process with verify code sep 12 2019 */
//                Intent intent=new Intent(ConfirmMobileNumberActivity.this,VerifyMobileActivity.class);
//                intent.putExtra("authType",3);
//                intent.putExtra("phoneNumber",phonenumber);
//                intent.putExtra("password",phonenumber);
//                intent.putExtra("facebooktokenkey",facebooktokenkey);
//                intent.putExtra("facebookid",facebookid);
//                intent.putExtra("facebookname",facebookname);
//                intent.putExtra("imageurl",imageurl);
//                intent.putExtra("gender",gender);
//                intent.putExtra("birthday",birth);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                registerUserwithAPI(phonenumber);

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

    private void registerUserwithAPI(String username){
        String url=ConsumeAPI.BASE_URL+"api/v1/users/";
        OkHttpClient client=new OkHttpClient();
        JSONObject postdata=new JSONObject();
        JSONObject post_body=new JSONObject();
        try{
            String pwd="";
            if(usergroup==1)
                pwd="1234";//default password for register with fb
            else
                pwd=username;

            postdata.put("username",username);
            postdata.put("password",pwd);//default password for register with fb
            postdata.put("first_name",facebookname);
            postdata.put("last_name",facebookid);
            post_body.put("gender",gender);
            post_body.put("telephone", username);
            post_body.put("group",usergroup);
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
                convertUser(mMessage);
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



    private void convertUser(String mMessage){
        Gson gson=new Gson();
        Convert_Json_Java user=new Convert_Json_Java();
        try{
            Log.d(TAG,"Result User "+mMessage);
            user = gson.fromJson(mMessage, Convert_Json_Java.class);
            int g=user.getGroup();
            int id = user.getId();
            String username=user.getUsername();
            Log.d(TAG,"User Group "+g);
            if (id!=0){
                String pwd="";
                if(usergroup==1)
                    pwd="1234";
                else
                    pwd=username;

                SharedPreferences.Editor editor =prefer.edit();
                editor.putInt("id",id);
                editor.putString("name",username);
                editor.putString("pass",pwd);
                editor.putString("groups",String.valueOf(usergroup));
                editor.commit();

                userEmail=ConsumeAPI.PREFIX_EMAIL+id+"@email.com";
                Log.d(TAG,userEmail+" "+username);
                registerUserwithFirebase(username,userEmail,String.valueOf(usergroup));

            }else {
                android.app.AlertDialog alertDialog=new android.app.AlertDialog.Builder(ConfirmMobileNumberActivity.this).create();
                alertDialog.setTitle(getString(R.string.register));
                alertDialog.setMessage(getString(R.string.verify_code_message));
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                mProgress.dismiss();
            }

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
        catch (JsonParseException e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.app.AlertDialog alertDialog=new android.app.AlertDialog.Builder(ConfirmMobileNumberActivity.this).create();
                    alertDialog.setTitle(getString(R.string.register));
                    alertDialog.setMessage(getString(R.string.verify_code_message));
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    mProgress.dismiss();
                }
            });
        }
    }

    private void registerUserwithFirebase(String username, String userEmail, String usergroup) {
        String password=usergroup.equals("1")?"1234__":username;
        auth.createUserWithEmailAndPassword(userEmail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("coverURL","default");
                            hashMap.put("status","online");
                            hashMap.put("search",username.toString());
                            hashMap.put("password",password);
                            hashMap.put("email",userEmail);
                            hashMap.put("group",usergroup);
                            reference.child("users").child(userid).setValue(hashMap);

                            //start send notification

                            String topic="/topics/"+ConsumeAPI.FB_Notification;
                            JSONObject notification=new JSONObject();
                            JSONObject notifcationBody=new JSONObject();

                            try
                            {
                                notifcationBody.put("title", "Register");
                                notifcationBody.put("message", "Register successfully.");  //Enter your notification message
                                notifcationBody.put("to",firebaseUser.getUid());
                                notification.put("to", topic);
                                notification.put("data", notifcationBody);
                                FBPostCommonFunction.submitNofitication(firebaseUser.getUid(),notifcationBody.toString());
                                Log.e("TAG", "try");
                            }catch (JSONException e){
                                Log.e("TAG","onCreate: "+e.getMessage());
                            }
                            sendNotification(notification);
                            //end send notification

                            if(usergroup.equals("1")) {
                                Intent intent = new Intent(ConfirmMobileNumberActivity.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(ConfirmMobileNumberActivity.this, EditAccountActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                            mProgress.dismiss();
                        }
                    }
                });
    }

    private void sendNotification(JSONObject notification){
        Log.e("TAG", "sendNotification"+notification);
        RequestQueue requestQueue= Volley.newRequestQueue(this.getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(FCM_API, notification, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("TAG", "onResponse: "+response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConfirmMobileNumberActivity.this, "Request error", Toast.LENGTH_LONG).show();
                Log.i("TAG", "onErrorResponse: Didn't work");
            }
        }){
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Authorization", serverKey);
                headers.put("Content-Type", contentType);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
