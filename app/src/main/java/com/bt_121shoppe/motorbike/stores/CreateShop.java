package com.bt_121shoppe.motorbike.stores;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.Login_Register.Register;
import com.bt_121shoppe.motorbike.Login_Register.SelectUserTypeActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.Dealerstore;
import com.bt_121shoppe.motorbike.fragments.FragmentMap;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.UserShopViewModel;
import com.bt_121shoppe.motorbike.fragments.List_store_post;
import com.bt_121shoppe.motorbike.utils.FileCompressor;
import com.bt_121shoppe.motorbike.utils.ImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateShop extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    static final int REQUEST_TAKE_PHOTO=1;
    static final int REQUEST_GALLERY_PHOTO=2;
    private static final int REQUEST_LOCATION = 1;
    TextView tv_back;
    ImageView img_map;
    CircleImageView img_shop;
    String[] photo_select;
    private Uri imageUri,photo;
    private FileCompressor mCompressor;
    private int pk=0;
    SharedPreferences prefer;
    private ProgressDialog mProgress;
    private File mPhotoFile;
    private Bitmap bitmap;
    private EditText editPhone,editShopname,editAddress,editMap,editWing_number,editWing_account;
    private EditText editPhone1,editPhone2;
    private TextView PhoneError,shopname_alert,map_alert,address_alert;
    private TextView tv_wing_number,tv_wing_account,wing_number_alert,wing_account_alert,tv_add,tv_add1,tv_cancel;
    private String shopName,number_wing,account_wing,addresses,phone_number,phone_number1,phone_number2,location,lat_long,register;
    private String url,name,pass1,Encode,intent_edit,edit;
    private List<UserShopViewModel> userShops;
    private Button bt_add,bt_edit;
    private RelativeLayout layout_phone1,layout_phone2;
    private int mDealerShopId=0;
    private LocationManager locationManager;
    private double latitude,longtitude;
    private String storeName,storeLocation,storeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_shopp);
        Variable_Field();
        editMap.setFocusable(false);
        userShops   = new ArrayList<>();
        mCompressor = new FileCompressor(this);
        mProgress   = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.update));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        Log.e("pk",""+pk);
        url    = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        name   = prefer.getString("name","");
        pass1  = prefer.getString("pass","");
        Encode = getEncodedString(name,pass1);

        Intent intent = getIntent();
        shopName      = intent.getStringExtra("shop");
        number_wing   = intent.getStringExtra("wing_number");
        account_wing  = intent.getStringExtra("wing_name");
        photo         = intent.getParcelableExtra("photo");
        addresses     = intent.getStringExtra("addresses");
        phone_number  = intent.getStringExtra("phone_number");
        phone_number1 = intent.getStringExtra("phone_number1");
        phone_number2 = intent.getStringExtra("phone_number2");
        location      = intent.getStringExtra("road");
        register      = intent.getStringExtra("Register_verify");
        lat_long      = intent.getStringExtra("location");
        intent_edit   = intent.getStringExtra("edit_store");
        edit          = intent.getStringExtra("edit");
        mDealerShopId = intent.getIntExtra("shopId",0);
        if (intent_edit != null){
            getShop_Detail(mDealerShopId);
            getShop_Info(pk,Encode);
            bt_edit.setVisibility(View.VISIBLE);
            bt_add.setVisibility(View.GONE);
        }else {
            bt_add.setVisibility(View.VISIBLE);
            bt_edit.setVisibility(View.GONE);
        }
        editShopname.setText(shopName);
        editAddress.setText(addresses);
        editWing_account.setText(account_wing);
        editWing_number.setText(number_wing);
        editPhone.setText(phone_number);
        editPhone1.setText(phone_number1);
        editPhone2.setText(phone_number2);
        if (location != null) {
            if (location.length() > 30) {
                String loca = location.substring(0,30) + "...";
                editMap.setText(loca);
            }
        }else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            getLocation(true);
        }

        Glide.with(CreateShop.this).asBitmap().load(photo).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                img_shop.setImageBitmap(resource);
                bitmap = resource;
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        photo_select = getResources().getStringArray(R.array.select_option);

        img_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (edit != null){
                   Intent intent = new Intent(CreateShop.this, Detail_store.class);
                   intent.putExtra("edit_store",intent_edit);
                   intent.putExtra("shopId",mDealerShopId);
                   intent.putExtra("shop_name",storeName);
                   intent.putExtra("address",storeLocation);
                   intent.putExtra("shop_image",storeImage);
                   startActivity(intent);
               }else if (register == null){
                   Intent intent = new Intent(CreateShop.this, Register.class);
                   startActivity(intent);
               }else
                   finish();
            }
        });
        tv_add.setVisibility(View.VISIBLE);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone1.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
                tv_add.setVisibility(View.GONE);
            }
        });
        tv_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone2.setVisibility(View.VISIBLE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone1.setVisibility(View.GONE);
                layout_phone2.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PutShop(url,Encode,false,true);
            }
        });
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PutShop(url,Encode,true,false);
            }
        });
        img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(CreateShop.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                }else {
                    Intent intent = new Intent(CreateShop.this, FragmentMap.class);
                    intent.putExtra("edit","edit");
                    intent.putExtra("shopId",mDealerShopId);
                    intent.putExtra("addresses",editAddress.getText().toString());
                    intent.putExtra("shop",editShopname.getText().toString());
                    intent.putExtra("phone_number",editPhone.getText().toString());
                    intent.putExtra("phone_number1",editPhone1.getText().toString());
                    intent.putExtra("phone_number2",editPhone2.getText().toString());
                    intent.putExtra("photo",imageUri);
                    intent.putExtra("wing_number",editWing_account.getText().toString());
                    intent.putExtra("wing_account",editWing_number.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

    private void selectImage(){
        android.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateShop.this);
        dialogBuilder.setItems(photo_select, (dialog, which) -> {
            switch (which){
                case 0:
                    requestStoragePermission(true);
                    break;
                case 1:
                    requestStoragePermission(false);
                    break;
                case 2:
                    break;
            }
        });
        dialogBuilder.create().show();
    }
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                        }else {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) { }
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
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode==REQUEST_GALLERY_PHOTO){
                try {
                    imageUri = data.getData();
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(imageUri)));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else if(requestCode==REQUEST_TAKE_PHOTO){
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageUri=Uri.fromFile(mPhotoFile);
            }
            bitmap= BitmapFactory.decodeFile(mPhotoFile.getPath());
            Log.e("bitmap", String.valueOf(bitmap));
            Glide.with(CreateShop.this).load(bitmap).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(img_shop);
            Log.d(TAG,"IMage "+imageUri);
        }
    }
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void PutShop(String url,String encode,boolean edit,boolean add_new ) {
        MediaType media = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        JSONObject data = new JSONObject();
        JSONObject pro  = new JSONObject();
        try{
            data.put("username",name);
            data.put("password",pass1);
            pro.put("group",3);
            pro.put("wing_account_number",editWing_number.getText().toString());
            pro.put("wing_account_name",editWing_account.getText().toString());
            data.put("profile",pro);
            data.put("groups", new JSONArray("[\"1\"]"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG,data.toString());
        String auth = "Basic " + encode;
        RequestBody body = RequestBody.create(media, data.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String message = e.getMessage().toString();
                Log.d("failure Response",message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.dismiss();
                    }
                });
                mProgress.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.body().string();
                Log.d("Response EEEEE", message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String shop = editShopname.getText().toString();
                        String address = editAddress.getText().toString();
                        userShops.add(new UserShopViewModel(mDealerShopId,pk,shop,address,bitmap,1,"",edit,add_new));
                        if(userShops.size()>0){
                            for(int i=0;i<userShops.size();i++){
                                if(userShops.get(i).isEdit())
                                    putUserShop(userShops.get(i).getId(),userShops.get(i),encode);
                                else if(userShops.get(i).isAddNew())
                                    postUserShop(userShops.get(i),encode);
                            }
                        }
                        mProgress.dismiss();
                        startActivity(new Intent(CreateShop.this,Dealerstore.class));
                    }
                });
            }
        });
    }
    private void postUserShop(UserShopViewModel usershop, String encode){
        String usershopurl= ConsumeAPI.BASE_URL+"api/v1/shop/";
        OkHttpClient client1=new OkHttpClient();
        JSONObject post1=new JSONObject();
        try{
            post1.put("user",pk);
            post1.put("shop_name",usershop.getShop_name());
            post1.put("shop_address",usershop.getShop_address());
            post1.put("record_status",1);
            if(usershop.getShop_image()==null){
                post1.put("shop_image",null);
            }else{
                try {
                    post1.put("shop_image", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, usershop.getShop_image())));
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
            RequestBody body1=RequestBody.create(ConsumeAPI.MEDIA_TYPE,post1.toString());
            String auth = "Basic " + encode;
            Request request1 = new Request.Builder()
                    .url(usershopurl)
                    .post(body1)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    .header("Authorization",auth)
                    .build();
            client1.newCall(request1).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String message = e.getMessage().toString();
                    Log.d("failure Response",message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    mProgress.dismiss();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String message = response.body().string();
                    Log.d(TAG,"User shop success"+ message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.dismiss();

                        }
                    });
                    //finish();
                }
            });
        }catch (JSONException je){
            je.printStackTrace();
        }
    }

    private void putUserShop(int id,UserShopViewModel usershop,String encode){
        String usershopurl= ConsumeAPI.BASE_URL+"api/v1/shop/"+id+"/";
        OkHttpClient client1=new OkHttpClient();
        JSONObject post1=new JSONObject();
        try{
            post1.put("user",pk);
            post1.put("shop_name",usershop.getShop_name());
            post1.put("shop_address",usershop.getShop_address());
            if(usershop.getShop_image()==null){
                post1.put("shop_image",null);
            }else{
                try {
                    post1.put("shop_image", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, usershop.getShop_image())));
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
            RequestBody body1=RequestBody.create(ConsumeAPI.MEDIA_TYPE,post1.toString());
            String auth = "Basic " + encode;
            Request request1 = new Request.Builder()
                    .url(usershopurl)
                    .put(body1)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    .header("Authorization",auth)
                    .build();
            client1.newCall(request1).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String message = e.getMessage().toString();
                    Log.d("failure Response",message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    mProgress.dismiss();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String message = response.body().string();
                    Log.d(TAG,"User shop success"+ message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.dismiss();

                        }
                    });
                    //finish();
                }
            });
        }catch (JSONException je){
            je.printStackTrace();
        }
    }
    private void getLocation(boolean isCurent) {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location!=null){
                if(isCurent) {
                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();
                }

                try{
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addressList = null;
                    addressList = geocoder.getFromLocation(latitude,longtitude,1);
                    String road = addressList.get(0).getAddressLine(0);
                    if (road != null) {
                        if (road.length() > 30) {
                            String loca = road.substring(0,30) + "...";
                            editMap.setText(loca);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getShop_Info(int pk, String encode) {
        Service api = Client.getClient().create(Service.class);
        retrofit2.Call<User_Detail> call = api.getDetailUser(pk,encode);
        call.enqueue(new retrofit2.Callback<User_Detail>() {
            @Override
            public void onResponse(retrofit2.Call<User_Detail> call, retrofit2.Response<User_Detail> response) {
                if (response.isSuccessful()) {
                    String stphone = response.body().getProfile().getTelephone();
                    editPhone.setText(method(stphone));
                    editWing_account.setText(response.body().getProfile().getWing_account_name());
                    editWing_number.setText(response.body().getProfile().getWing_account_number());
                    editMap.setText(response.body().getProfile().getResponsible_officer());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<User_Detail> call, Throwable t) {

            }
        });
    }
    private void getShop_Detail(int id) {
        Service api = Client.getClient().create(Service.class);
        retrofit2.Call<ShopViewModel> call = api.getDealerShop(id);
        call.enqueue(new retrofit2.Callback<ShopViewModel>() {
            @Override
            public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response) {
                if (response.isSuccessful()) {
                    editShopname.setText(response.body().getShop_name());
                    editAddress.setText(response.body().getShop_address());
                    String image = response.body().getShop_image();
                    Glide.with(CreateShop.this).asBitmap().load(image).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            img_shop.setImageBitmap(resource);
                            bitmap = resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ShopViewModel> call, Throwable t) {

            }
        });
    }
    public String method(String str) {
        for (int i=0;i<str.length();i++){
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    private void Variable_Field(){
        bt_add             = (Button) findViewById(R.id.bt_add);
        bt_edit            = (Button) findViewById(R.id.bt_edit);

        editPhone          = (EditText) findViewById(R.id.et_phone);
        editPhone1         = (EditText) findViewById(R.id.et_phone1);
        editPhone2         = (EditText) findViewById(R.id.et_phone2);
        editAddress        = (EditText) findViewById(R.id.et_address);
        editShopname       = (EditText) findViewById(R.id.et_shopname);
        editWing_account   = (EditText) findViewById(R.id.et_wing_account);
        editWing_number    = (EditText) findViewById(R.id.et_wing_number);
        editMap            = (EditText) findViewById(R.id.et_map);

        PhoneError         = (TextView) findViewById(R.id.phone_alert);
        tv_back            = (TextView) findViewById(R.id.tv_back);
        tv_add             = (TextView) findViewById(R.id.tv_add);
        tv_add1            = (TextView) findViewById(R.id.tv_add1);
        tv_cancel          = (TextView) findViewById(R.id.tv_cancel);
        tv_wing_number     = (TextView) findViewById(R.id.tv_wing_number);
        tv_wing_account    = (TextView) findViewById(R.id.tv_wing_account);
        wing_account_alert = (TextView) findViewById(R.id.wing_account_acc_alert);
        wing_number_alert  = (TextView) findViewById(R.id.wing_number_alert);
        wing_number_alert  = (TextView) findViewById(R.id.wing_number_alert);
        wing_account_alert = (TextView) findViewById(R.id.wing_account_acc_alert);
        address_alert      = (TextView) findViewById(R.id.address_alert);
        map_alert          = (TextView) findViewById(R.id.map_alert);
        tv_add             = (TextView) findViewById(R.id.tv_add);
        tv_add1            = (TextView) findViewById(R.id.tv_add1);
        tv_cancel          = (TextView) findViewById(R.id.tv_cancel);

        img_map            = (ImageView) findViewById(R.id.map);
        img_map            = (ImageView) findViewById(R.id.map);

        img_shop           = (CircleImageView) findViewById(R.id.imgShop);

        layout_phone1      = (RelativeLayout) findViewById(R.id.layout_phone1);
        layout_phone2      = (RelativeLayout) findViewById(R.id.layout_phone2);
    }
}
