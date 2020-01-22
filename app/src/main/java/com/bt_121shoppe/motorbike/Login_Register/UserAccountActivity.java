package com.bt_121shoppe.motorbike.Login_Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.CheckGroup;
import com.bt_121shoppe.motorbike.activities.Dealerstore;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.activities.NotificationActivity;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.models.User;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAccountActivity extends AppCompatActivity {

    private static final String TAG= UserAccountActivity.class.getSimpleName();
    private Button Login,Register;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String verify;
    private int product_id;

    private FirebaseAuth auth;
    private SharedPreferences prefer;
    private int pk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LoginManager.getInstance().logOut();

        Intent intent = getIntent();
        verify = intent.getStringExtra("verify");
        product_id = intent.getIntExtra("product_id",0);
        SharedPreferences preferences= getApplication().getSharedPreferences("Register",MODE_PRIVATE);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }
        Log.e("pk", String.valueOf(pk));

        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        auth=FirebaseAuth.getInstance();

        Login = (Button)findViewById(R.id.btnLogin);
        Register =(Button)findViewById(R.id.btnRegister);
        loginButton=(LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_gender","user_location"));

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("Login_verify",verify);
                intent.putExtra("product_id",product_id);
                startActivity(intent);

                //Log.d("LoginActivity Verify",verify);
            //    startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectUserTypeActivity.class);
                intent.putExtra("Register_verify",verify);
                intent.putExtra("product_id",product_id);
                intent.putExtra("processtype",CommonFunction.ProcessType.MobileRegister.toString());
                startActivity(intent);
   //             startActivity(new Intent(v.getContext(),RegisterActivity.class));
            }
        });

        callbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken=loginResult.getAccessToken();
                //Toast.makeText(UserAccountActivity.this,accessToken.toString(),Toast.LENGTH_LONG).show();
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
    }

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

                            Intent intent=new Intent(UserAccountActivity.this, SelectUserTypeActivity.class);
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
                    User user=snapshot.getValue(User.class);
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
                            intent = new Intent(UserAccountActivity.this, Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            CheckGroup check = new CheckGroup();
                            int g = check.getGroup(pk, UserAccountActivity.this);
                            if (g != 3) {
                                switch (verify) {
                                    case "notification":
                                        intent = new Intent(UserAccountActivity.this, NotificationActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "camera":
                                        intent = new Intent(UserAccountActivity.this, Camera.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "message":
                                        intent = new Intent(UserAccountActivity.this, ChatMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "account":
                                        intent = new Intent(UserAccountActivity.this, Account.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "detail":
                                        intent = new Intent(UserAccountActivity.this, Detail_new_post_java.class);
                                        intent.putExtra("ID", product_id);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        intent = new Intent(UserAccountActivity.this, Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
                            }else {
                                switch (verify) {
                                    case "notification":
                                        intent = new Intent(UserAccountActivity.this, NotificationActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "camera":
                                        intent = new Intent(UserAccountActivity.this, Dealerstore.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "message":
                                        intent = new Intent(UserAccountActivity.this, ChatMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "account":
                                        intent = new Intent(UserAccountActivity.this, Account.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "detail":
                                        intent = new Intent(UserAccountActivity.this, Detail_new_post_java.class);
                                        intent.putExtra("ID", product_id);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        intent = new Intent(UserAccountActivity.this, Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
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
}
