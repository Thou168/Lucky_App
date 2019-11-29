package com.bt_121shoppe.motorbike.Activity;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.settings.AboutUsActivity;
import com.bt_121shoppe.motorbike.settings.ContactActivity;
import com.bt_121shoppe.motorbike.settings.Setting;
import com.bt_121shoppe.motorbike.settings.TermPrivacyActivity;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.fragments.Like_byuser;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
import com.bt_121shoppe.motorbike.utils.FileCompressor;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account extends AppCompatActivity  implements TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener{

    private final static String TAG= Account.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO=1;
    private static final int REQUEST_GALLARY_PHOTO=2;
    private static final int GALLERY=1;
    private static final int CAMERA=2;
    private static final int PRIVATE_MODE=0;
    private static final int IMAGE_REQUEST=1;

    TabLayout tabs;
    ViewPager viewPager;
    SharedPreferences preferences,lanugau_pre;
    private File mPhotoFile;
    private FileCompressor mCompressor;
    private Bitmap bitmapImage;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private Uri imageUri;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private  BottomNavigationView bnavigation;
    String username,password,encodeAuth,type,API_ENDPOINT="";
    int pk=0,PICK_IMAGE=1;

    Button uploadcover;
    ImageButton edit_account,setting;
    ImageView imgCover,upload,uploadprofile;
    TextView tvUsername,tvusername_drawer;
    int inttab;
    String lang,tmppost,tmplike,tmploan;
    ImageView logo_kh,logo_en;
    String[] photo_select;
    private DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    Menu menu;
    MenuItem nav_profile,nav_post,nav_like,nav_loan,nav_setting,nav_about,nav_contact,nav_term;
    View view_header;
    CircleImageView img_profile;
//    ImageButton im_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout1);
//        locale();
        photo_select = getResources().getStringArray(R.array.select_photo);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        logo_kh = findViewById(R.id.khmer);
        logo_en = findViewById(R.id.english);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pagerMain);
        uploadcover = findViewById(R.id.btnUpload_Cover);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        view_header = navigationView.getHeaderView(0);

        View headerView = navigationView.getHeaderView(0);
        tvusername_drawer =  headerView.findViewById(R.id.drawer_username);
        img_profile = headerView.findViewById(R.id.imageView);

        menu = navigationView.getMenu();

        nav_profile = menu.findItem(R.id.nav_profile);
        nav_post = menu.findItem(R.id.nav_post);
        nav_like = menu.findItem(R.id.nav_like);
        nav_loan = menu.findItem(R.id.nav_loan);
        nav_setting = menu.findItem(R.id.nav_setting);
        nav_about = menu.findItem(R.id.nav_about);
        nav_contact = menu.findItem(R.id.nav_contact);
        nav_term = menu.findItem(R.id.nav_privacy);
        Switch_language();
//        lanugau_pre = getSharedPreferences("Settings",Context.MODE_PRIVATE);
//        lang = lanugau_pre.getString("My_Lang","");
//        logo_kh = findViewById(R.id.khmer);
//        logo_en = findViewById(R.id.english);
//        if (lang.equals("km")){
//            logo_kh.setVisibility(View.GONE);
//            logo_en.setVisibility(View.VISIBLE);
////            logo_en.setOnClickListener(v -> language("en"));
//        }else {
//            logo_kh.setVisibility(View.VISIBLE);
//            logo_en.setVisibility(View.GONE);
////            logo_kh.setOnClickListener(v -> language("km"));
//        }

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
            Intent intent = new Intent(Account.this, UserAccountActivity.class);
            startActivity(intent);
            finish();
        }

        imgCover = findViewById(R.id.imgCover);
        upload=findViewById(R.id.imgProfile);
        uploadprofile=findViewById(R.id.imgCover);
        tvUsername=findViewById(R.id.tvUsername);

        inttab=0;

        //tabs.setupWithViewPager(viewPager);
        //inttab = getIntent().getIntExtra("Tab",0);
        //Log.d("Acc",inttab+" "+tabs);
        //tabs.getTabAt(inttab).select();

        mCompressor = new FileCompressor(this);
        setUpPager();
        inttab = getIntent().getIntExtra("Tab",0);
        tabs.getTabAt(inttab).select();

        bnavigation = findViewById(R.id.bnaviga);
        bnavigation.getMenu().getItem(4).setChecked(true);
        bnavigation.setOnNavigationItemSelectedListener(mlistener);

        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null) {
            reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

//        com.bt_121shoppe.motorbike.utils.Notification.sendNotification(fuser.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user.getImageURL().equals("default")) {
                        Glide.with(Account.this).load(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(upload);
                        img_profile.setImageResource(R.mipmap.ic_launcher_round);
                    } else {
                        Glide.with(getBaseContext()).load(user.getImageURL()).placeholder(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(upload);
                        Glide.with(getBaseContext()).load(user.getImageURL()).placeholder(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(img_profile);
                    }
                    if (user.getCoverURL().equals("default")) {
                        Glide.with(Account.this).load(R.drawable.logo_motobike).thumbnail(0.1f).into(imgCover);
                    } else {
                        Glide.with(getBaseContext()).load(user.getCoverURL()).placeholder(R.drawable.logo_motobike).thumbnail(0.1f).into(imgCover);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        getUserProfile();

        edit_account = findViewById(R.id.btn_edit);
        edit_account.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, EditAccountActivity.class);
            startActivity(intent);
        });
        setting = findViewById(R.id.btn_setting);
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this,Setting.class);
            startActivity(intent);
        });

        uploadcover.setOnClickListener(v -> { type = "cover";
            selectImage();
        });

        imgCover.setOnClickListener(v -> { type = "cover";
            selectImage();
        });

        upload.setOnClickListener(v -> { type="profile";
            selectImage();
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
        nav_profile.setTitle(resources.getString(R.string.menu_profile));
        nav_post.setTitle(resources.getString(R.string.menu_post));
        nav_like.setTitle(resources.getString(R.string.menu_like));
        nav_loan.setTitle(resources.getString(R.string.menu_loan));
        nav_setting.setTitle(resources.getString(R.string.menu_setting));
        nav_about.setTitle(resources.getString(R.string.menu_about));
        nav_contact.setTitle(resources.getString(R.string.menu_contact));
        nav_term.setTitle(resources.getString(R.string.menu_privacy));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            Log.d(TAG,"REQUEST CODE "+requestCode);
            if(requestCode==REQUEST_GALLARY_PHOTO){
                imageUri=data.getData();
                /*
                Log.d(TAG,"RUN IMAGE RUL"+imageUri);
                if(uploadTask !=null && uploadTask.isInProgress()){
                    Toast.makeText(Account.this,"Upload in progress.",Toast.LENGTH_LONG).show();
                }else{
                    uploadImage(type);
                }
                */
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
        /*
        if(requestCode== IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null && data.getData() !=null){
            imageUri=data.getData();

            if(uploadTask !=null && uploadTask.isInProgress()){
                Toast.makeText(Account.this,"Upload in progress.",Toast.LENGTH_LONG).show();
            }else{
                uploadImage();
            }
        }
        */
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
                        tvusername_drawer.setText(response.body().getUsername());
                    }else{
                        tvUsername.setText(response.body().getFirst_name());
                        tvusername_drawer.setText(response.body().getFirst_name());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });


//        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
//        MediaType MEDIA_TYPE=MediaType.parse("application/json");
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .header("Accept","application/json")
//                .header("Content-Type","application/json")
//                .header("Authorization",encodeAuth)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                String respon = response.body().string();
//                Gson gson = new Gson();
//                User user = new User();
//                try{
//                    user = gson.fromJson(respon, User.class);
//                    runOnUiThread(() -> {
//
//                        FirebaseUser fuser =  FirebaseAuth.getInstance().getCurrentUser();
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
//
//                        reference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                User ffuser = dataSnapshot.getValue(User.class);
//                                if (ffuser.getImageURL().equals("default")) {
//                                    img_profile.setImageResource(R.drawable.square_logo);
//                                } else {
//                                    Glide.with(getBaseContext()).load(ffuser.getImageURL()).placeholder(R.drawable.square_logo).thumbnail(0.1f).into(img_profile);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                        com.bt_121shoppe.motorbike.Api.User converJsonJava = new com.bt_121shoppe.motorbike.Api.User();
//                        converJsonJava = gson.fromJson(respon, com.bt_121shoppe.motorbike.Api.User.class);
//                        if(converJsonJava.getFirst_name()==null || converJsonJava.getFirst_name().isEmpty()) {
//                            tvUsername.setText(converJsonJava.getUsername());
//                            tvusername_drawer.setText(converJsonJava.getUsername());
//                        }
//                        else {
//                            tvUsername.setText(converJsonJava.getFirst_name());
//                            tvusername_drawer.setText(converJsonJava.getFirst_name());
//                        }
//                    });
//                }catch (JsonParseException e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//        });
    }

    private void selectImage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Account.this);
//        if (language.equals("km")){
            dialogBuilder.setItems(photo_select, (dialog, which) -> {
                switch (which){
                    case 0:
                        Log.e("TAG","here request camera");
                        requestStoragePermission(true);
                        break;
                    case 1:
                        Log.e("TAG","here request photo");
                        requestStoragePermission(false);
                        break;
  //Add case 2 by Raksmey
                    case 2:
                        AlertDialog builder = new AlertDialog.Builder(Account.this).create();
                        builder.setMessage(getString(R.string.delete_photo));
                        builder.setCancelable(false);
                        builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (type.equals("cover")){
                                    try {
                                        RemoveImage(type);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else if (type.equals("profile")){
                                    try {
                                        RemoveImage(type);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        builder.setButton(Dialog.BUTTON_NEGATIVE,getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectImage();
                            }
                        });
                        builder.show();
                        break;
 //End
                    case 3:
//                        Toast.makeText(Account.this,""+photo_select[which],Toast.LENGTH_SHORT).show();
                        break;
                }
            });
            dialogBuilder.create().show();
//        }
//        else if (language.equals("en")){
//            dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (which){
//                        case 0:
//                            requestStoragePermission(true);
//                            break;
//                        case 1:
//                            requestStoragePermission(false);
//                            break;
//                        case 2:
//                            Toast.makeText(Account.this,""+items[which].toString(),Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                }
//            });
//        }

    }
    private void requestStoragePermission(boolean isCamera) {

        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.e("TAG","It's allow permiision");
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
        Log.e("TAG","y not show");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        this.getPackageName() + ".provider",
                        photoFile);
                //BuildConfig.APPLICATION_ID
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLARY_PHOTO);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
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
                        Intent myIntent2 = new Intent(Account.this, NotificationActivity.class);
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
    //                    Intent myIntent5 = new Intent(Account.this, Camera.class);
    //                    startActivity(myIntent5);
                        break;
                }
                return true;
            };

    private void setUpPager(){
        tabs.addTab(tabs.newTab().setText(R.string.tab_post));
        tabs.addTab(tabs.newTab().setText(R.string.tab_like));
        tabs.addTab(tabs.newTab().setText(R.string.tab_loan));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, EditAccountActivity.class);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        } else if (id == R.id.nav_post) {
            drawer.closeDrawer(GravityCompat.START);
            tabs.getTabAt(0).select();
        } else if (id == R.id.nav_like) {
            drawer.closeDrawer(GravityCompat.START);
            tabs.getTabAt(1).select();
        } else if (id == R.id.nav_loan) {
            drawer.closeDrawer(GravityCompat.START);
            tabs.getTabAt(2).select();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, Setting.class);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(this, TermPrivacyActivity.class);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);
        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        im_back = findViewById(R.id.ib_back);
//        im_back.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.closeDrawer(GravityCompat.START);
//            }
//        } );
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    MainPostList tab1 = new MainPostList();
                    return tab1;
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
    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
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
 //add function RemoveImage by Raksmey
    private void RemoveImage(String type) throws IOException {

        final ProgressDialog pd=new ProgressDialog(Account.this);
        pd.setMessage(getString(R.string.deleting));
        pd.show();
        imageUri = Uri.fromFile(createImageFile());
        if(imageUri!=null){
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
    }
    //End

    @Override
    protected void onStart() {
        super.onStart();
        bnavigation.getMenu().getItem(4).setChecked(true);
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
//
//    private void locale() {
//        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
//        String language = prefer.getString("My_Lang","");
//        Log.d("language",language);
//        language(language);
//    }
}

