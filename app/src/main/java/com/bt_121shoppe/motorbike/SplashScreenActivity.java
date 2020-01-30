package com.bt_121shoppe.motorbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bt_121shoppe.motorbike.Activity.CheckNetworkConnectionHelper;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.listener.OnNetworkConnectionChangeListener;
import com.bt_121shoppe.motorbike.nointernet.NoInternetActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG= SplashScreenActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView myImageView= (ImageView)findViewById(R.id.imageView3);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CheckNetworkConnectionHelper
                        .getInstance()
                        .registerNetworkChangeListener(new OnNetworkConnectionChangeListener() {
                            @Override
                            public void onDisconnected() {
                                //Do your task on Network Disconnected!
                                Log.e(TAG, "onDisconnected");
                                Intent intent = new Intent(SplashScreenActivity.this, NoInternetActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onConnected() {
                                //Do your task on Network Connected!
                                Log.e(TAG, "onConnected");
                                Intent intent = new Intent(SplashScreenActivity.this, Home.class);
                                startActivity(intent);
                            }

                            @Override
                            public Context getContext() {
                                return SplashScreenActivity.this;
                            }
                        });
            }
        },SPLASH_TIME_OUT);

    }
}
