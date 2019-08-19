package com.bt_121shoppe.lucky_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.lucky_app.AccountTab.MainLoanList;
import com.bt_121shoppe.lucky_app.AccountTab.MainPostList;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.Setting.Setting;
import com.bt_121shoppe.lucky_app.chats.ChatMainActivity;
import com.bt_121shoppe.lucky_app.fragments.FragmentB1;
import com.bt_121shoppe.lucky_app.models.User;
import com.bt_121shoppe.lucky_app.useraccount.Edit_account;
import com.bt_121shoppe.lucky_app.utils.FileCompressor;
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
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Account extends AppCompatActivity  implements TabLayout.OnTabSelectedListener{

    private final static String TAG= Account.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO=1;
    private static final int REQUEST_GALLARY_PHOTO=2;
    private static final int GALLERY=1;
    private static final int CAMERA=2;
    private static final int PRIVATE_MODE=0;
    private static final int IMAGE_REQUEST=1;

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
    private  BottomNavigationView bnavigation;
    String username,password,encodeAuth,type,API_ENDPOINT="";
    int pk=0,PICK_IMAGE=1;

    Button uploadcover;
    ImageButton edit_account,setting;
    ImageView imgCover,upload,uploadprofile;
    TextView tvUsername;
    int inttab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        locale();
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
        //Log.d("Account","Breand pk"+pk);
        if (pk==0){
            Intent intent = new Intent(Account.this, UserAccount.class);
            startActivity(intent);
            finish();
        }

        imgCover = findViewById(R.id.imgCover);
        upload=findViewById(R.id.imgProfile);
        uploadprofile=findViewById(R.id.imgCover);
        tvUsername=findViewById(R.id.tvUsername);
        viewPager = findViewById(R.id.pagerMain);
        inttab=0;

        tabs = findViewById(R.id.tabs);
        //tabs.setupWithViewPager(viewPager);
        //inttab = getIntent().getIntExtra("Tab",0);
        Log.d("Acc",inttab+" "+tabs);
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
        reference= FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);

                if(user.getImageURL().equals("default")){
                    Glide.with(Account.this).load("http://www.seedcoworking.com/wp-content/uploads/2018/06/placeholder.jpg").into(upload);
                }else{
                    Glide.with(Account.this).load(user.getImageURL()).into(upload);
                }
                if(user.getCoverURL().equals("default")){
                    Glide.with(Account.this).load("https://www.templaza.com/blog/components/com_easyblog/themes/wireframe/images/placeholder-image.png").into(imgCover);
                }else{
                    Glide.with(Account.this).load(user.getCoverURL()).into(imgCover);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getUserProfile();

        edit_account = findViewById(R.id.btn_edit);
        edit_account.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, Edit_account.class);
            startActivity(intent);
        });
        setting = findViewById(R.id.btn_setting);
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this,Setting.class);
            startActivity(intent);
        });

        uploadcover = findViewById(R.id.btnUpload_Cover);
        uploadcover.setOnClickListener(v -> {
            type = "cover";
            selectImage();
        });

        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "cover";
                selectImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="profile";
                selectImage();
            }
        });
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
            Log.d(TAG,"RUN IMAGE RUL"+imageUri);
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
        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        MediaType MEDIA_TYPE=MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String respon = response.body().string();
                Gson gson = new Gson();
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            com.bt_121shoppe.lucky_app.Api.User converJsonJava = new com.bt_121shoppe.lucky_app.Api.User();
                            converJsonJava = gson.fromJson(respon, com.bt_121shoppe.lucky_app.Api.User.class);
                            if(converJsonJava.getFirst_name()==null)
                                tvUsername.setText(converJsonJava.getUsername());
                            else
                                tvUsername.setText(converJsonJava.getFirst_name());
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

    private void selectImage(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        final CharSequence[] itemkh = {"ថតរូប", "វិចិត្រសាល","បោះបង់"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Account.this);
        if (language.equals("km")){
            dialogBuilder.setItems(itemkh, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            requestStoragePermission(true);
                            break;
                        case 1:
                            requestStoragePermission(false);
                            break;
                        case 2:
                            Toast.makeText(Account.this,""+itemkh[which].toString(),Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }else if (language.equals("en")){
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
                            Toast.makeText(Account.this,""+items[which].toString(),Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
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
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent myIntent = new Intent(Account.this, Home.class);
                    startActivity(myIntent);
                    break;
                case R.id.notification:
                    Intent myIntent2 = new Intent(Account.this, Notification.class);
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
        }
    };

    private void setUpPager(){
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_post)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_like)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_loan)));
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

    private void locale() {
        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefer.getString("My_Lang","");
        Log.d("language",language);
        language(language);
    }
}

