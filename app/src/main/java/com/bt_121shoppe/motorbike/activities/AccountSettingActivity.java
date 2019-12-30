package com.bt_121shoppe.motorbike.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.settings.AboutUsActivity;
import com.bt_121shoppe.motorbike.settings.ContactActivity;
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
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        initToolbar();


        Button btnProfile=findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, Register.class));
            }
        });

        Button btnSetting=findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this,SettingActivity.class));
            }
        });

        Button btnNofitification=findViewById(R.id.btnNotification);
        btnNofitification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, NotificationActivity.class));
            }
        });

        Button btnAbout=findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, AboutUsActivity.class));
            }
        });

        Button btnContact=findViewById(R.id.btnContact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, ContactActivity.class));
            }
        });

        Button btnTermofPrivacy=findViewById(R.id.btnTermOfPrivacy);
        btnTermofPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingActivity.this, TermPrivacyActivity.class));
            }
        });

        Button btnSignout=findViewById(R.id.btnLogout);
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
