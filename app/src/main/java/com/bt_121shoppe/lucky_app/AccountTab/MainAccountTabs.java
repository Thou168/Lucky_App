package com.bt_121shoppe.lucky_app.AccountTab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bt_121shoppe.lucky_app.Activity.Camera;
import com.bt_121shoppe.lucky_app.Activity.Home;
import com.bt_121shoppe.lucky_app.Activity.Message;
import com.bt_121shoppe.lucky_app.Activity.Notification;
import com.bt_121shoppe.lucky_app.Api.User;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;
import com.bt_121shoppe.lucky_app.utils.FileCompressor;
import com.bt_121shoppe.lucky_app.utils.NonScrollableVP;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainAccountTabs extends FragmentActivity {

    static final  int REQUEST_TAKE_PHOTO=1;
    static final int REQUEST_GALLARY_PHOTO=2;
    static final int GALLERY=1;
    static final int CAMERA=2;
    static final int PRIVATE_MODE=0;

    private String type,username="",password="",encodeAuth="",API_ENDPOINT="";
    private int pk=0;

    private Button uploadcover;
    private ImageView upload;
    private ImageView uploadprofile;
    private TextView tvUsername;

    SharedPreferences preferences;
    NonScrollableVP mainPager;
    TabLayout tabs;
    Toolbar tb;
    private User user;
    private File mPhotoFile;
    private FileCompressor mCompressor;
    private Bitmap bitmapImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);

        BottomNavigationView bnavigation = findViewById(R.id.bnaviga);
        bnavigation.getMenu().getItem(4).setChecked(true);
        bnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        break;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                        break;
                    case R.id.camera:
                        startActivity(new Intent(getApplicationContext(), Camera.class));
                        break;
                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(), Message.class));
                        break;
                    case R.id.account :

                        break;
                }
                return false;
            }
        });


        preferences = getSharedPreferences("Register", Context.MODE_PRIVATE);
        username=preferences.getString("name","");
        password=preferences.getString("pass","");
        encodeAuth="Basic "+ CommonFunction.getEncodedString(username,password);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0);
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }

        mainPager=(NonScrollableVP) findViewById(R.id.pagerMain);
        tabs=(TabLayout)findViewById(R.id.tabs);
        tvUsername=(TextView) findViewById(R.id.tvUsername);

        setUpPager();
    }

    private void setUpPager() {
        MainPostList posts =MainPostList.newInstance();
        MainLikeList likes =new MainLikeList();
        MainLoanList loans=MainLoanList.newInstance();
        MainPager adapter = new MainPager(getSupportFragmentManager());

        adapter.addFragment(posts, "Post (0)");
        adapter.addFragment(likes, "Like (0)");
        adapter.addFragment(loans,"Loan (0)");

        mainPager.setAdapter(adapter);
        tabs.setupWithViewPager(mainPager);

    }
    private class MainPager extends FragmentPagerAdapter {

        List<Fragment> mFragments = new ArrayList<>();
        List<String> mFragmentsTitle = new ArrayList<>();

        public MainPager(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment f, String s) {
            mFragments.add(f);
            mFragmentsTitle.add(s);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("mylog", mFragmentsTitle.get(position));
            return mFragmentsTitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
