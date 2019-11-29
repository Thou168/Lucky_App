package com.bt_121shoppe.motorbike.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bt_121shoppe.motorbike.Activity.Account;
import com.bt_121shoppe.motorbike.Activity.Camera;
import com.bt_121shoppe.motorbike.Activity.Home;
import com.bt_121shoppe.motorbike.Activity.Message;
import com.bt_121shoppe.motorbike.Activity.NotificationActivity;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton btnFacebookLogin;
    private Button btnSubmit;
    private EditText editPhone,editComfirmPass,editPassword;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextView textView;
    private TextView PhoneError,PasswordError,ComfirmPassError;
    private Context context;
    private ProgressDialog mProgress;
    SharedPreferences prefer;
    String phone;
    String comfirm;
    String pass;
    private String userEmail;
    private String register_verify;
    private int product_id,user_group;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private AlertDialog.Builder dialog;
    private String FCM_API="https://fcm.googleapis.com/fcm/send";
    private String serverKey="key=AAAAc-OYK_o:APA91bFUyiHEPdYUjVatqxaVzfLPwVcd090bMY5emPPh-ubQtu76mEDAdmthgR03jYwhClbDqy0lqbSr_HAAvD0vnTqigM16YH4x-Xr1TMb3q_sz9PLtjNLpfnLi6NdCI-v6dyX6-5jB";
    private String contentType = "application/json";

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
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+ConsumeAPI.FB_Notification);

        Intent intent = getIntent();
        register_verify = intent.getStringExtra("Register_verify");
        product_id      = intent.getIntExtra("product_id",0);
        user_group = intent.getIntExtra("user_group",0);

        if (user_group == 1){
            PasswordError.setText(R.string.user_message);
            ComfirmPassError.setText(R.string.user_message);
            editPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editComfirmPass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editPassword.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(4)});
            editComfirmPass.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(4)});

        }else {
            PasswordError.setText(R.string.dealer_message);
            ComfirmPassError.setText(R.string.dealer_message);
        }

        editComfirmPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnSubmit.performClick();
                }
                return false;
            }
        });
        btnSubmit = (Button)findViewById(R.id.btnSub);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check validation by samang 10/09/19
                pass=editPassword.getText().toString();
                String Number_Phone = editPhone.getText().toString();
                String Password = editPassword.getText().toString();
                String ComfirmPass = editComfirmPass.getText().toString();
                if (user_group == 1){
                    if (Number_Phone.length()<9 || Password.length() != 4  || !Password.equals(ComfirmPass)) {
//                    register_error();
                        if (Number_Phone.isEmpty()) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhone);
                        } else if (Number_Phone.length() < 9) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhoneWrong);
                        } else {
                            PhoneError.setText("");
                        }

                        if (Password.isEmpty()) {
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.inputPassword);
                        } else if (Password.length() != 4) {
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.user_message);
                        } else {
                            PasswordError.setText("");
                        }

                        if (ComfirmPass.isEmpty()) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.inputComfirm);
                        } else if (!Password.equals(ComfirmPass)) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.wrongInputPasswordSecond);
                        } else {
                            ComfirmPassError.setText("");
                        }
                    }else if (CheckNumber(ComfirmPass)) {
                        mProgress.show();
                        PhoneError.setText("");
                        ComfirmPassError.setText("");
                        PasswordError.setText("");
                        /* block for verify code sep 12 2019 */
//                        Intent intent = new Intent(RegisterActivity.this, VerifyMobileActivity.class);
//                        intent.putExtra("authType", 1);
//                        intent.putExtra("phoneNumber", editPhone.getText().toString());
//                        intent.putExtra("password", comfirm = editPassword.getText().toString());
//                        intent.putExtra("Register_verify", register_verify);
//                        intent.putExtra("product_id", product_id);
//                        intent.putExtra("user_group", user_group);
//                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        registerAPIUser(editPhone.getText().toString(),Password,user_group);
                    } else {
                        mProgress.dismiss();
                    }
                }else {
                    Pattern lowerCasePatten = Pattern.compile("[a-zA-Z ]");
                    Pattern digitCasePatten = Pattern.compile("[0-9 ]");
                    if (Number_Phone.length()<9 || !lowerCasePatten.matcher(Password).find() || Password.trim().length()<6 || !digitCasePatten.matcher(Password).find() || !Password.equals(ComfirmPass)) {
//                    register_error();
                        if (Number_Phone.isEmpty()) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhone);
                        } else if (Number_Phone.length() < 9) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhoneWrong);
                        } else {
                            PhoneError.setText("");
                        }

                        if (Password.isEmpty()) {
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.inputPassword);
                        }
                        else if (Password.trim().length()<6){
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.user_message_dealer);
                        }
                        else if (!lowerCasePatten.matcher(Password).find() || !digitCasePatten.matcher(Password).find()){
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.valid_dealer_pass);
                        }
                        else {
                            PasswordError.setText("");
                        }

                        if (ComfirmPass.isEmpty()) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.inputComfirm);
                        } else if (!Password.equals(ComfirmPass)) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.wrongInputPasswordSecond);
                        } else {
                            ComfirmPassError.setText("");
                        }
                    }else {
                        mProgress.show();
                        PhoneError.setText("");
                        ComfirmPassError.setText("");
                        PasswordError.setText("");
                        registerAPIUser(editPhone.getText().toString(), Password, user_group);
                    }
                }
            }
        });
    }

    private void register_error(){
        dialog = new AlertDialog.Builder(RegisterActivity.this,R.style.AlertDialogCustom);
        dialog.setTitle(R.string.head_dialog_register);
        dialog.setMessage(R.string.wrong_pass_phone);
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_delete);
        dialog.setPositiveButton(R.string.btn_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    private void registerAPIUser(String username,String password,int group){
        String url=ConsumeAPI.BASE_URL+"api/v1/users/";
        OkHttpClient client=new OkHttpClient();
        JSONObject postdata=new JSONObject();
        JSONObject post_body=new JSONObject();
        try{
            postdata.put("username", username);
            postdata.put("password", password);
            post_body.put("telephone", username);
            post_body.put("group",group);
            postdata.put("profile", post_body);
            postdata.put("groups",new JSONArray("[\"1\"]"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG,"Data"+postdata);
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
        Gson gson =new  Gson();
        Convert_Json_Java convertJsonJava =new Convert_Json_Java();
        try {
            convertJsonJava = gson.fromJson(mMessage, Convert_Json_Java.class);
            int g=convertJsonJava.getGroup();
            int id = convertJsonJava.getId();
            String username=convertJsonJava.getUsername();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (id!=0){
                        SharedPreferences.Editor editor =prefer.edit();
                        editor.putInt("id",id);
                        editor.putString("name",username);
                        editor.putString("pass",editComfirmPass.getText().toString());
                        editor.putString("groups",String.valueOf(g));
                        editor.commit();

                        userEmail=ConsumeAPI.PREFIX_EMAIL+id+"@email.com";
                        //Log.d(TAG,"2221111@@---- "+userEmail+" "+username+" "+pass+","+user_group);
                        if (user_group == 1) {
                            registerUserFirebase(username, userEmail, pass, String.valueOf(1));
                        }else if (user_group == 3){
                            registerUserAccount(username, userEmail, pass, String.valueOf(3),id);
                        }
                    }else {
                        AlertDialog alertDialog=new AlertDialog.Builder(RegisterActivity.this).create();
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
                }
            });

        } catch (JsonParseException e) {
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog=new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle(getString(R.string.register));
                    alertDialog.setMessage(getString(R.string.verify_code_message));
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    PhoneError.setText(R.string.verify_code_message);
                    PhoneError.setTextColor(getColor(R.color.red));
                    alertDialog.show();
                    mProgress.dismiss();
                }
            });

        }
    }

    private void registerUserFirebase(String username,String email,String pass1,String group){
        String password=group.equals("1")?pass1+"__":pass1; //if group=1 is public user
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userId=firebaseUser.getUid();
                            //save user information to firebase
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("coverURL","default");
                            hashMap.put("status","online");
                            hashMap.put("search",username.toString());
                            hashMap.put("password",password);
                            hashMap.put("email",email);
                            hashMap.put("group",group);
                            reference.child("users").child(userId).setValue(hashMap);

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

                            Intent intent;
                            if(register_verify!=null) {
                                switch (register_verify) {
                                    case "notification":
                                        intent = new Intent(RegisterActivity.this, NotificationActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "camera":
                                        intent = new Intent(RegisterActivity.this, Camera.class);
                                        intent.putExtra("Register_verify",register_verify);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "message":
                                        intent = new Intent(RegisterActivity.this, ChatMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "account":
                                        intent = new Intent(RegisterActivity.this, Account.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "detail":
                                        intent = new Intent(RegisterActivity.this, Detail_New_Post.class);
                                        intent.putExtra("ID", product_id);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        intent = new Intent(RegisterActivity.this, Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
                            }else{
                                intent = new Intent(RegisterActivity.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                            mProgress.dismiss();
                        }else{
                            Toast.makeText(RegisterActivity.this,"You cannot register with email or password."+task.getException(),Toast.LENGTH_SHORT).show();

                            Log.d(TAG,"Error "+task.getException()+" "+task.getResult());
                        }
                    }
                });
    }

    // for intent to edit_account by samang 12/09
    private void registerUserAccount(String username, String email, String pass1, String group, int id){
        String password=group.equals("3")?pass1+"__":pass1;
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userId=firebaseUser.getUid();
                            //save user information to firebase
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("coverURL","default");
                            hashMap.put("status","online");
                            hashMap.put("search",username.toString());
                            hashMap.put("password",password);
                            hashMap.put("email",email);
                            hashMap.put("group",group);
                            reference.child("users").child(userId).setValue(hashMap);

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


                            Intent intent = new Intent(RegisterActivity.this, EditAccountActivity.class);
                            intent.putExtra("id_register",id);
                            intent.putExtra("ID",product_id);
                            intent.putExtra("Register_verify",register_verify);
                            startActivity(intent);
                            mProgress.dismiss();

                        }else{
                            Toast.makeText(RegisterActivity.this,"You cannot register with email or password."+task.getException(),Toast.LENGTH_SHORT).show();
                            //Log.d(TAG,"Error "+task.getException()+" "+task.getResult());
                        }
                    }
                });
          }
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

    private RequestQueue requestQueue(){
        return Volley.newRequestQueue(this.getApplicationContext());
    }

    private void sendNotification(JSONObject notification){
        Log.e("TAG", "sendNotification"+notification);
        RequestQueue requestQueue=Volley.newRequestQueue(this.getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(FCM_API, notification, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("TAG", "onResponse: "+response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Request error", Toast.LENGTH_LONG).show();
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
