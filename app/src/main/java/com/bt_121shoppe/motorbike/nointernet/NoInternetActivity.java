package com.bt_121shoppe.motorbike.nointernet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.Activity.CheckNetworkConnectionHelper;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.SplashScreenActivity;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.activities.SettingActivity;
import com.bt_121shoppe.motorbike.listener.OnNetworkConnectionChangeListener;
import com.bt_121shoppe.motorbike.newversion.MainActivity;

public class NoInternetActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = NoInternetActivity.class.getSimpleName();
    private TextView btntryagin;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        btntryagin=findViewById(R.id.btntryagain);
        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        btntryagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetwork()){
                    // App has internet connection
                    startActivity(new Intent(NoInternetActivity.this, Home.class));
                }else{
                    startActivity(new Intent(NoInternetActivity.this, SplashScreenActivity.class));
                }
            }
        });

        onConnectionChange();
    }

    //connection on and off
    private void onConnectionChange(){
        // Inflate the layout for this fragment
        CheckNetworkConnectionHelper
                .getInstance()
                .registerNetworkChangeListener(new OnNetworkConnectionChangeListener() {
                    @Override
                    public void onDisconnected() {
                        //Do your task on Network Disconnected!
                        Log.e(TAG, "onDisconnected");
                    }

                    @Override
                    public void onConnected() {
                        //Do your task on Network Connected!
                        Log.e(TAG, "onConnected");
                        startActivity(new Intent(NoInternetActivity.this,SplashScreenActivity.class));
                    }

                    @Override
                    public Context getContext() {
                        return NoInternetActivity.this;
                    }
                });
    }

    private boolean haveNetwork() {
        boolean has_wifi = false;
        boolean has_mobile_data = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos= connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info: networkInfos){
            if(info.getTypeName().equalsIgnoreCase("Wifi")){
                if(info.isConnected()){
                    has_wifi=true;
                }
            }
            if(info.getTypeName().equalsIgnoreCase("Mobile")){
                if(info.isConnected()){
                    has_mobile_data=true;
                }
            }
        }
        return has_wifi || has_mobile_data;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        startActivity(new Intent(NoInternetActivity.this, SplashScreenActivity.class));
    }
}
