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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.AccountTab.MainAccountTabs;
import com.bt_121shoppe.motorbike.Activity.Account;
import com.bt_121shoppe.motorbike.Activity.Camera;
import com.bt_121shoppe.motorbike.Activity.Home;
import com.bt_121shoppe.motorbike.Activity.Notification;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.Startup.MainActivity;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    private ImageButton btnFacebookLogin;
    private Button btnSubmit;
    private EditText editPhone,editComfirmPass,editPassword;
    private static final String TAG = Register.class.getSimpleName();
    private TextView textView;
    private TextView PhoneError,PasswordError,ComfirmPassError;
    private Context context;
    ProgressDialog mProgress;
    SharedPreferences prefer;
    String phone;
    String comfirm;
    String pass;
    private String userEmail;
    private String register_verify;
    private int product_id,user_group;

    private FirebaseAuth auth;
    private DatabaseReference reference;

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
                }else if (user_group == 1) {
                    if (CheckNumber(ComfirmPass)) {
                        /* block for verify code sep 12 2019 */
//                        Intent intent = new Intent(Register.this, VerifyMobileActivity.class);
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
                        PasswordError.setText("Password is only numbers");
                        ComfirmPassError.setText("Password is only numbers");
                    }
                    //registerUserFirebase("user1","borithnget@email.com","1234__");
                }
                else{
                    registerAPIUser(editPhone.getText().toString(),Password,user_group);
                }

                mProgress.show();

            }
        });
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

                            Intent intent;
                            if(register_verify!=null) {
                                switch (register_verify) {
                                    case "notification":
                                        intent = new Intent(Register.this, Notification.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "camera":
                                        intent = new Intent(Register.this, Camera.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "message":
                                        intent = new Intent(Register.this, ChatMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "account":
                                        intent = new Intent(Register.this, Account.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "detail":
                                        intent = new Intent(Register.this, Detail_New_Post.class);
                                        intent.putExtra("ID", product_id);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        intent = new Intent(Register.this, Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
                            }else{
                                intent = new Intent(Register.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Toast.makeText(Register.this,"You cannot register with email or password."+task.getException(),Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Error "+task.getException()+" "+task.getResult());
                        }
                    }
                });
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
                        editor.putString("pass",pass);
                        editor.putString("groups",String.valueOf(g));
                        editor.commit();

                        userEmail=ConsumeAPI.PREFIX_EMAIL+id+"@email.com";
                        Log.d(TAG,userEmail+" "+username+" "+pass);
                        registerUserFirebase(username,userEmail,pass,String.valueOf(1));

                    }else {
                        AlertDialog alertDialog=new AlertDialog.Builder(Register.this).create();
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
                    AlertDialog alertDialog=new AlertDialog.Builder(Register.this).create();
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
}
