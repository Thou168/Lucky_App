package com.bt_121shoppe.motorbike.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Fragment.AccountFragment;
import com.bt_121shoppe.motorbike.Fragment.ChatFragment;
import com.bt_121shoppe.motorbike.Fragment.HomeFragment;
import com.bt_121shoppe.motorbike.Fragment.NotificationFragment;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.Setting.AboutUsActivity;
import com.bt_121shoppe.motorbike.Setting.ContactActivity;
import com.bt_121shoppe.motorbike.Setting.Setting;
import com.bt_121shoppe.motorbike.Setting.TermPrivacyActivity;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,TabLayout.OnTabSelectedListener,SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG=DashboardActivity.class.getSimpleName();
    private SharedPreferences mSharedPreferences,sharedPref;
    private DrawerLayout mDrawerLayout;
    private ImageView mImageViewEnglish, mImageViewKhmer,mProfileImageDrawer;
    Menu menu;
    MenuItem nav_profile,nav_post,nav_like,nav_loan,nav_setting,nav_about,nav_contact,nav_term;
    TextView tvUsername,tvusername_drawer;
    ImageView img_profile;
    BottomNavigationView navigationView;

    final Fragment fragmentHome=new HomeFragment();
    final Fragment fragmentAccount =new AccountFragment();
    final Fragment fragmentNotification=new NotificationFragment();
    final Fragment fragmentChat=new ChatFragment();
    final FragmentManager fm=getSupportFragmentManager();
    Fragment active= fragmentHome;
    private Fragment currentFragment;
    private String myReferences="mypref";
    private String name="",pass="",Encode="";
    private int pk=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        locale();
        mSharedPreferences=getSharedPreferences(myReferences, Context.MODE_PRIVATE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences prefer=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefer.getString("My_Lang","");

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI){
            Log.e(TAG,"Use wifi....");
        }
        else if(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
            Log.e(TAG,"Use Mobile data "+activeNetwork.getSubtype());
        }


        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mImageViewEnglish=findViewById(R.id.english);
        mImageViewKhmer =findViewById(R.id.khmer);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        NavigationView mNavigationView=findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        tvusername_drawer =  headerView.findViewById(R.id.drawer_username);
        img_profile = headerView.findViewById(R.id.imageView);
        menu = mNavigationView.getMenu();
        nav_profile = menu.findItem(R.id.nav_profile);
        nav_post = menu.findItem(R.id.nav_post);
        nav_like = menu.findItem(R.id.nav_like);
        nav_loan = menu.findItem(R.id.nav_loan);
        nav_setting = menu.findItem(R.id.nav_setting);
        nav_about = menu.findItem(R.id.nav_about);
        nav_contact = menu.findItem(R.id.nav_contact);
        nav_term = menu.findItem(R.id.nav_privacy);

//        fm.beginTransaction().add(R.id.main_container, fragmentNotification, "4").hide(fragmentNotification).commit();
//        fm.beginTransaction().add(R.id.main_container, fragmentChat, "3").hide(fragmentChat).commit();
//        fm.beginTransaction().add(R.id.main_container, fragmentAccount, "2").hide(fragmentAccount).commit();
//        fm.beginTransaction().add(R.id.main_container, fragmentHome, "1").commit();

        mNavigationView.setNavigationItemSelectedListener(this);
        navigationView=(BottomNavigationView) findViewById(R.id.navigation);
        //navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.home:
                    fm.beginTransaction().hide(active).show(fragmentHome).commit();
                    active = fragmentHome;
                    return true;

                case R.id.account:
                    if(sharedPref.contains("token")||sharedPref.contains("id")){
                        if(Active_user.isUserActive(DashboardActivity.this,pk))
                        {
                            Bundle bundle=new Bundle();
                            bundle.putString("fullname",tvusername_drawer.getText().toString());
                            fragmentAccount.setArguments(bundle);
                            fm.beginTransaction().hide(active).show(fragmentAccount).commit();
                            active = fragmentAccount;
                            return true;
                        }else
                            Active_user.clearSession(getApplicationContext());
                    }else{
                        Intent intent=new Intent(DashboardActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","account");
                        startActivity(intent);
                    }

                    return true;

                case R.id.camera:
                    //fm.beginTransaction().hide(active).show(fragment3).commit();
                    //active = fragment3;
                    return true;
                case R.id.notification:
                    fm.beginTransaction().hide(active).show(fragmentNotification).commit();
                    active = fragmentNotification;
                    return true;
                case R.id.message:
                    fm.beginTransaction().hide(active).show(fragmentChat).commit();
                    active = fragmentChat;
                    return true;
            }
            return false;
        });



        SwitchLanguage();
        sharedPref=getSharedPreferences("Register",Context.MODE_PRIVATE);
        ActionBarDrawerToggle mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        if(sharedPref.contains("token") || sharedPref.contains("id")){
            mNavigationView.setVisibility(View.VISIBLE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            name=sharedPref.getString("name","");
            pass=sharedPref.getString("pass","");
            Encode= "Basic "+ CommonFunction.getEncodedString(name,pass);
            if(sharedPref.contains("token"))
                pk=sharedPref.getInt("Pk",0);
            else if (sharedPref.contains("id"))
                pk=sharedPref.getInt("id",0);
            //get navigation user profiler here
            getUserProfile();
        }else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }


    @Override
    public void onRefresh() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.onCreate(null);
    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
            Log.e(TAG, "Run on back pressed event.");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
//        navigationView.getMenu().getItem(0).setChecked(true);
//        currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//        Log.e(TAG,"current Fragment onStart "+currentFragment);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, EditAccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_post) {
            //tabs.getTabAt(0).select();
        } else if (id == R.id.nav_like) {
            //tabs.getTabAt(1).select();
        } else if (id == R.id.nav_loan) {
            //tabs.getTabAt(2).select();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(this, TermPrivacyActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void SwitchLanguage(){
        Paper.init(this);
        String language=Paper.book().read("language");
        Log.e(TAG,"Current language is "+language);
        if(language==null){
            Paper.book().write("language","km");
            updateView("km");
            language("km");
            mImageViewKhmer.setVisibility(View.GONE);
            mImageViewEnglish.setVisibility(View.VISIBLE);
        }else{
            if(language.equals("km")){
                mImageViewKhmer.setVisibility(View.GONE);
                mImageViewEnglish.setVisibility(View.VISIBLE);
            }else {
                mImageViewKhmer.setVisibility(View.VISIBLE);
                mImageViewEnglish.setVisibility(View.GONE);
            }
            mImageViewEnglish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Paper.book().write("language","en");
                    updateView(Paper.book().read("language"));
                    language("en");
                    mImageViewKhmer.setVisibility(View.VISIBLE);
                    mImageViewEnglish.setVisibility(View.GONE);
                }
            });
        }

        mImageViewKhmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("language","km");
                updateView(Paper.book().read("language"));
                language("km");
                mImageViewKhmer.setVisibility(View.GONE);
                mImageViewEnglish.setVisibility(View.VISIBLE);

            }
        });
    }

    private void updateView(String language){
        language=language==null?"km":language;
        Context context= LocaleHapler.setLocale(this,language);
        Resources resources=context.getResources();
        //title menu
        if(context!=null && resources!=null) {
            nav_profile.setTitle(resources.getString(R.string.menu_profile));
            nav_post.setTitle(resources.getString(R.string.menu_post));
            nav_like.setTitle(resources.getString(R.string.menu_like));
            nav_loan.setTitle(resources.getString(R.string.menu_loan));
            nav_setting.setTitle(resources.getString(R.string.menu_setting));
            nav_about.setTitle(resources.getString(R.string.menu_about));
            nav_contact.setTitle(resources.getString(R.string.menu_contact));
            nav_term.setTitle(resources.getString(R.string.menu_privacy));
        }
//        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
//        if(currentFragment!=null) {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            if (Build.VERSION.SDK_INT >= 26) {
//                ft.setReorderingAllowed(false);
//            }
//            ft.detach(currentFragment).attach(currentFragment).commit();
//        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void getUserProfile(){
        //get user profile
        FirebaseUser fuser =  FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User ffuser = dataSnapshot.getValue(User.class);
                Log.d(TAG,"User "+ffuser.getImageURL());
                if (ffuser.getImageURL().equals("default")) {
                    img_profile.setImageResource(R.drawable.square_logo);
                } else {
                    Glide.with(getBaseContext()).load(ffuser.getImageURL()).placeholder(R.drawable.square_logo).thumbnail(0.1f).into(img_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get username
        Service apiService= Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getFirst_name()==null || response.body().getFirst_name().isEmpty()){
                        tvusername_drawer.setText(response.body().getUsername());
                    }else{
                        tvusername_drawer.setText(response.body().getFirst_name());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });
    }
}
