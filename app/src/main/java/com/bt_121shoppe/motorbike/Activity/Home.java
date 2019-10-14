package com.bt_121shoppe.motorbike.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.Setting.AboutUsActivity;
import com.bt_121shoppe.motorbike.Setting.ContactActivity;
import com.bt_121shoppe.motorbike.Setting.Setting;
import com.bt_121shoppe.motorbike.Setting.TermPrivacyActivity;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.homes.HomeFragment;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.custom.sliderimage.logic.SliderImage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG= Home.class.getSimpleName();
    private static final int REQUEST_PHONE_CALL =1;

    private SharedPreferences mSharedPreferences;
    private DrawerLayout mDrawerLayout;
    private ImageView mImageViewEnglish, mImageViewKhmer,mProfileImageDrawer;
    private RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavigation;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressbar;
    private Button mFilterCategory,mFilterBrand,mFilterYear,mFilterPriceRange,mFilterPostType;
    private ProgressDialog mProgress;
    private Context context=this;
    private NestedScrollView mNestedScrollView;
    private LocationManager mLocationManager;
    private Menu menu;
    private MenuItem nav_profile,nav_post,nav_like,nav_loan,nav_setting,nav_about,nav_contact,nav_term;
    private TextView mUserNameDrawer;

    private String myReferences="mypref";
    private String name="",pass="",Encode="",mViewType="List",mPostType="";
    private Bundle bundle;
    private int pk=0,mPostTypeId=0,mCategoryId=0,mBrandId=0,mYearId=0;
    private double mMinPrice=0,mMaxPrice=0;
    private Fragment currentFragment;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locale();
        mSharedPreferences=getSharedPreferences(myReferences, Context.MODE_PRIVATE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_search_type);

        SharedPreferences prefer=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefer.getString("My_Lang","");

        //Log.d(TAG,"Current Language is "+language);
//        if(language.isEmpty()){
//            language("km");
//            recreate();
//        }

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mImageViewEnglish=findViewById(R.id.english);
        mImageViewKhmer =findViewById(R.id.khmer);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        NavigationView mNavigationView=findViewById(R.id.nav_view);
        mSwipeRefreshLayout=findViewById(R.id.refresh);
        mRecyclerView=findViewById(R.id.list_new_post);
        mProgressbar=findViewById(R.id.progress_bar1);
        mNestedScrollView=findViewById(R.id.nestedScrollView);
        View headerView = mNavigationView.getHeaderView(0);
        mUserNameDrawer=headerView.findViewById(R.id.drawer_username);
        mProfileImageDrawer=headerView.findViewById(R.id.imageView);

        SwitchLanguage();
        SharedPreferences sharedPref=getSharedPreferences("Register",Context.MODE_PRIVATE);
        ActionBarDrawerToggle mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        if(sharedPref.contains("token") || sharedPref.contains("id")){
            mNavigationView.setVisibility(View.VISIBLE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            name=sharedPref.getString("name","");
            pass=sharedPref.getString("pass","");
            Encode= "Basic "+CommonFunction.getEncodedString(name,pass);
            if(sharedPref.contains("token"))
                pk=sharedPref.getInt("Pk",0);
            else if (sharedPref.contains("id"))
                pk=sharedPref.getInt("id",0);
            //get navigation user profiler here
            getUserProfile();
        }else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

//        if(language.equals("km")){
//            mImageViewEnglish.setVisibility(View.GONE);
//            mImageViewKhmer.setVisibility(View.VISIBLE);
//        }else{
//            mImageViewEnglish.setVisibility(View.VISIBLE);
//            mImageViewKhmer.setVisibility(View.GONE);
//        }

        requestStoragePermission(false);
        requestStoragePermission(true);
        mLocationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //buildAlertMessageNoGps();
                }
            });
        }

        mNavigationView.setNavigationItemSelectedListener(this);
        menu = mNavigationView.getMenu();
        nav_profile = menu.findItem(R.id.nav_profile);
        nav_post = menu.findItem(R.id.nav_post);
        nav_like = menu.findItem(R.id.nav_like);
        nav_loan = menu.findItem(R.id.nav_loan);
        nav_setting = menu.findItem(R.id.nav_setting);
        nav_about = menu.findItem(R.id.nav_about);
        nav_contact = menu.findItem(R.id.nav_contact);
        nav_term = menu.findItem(R.id.nav_privacy);

        /* start implementation bottom navigation */
        mBottomNavigation=findViewById(R.id.bnaviga);
        mBottomNavigation.getMenu().getItem(0).setChecked(true);
        mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.home:
                    mNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                    break;
                case R.id.notification:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(Home.this, Notification.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent=new Intent(Home.this, UserAccountActivity.class);
                        intent.putExtra("verify","notification");
                        startActivity(intent);
                    }
                    break;
                case R.id.camera:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(Home.this, Camera.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent=new Intent(Home.this, UserAccountActivity.class);
                        intent.putExtra("verify","camera");
                        startActivity(intent);
                    }
                    break;
                case R.id.message:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(Home.this, ChatMainActivity.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent=new Intent(Home.this, UserAccountActivity.class);
                        intent.putExtra("verify","message");
                        startActivity(intent);
                    }
                    break;
                case R.id.account:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(Home.this, Account.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent=new Intent(Home.this, UserAccountActivity.class);
                        intent.putExtra("verify","account");
                        startActivity(intent);
                    }
                    break;
            }
            return false;
        });
        /*end implementation bottom navigation */

        /* start implementation slider navigation */
        SliderImage mSliderImages=findViewById(R.id.slider);
        List<String> mImages=new ArrayList<>();
        mImages.add("http://cambo-report.com/storage/0MX5fa6STYIdLNYePG9x1rQHKPYWQSxazY8rRI1S.jpeg");
        mImages.add("https://i.ytimg.com/vi/iAkUDrdAmUU/maxresdefault.jpg");
        mImages.add("https://www.tracker.co.uk/application/files/thumbnails/hero_banner_small/6015/4867/2711/motorbike-banner.jpg");
        mImages.add("https://www.coxmotorgroup.com/images/cmg/new-motorcycles/new-motorcycle-banner.jpg");
        mSliderImages.setItems(mImages);
        mSliderImages.addTimerToSlide(3000);
        mSliderImages.getIndicator();
        /* end implementation slider navigation */
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mProgressbar.setVisibility(View.VISIBLE);

        mProgress=new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        //mProgress.show();

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            HomeFragment details = new HomeFragment();
            //details.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.frameLayout, details).commit();
        }
        //loadFragment(new HomeFragment(),"FHome");




        /* -------------- start event listener -------------------------*/
//        mImageViewKhmer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                language("km");
//                recreate();
//            }
//        });
//
//        mImageViewEnglish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                language("en");
//                recreate();
//            }
//        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, EditAccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_post) {
            Intent intent = new Intent(this, Account.class);
            startActivity(intent);
        } else if (id == R.id.nav_like) {
            Intent intent = new Intent(this, Account.class);
            intent.putExtra("Tab",1);
            startActivity(intent);
        } else if (id == R.id.nav_loan) {
            Intent intent = new Intent(this, Account.class);
            intent.putExtra("Tab",2);
            startActivity(intent);
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

    private void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.gps))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes_loan), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton(getString(R.string.no_loan), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },1500);
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
        mBottomNavigation.getMenu().getItem(0).setChecked(true);
        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
        Log.e(TAG,"current Fragment onStart "+currentFragment);
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

    private void loadFragment(Fragment fragment,String fTag){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment,fTag);
        fragmentTransaction.commit();
    }


    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {

                            } else {

                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                        // add call requestpermission by samang 27/08
                        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                            ShowTestMessage();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.permission));
        builder.setMessage(getString(R.string.setting_permission));
        builder.setPositiveButton(getString(R.string.go_setting), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        //Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void ShowTestMessage(){
        Dialog builder =new  Dialog(this);
        builder.setContentView(R.layout.dialog_custom);
        builder.setCancelable(true);
        Button btn_dialog=(Button) builder.findViewById(R.id.btn_dialog);
        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    private void SwitchLanguage(){
        Paper.init(this);
        String language=Paper.book().read("language");
        Log.e(TAG,"Current language is "+language);
        if(language==null){
            Paper.book().write("language","km");
            language("km");
            mImageViewKhmer.setVisibility(View.GONE);
            mImageViewEnglish.setVisibility(View.VISIBLE);
            updateView(Paper.book().read("language"));
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
                    language("en");
                    mImageViewKhmer.setVisibility(View.VISIBLE);
                    mImageViewEnglish.setVisibility(View.GONE);
                    updateView(Paper.book().read("language"));
                }
            });
        }

        mImageViewKhmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("language","km");
                language("km");
                mImageViewKhmer.setVisibility(View.GONE);
                mImageViewEnglish.setVisibility(View.VISIBLE);
                updateView(Paper.book().read("language"));
            }
        });
    }

    private void updateView(String language){
        //Log.e(TAG,"current Fragment on change language "+language);
        language(language);
        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(currentFragment).attach(currentFragment).commit();

//        switch (language){
//            case "km":
//                mImageViewKhmer.setVisibility(View.VISIBLE);
//                mImageViewEnglish.setVisibility(View.GONE);
//                break;
//            case "en":
//                mImageViewKhmer.setVisibility(View.GONE);
//                mImageViewEnglish.setVisibility(View.VISIBLE);
//                break;
//        }
        Context context= LocaleHapler.setLocale(this,language);
        Resources resources=context.getResources();
        //title menu
        nav_profile.setTitle(resources.getString(R.string.menu_profile));
        nav_post.setTitle(resources.getString(R.string.menu_post));
        nav_like.setTitle(resources.getString(R.string.menu_like));
        nav_loan.setTitle(resources.getString(R.string.menu_loan));
        nav_setting.setTitle(resources.getString(R.string.menu_setting));
        nav_about.setTitle(resources.getString(R.string.menu_about));
        nav_contact.setTitle(resources.getString(R.string.menu_contact));
        nav_term.setTitle(resources.getString(R.string.menu_privacy));

    }

    private void getUserProfile(){

        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        MediaType MEDIA_TYPE=MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",Encode)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String respon = response.body().string();
                Gson gson = new Gson();
                User user = new User();
                try{
                    user = gson.fromJson(respon, User.class);
                    runOnUiThread(() -> {

                        FirebaseUser fuser =  FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User ffuser = dataSnapshot.getValue(User.class);
                                Log.d(TAG,"User "+ffuser.getImageURL());
                                if (ffuser.getImageURL().equals("default")) {
                                    mProfileImageDrawer.setImageResource(R.drawable.square_logo);
                                } else {
                                    Glide.with(getBaseContext()).load(ffuser.getImageURL()).placeholder(R.drawable.square_logo).thumbnail(0.1f).into(mProfileImageDrawer);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        com.bt_121shoppe.motorbike.Api.User converJsonJava = new com.bt_121shoppe.motorbike.Api.User();
                        Log.d(TAG,respon);
                        converJsonJava = gson.fromJson(respon, com.bt_121shoppe.motorbike.Api.User.class);

                        if(converJsonJava.getFirst_name()==null || converJsonJava.getFirst_name().isEmpty()) {
                            mUserNameDrawer.setText(converJsonJava.getUsername());
                        }
                        else {
                            mUserNameDrawer.setText(converJsonJava.getFirst_name());
                        }
                    });
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

}
