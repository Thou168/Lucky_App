package com.bt_121shoppe.motorbike.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.dealerstores.DealerStoreListFragment;
import com.bt_121shoppe.motorbike.stores.CreateShop;
import com.bt_121shoppe.motorbike.dealerstores.DealerStoreAllPostListFragment;
import com.bt_121shoppe.motorbike.stores.StoreListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;


public class DealerStoreActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG= DealerStoreActivity.class.getSimpleName();

    private SharedPreferences mSharedPreferences;
    private DrawerLayout mDrawerLayout;
    private BottomNavigationView mBottomNavigation;
    private String myReferences="mypref";
    private Fragment currentFragment;
    private TabLayout tabs;
    private ViewPager viewPager;
    private Button btAdd_store;
    int inttab = 0;
    String verify;
    String currentLanguage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        verify = i.getStringExtra("verify");
        locale();
        mSharedPreferences=getSharedPreferences(myReferences, Context.MODE_PRIVATE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.dealer_store);
        btAdd_store = findViewById(R.id.add_store);

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = prefer.getString("My_Lang", "");

        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pagerDealer);
//        setupTabText();
        tabs.setupWithViewPager(viewPager);
        setUpPager();

        SharedPreferences sharedPref=getSharedPreferences("Register",Context.MODE_PRIVATE);

        btAdd_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DealerStoreActivity.this, CreateShop.class);
                startActivity(intent);
            }
        });

        /* start implementation bottom navigation */
        mBottomNavigation=findViewById(R.id.bottom_nav);
        mBottomNavigation.getMenu().getItem(2).setChecked(true);
        mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.home:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        startActivity(new Intent(DealerStoreActivity.this, Home.class));
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    }else{
                        Intent intent=new Intent(DealerStoreActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","home");
                        startActivity(intent);
                    }
                    break;
                case R.id.notification:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        startActivity(new Intent(DealerStoreActivity.this, StoreListActivity.class));
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    }else{
                        Intent intent=new Intent(DealerStoreActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","notification");
                        startActivity(intent);
                    }
                    break;
                case R.id.dealer:
                    break;
                case R.id.message:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        startActivity(new Intent(DealerStoreActivity.this, ChatMainActivity.class));
                    }else{
                        Intent intent=new Intent(DealerStoreActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","message");
                        startActivity(intent);
                    }
                    break;
                case R.id.account:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        startActivity(new Intent(DealerStoreActivity.this, Account.class));
                    }else{
                        Intent intent=new Intent(DealerStoreActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","account");
                        startActivity(intent);
                    }
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.onCreate(null);
    }

    @Override
    public void onBackPressed(){
//        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        else {
//            super.onBackPressed();
//            Log.e(TAG, "Run on back pressed event.");
//        }

        if (verify!=null){
            if ("camera".equals(verify)) {
                Intent i2 = new Intent(DealerStoreActivity.this,Home.class);
                startActivity(i2);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        }else {
            Intent i2 = new Intent(DealerStoreActivity.this,Home.class);
            startActivity(i2);
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mBottomNavigation.getMenu().getItem(2).setChecked(true);
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
//    private void setupTabText() {
//        tabs.getTabAt(0).setText(R.string.tab_store);
//        tabs.getTabAt(1).setText(R.string.tab_postlist);
//    }
    private void setUpPager(){
        tabs.addTab(tabs.newTab().setText(R.string.tab_store));
        tabs.addTab(tabs.newTab().setText(R.string.tab_postlist));
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
                    return new DealerStoreListFragment();
                case 1:
                    return new DealerStoreAllPostListFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            if (position==0) {
//                return getApplicationContext().getString(R.string.tab_store);
//            } else if (position==1) {
//                return getApplicationContext().getString(R.string.tab_postlist);
//            } else {
//                return null;
//            }
            if (position==0) {
                if (currentLanguage.equals("en")) {
                    return "Stores";
                }else return "ហាង";
            } else {
                if (currentLanguage.equals("en")) {
                    return "Post list";
                }else return "បញ្ជីការប្រកាស";
            }
        }
    }
}
