package com.bt_121shoppe.motorbike.newversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatAllFragment;
import com.bt_121shoppe.motorbike.checkupdates.GooglePlayStoreAppVersionNameLoader;
import com.bt_121shoppe.motorbike.checkupdates.WSCallerVersionListener;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.newversion.accounts.AccountFragment;
import com.bt_121shoppe.motorbike.newversion.chats.ChatFragment;
import com.bt_121shoppe.motorbike.newversion.homes.HomeFragment;
import com.bt_121shoppe.motorbike.newversion.notifications.NotificationFragment;
import com.bt_121shoppe.motorbike.nointernet.NoInternetActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements WSCallerVersionListener {

    private static final String TAG=MainActivity.class.getSimpleName();
    private static final String PROFILE_IMAGE="profile_image";
    private static final int REQUEST_PHONE_CALL =1;

    private SharedPreferences sharedPref;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private Toolbar toolbar,toolbarnormal;
    private DrawerLayout drawer;
    private ImageView mImageViewEnglish, mImageViewKhmer,mAppLogo;
    private Menu menu;
    private MenuItem nav_profile,nav_post,nav_like,nav_loan,nav_setting,nav_about,nav_contact,nav_term;
    private RelativeLayout rltoolbarversion1;

    private String username="",password="",encode="";
    private int pk=0;
    private Fragment currentFragment;
    private SharedPreferences mSharedPreferences;
    private String myReferences="mypref";
    boolean isForceUpdate = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            toolbarnormal.setTitle(item.getTitle());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_post:
                    if(sharedPref.contains("token") || sharedPref.contains("id")) {
                        if (Active_user.isUserActive(MainActivity.this, pk)) {
                            startActivity(new Intent(MainActivity.this, Camera.class));
                            viewPager.setCurrentItem(1);
                        } else
                            Active_user.clearSession(MainActivity.this);
                    }else
                    {
                        Intent intent=new Intent(MainActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","camera");
                        startActivity(intent);
                    }
                    navigation.getMenu().getItem(1).setChecked(true);
                    return true;
                case R.id.navigation_chats:
                    if(sharedPref.contains("token") || sharedPref.contains("id")){
                        if(Active_user.isUserActive(MainActivity.this,pk)) {
                            rltoolbarversion1.setVisibility(View.GONE);
                            toolbarnormal.setVisibility(View.VISIBLE);
                            viewPager.setCurrentItem(2);
                        }
                        else
                            Active_user.clearSession(MainActivity.this);
                    }
                    else{
                        Intent intent=new Intent(MainActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","message");
                        startActivity(intent);
                    }
                    navigation.getMenu().getItem(2).setChecked(true);
                    return true;
                case R.id.navigation_notifications:
                    if(sharedPref.contains("token")||sharedPref.contains("id")){
                        if(Active_user.isUserActive(MainActivity.this,pk)) {
                            rltoolbarversion1.setVisibility(View.GONE);
                            toolbarnormal.setVisibility(View.VISIBLE);
                            viewPager.setCurrentItem(3);
                        }
                        else
                            Active_user.clearSession(MainActivity.this);
                    }else{
                        Intent intent=new Intent(MainActivity.this,UserAccountActivity.class);
                        intent.putExtra("verify","notification");
                        startActivity(intent);
                    }
                    navigation.getMenu().getItem(3).setChecked(true);
                    return true;
                case R.id.navigation_accounts:
                    if(sharedPref.contains("token")||sharedPref.contains("id")){
                        if(Active_user.isUserActive(MainActivity.this,pk)) {
                            toolbarnormal.setVisibility(View.GONE);
                            rltoolbarversion1.setVisibility(View.VISIBLE);
                            viewPager.setCurrentItem(4);
                        }
                        else
                            Active_user.clearSession(MainActivity.this);
                    }else{
                        Intent intent=new Intent(MainActivity.this, UserAccountActivity.class);
                        intent.putExtra("verify","account");
                        startActivity(intent);
                    }
                    navigation.getMenu().getItem(4).setChecked(true);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locale();
        mSharedPreferences=getSharedPreferences(myReferences, Context.MODE_PRIVATE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main4);
        //check internet connection
        if(!haveNetwork()){
            // App has internet connection
            Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
            startActivity(intent);
        }
        //check app version update from playstore
        new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();

        mAppLogo=findViewById(R.id.applogo);
        mImageViewEnglish=findViewById(R.id.english);
        mImageViewKhmer =findViewById(R.id.khmer);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbarnormal=findViewById(R.id.toolbar1);
        rltoolbarversion1=findViewById(R.id.rltoolbar);
        //toolbar.setTitle("");
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbarnormal);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = findViewById(R.id.view_pager);
        GooglePlusFragmentPageAdapter adapter = new GooglePlusFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);


        requestStoragePermission(false);
        requestStoragePermission(true);
        //initTitle();
        SwitchLanguage();
        sharedPref=getSharedPreferences("Register",MODE_PRIVATE);
        if(sharedPref.contains("token")||sharedPref.contains("id")){
            username=sharedPref.getString("name","");
            password=sharedPref.getString("pass","");
            encode= "Basic "+ CommonFunction.getEncodedString(username,password);
            if(sharedPref.contains("token"))
                pk=sharedPref.getInt("Pk",0);
            else if (sharedPref.contains("id"))
                pk=sharedPref.getInt("id",0);
        }
        viewPager.setCurrentItem(0);
        navigation.getMenu().getItem(0).setChecked(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        navigation.getMenu().getItem(0).setChecked(true);
        toolbar.setTitle("");
    }

    private void initialUserInformation(CircleImageView imgprofileimage,TextView tvusername){
        FirebaseUser fuser =  FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User ffuser = dataSnapshot.getValue(User.class);
                if (ffuser.getImageURL().equals("default")) {
                    imgprofileimage.setImageResource(R.drawable.square_logo);
                } else {
                    Glide.with(getBaseContext()).load(ffuser.getImageURL()).placeholder(R.drawable.square_logo).thumbnail(0.1f).into(imgprofileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Service apiService= Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getFirst_name()==null || response.body().getFirst_name().isEmpty()){
                        tvusername.setText(response.body().getUsername());
                    }else{
                        tvusername.setText(response.body().getFirst_name());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });
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

        viewPager.getAdapter().notifyDataSetChanged();
    }

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
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
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

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        //Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }

    private static class GooglePlusFragmentPageAdapter extends FragmentPagerAdapter {

        public GooglePlusFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return ChatFragment.newInstance();
                case 2:
                    return ChatAllFragment.newInstance();
                case 3:
                    return NotificationFragment.newInstance();
                case 4:
                    return AccountFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

    }

    public void showUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle(MainActivity.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(MainActivity.this.getString(R.string.update_message));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
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

}
