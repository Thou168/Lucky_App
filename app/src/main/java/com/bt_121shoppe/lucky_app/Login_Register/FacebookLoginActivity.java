package com.bt_121shoppe.lucky_app.Login_Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.lucky_app.R;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView displayName,emailId;
    private ImageView displayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        displayName=findViewById(R.id.display_name);
        emailId=findViewById(R.id.email);
        displayImage=findViewById(R.id.image_view);
        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        callbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken=loginResult.getAccessToken();
                Toast.makeText(FacebookLoginActivity.this,accessToken.toString(),Toast.LENGTH_LONG).show();
                userLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook login ",error.getMessage());
                Toast.makeText(FacebookLoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void userLoginInformation(AccessToken accessToken){
        Toast.makeText(FacebookLoginActivity.this,accessToken.toString(),Toast.LENGTH_LONG).show();
        GraphRequest request=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try{
                    String name=object.getString("name");
                    String email=object.getString("email");
                    String image=object.getJSONObject("picture").getJSONObject("data").getString("url");
                    displayName.setText(name);
                    emailId.setText(email);
                    Glide.with(getApplicationContext()).load(image).into(displayImage);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }
}
