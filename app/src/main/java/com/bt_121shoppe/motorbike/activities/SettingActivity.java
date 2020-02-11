package com.bt_121shoppe.motorbike.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ChangepassModel;
import com.bt_121shoppe.motorbike.settings.Changelanguage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Changelanguage.BottomSheetListener {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private ImageView img_back;
    EditText old_pass,new_pass,renew_pass;
    Button reset_pass;
    SharedPreferences prefer;
    private String name,pass,Encode;
    RadioGroup radio_groupnotification,radio_language;
    RadioButton radio_kh,radio_en;
    RadioButton radio_enable,radio_disable;
    private int index = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    boolean radioCheck = false;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private int pk=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        radio_groupnotification=findViewById(R.id.radio_groupnotification);
        radio_language=findViewById(R.id.radio_language);
        radio_language.clearCheck();
        radio_kh=findViewById(R.id.radio_khmerlanguage);
        radio_en=findViewById(R.id.radio_englishlanguage);
        radio_enable=findViewById(R.id.radio_notificationenable);
        radio_disable=findViewById(R.id.radio_notificationdisable);

        mSwipeRefreshLayout=findViewById(R.id.refresh_setting);
        mSwipeRefreshLayout.setEnabled(false);

        SharedPreferences preferences= getApplication().getSharedPreferences("Register",MODE_PRIVATE);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        old_pass=findViewById(R.id.old_pass);
        new_pass=findViewById(R.id.new_pass);
        renew_pass=findViewById(R.id.renew_pass);
        reset_pass=findViewById(R.id.reset_pass);

        reference= FirebaseDatabase.getInstance().getReference();
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                if (renew_pass.getText().toString().equals(new_pass.getText().toString())){
                    Change_password(Encode);
                }else {
                    Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                }

            }
        });

        switch_paper();
        initToolbar();
        Notification();
        locale();
        change_pass();
    }

    //for notification
    private void Notification(){
        radio_enable.toggle();
        radio_groupnotification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                index=radio_groupnotification.indexOfChild(radioButton);
                switch (index) {
                    case 0:
                        index=0;
                        Toast.makeText(SettingActivity.this,"Enable",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        index=1;
                        Toast.makeText(SettingActivity.this,"Disable",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        old_pass.clearFocus();
        new_pass.clearFocus();
        renew_pass.clearFocus();
    }

    private void change_pass(){
        CheckGroup check = new CheckGroup();
        int g = check.getGroup(pk,this);
        if (g!=3){
            old_pass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
            new_pass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
            renew_pass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});

            old_pass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            new_pass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            renew_pass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }else {
            old_pass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
            new_pass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
            renew_pass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

            old_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            new_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            renew_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    //language
    private void switch_paper(){
        Paper.init(this);
        String language=Paper.book().read("language");
        Log.e(TAG,"Current language is "+language);
        if(language==null){
            Paper.book().write("language","km");
            language("km");
        }else{
//            radio_language.setOnCheckedChangeListener((group, checkedId) -> {
//                View radioButton = group.findViewById(checkedId);
//                index = radio_language.indexOfChild(radioButton);
//                switch (index) {
//                    case 0:
//                        Toast.makeText(SettingActivity.this, R.string.khmer, Toast.LENGTH_SHORT).show();
//                        Paper.book().write("language","km");
//                        language("km");
//                        break;
//                    case 1:
//                        Toast.makeText(SettingActivity.this, R.string.english_khmer, Toast.LENGTH_SHORT).show();
//                        Paper.book().write("language", "en");
//                        language("en");
//                        break;
//                }
//            });
            radio_kh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radio_kh.toggle();
                    language("km");
                    Paper.book().write("language","km");
                    onRefresh();
                }
            });
            radio_en.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radio_en.toggle();
                    language("en");
                    Paper.book().write("language","en");
                    onRefresh();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this,AccountSettingActivity.class));
        finish();
    }

    public void language(String lang){
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration confi=new Configuration();
        confi.locale=locale;
        getBaseContext().getResources().updateConfiguration(confi,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
        if ("km".equals(lang)) {
            radio_kh.toggle();
        }
        else {
            radio_en.toggle();
        }

    }

    public void locale(){
        SharedPreferences prefer=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefer.getString("My_Lang","");
        language(language);
    }

    //change password
    private void Change_password(String encode) {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/changepassword/");
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        try{
            post.put("old_password",old_pass.getText().toString());
            post.put("new_password",renew_pass.getText().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        String auth = "Basic "+ encode;
        RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String st = response.body().string();
                Log.d("Result",st);
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
                                    ChangePassWithFirebase(renew_pass.getText().toString());
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
                                AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                                alertDialog.setTitle(getString(R.string.title_change_password));
                                alertDialog.setMessage(getString(R.string.fail_change_password));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
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

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

    private void initToolbar(){
        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,AccountSettingActivity.class));
                finish();
            }
        });
    }
    private void ChangePassWithFirebase(String newPass){
        //String newPassword=newPass+"__";
        String newPassword=newPass;
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null) {
            Query query = reference.child("users").orderByChild(fuser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("password", newPassword);
                    reference.updateChildren(map);
                    AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                    alertDialog.setTitle(getString(R.string.title_change_password));
                    alertDialog.setMessage(getString(R.string.success_change_password));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> {
                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putString("name", name);
                        editor.putString("pass", newPass);
                        editor.commit();
                        startActivity(new Intent(SettingActivity.this, Home.class));
                        dialog.dismiss();
                        finish();
                    });
                    alertDialog.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
