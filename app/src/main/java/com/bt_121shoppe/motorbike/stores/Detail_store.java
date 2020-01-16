package com.bt_121shoppe.motorbike.stores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.fragments.List_store_post;
import com.bt_121shoppe.motorbike.fragments.history_store;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Detail_store extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG= Detail_store.class.getSimpleName();

    private SharedPreferences mSharedPreferences;
    private SharedPreferences prefer;
    private String name,pass,Encode;
    private String myReferences="mypref";
    private TabLayout tabs;
    private ViewPager viewPager;
    private Button btAdd_post;
    public RelativeLayout layout;
    private CircleImageView img_user;
    private TextView tv_back,tv_dealer,tv_location,tv_phone;
    int inttab = 0,pk = 0;
    private String location,storeName,image;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locale();
        mSharedPreferences=getSharedPreferences(myReferences, Context.MODE_PRIVATE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.detail_store);
        btAdd_post = findViewById(R.id.add_post);
        layout = findViewById(R.id.linearLayout);
        tv_back = findViewById(R.id.tv_back);
        tv_dealer = findViewById(R.id.tv_dealer);
        tv_location = findViewById(R.id.location_store);
        tv_phone = findViewById(R.id.phone);
        img_user = findViewById(R.id.img_user);

        prefer = getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }

        Intent intent = getIntent();
        storeName = intent.getStringExtra("shop_name");
        location = intent.getStringExtra("address");
        image = intent.getStringExtra("shop_image");

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pagerDealer);
        setUpPager();
//        if (tabs.getTabAt(1))

//        getPhone(pk,Encode);
//        tv_location.setText(location);
//        tv_dealer.setText(storeName);
//        Glide.with(Detail_store.this).asBitmap().load(image).into(new CustomTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                img_user.setImageBitmap(resource);
//            }
//
//            @Override
//            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//            }
//        });

        SharedPreferences sharedPref=getSharedPreferences("Register",Context.MODE_PRIVATE);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btAdd_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_store.this, Camera.class);
                startActivity(intent);
            }
        });
    }
    private void getPhone(int pk, String encode) {
        Service api = Client.getClient().create(Service.class);
        Call<User_Detail> call = api.getDetailUser(pk,encode);
        call.enqueue(new Callback<User_Detail>() {
            @Override
            public void onResponse(Call<User_Detail> call, Response<User_Detail> response) {
                String stphone = response.body().getProfile().getTelephone();
                tv_phone.setText(method(stphone));
            }
            @Override
            public void onFailure(Call<User_Detail> call, Throwable t) {

            }
        });
    }
    public String method(String str) {
        for (int i=0;i<str.length();i++){
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.onCreate(null);
    }

    @Override
    public void onBackPressed(){
        fileList();
    }

    private void language(String lang){
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration confi=new Configuration();
        confi.locale=locale;
        getBaseContext().getResources().updateConfiguration(confi,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    private void locale(){
        SharedPreferences prefer=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefer.getString("My_Lang","");
        language(language);
    }
    private void setUpPager(){
        tabs.addTab(tabs.newTab().setText(R.string.store_list));
        tabs.addTab(tabs.newTab().setText(R.string.history));
        tabs.setOnTabSelectedListener(this);
        Pager adapter = new Pager(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class Pager extends FragmentStatePagerAdapter {
        int tabCount;
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    List_store_post tab1 = new List_store_post();
                    Bundle bundle=new Bundle();
                    bundle.putString("storeName",storeName);
                    bundle.putString("storeLocation",location);
                    bundle.putString("storeImage",image);
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    history_store tab2 = new history_store();
                    return tab2;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
