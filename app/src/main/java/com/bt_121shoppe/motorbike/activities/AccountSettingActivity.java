package com.bt_121shoppe.motorbike.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.settings.AboutUsActivity;
import com.bt_121shoppe.motorbike.settings.ContactActivity;
import com.bt_121shoppe.motorbike.settings.Setting;
import com.bt_121shoppe.motorbike.settings.TermPrivacyActivity;
import com.bt_121shoppe.motorbike.Login_Register.Register;
import com.facebook.login.LoginManager;
import com.facebook.share.Share;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private int user_group;
    private ImageView img_back;
    TextView view_ver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        Intent intent = getIntent();
        user_group = intent.getIntExtra("user_group",0);
        view_ver=findViewById(R.id.version_view);
        //initToolbar();
        try {
            Context context = getApplicationContext();
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo("com.bt_121shoppe.motorbike", 0);
            String version = pInfo.versionName;
            Log.d(TAG, "checkVersion.DEBUG: App version: "+version);
            view_ver.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this,Account.class));
            }
        });

        LinearLayout btnProfile=findViewById(R.id.llProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountSettingActivity.this,Register.class);
                intent.putExtra("user_group",user_group);
                intent.putExtra("edit","edit");
                startActivity(intent);
            }
        });

        LinearLayout btnSetting=findViewById(R.id.llSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, SettingActivity.class));
            }
        });

        LinearLayout btnNofitification=findViewById(R.id.llNotification);
        btnNofitification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, NotificationActivity.class));
            }
        });

        LinearLayout btnAbout=findViewById(R.id.llAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, AboutUsActivity.class));
            }
        });

        LinearLayout btnContact=findViewById(R.id.llContact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, ContactActivity.class));
            }
        });

        LinearLayout btnTermofPrivacy=findViewById(R.id.llTerm);
        btnTermofPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, TermPrivacyActivity.class));
            }
        });

        LinearLayout btnSignout=findViewById(R.id.llSignOut);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AccountSettingActivity.this);
                dialog.setTitle(R.string.logout)
                        .setMessage(R.string.logout_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefer = getSharedPreferences("Register", MODE_PRIVATE);
                                SharedPreferences.Editor editor=prefer.edit();
                                editor.clear();
                                editor.commit();
                                dialog.cancel();
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                startActivity(new Intent(AccountSettingActivity.this,Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AccountSettingActivity.this,Account.class));
    }

    private void initToolbar(){
        mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("Account Settings");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
