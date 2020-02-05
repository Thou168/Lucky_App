package com.bt_121shoppe.motorbike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.AccountTab.MainLoanList;
import com.bt_121shoppe.motorbike.AccountTab.MainPostList;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.fragments.Like_byuser;
import com.bt_121shoppe.motorbike.fragments.history_postbyuser;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.stores.StoreListActivity;
import com.bt_121shoppe.motorbike.utils.FileCompressor;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account extends AppCompatActivity  implements TabLayout.OnTabSelectedListener{

    private final static String TAG= Account.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO=1;
    private static final int REQUEST_GALLARY_PHOTO=2;

    TabLayout tabs;
    ViewPager viewPager;
    SharedPreferences preferences;
    private File mPhotoFile;
    private FileCompressor mCompressor;
    private Bitmap bitmapImage;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private Uri imageUri;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private  BottomNavigationView bnavigation,bnavigation1;
    String username,password,encodeAuth,type;
    int pk=0,g=0;

    Button uploadcover;
    ImageView upload;
    TextView tvUsername,tvFullname;
    int inttab;
    ImageView logo_kh,logo_en;
    String[] photo_select;
    Bundle bundle;
    String login_verify,register_intent;
//    ImageButton im_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout1);
//        locale();
        bundle = getIntent().getExtras();
        if (bundle!=null){
            login_verify = bundle.getString("Login_verify");
            register_intent = bundle.getString("Register_verify");
        }
        photo_select = getResources().getStringArray(R.array.select_photo);
        logo_kh = findViewById(R.id.khmer);
        logo_en = findViewById(R.id.english);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pagerMain);
        uploadcover = findViewById(R.id.btnUpload_Cover);
        bnavigation1 = findViewById(R.id.bottom_nav);
        bnavigation = findViewById(R.id.bnaviga);

        preferences = getSharedPreferences("Register", Context.MODE_PRIVATE);
        username = preferences.getString("name","");
        password = preferences.getString("pass","");
        encodeAuth = "Basic "+getEncodedString(username,password);
        //Log.d("EncodeAuth",encodeAuth);
        if (preferences.contains("token")){
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")){
            pk = preferences.getInt("id",0);
        }

        CheckGroup check = new CheckGroup();
        g = check.getGroup(pk,this);
        Log.e(TAG,"Group "+g);
        if (g == 3){
            bnavigation.setVisibility(View.GONE);
            bnavigation1.setVisibility(View.VISIBLE);
            bnavigation1.getMenu().getItem(4).setChecked(true);
            bnavigation1.setOnNavigationItemSelectedListener(mlistener1);
        }else {

            bnavigation.setVisibility(View.VISIBLE);
            bnavigation1.setVisibility(View.GONE);
            bnavigation.getMenu().getItem(4).setChecked(true);
            bnavigation.setOnNavigationItemSelectedListener(mlistener);
        }

        //check active and deactive account by samang 2/09/19
        Active_user activeUser = new Active_user();
        String active;
        active = activeUser.isUserActive(pk,this);
        if (active.equals("false")){
            activeUser.clear_session(this);
        }
        // end
        //Log.d("Account","Breand pk"+pk);
        if (pk==0){
            Intent intent = new Intent(Account.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        upload=findViewById(R.id.imgProfile);
        tvUsername=findViewById(R.id.tvUsername);
        tvFullname=findViewById(R.id.tvFullname);
        inttab=0;

//        tabs.setupWithViewPager(viewPager);
//        inttab = getIntent().getIntExtra("Tab",0);
//        Log.d("Acc",inttab+" "+tabs);
//        tabs.getTabAt(inttab).select();

        mCompressor = new FileCompressor(this);
        setUpPager();
        inttab = getIntent().getIntExtra("Tab",0);
        tabs.getTabAt(inttab).select();

        ImageButton imgSetting=findViewById(R.id.btnsetting);
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account.this,AccountSettingActivity.class);
                intent.putExtra("user_group",g);
                startActivity(intent);
            }
        });

        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null) {
            reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                        tvFullname.setText(user.getUsername());
                        Log.e(TAG,"Profile pic "+user.getImageURL());
                    if (user.getImageURL().equals("default")) {
                        Glide.with(Account.this).load(R.drawable.group_2293).thumbnail(0.1f).into(upload);
//                        img_profile.setImageResource(R.mipmap.ic_launcher_round);
                    } else {
                        Glide.with(getBaseContext()).load(user.getImageURL()).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(upload);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        getUserProfile();

    }  // onCreate

    private void Switch_language(){
        Paper.init(this);
        String language = Paper.book().read("language");
        //Log.d("44444444","444"+language);
        if (language == null) {
            Paper.book().write("language", "km");
            updateView(Paper.book().read("language"));
            language("km");
            logo_kh.setVisibility(View.GONE);
            logo_en.setVisibility(View.VISIBLE);
        }else {
            if (language.equals("km")) {
                logo_kh.setVisibility(View.GONE);
                logo_en.setVisibility(View.VISIBLE);
            } else {
                logo_kh.setVisibility(View.VISIBLE);
                logo_en.setVisibility(View.GONE);
            }
            logo_en.setOnClickListener(view -> {
                Paper.book().write("language", "en");
                updateView(Paper.book().read("language"));
                language("en");
                logo_kh.setVisibility(View.VISIBLE);
                logo_en.setVisibility(View.GONE);
            });
        }
        logo_kh.setOnClickListener(view -> {
            Paper.book().write("language","km");
            updateView(Paper.book().read("language"));
            language("km");
            logo_kh.setVisibility(View.GONE);
            logo_en.setVisibility(View.VISIBLE);
        });
    }
    private void updateView(String language) {
        Context context = LocaleHapler.setLocale(this,language);
//        LocaleHapler lh = new LocaleHapler();
//        lh.language(getBaseContext(),language);
        Resources resources = context.getResources();
        photo_select = resources.getStringArray(R.array.select_photo);
        uploadcover.setText(resources.getString(R.string.upload));
        //Tabs
        tabs.removeAllTabs();
        tabs.addTab(tabs.newTab().setText(resources.getString(R.string.tab_post)));
        tabs.addTab(tabs.newTab().setText(resources.getString(R.string.tab_like)));
        tabs.addTab(tabs.newTab().setText(resources.getString(R.string.tab_loan)));
        tabs.setOnTabSelectedListener(this);
        Pager adapter = new Pager(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(adapter);
        //title menu
//        nav_profile.setTitle(resources.getString(R.string.menu_profile));
//        nav_post.setTitle(resources.getString(R.string.menu_post));
//        nav_like.setTitle(resources.getString(R.string.menu_like));
//        nav_loan.setTitle(resources.getString(R.string.menu_loan));
//        nav_setting.setTitle(resources.getString(R.string.menu_setting));
//        nav_about.setTitle(resources.getString(R.string.menu_about));
//        nav_contact.setTitle(resources.getString(R.string.menu_contact));
//        nav_term.setTitle(resources.getString(R.string.menu_privacy));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            Log.d(TAG,"REQUEST CODE "+requestCode);
            if(requestCode==REQUEST_GALLARY_PHOTO){
                imageUri=data.getData();
            }else if(requestCode==REQUEST_TAKE_PHOTO){
                Log.d(TAG,"URN "+mPhotoFile);
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageUri=Uri.fromFile(mPhotoFile);

            }
            //Log.d(TAG,"RUN IMAGE RUL"+imageUri);
            if(uploadTask !=null && uploadTask.isInProgress()){
                Toast.makeText(Account.this,"Upload in progress.",Toast.LENGTH_LONG).show();
            }else{
                uploadImage(type);
            }
        }
    }

    private void getUserProfile(){
        //get username
        Service apiService= Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getFirst_name()==null || response.body().getFirst_name().isEmpty()){
                        tvUsername.setText(response.body().getUsername());
                    }else{
                        tvUsername.setText(response.body().getFirst_name());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });


    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mlistener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent myIntent = new Intent(Account.this, Home.class);
                        startActivity(myIntent);
                        break;
                    case R.id.notification:
                        Intent myIntent2 = new Intent(Account.this, StoreListActivity.class);
                        startActivity(myIntent2);
                        break;
                    case R.id.camera:
                        Intent myIntent3 = new Intent(Account.this, Camera.class);
                        startActivity(myIntent3);
                        break;
                    case R.id.message:
                        Intent myIntent4 = new Intent(Account.this, ChatMainActivity.class);
                        startActivity(myIntent4);
                        break;
                    case R.id.account:
                        break;
                }
                return true;
            };
    private BottomNavigationView.OnNavigationItemSelectedListener mlistener1
            = item -> {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(Account.this, Home.class));
                break;
            case R.id.notification:
                startActivity(new Intent(Account.this, StoreListActivity.class));
                break;
            case R.id.dealer:
                startActivity(new Intent(Account.this, DealerStoreActivity.class));
                break;
            case R.id.message:
                startActivity(new Intent(Account.this, ChatMainActivity.class));
                break;
            case R.id.account:
                break;
        }
        return true;
    };

    private void setUpPager(){
        if(g==3)
            tabs.addTab(tabs.newTab().setText(R.string.history));
        else
            tabs.addTab(tabs.newTab().setText(R.string.tab_post));
        tabs.addTab(tabs.newTab().setText(R.string.tab_like));
        tabs.addTab(tabs.newTab().setText(R.string.tab_loan));
        tabs.setOnTabSelectedListener(this);
        tabs.setupWithViewPager(viewPager);
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
                    //MainPostList tab1 = new MainPostList();
                    if(g==3){
                        history_postbyuser n2 = new history_postbyuser();
                        return n2;
                    }
                    else{
                        MainPostList tab1=new MainPostList(g);
                        return tab1;
                    }
                case 1:
                    Like_byuser tab2 = new Like_byuser();
                    return tab2;
                case 2:
                    MainLoanList tab3 = new MainLoanList();
                    return tab3;
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
            if (position==0) {
                return getApplicationContext().getString(R.string.tab_post);
            } else if (position==1) {
                return getApplicationContext().getString(R.string.tab_like);
            } else {
                return getApplicationContext().getString(R.string.tab_loan);
            }
        }
    }


    private String getFileExtenstion(Uri uri){
        ContentResolver contentResolver= Account.this.getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(String type){

        final ProgressDialog pd=new ProgressDialog(Account.this);
        pd.setMessage(getString(R.string.uploading));
        pd.show();

        if(imageUri!=null){
            Log.d(TAG,"RUN "+type);
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtenstion(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri= (Uri) task.getResult();
                        String mUri=downloadUri.toString();
                        reference=FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                        HashMap<String,Object> map=new HashMap<>();
                        if(type.equals("profile"))
                            map.put("imageURL",mUri);
                        else if(type.equals("cover"))
                            map.put("coverURL",mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                    }else{
                        Toast.makeText(Account.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Account.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
        else {
            Toast.makeText(Account.this,"No image selected.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (login_verify != null || register_intent != null){
            startActivity(new Intent(Account.this,Home.class));
        } else finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckGroup check = new CheckGroup();
        int g = check.getGroup(pk,this);
        if (g == 3){
            bnavigation1.getMenu().getItem(4).setChecked(true);
        }else {
            bnavigation.getMenu().getItem(4).setChecked(true);
        }
    }
    private void language(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration confi =new  Configuration();
        confi.locale = locale;
        getBaseContext().getResources().updateConfiguration(confi, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

}

