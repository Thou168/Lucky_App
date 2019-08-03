package com.bt_121shoppe.lucky_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bt_121shoppe.lucky_app.AccountTab.MainLoanList;
import com.bt_121shoppe.lucky_app.AccountTab.MainPostList;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Api.api.AllResponse;
import com.bt_121shoppe.lucky_app.Api.api.Client;
import com.bt_121shoppe.lucky_app.Api.api.Service;
import com.bt_121shoppe.lucky_app.Api.api.User;
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.Setting.Setting;
import com.bt_121shoppe.lucky_app.chats.ChatMainActivity;
import com.bt_121shoppe.lucky_app.fragments.FragmentA1;
import com.bt_121shoppe.lucky_app.fragments.FragmentB1;
import com.bt_121shoppe.lucky_app.fragments.FragmentC1;
import com.bt_121shoppe.lucky_app.useraccount.Edit_account;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account1 extends AppCompatActivity  implements TabLayout.OnTabSelectedListener{

    BottomNavigationView bnavigation;
    TabLayout tabs;
    ViewPager viewPager;
    SharedPreferences preferences;
    Button uploadcover;
    String username,password,encodeAuth,type;
    int pk,PICK_IMAGE=1;
    ImageButton edit_account,setting;
    ImageView imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);

        preferences = getSharedPreferences("Register", Context.MODE_PRIVATE);
        username = preferences.getString("name","");
        password = preferences.getString("pass","");
        encodeAuth = "Basic "+getEncodedString(username,password);
        Log.d("EncodeAuth",encodeAuth);
        if (preferences.contains("token")){
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")){
            pk = preferences.getInt("id",0);
        }
        Log.d("Account","User pk"+pk);
        if (pk==0){
            Intent intent = new Intent(Account1.this, UserAccount.class);
            startActivity(intent);
            finish();
        }

        imgCover = findViewById(R.id.imgCover);
        edit_account = findViewById(R.id.btn_edit);
        edit_account.setOnClickListener(v -> {
            Intent intent = new Intent(Account1.this, Edit_account.class);
            startActivity(intent);
        });
        setting = findViewById(R.id.btn_setting);
        setting.setOnClickListener(v -> {
           Intent intent = new Intent(Account1.this,Setting.class);
           startActivity(intent);
        });

        viewPager = findViewById(R.id.pagerMain);
        tabs = findViewById(R.id.tabs);
        setUpPager();

        bnavigation = findViewById(R.id.bnaviga);
        bnavigation.getMenu().findItem(R.id.account).setChecked(true);
        bnavigation.setOnNavigationItemSelectedListener(mlistener);

        uploadcover = findViewById(R.id.btnUpload_Cover);
        uploadcover.setOnClickListener(v -> {
            type = "cover";
            selectImage();
        });
        getUserProfile();
    }
    private void getUserProfile(){

    }
    private void selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Account1.this);
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        requestStoragePermission(true);
                        break;
                    case 1:
                        requestStoragePermission(false);
                        break;
                        case 2:
                        Toast.makeText(Account1.this,""+items[which].toString(),Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        dialogBuilder.create().show();

    }
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

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
    private void dispatchTakePictureIntent(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mlistener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent myIntent = new Intent(Account1.this, Home.class);
                    startActivity(myIntent);
                    break;
                case R.id.notification:
                    Intent myIntent2 = new Intent(Account1.this, Notification.class);
                    startActivity(myIntent2);
                    break;
                case R.id.camera:
                    Intent myIntent3 = new Intent(Account1.this, Camera.class);
                    startActivity(myIntent3);
                    break;
                case R.id.message:
                    Intent myIntent4 = new Intent(Account1.this, ChatMainActivity.class);
                    startActivity(myIntent4);
                    break;
                case R.id.account:
//                    Intent myIntent5 = new Intent(Account1.this, Camera.class);
//                    startActivity(myIntent5);
                    break;
            }
            return true;
        }
    };

    private void setUpPager(){
        tabs.addTab(tabs.newTab().setText("Posts"));
        tabs.addTab(tabs.newTab().setText("Like"));
        tabs.addTab(tabs.newTab().setText("Loan"));
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

        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    MainPostList tab1 = new MainPostList();
                    return tab1;
                case 1:
                    FragmentB1 tab2 = new FragmentB1();
                    return tab2;
                case 2:
                    MainLoanList tab3 = new MainLoanList();
                    return tab3;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }
}

