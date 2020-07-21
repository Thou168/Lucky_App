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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.AccountTab.MainLoanList;
import com.bt_121shoppe.motorbike.AccountTab.MainPostList;
import com.bt_121shoppe.motorbike.Api.Profile;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.Login_Register.Register;
import com.bt_121shoppe.motorbike.Login_Register.SelectUserTypeActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.fragments.DealerPostHistoryFragment;
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
import java.time.Instant;
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
    TextView tvUsername,tvFullname,tvUserGroup;
    int inttab;
    ImageView logo_kh,logo_en;
    String[] photo_select;
    Bundle bundle;
    String login_verify,register_intent;
    RelativeLayout rela_profile;
    private int process_type=0;
    String currentLanguage;
//    ImageButton im_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout1);

//        locale();
        bundle = getIntent().getExtras();
        if (bundle!=null){
            login_verify = bundle.getString("Login_verify");
            register_intent = bundle.getString("Register_verify");
            process_type = bundle.getInt("process_type",0);
        }
        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = prefer.getString("My_Lang", "");
        preferences = getSharedPreferences("Register", Context.MODE_PRIVATE);
        username = preferences.getString("name","");
        password = preferences.getString("pass","");
        if (preferences.contains("token")){
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")){
            pk = preferences.getInt("id",0);
        }

        photo_select = getResources().getStringArray(R.array.select_photo);
        logo_kh = findViewById(R.id.khmer);
        logo_en = findViewById(R.id.english);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pagerMain);
        uploadcover = findViewById(R.id.btnUpload_Cover);
        bnavigation1 = findViewById(R.id.bottom_nav);
        bnavigation = findViewById(R.id.bnaviga);
        tvUserGroup=findViewById(R.id.tvUserGroup);

        encodeAuth = "Basic "+getEncodedString(username,password);
        upload=findViewById(R.id.imgProfile);
        tvUsername=findViewById(R.id.tvUsername);
        tvFullname=findViewById(R.id.tvFullname);
        inttab=0;
        mCompressor = new FileCompressor(this);

        Service apiService=Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    int group=response.body().getProfile().getGroup();
                    tvUserGroup.setText(String.valueOf(group));
                    //Glide.with(getBaseContext()).load(response.body().getProfile().getProfile_photo()).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(upload);
                    tvFullname.setText(response.body().getUsername());
                    if(response.body().getFirst_name()==null || response.body().getFirst_name().isEmpty()){
                        tvUsername.setText(response.body().getUsername());
                    }else{
                        tvUsername.setText(response.body().getFirst_name());
                    }
                    //Log.e("TAG","User group in response "+tvUserGroup.getText());
                    if (group == 3){
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

                    setUpPager(group);
                }else{
                    bnavigation.setVisibility(View.VISIBLE);
                    bnavigation1.setVisibility(View.GONE);
                    bnavigation.getMenu().getItem(4).setChecked(true);
                    bnavigation.setOnNavigationItemSelectedListener(mlistener);
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });

//        CheckGroup checkGroup=new CheckGroup();
//        g=checkGroup.getGroup(pk,this);
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null){
            reference =FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    if(user.getImageURL().equals("default"))
                        Glide.with(getBaseContext()).load(R.drawable.group_2293).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(upload);
                    else
                        Glide.with(getBaseContext()).load(user.getImageURL()).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(upload);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        ImageButton imgSetting=findViewById(R.id.btnsetting);
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account.this,AccountSettingActivity.class);
                intent.putExtra("user_group",g);
                startActivity(intent);
            }
        });

        //profile_click
        rela_profile = findViewById(R.id.relative_profile);
        rela_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Account.this, Register.class);
                startActivity(i);
            }
        });

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            //Log.d(TAG,"REQUEST CODE "+requestCode);
            if(requestCode==REQUEST_GALLARY_PHOTO){
                imageUri=data.getData();
            }else if(requestCode==REQUEST_TAKE_PHOTO){
                //Log.d(TAG,"URN "+mPhotoFile);
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

                    Glide.with(getBaseContext()).load(response.body().getProfile().getProfile_photo()).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(upload);
                    tvFullname.setText(response.body().getUsername());
                    if(response.body().getFirst_name()==null || response.body().getFirst_name().isEmpty()){
                        tvUsername.setText(response.body().getUsername());
                    }else{
                        tvUsername.setText(response.body().getFirst_name());
                    }

                    //Log.e("TAG","endrender render function profile fullname "+ Instant.now().toString());
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
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        break;
                    case R.id.notification:
                        Intent myIntent2 = new Intent(Account.this, StoreListActivity.class);
                        startActivity(myIntent2);
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        break;
                    case R.id.camera:
                        Intent myIntent3 = new Intent(Account.this, Camera.class);
                        startActivity(myIntent3);
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        break;
                    case R.id.message:
                        Intent myIntent4 = new Intent(Account.this, ChatMainActivity.class);
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.notification:
                startActivity(new Intent(Account.this, StoreListActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.dealer:
                startActivity(new Intent(Account.this, DealerStoreActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.message:
                startActivity(new Intent(Account.this, ChatMainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.account:
                break;
        }
        return true;
    };

    private void setUpPager(int group){
        if(group==3)
            tabs.addTab(tabs.newTab().setText(R.string.history));
        else
            tabs.addTab(tabs.newTab().setText(R.string.post));
        tabs.addTab(tabs.newTab().setText(R.string.tab_like));
        tabs.addTab(tabs.newTab().setText(R.string.tab_loan));
        tabs.setOnTabSelectedListener(this);
        tabs.setupWithViewPager(viewPager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabs.getTabCount(),group);
        viewPager.setAdapter(adapter);

        inttab = getIntent().getIntExtra("Tab",0);
        tabs.getTabAt(inttab).select();
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
        int pgroup=0;
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }
        public Pager(FragmentManager fm, int tabCount,int pgroup) {
            super(fm);
            this.tabCount= tabCount;
            this.pgroup=pgroup;
        }
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    if(pgroup==3){
                        DealerPostHistoryFragment n2=new DealerPostHistoryFragment();
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
                if (pgroup!=3) {
                    if (currentLanguage.equals("en")) {
                        return "Post";
                    } else return "ប្រកាស";
                }else {
                    if (currentLanguage.equals("en")) {
                        return "History";
                    } else return "ប្រវត្តិការប្រកាស";
                }
            } else if (position==1) {
                if (currentLanguage.equals("en")) {
                    return "Like";
                }else return "ចូលចិត្ត";
            } else {
                if (currentLanguage.equals("en")) {
                    return "Loan";
                }else return "ឥណទាន";
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
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else if (process_type!=1 || process_type!=2){
            startActivity(new Intent(Account.this,Home.class));
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bnavigation.getMenu().getItem(4).setChecked(true);
        bnavigation1.getMenu().getItem(4).setChecked(true);
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

