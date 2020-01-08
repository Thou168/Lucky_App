package com.bt_121shoppe.motorbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Home;

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
                Intent intent = new Intent(SplashScreenActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
