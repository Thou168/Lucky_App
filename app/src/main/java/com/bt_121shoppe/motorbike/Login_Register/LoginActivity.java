package com.bt_121shoppe.motorbike.Login_Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.activities.NotificationActivity;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText Username,Password;
    private Button btnSubmit;
    private static final String TAG = "Response";
    private String name,pass;
    private Context context;
    ProgressDialog mProgress;
    SharedPreferences prefer;
    private String login_verify,verify;
    private int product_id;
    private String username_message;
    private String password_message;
    private TextView alert_phone,back,tv_signup;
    private TextView alert_password;
    private LoginButton facebook_login;
    int error = 0;
    String encode;

    private AlertDialog.Builder dialog;
    private CallbackManager callbackManager;
    private FirebaseAuth auth;
//    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LoginManager.getInstance().logOut();

        encode = "Basic "+com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString(username_message,password_message);
        alert_phone = (TextView)findViewById(R.id.phone_alert);
        alert_password = (TextView)findViewById(R.id.password_alert);
        Username = (EditText)findViewById(R.id.editPhoneLogin);
        Password = (EditText)findViewById(R.id.editPasswordLogin);
        btnSubmit = (Button)findViewById(R.id.btnSubmitLogin);
        back = (TextView) findViewById(R.id.tv_back);
        tv_signup = findViewById(R.id.sign_up);
        facebook_login = findViewById(R.id.login_button);
        facebook_login.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_gender","user_location"));

        prefer = getSharedPreferences("Register",MODE_PRIVATE);
//        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        auth=FirebaseAuth.getInstance();

        Intent intent = getIntent();
        verify = intent.getStringExtra("verify");
        login_verify = intent.getStringExtra("Login_verify");
        product_id   = intent.getIntExtra("product_id",0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Home.class));
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SelectUserTypeActivity.class);
                intent.putExtra("Register_verify",verify);
                intent.putExtra("product_id",product_id);
                intent.putExtra("processtype", CommonFunction.ProcessType.MobileRegister.toString());
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                postRequest();
                jilapatitus();
            }
        });

        TextView tvForgetPassword=findViewById(R.id.tv_forget_password);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SearchAccountActivity.class);
                startActivity(intent);
            }
        });
        Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnSubmit.performClick();
                }
                return false;
            }
        });
        callbackManager= CallbackManager.Factory.create();
        facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken=loginResult.getAccessToken();
                userLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook login error",error.getMessage());
                //Toast.makeText(UserAccountActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    } //create
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void userLoginInformation(AccessToken accessToken){
        //Toast.makeText(UserAccountActivity.this,accessToken.toString(),Toast.LENGTH_LONG).show();
        GraphRequest request=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("bjfjfjfjjfjjj",response.toString());
                try{
                    String name=object.getString("name");
                    String facebookid=object.getString("id");
//                    String gender=object.getString("gender");
//                    String birth=object.getString("birthday");
                    String image=object.getJSONObject("picture").getJSONObject("data").getString("url");
//                    displayName.setText(name);
//                    emailId.setText(email);
//                    Glide.with(getApplicationContext()).load(image).into(displayImage);
                    String url= ConsumeAPI.BASE_URL+"api/v1/userfilter/?last_name="+facebookid;
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
                            //register user
                            /* old proccess  sep 12 2019  */
//                            Intent intent=new Intent(UserAccountActivity.this,ConfirmMobileNumberActivity.class);
//                            intent.putExtra("facebooktokenkey",accessToken.toString());
//                            intent.putExtra("facebookid",facebookid);
////                            intent.putExtra("gender",gender);
////                            intent.putExtra("birthday",birth);
//                            intent.putExtra("facebookname",name);
//                            intent.putExtra("imageurl",image);
//                            startActivity(intent);

                            Intent intent=new Intent(LoginActivity.this, SelectUserTypeActivity.class);
                            intent.putExtra("processtype",CommonFunction.ProcessType.FacebookRegister.toString());
                            intent.putExtra("facebooktokenkey",accessToken.toString());
                            intent.putExtra("facebookid",facebookid);
                            intent.putExtra("facebookname",name);
                            intent.putExtra("imageurl",image);
                            intent.putExtra("Register_verify",verify);
                            intent.putExtra("product_id",product_id);
                            startActivity(intent);
                        }
                        else{
                            //login user
                            Log.d(TAG,"RUN RESULT "+result);
                            JSONArray jsonArray=obj.getJSONArray("results");
                            JSONObject obj1=jsonArray.getJSONObject(0);
                            int apiUserId=obj1.getInt("id");
                            String apiUsername=obj1.getString("username");

                            /* old process sep 12 2019 */
//                            Intent intent=new Intent(UserAccountActivity.this,VerifyMobileActivity.class);
//                            intent.putExtra("Register_verify",verify);
//                            intent.putExtra("Login_verify",verify);
//                            intent.putExtra("authType",4);
//                            intent.putExtra("phoneNumber",apiUsername);
//                            intent.putExtra("password",apiUsername);
//                            intent.putExtra("facebooktokenkey",accessToken.toString());
////                            intent.putExtra("gender",gender);
////                            intent.putExtra("birthday",birth);
//                            intent.putExtra("facebookid",facebookid);
//                            intent.putExtra("facebookname",name);
//                            intent.putExtra("imageurl",image);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);

                            searchFBUser(apiUsername);

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,birthday,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }
    private void searchFBUser(String username){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.e("LOG",snapshot.getKey());
                    com.bt_121shoppe.motorbike.models.User user=snapshot.getValue(com.bt_121shoppe.motorbike.models.User.class);
                    if(user.getUsername().equals(username)){
                        String group=user.getGroup();
                        String password=user.getPassword();
                        String email=user.getEmail();
                        if(group.equals("1"))
                            password=password.substring(0,4);

                        String finalPassword = password;
                        auth.signInWithEmailAndPassword(email,user.getPassword())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            loginWithAPI(username, finalPassword);
                                        }
                                    }
                                });

                        Log.d(TAG,"Group "+group+" password "+password);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loginWithAPI(String username,String password){
        String url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/rest-auth/login/");
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username", username);
            postdata.put("password", password);

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
                if(response.isSuccessful()) {
                    final String mMessage = response.body().string();
                    Log.d(TAG,mMessage);
                    converting(mMessage,username,password);
                }else {
                    //mProgress.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //error = 1;
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //mProgress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"failure Response:"+ mMessage,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void converting(String mMessage,String username,String password) {
        Gson gson = new Gson();
        Convert_Json_Java convertJsonJava = new Convert_Json_Java();
        try{
            convertJsonJava = gson.fromJson(mMessage,Convert_Json_Java.class);
            Log.d(TAG, convertJsonJava.getUsername()   + "\t" + convertJsonJava.getToken() + "\t" + convertJsonJava.getStatus());
            final String key = convertJsonJava.getToken();
            com.bt_121shoppe.motorbike.Api.User user = convertJsonJava.getUser();
            final int pk = user.getPk();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (key!=null){
                        Log.d("Get Key",key.toString());

                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putString("token",key);
                        editor.putString("name",username);
                        editor.putString("pass",password);
                        editor.putInt("Pk",pk);
                        editor.commit();
                        //mProgress.dismiss();
                        Intent intent;
                        if(verify==null){
                            intent = new Intent(LoginActivity.this, Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            switch (verify) {
                                case "notification":
                                    intent = new Intent(LoginActivity.this, NotificationActivity.class);
                                    intent.putExtra("verify",verify);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "camera":
                                    intent = new Intent(LoginActivity.this, Camera.class);
                                    intent.putExtra("verify",verify);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "message":
                                    intent = new Intent(LoginActivity.this, ChatMainActivity.class);
                                    intent.putExtra("verify",verify);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "account":
                                    intent = new Intent(LoginActivity.this, Account.class);
                                    intent.putExtra("verify",verify);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "detail":
                                    intent = new Intent(LoginActivity.this, Detail_new_post_java.class);
                                    intent.putExtra("ID", product_id);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;
                                default:
                                    intent = new Intent(LoginActivity.this, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;
                            }
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"LoginActivity failure",Toast.LENGTH_SHORT).show();
                        //mProgress.dismiss();
                    }
                }
            });
        }catch (JsonParseException e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"LoginActivity failure",Toast.LENGTH_SHORT).show();
                    //mProgress.dismiss();
                }
            });
        }
    }

    private void jilapatitus(){
        String tpm = Username.getText().toString();
        Service api = Client.getClient().create(Service.class);
        retrofit2.Call<AllResponse> call = api.getUsername(tpm);
        call.enqueue(new retrofit2.Callback<AllResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AllResponse> call, retrofit2.Response<AllResponse> response) {

                //Log.d("33333",String.valueOf(response.body().getCount()));
                String empty_user = Username.getText().toString();
                String empty_pass = Password.getText().toString();

                //phone
                if (empty_user.isEmpty()){
                    alert_phone.setText(R.string.alert_phone1);
                    alert_phone.setTextColor(getResources().getColor(R.color.red));
                }else if(response.body().getCount()==1 ){
                    if (response.body().getCount()==1){
                        alert_phone.setText("");
                    }
                }else {
                    alert_phone.setText(R.string.alert_phone);
                    alert_phone.setTextColor(getResources().getColor(R.color.red));
                }

                //password
                if (empty_pass.isEmpty()){
                    alert_password.setText(R.string.secret_number1);
                    alert_password.setTextColor(getResources().getColor(R.color.red));
                }else if (!empty_pass.isEmpty()){
                    if (error == 1){
                        alert_password.setText(R.string.secret_number);
                        alert_password.setTextColor(getResources().getColor(R.color.red));
                    }
                    else {
                        alert_password.setText("");
                        Log.d("ajdmsamwjdjansmd","mgnnwejwgwekfkk");
                    }
                }
                mProgress.dismiss();

            }

            @Override
            public void onFailure(retrofit2.Call<AllResponse> call, Throwable t) {


            }
        });
    }

//    private void login_wrong(){
//
//        dialog = new Dialog(this,R.style.MyDialogTheme);
//        dialog.setContentView(R.layout.dialog_custom);
//        head = (TextView)dialog.findViewById(R.id.a);
//        head.setText(R.string.head_dialog_login);
//        head.setTextColor(getResources().getColor(R.color.dack_color));
//        head.setTextSize(30);
//        info_image = (ImageView)dialog.findViewById(R.id.log_info);
//        info_image.setImageResource(R.drawable.ic_close_black_24dp);
//        title = (TextView) dialog.findViewById(R.id.text_dialog);
//        title.setText(R.string.wrong_pass_phone);
//        title.setTextSize(15);
//        btnOk = (Button)dialog.findViewById(R.id.btn_dialog);
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.show();
//
//    }

    private void login_error(){
        dialog = new AlertDialog.Builder(LoginActivity.this,R.style.AlertDialogCustom);
        dialog.setTitle(R.string.head_dialog_login);
        dialog.setMessage(R.string.wrong_pass_phone);
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_delete);
        dialog.setPositiveButton(R.string.btn_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Password.requestFocus();
//                imm.showSoftInput(Password,InputMethodManager.SHOW_IMPLICIT);
            }
        });
        dialog.create();
        dialog.show();
    }

    private void postRequest() {
        name = Username.getText().toString();
        pass = Password.getText().toString();
        String url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/rest-auth/login/");
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username", name);
            postdata.put("password", pass);

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
                if(response.isSuccessful()) {
                    final String mMessage = response.body().string();
                    Log.d(TAG,mMessage);
                    converting(mMessage);

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            login_wrong();
//                            login_error();
                            error = 1;
                            Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_failure),Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                mProgress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"failure Response:"+ mMessage,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    } // postRequest

    private void converting(String mMessage) {
        Gson gson = new Gson();
        Convert_Json_Java convertJsonJava = new Convert_Json_Java();
        try{
            convertJsonJava = gson.fromJson(mMessage,Convert_Json_Java.class);
            Log.d(TAG, convertJsonJava.getUsername()   + "\t" + convertJsonJava.getToken() + "\t" + convertJsonJava.getStatus());
            final String key = convertJsonJava.getToken();
            User user = convertJsonJava.getUser();
            final int pk = user.getPk();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (key!=null){
                        Log.d("Get Key",key.toString());

                        //login with confirm code sep 10 2019
//                        Intent intent = new Intent(LoginActivity.this, VerifyMobileActivity.class);
//                        intent.putExtra("authType",2);
//                        intent.putExtra("phoneNumber",name);
//                        intent.putExtra("password",pass);
//                        intent.putExtra("Login_verify",login_verify);
//                        intent.putExtra("product_id",product_id);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        mProgress.dismiss();
//                        startActivity(intent);
//                        finish();

                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putString("token",key);
                        editor.putString("name",Username.getText().toString());
                        editor.putString("pass",Password.getText().toString());
                        editor.putInt("Pk",pk);
                        editor.apply();

                        loginEmailWithFirebase(user.getUsername());

                    }else {
                        Toast.makeText(getApplicationContext(),"LoginActivity failure",Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                }
            });
        }catch (JsonParseException e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"LoginActivity failure",Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            });
        }
    }

    private void loginEmailWithFirebase(String username){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    com.bt_121shoppe.motorbike.models.User user=snapshot.getValue(com.bt_121shoppe.motorbike.models.User.class);
                    Log.e("user",""+user);
                    Log.e("username",""+username);
                    Log.e("email", "" + user.getEmail());
                    Log.e("password", "" + user.getPassword());
                    if (user.getUsername() != null) {
                        if (user.getUsername().equals(username)) {
                            auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                setfirebasepassword(user.getId(), user.getPassword());
                                                //changeFirebasePassword();
                                                Intent intent;
                                                if (verify == null) {
                                                    intent = new Intent(LoginActivity.this, Home.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    switch (verify) {
                                                        case "notification":
                                                            intent = new Intent(LoginActivity.this, NotificationActivity.class);
                                                            intent.putExtra("verify",verify);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                            break;
                                                        case "camera":
                                                            intent = new Intent(LoginActivity.this, Camera.class);
                                                            intent.putExtra("verify",verify);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                            break;
                                                        case "message":
                                                            intent = new Intent(LoginActivity.this, ChatMainActivity.class);
                                                            intent.putExtra("verify",verify);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                            break;
                                                        case "account":
                                                            intent = new Intent(LoginActivity.this, Account.class);
                                                            intent.putExtra("verify",verify);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                            break;
                                                        case "detail":
                                                            intent = new Intent(LoginActivity.this, Detail_new_post_java.class);
                                                            intent.putExtra("ID", product_id);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                            break;
                                                        default:
                                                            intent = new Intent(LoginActivity.this, Home.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                            break;
                                                    }
                                                }
                                                mProgress.dismiss();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "You cannot login with email or password.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setfirebasepassword(String userid,String password){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(userid);
        HashMap<String,Object> hashMap=new HashMap<>();
        //hashMap.put("password",password);
        hashMap.put("status","online");
        reference.updateChildren(hashMap);
    }

    private void changeFirebasePassword(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential= EmailAuthProvider.getCredential("user12155@email.com","1234__");
        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            firebaseUser.updatePassword("rootuser").addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }
}