package com.bt_121shoppe.motorbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.bt_121shoppe.motorbike.Activity.Home;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG= SplashScreenActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
