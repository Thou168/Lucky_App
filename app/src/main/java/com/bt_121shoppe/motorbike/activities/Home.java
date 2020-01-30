package com.bt_121shoppe.motorbike.activities;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import com.bt_121shoppe.motorbike.Activity.Camera_new;
import com.bt_121shoppe.motorbike.Activity.CheckNetworkConnectionHelper;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Slider;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.Startup.Filter;
import com.bt_121shoppe.motorbike.Startup.Search1;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.checkupdates.GooglePlayStoreAppVersionNameLoader;
import com.bt_121shoppe.motorbike.checkupdates.WSCallerVersionListener;
import com.bt_121shoppe.motorbike.homes.HomeFragment;
import com.bt_121shoppe.motorbike.listener.OnNetworkConnectionChangeListener;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.nointernet.NoInternetActivity;
import com.bt_121shoppe.motorbike.stores.StoreListActivity;
import com.bumptech.glide.Glide;
import com.custom.sliderimage.logic.SliderImage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;

public class Home extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, WSCallerVersionListener {

    private static final String TAG= Home.class.getSimpleName();
    private static final int REQUEST_PHONE_CALL =1;

    private SharedPreferences mSharedPreferences;
    private DrawerLayout mDrawerLayout;
    private ImageView mImageViewEnglish, mImageViewKhmer,mProfileImageDrawer;
    private RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavigation,mBottomNavigation1;
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

    private String myReferences="mypref",username,password,basicEncode;
    private String name="",pass="",Encode="",mViewType=
            "List",mPostType="";
    private Bundle bundle;
    private int pk=0,mPostTypeId=0,mCategoryId=0,mBrandId=0,mYearId=0;
    private double mMinPrice=0,mMaxPrice=0;
    private Fragment currentFragment;
    boolean isForceUpdate = true;

    private ImageView search_homepage;

    private List<Slider> mImages=new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connecttion on and off
        onConnectionChange();

        locale();
        mSharedPreferences=getSharedPreferences(myReferences, Context.MODE_PRIVATE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_search_type);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        //check app version update from playstore
        new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();
        SharedPreferences prefer=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefer.getString("My_Lang","");


        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        SharedPreferences preferences= getApplication().getSharedPreferences("Register",MODE_PRIVATE);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }
        Log.e("pk", String.valueOf(pk));

        search_homepage = findViewById(R.id.search_homepage);
        setSearch_homepage();

        mImageViewEnglish=findViewById(R.id.english);
        mImageViewKhmer =findViewById(R.id.khmer);
        mDrawerLayout=findViewById(R.id.drawer_layout);
//        NavigationView mNavigationView=findViewById(R.id.nav_view);
        mSwipeRefreshLayout=findViewById(R.id.refresh);
        mRecyclerView=findViewById(R.id.list_new_post);

        //
        mProgressbar=findViewById(R.id.progress_bar1);
        mNestedScrollView=findViewById(R.id.nestedScrollView);
//        View headerView = mNavigationView.getHeaderView(0);
//        mUserNameDrawer=headerView.findViewById(R.id.drawer_username);
//        mProfileImageDrawer=headerView.findViewById(R.id.imageView);

        SwitchLanguage();
        SharedPreferences sharedPref=getSharedPreferences("Register",Context.MODE_PRIVATE);
//        ActionBarDrawerToggle mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();

//        if(sharedPref.contains("token") || sharedPref.contains("id")){
//            mNavigationView.setVisibility(View.VISIBLE);
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//            name=sharedPref.getString("name","");
//            pass=sharedPref.getString("pass","");
//            Encode= "Basic "+CommonFunction.getEncodedString(name,pass);
//            if(sharedPref.contains("token"))
//                pk=sharedPref.getInt("Pk",0);
//            else if (sharedPref.contains("id"))
//                pk=sharedPref.getInt("id",0);
//            //get navigation user profiler here
//            getUserProfile();
//        }else {
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        }

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


//        mNavigationView.setNavigationItemSelectedListener(this);
//        menu = mNavigationView.getMenu();
//        nav_profile = menu.findItem(R.id.nav_profile);
//        nav_post = menu.findItem(R.id.nav_post);
//        nav_like = menu.findItem(R.id.nav_like);
//        nav_loan = menu.findItem(R.id.nav_loan);
//        nav_setting = menu.findItem(R.id.nav_setting);
//        nav_about = menu.findItem(R.id.nav_about);
//        nav_contact = menu.findItem(R.id.nav_contact);
//        nav_term = menu.findItem(R.id.nav_privacy);

        /* start implementation bottom navigation */
        CheckGroup check = new CheckGroup();
        int g = check.getGroup(pk,this);
        if (g == 3){
            mBottomNavigation1=findViewById(R.id.bottom_nav);
            mBottomNavigation1.setVisibility(View.VISIBLE);
            mBottomNavigation1.getMenu().getItem(0).setChecked(true);
            mBottomNavigation1.setOnNavigationItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        mNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        break;
                    case R.id.notification:
                        startActivity(new Intent(Home.this, StoreListActivity.class));
                        break;
                    case R.id.dealer:
                        if(sharedPref.contains("token") || sharedPref.contains("id")){
                            startActivity(new Intent(Home.this, Dealerstore.class));
                        }else{
                            Intent intent=new Intent(Home.this, LoginActivity.class);
                            intent.putExtra("Login_verify","camera");
                            startActivity(intent);
                        }
                        break;
                    case R.id.message:
                        if(sharedPref.contains("token") || sharedPref.contains("id")){
                            startActivity(new Intent(Home.this, ChatMainActivity.class));
                        }else{
                            Intent intent=new Intent(Home.this, LoginActivity.class);
                            intent.putExtra("Login_verify","message");
                            startActivity(intent);
                        }
                        break;
                    case R.id.account:
                        if(sharedPref.contains("token") || sharedPref.contains("id")){
                            startActivity(new Intent(Home.this, Account.class));
                        }else{
                            Intent intent=new Intent(Home.this, LoginActivity.class);
                            intent.putExtra("Login_verify","account");
                            startActivity(intent);
                        }
                        break;
                }
                return false;
            });
        }else {
            mBottomNavigation=findViewById(R.id.bnaviga);
            mBottomNavigation.setVisibility(View.VISIBLE);
            mBottomNavigation.getMenu().getItem(0).setChecked(true);
            mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        mNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        break;
                    case R.id.notification:
                        startActivity(new Intent(Home.this, StoreListActivity.class));
                        break;
                    case R.id.camera:
                        if(sharedPref.contains("token") || sharedPref.contains("id")){
                            startActivity(new Intent(Home.this, Camera.class));
                        }else{
                            Intent intent=new Intent(Home.this, LoginActivity.class);
                            intent.putExtra("Login_verify","camera");
                            startActivity(intent);
                        }
                        break;
                    case R.id.message:
                        if(sharedPref.contains("token") || sharedPref.contains("id")){
                            startActivity(new Intent(Home.this, ChatMainActivity.class));
                        }else{
                            Intent intent=new Intent(Home.this, LoginActivity.class);
                            intent.putExtra("Login_verify","message");
                            startActivity(intent);
                        }
                        break;
                    case R.id.account:
                        if(sharedPref.contains("token") || sharedPref.contains("id")){
//                        if(Active_user.isUserActive(this,pk)){
//                            startActivity(new Intent(Home.this, Account.class));
//                        }else{
//                            Active_user.clearSession(this);
//                        }
                            startActivity(new Intent(Home.this, Account.class));
                        }else{
                            Intent intent=new Intent(Home.this, LoginActivity.class);
                            intent.putExtra("Login_verify","account");
                            startActivity(intent);
                        }

                        break;
                }
                return false;
            });
        }
        /*end implementation bottom navigation */

        /* start implementation slider navigation */
        SliderImage mSliderImages=findViewById(R.id.slider);
        List<String> mImages1 =new ArrayList<>();
        Service apiService = Client.getClient().create(Service.class);
        retrofit2.Call<AllResponse> call = apiService.getSliderImage();
        call.enqueue(new retrofit2.Callback<AllResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AllResponse> call, retrofit2.Response<AllResponse> response) {
                mImages = response.body().getresults();
                if (!response.isSuccessful()){
                    Log.d("211111111111111212", String.valueOf(response.code()));
                }
                for (int i=0;i<mImages.size();i++){
                    mImages1.add(mImages.get(i).getImg());
                }
                mSliderImages.setItems(mImages1);
                mSliderImages.addTimerToSlide(3000);
                mSliderImages.getIndicator().setPaddingRelative(400,60,10,10);
            }

            @Override
            public void onFailure(retrofit2.Call<AllResponse> call, Throwable t) {

            }
        });

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
        currentFragment=new HomeFragment();
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            HomeFragment details = new HomeFragment();
            //details.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.frameLayout, details).commit();
        }
        //loadFragment(new HomeFragment(),"FHome");

    }

    private void setSearch_homepage(){
        search_homepage.setOnClickListener(v -> {
//            Toast.makeText(getApplicationContext(),"Search_button",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Home.this, Search1.class);
            startActivity(intent);
        });
    }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_profile) {
//            Intent intent = new Intent(this, EditAccountActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_post) {
//            Intent intent = new Intent(this, Account.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_like) {
//            Intent intent = new Intent(this, Account.class);
//            intent.putExtra("Tab",1);
//            startActivity(intent);
//        } else if (id == R.id.nav_loan) {
//            Intent intent = new Intent(this, Account.class);
//            intent.putExtra("Tab",2);
//            startActivity(intent);
//        } else if (id == R.id.nav_setting) {
//            Intent intent = new Intent(this, Setting.class);
//            startActivity(intent);
//        }else if (id == R.id.nav_about) {
//            Intent intent = new Intent(this, AboutUsActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_contact) {
//            Intent intent = new Intent(this, ContactActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_privacy) {
//            Intent intent = new Intent(this, TermPrivacyActivity.class);
//            startActivity(intent);
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

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

        mSwipeRefreshLayout.setRefreshing(true);
        dorefresh();
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

                    }

                    @Override
                    public Context getContext() {
                        return Home.this;
                    }
                });
    }

    private void dorefresh(){
        mSwipeRefreshLayout.setRefreshing(false);
        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
        if(currentFragment!=null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(currentFragment).attach(currentFragment).commit();
        }
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
        CheckGroup check = new CheckGroup();
        int g = check.getGroup(pk,this);
        if (g == 3){
            mBottomNavigation1.getMenu().getItem(0).setChecked(true);
        }else {
            mBottomNavigation.getMenu().getItem(0).setChecked(true);
        }
        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
        Log.e(TAG,"current Fragment onStart "+currentFragment);
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        //Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
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
                            //showSettingsDialog();
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
                }).withErrorListener(error ->
                //Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()
                Log.e(TAG,"Error occurred.")
        )
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

        }
        currentFragment = this.getFragmentManager().findFragmentById(R.id.frameLayout);
        if(currentFragment!=null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(currentFragment).attach(currentFragment).commit();
        }
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
                        if(fuser!=null) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User ffuser = dataSnapshot.getValue(User.class);
                                    Log.d(TAG, "User " + ffuser.getImageURL());
                                    if (ffuser.getImageURL().equals("default")) {
                                        mProfileImageDrawer.setImageResource(R.mipmap.ic_launcher_round);
                                    } else {
                                        Glide.with(getBaseContext()).load(ffuser.getImageURL()).placeholder(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(mProfileImageDrawer);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
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

    public void showUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);

        alertDialogBuilder.setTitle(Home.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(Home.this.getString(R.string.update_message));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Home.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceUpdate) {
                    //finish();
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

}
