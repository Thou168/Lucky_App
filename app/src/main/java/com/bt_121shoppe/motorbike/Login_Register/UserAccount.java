package com.bt_121shoppe.motorbike.Login_Register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class UserAccount extends AppCompatActivity {

    private static final String TAG=UserAccount.class.getSimpleName();
    private Button Login,Register;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String verify;
    private int product_id;

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

        Login = (Button)findViewById(R.id.btnLogin);
        Register =(Button)findViewById(R.id.btnRegister);
        loginButton=(LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_gender","user_location"));

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),LoginActivity.class);
                intent.putExtra("Login_verify",verify);
                intent.putExtra("product_id",product_id);
                startActivity(intent);

                Log.d("Login Verify",verify);
            //    startActivity(new Intent(v.getContext(), Login.class));
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectUserTypeActivity.class);
                intent.putExtra("Register_verify",verify);
                intent.putExtra("product_id",product_id);
                startActivity(intent);

                Log.d("Register Verify",verify);
   //             startActivity(new Intent(v.getContext(),Register.class));
            }
        });

        callbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken=loginResult.getAccessToken();
                //Toast.makeText(UserAccount.this,accessToken.toString(),Toast.LENGTH_LONG).show();
                userLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook login error",error.getMessage());
                //Toast.makeText(UserAccount.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void userLoginInformation(AccessToken accessToken){
        //Toast.makeText(UserAccount.this,accessToken.toString(),Toast.LENGTH_LONG).show();
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
                            Intent intent=new Intent(UserAccount.this,ConfirmMobileNumberActivity.class);
                            intent.putExtra("facebooktokenkey",accessToken.toString());
                            intent.putExtra("facebookid",facebookid);
//                            intent.putExtra("gender",gender);
//                            intent.putExtra("birthday",birth);
                            intent.putExtra("facebookname",name);
                            intent.putExtra("imageurl",image);
                            startActivity(intent);
                        }
                        else{
                            //login user
                            Log.d(TAG,"RUN RESULT "+result);
                            JSONArray jsonArray=obj.getJSONArray("results");
                            JSONObject obj1=jsonArray.getJSONObject(0);
                            int apiUserId=obj1.getInt("id");
                            String apiUsername=obj1.getString("username");
                            Intent intent=new Intent(UserAccount.this,VerifyMobileActivity.class);
                            intent.putExtra("Register_verify",verify);
                            intent.putExtra("Login_verify",verify);
                            intent.putExtra("authType",4);
                            intent.putExtra("phoneNumber",apiUsername);
                            intent.putExtra("password",apiUsername);
                            intent.putExtra("facebooktokenkey",accessToken.toString());
//                            intent.putExtra("gender",gender);
//                            intent.putExtra("birthday",birth);
                            intent.putExtra("facebookid",facebookid);
                            intent.putExtra("facebookname",name);
                            intent.putExtra("imageurl",image);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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

}
