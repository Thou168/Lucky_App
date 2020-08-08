package com.bt_121shoppe.motorbike.activities;

import android.app.Activity;
import android.app.Dialog;
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
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.regex.Pattern;

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

    TextView old_pw,new_pw,renew_pw;
    CheckGroup check = new CheckGroup();
    int g;
    Dialog alertDialog;
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

//        id for password change error

        old_pw=findViewById(R.id.old_pw);
        new_pw=findViewById(R.id.new_pw);
        renew_pw=findViewById(R.id.renew_pw);

        g = check.getGroup(pk,this);
        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
//                if (renew_pass.getText().toString().isEmpty() || new_pass.getText().toString().isEmpty() || old_pass.getText().toString().isEmpty()){
//                    if (old_pass.getText().toString().isEmpty()){
//                        LayoutInflater factory = LayoutInflater.from(SettingActivity.this);
//                        final View clearDialogView = factory.inflate(R.layout.layout_warnning_dialog, null);
//                        final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(SettingActivity.this).create();
//                        clearDialog.setView(clearDialogView);
//                        clearDialog.setIcon(R.drawable.tab_message_selector);
//                        clearDialog.setCancelable(false);
//                        TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
//                        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
//                        Mssloan.setText(R.string.alert_old_password);
//                        title.setText(R.string.title_change_password);
//                        Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
//                        btnYes.setText(R.string.ok);
//                        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(v14 -> {
//                            onResume();
//                            clearDialog.dismiss();
//                        });
//                        clearDialog.show();
//                    }else if (new_pass.getText().toString().isEmpty()){
//                        LayoutInflater factory = LayoutInflater.from(SettingActivity.this);
//                        final View clearDialogView = factory.inflate(R.layout.layout_warnning_dialog, null);
//                        final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(SettingActivity.this).create();
//                        clearDialog.setView(clearDialogView);
//                        clearDialog.setIcon(R.drawable.tab_message_selector);
//                        clearDialog.setCancelable(false);
//                        TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
//                        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
//                        Mssloan.setText(R.string.alert_new_password);
//                        title.setText(R.string.title_change_password);
//                        Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
//                        btnYes.setText(R.string.ok);
//                        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(v13 -> {
//                            onResume();
//                            clearDialog.dismiss();
//                        });
//                        clearDialog.show();
//                    }else if (renew_pass.getText().toString().isEmpty()){
//                        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
//                        final View clearDialogView = factory.inflate(R.layout.layout_warnning_dialog, null);
//                        final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(SettingActivity.this).create();
//                        clearDialog.setView(clearDialogView);
//                        clearDialog.setIcon(R.drawable.tab_message_selector);
//                        clearDialog.setCancelable(false);
//                        TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
//                        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
//                        Mssloan.setText(R.string.alert_renew_password);
//                        title.setText(R.string.title_change_password);
//                        Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
//                        btnYes.setText(R.string.ok);
//                        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(v12 -> {
//                            onResume();
//                            clearDialog.dismiss();
//                        });
//                        clearDialog.show();
//                    }
//                }else {
//                    if (renew_pass.getText().toString().equals(new_pass.getText().toString())){
//                        Change_password(Encode);
//                    }else {
//                        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
//                        final View clearDialogView = factory.inflate(R.layout.layout_warnning_dialog, null);
//                        final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(SettingActivity.this).create();
//                        clearDialog.setView(clearDialogView);
//                        clearDialog.setIcon(R.drawable.tab_message_selector);
//                        clearDialog.setCancelable(false);
//                        TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
//                        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
//                        Mssloan.setText(R.string.alert_wrong_password);
//                        title.setText(R.string.title_change_password);
//                        Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
//                        btnYes.setText(R.string.ok);
//                        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(v1 -> {
//                            onResume();
//                            clearDialog.dismiss();
//                        });
//                        clearDialog.show();
//                    }
//
//                }

                if (g!=3) {  // for public user
                    // on old pw
                    if (old_pass.getText().toString().isEmpty()) {
                        old_pw.setTextColor(getColor(R.color.red));
                        old_pw.setText(R.string.alert_old_password);
                    } else if (old_pass.length() < 4) {
                        old_pw.setTextColor(getColor(R.color.red));
                        old_pw.setText(R.string.user_message);
                    } else {
                        old_pw.setText("");
                    }

                    // on new pw
                    if (new_pass.getText().toString().isEmpty()) {
                        new_pw.setTextColor(getColor(R.color.red));
                        new_pw.setText(R.string.alert_new_password);
                    } else if (new_pass.length() < 4) {
                        new_pw.setTextColor(getColor(R.color.red));
                        new_pw.setText(R.string.user_message);
                    } else {
                        new_pw.setText("");
                    }

                    // on renew pw
                    if (renew_pass.getText().toString().isEmpty()) {
                        renew_pw.setTextColor(getColor(R.color.red));
                        renew_pw.setText(R.string.alert_renew_password);
                    } else if (!new_pass.getText().toString().equals(renew_pass.getText().toString())) {
                        renew_pw.setTextColor(getColor(R.color.red));
                        renew_pw.setText(R.string.wrongInputPasswordSecond);
                    } else if (renew_pass.getText().toString().equals(new_pass.getText().toString())){
                        renew_pw.setText("");
                    }

                    //for result
                    if (new_pass.getText().toString().trim().length()==4 && renew_pass.getText().toString().trim().length()==4 && !old_pass.getText().toString().isEmpty() && !(old_pass.getText().toString().trim().length()<4)) {
                        if (renew_pass.getText().toString().equals(new_pass.getText().toString())) {
                            Change_password(Encode);
                        }
                    }
                }else {  // for dealer user
                    Pattern lowerCasePatten = Pattern.compile("[a-zA-Z]");
                    Pattern digitCasePatten = Pattern.compile("[0-9 ]");
                    Pattern white_space = Pattern.compile("[\\s]");

                    if (old_pass.getText().toString().isEmpty()) {
                        old_pw.setTextColor(getColor(R.color.red));
                        old_pw.setText(R.string.inputPassword);
                    } else if (old_pass.getText().toString().trim().length()<6){
                        old_pw.setTextColor(getColor(R.color.red));
                        old_pw.setText(R.string.user_message_dealer);
                    }
                    else if (!lowerCasePatten.matcher(old_pass.getText().toString()).find() || !digitCasePatten.matcher(old_pass.getText().toString()).find()){
                        old_pw.setTextColor(getColor(R.color.red));
                        old_pw.setText(R.string.valid_dealer_pass);
                    } else if (white_space.matcher(old_pass.getText().toString()).find()){
                        old_pw.setText(R.string.no_whitespace_pass);
                        old_pw.setTextColor(getColor(R.color.red));
                    } else {
                        old_pw.setText("");
                    }

                    // on new pw
                    if (new_pass.getText().toString().isEmpty()) {
                        new_pw.setTextColor(getColor(R.color.red));
                        new_pw.setText(R.string.inputPassword);
                    } else if (new_pass.getText().toString().trim().length()<6){
                        new_pw.setTextColor(getColor(R.color.red));
                        new_pw.setText(R.string.user_message_dealer);
                    } else if (!lowerCasePatten.matcher(new_pass.getText().toString()).find() || !digitCasePatten.matcher(new_pass.getText().toString()).find()){
                        new_pw.setTextColor(getColor(R.color.red));
                        new_pw.setText(R.string.valid_dealer_pass);
                    } else if (white_space.matcher(new_pass.getText().toString()).find()){
                        new_pw.setText(R.string.no_whitespace_pass);
                        new_pw.setTextColor(getColor(R.color.red));
                    } else {
                        new_pw.setText("");
                    }

                    // on renew pw
                    if (renew_pass.getText().toString().isEmpty()) {
                        renew_pw.setTextColor(getColor(R.color.red));
                        renew_pw.setText(R.string.alert_renew_password);
                    } else if (!renew_pass.getText().toString().equals(new_pass.getText().toString())) {
                        renew_pw.setTextColor(getColor(R.color.red));
                        renew_pw.setText(R.string.wrongInputPasswordSecond);
                    }else {
                        renew_pw.setText("");
                    }

                    //for result
                    if (new_pass.getText().toString().trim().length()>=6 && renew_pass.getText().toString().trim().length()>=6 && !old_pass.getText().toString().isEmpty() && !(old_pass.getText().toString().trim().length()<6)) {
                        if (renew_pass.getText().toString().equals(new_pass.getText().toString())) {
                            Change_password(Encode);
                        }
                    }
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
                        Toast.makeText(SettingActivity.this,R.string.enable,Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        index=1;
                        Toast.makeText(SettingActivity.this,R.string.disable,Toast.LENGTH_SHORT).show();
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
        g = check.getGroup(pk,this);
        System.out.println("GG = "+g);
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
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
            if (!renew_pass.getText().toString().equals(old_pass.getText().toString())){
                post.put("old_password", old_pass.getText().toString());
                post.put("new_password", renew_pass.getText().toString());
            }
//            else{
//                alertDialog = new Dialog(SettingActivity.this);
//                AlertDialog.Builder customBuilder = new AlertDialog.Builder(SettingActivity.this);
//                View clearDialogView = getLayoutInflater().inflate(R.layout.layout_warnning_dialog, null);
//                customBuilder.setView(clearDialogView);
//                TextView title = clearDialogView.findViewById(R.id.textView_title);
//                TextView Mssloan = clearDialogView.findViewById(R.id.textView_message);
//                Mssloan.setText(R.string.this_is_your_old_pass);
//                title.setText(R.string.title_change_password);
//                Button btnYes = clearDialogView.findViewById(R.id.button_positive);
//                btnYes.setText(R.string.ok);
//                btnYes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//                alertDialog = customBuilder.create();
//                alertDialog.setCancelable(false);
//                alertDialog.show();
//            }
            System.out.println("Old pass = "+post.put("old_password",old_pass.getText().toString()));
            System.out.println("New pass = "+post.put("new_password",renew_pass.getText().toString()));

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
                        System.out.println("Status = "+ model.getStatus());
                        if (statusCode==201){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    old_pw.setText("");
                                    ChangePassWithFirebase(renew_pass.getText().toString());
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // run for error
//                                    Toast.makeText(getApplicationContext(),st,Toast.LENGTH_LONG).show();
                                    old_pw.setTextColor(getColor(R.color.red));
                                    old_pw.setText(R.string.alert_wrong_password);
                                }
                            });
                        }

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LayoutInflater factory = LayoutInflater.from(SettingActivity.this);
                                final View clearDialogView = factory.inflate(R.layout.layout_warnning_dialog, null);
                                final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(SettingActivity.this).create();
                                clearDialog.setView(clearDialogView);
                                clearDialog.setIcon(R.drawable.tab_message_selector);
                                clearDialog.setCancelable(false);
                                TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
                                TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
                                Mssloan.setText(R.string.fail_change_password);
                                title.setText(R.string.title_change_password);
                                Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
                                btnYes.setText(R.string.ok);
                                clearDialogView.findViewById(R.id.button_positive).setOnClickListener(v -> clearDialog.dismiss());
                                clearDialog.show();
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
                onBackPressed();
            }
        });
    }
    private void ChangePassWithFirebase(String newPass){
        //String newPassword=newPass+"__";
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null) {
            Query query = reference.child("users").orderByChild(fuser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //ok to confirm
                    reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("password", newPass);
                    System.out.println("Password New = "+map.put("password", newPass));
                    reference.updateChildren(map);

                    //put new pass
                    SharedPreferences.Editor editor = prefer.edit();
                    editor.putString("name", name);
                    editor.putString("pass", newPass);
                    editor.apply();

//                    startActivity(new Intent(SettingActivity.this, Home.class));
//                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    //dialog
                    try {
                        showMessageDialog();
                    }
                    catch (WindowManager.BadTokenException e) {
                        //use a log message
                        Log.e("WindowManagerBad ", e.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void showMessageDialog() {
        alertDialog = new Dialog(SettingActivity.this);
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(
                SettingActivity.this);
        View clearDialogView = getLayoutInflater().inflate(R.layout.layout_warnning_dialog, null);
        customBuilder.setView(clearDialogView);
        TextView title = clearDialogView.findViewById(R.id.textView_title);
        TextView Mssloan = clearDialogView.findViewById(R.id.textView_message);
        Mssloan.setText(R.string.success_change_password);
        title.setText(R.string.title_change_password);
        Button btnYes = clearDialogView.findViewById(R.id.button_positive);
        btnYes.setText(R.string.ok);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, Home.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        alertDialog = customBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
