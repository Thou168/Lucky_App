package com.bt_121shoppe.motorbike.Login_Register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.Convert_Json_Java;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.activities.NotificationActivity;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.date.YearMonthPickerDialog;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.fragments.FragmentMap;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.UserShopViewModel;
import com.bt_121shoppe.motorbike.stores.CreateShop;
import com.bt_121shoppe.motorbike.activities.AccountSettingActivity;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
import com.bt_121shoppe.motorbike.utils.FileCompressor;
import com.bt_121shoppe.motorbike.utils.ImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
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
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocketImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity implements BottomChooseGender.ItemClickListener{

    private static final String TAG = Register.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO=1;
    private static final int REQUEST_GALLERY_PHOTO=2;
    private static final int REQUEST_LOCATION = 1;
    private Button btnSubmit;
    private Button btUpgrade,btUpdate;
    private EditText editPhone,editComfirmPass,editPassword,editUsername,editEmail,editAddress,editGender,editMap,editDate,editWing_number,editWing_account;
    private EditText editPhone1,editPhone2;
    private TextView PhoneError,PasswordError,ComfirmPassError,date_alert,username_alert,gender_alert,map_alert,email_alert,address_alert;
    private TextView tv_wing_number,tv_wing_account,wing_number_alert,wing_account_alert,tv_add,tv_add1,tv_cancel,tv_password,tv_re_password,tv_privacy;
    private CheckBox term_privacy;
    private ImageView date,img_map;
    private RelativeLayout layout_phone1,layout_phone2;
    private int product_id,user_group,mYear,mMonth,mDay,pk=0;
    private CircleImageView imgProfile;
    private Bitmap bitmap,bitmapProfileImage,bitmpaDefault;
    private Uri image;
    private FileCompressor mCompressor;
    private FirebaseUser fuser;
    private StorageReference storageReference;
    private SharedPreferences prefer;
    private StorageTask uploadTask;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private File mPhotoFile;
    private ProgressDialog mProgress;
    private LocationManager locationManager;
    private double latitude,longtitude;

    private String[] photo_select;
    private String[] genderListItems;
    private String pass1,name,Encode;
    private String register_verify,location,lat_long;
    private String pass,map,gender,email,address,url;
    private String mEdit,date1,re_password,password,email1,phone,phone1,phone2,gender1,username,wing_account,wing_number,mProfile,edit_profile;
    private String FCM_API="https://fcm.googleapis.com/fcm/send";
    private String serverKey="key=AAAAc-OYK_o:APA91bFUyiHEPdYUjVatqxaVzfLPwVcd090bMY5emPPh-ubQtu76mEDAdmthgR03jYwhClbDqy0lqbSr_HAAvD0vnTqigM16YH4x-Xr1TMb3q_sz9PLtjNLpfnLi6NdCI-v6dyX6-5jB";
    private String contentType = "application/json";
    private  Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Variable_Field();

        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+ConsumeAPI.FB_Notification);
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        Log.e("pk",""+pk);
        url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        name = prefer.getString("name","");
        pass1 = prefer.getString("pass","");
        Encode =getEncodedString(name,pass1);

        btnSubmit.setVisibility(View.VISIBLE);
        term_privacy.setVisibility(View.VISIBLE);
        tv_privacy.setVisibility(View.VISIBLE);

        editDate.setFocusable(false);
        editGender.setFocusable(false);
        editMap.setFocusable(false);
        photo_select = getResources().getStringArray(R.array.select_option);
        mCompressor = new FileCompressor(this);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        intent = getIntent();
        edit_profile = intent.getStringExtra("edit_profile");
        user_group = intent.getIntExtra("user_group",0);
        Log.e("group",""+user_group);
        if (edit_profile == null){
            if (pk != 0) {
                initialUserInformation(url, Encode);
                editPassword.setVisibility(View.GONE);
                editComfirmPass.setVisibility(View.GONE);
                tv_password.setVisibility(View.GONE);
                tv_re_password.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                btUpdate.setVisibility(View.VISIBLE);
                tv_privacy.setVisibility(View.GONE);
                term_privacy.setVisibility(View.GONE);
            }
        }else {
            if (pk != 0) {
                editPassword.setVisibility(View.GONE);
                editComfirmPass.setVisibility(View.GONE);
                tv_password.setVisibility(View.GONE);
                tv_re_password.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                btUpdate.setVisibility(View.VISIBLE);
                tv_privacy.setVisibility(View.GONE);
                term_privacy.setVisibility(View.GONE);
                if (user_group == 1){
                    btUpgrade.setVisibility(View.VISIBLE);
                    tv_wing_account.setVisibility(View.GONE);
                    editWing_account.setVisibility(View.GONE);
                    wing_account_alert.setVisibility(View.GONE);
                    tv_wing_number.setVisibility(View.GONE);
                    editWing_number.setVisibility(View.GONE);
                    wing_number_alert.setVisibility(View.GONE);
                }else {
                    btUpgrade.setVisibility(View.GONE);
                    tv_wing_account.setVisibility(View.VISIBLE);
                    editWing_account.setVisibility(View.VISIBLE);
                    wing_account_alert.setVisibility(View.VISIBLE);
                    tv_wing_number.setVisibility(View.VISIBLE);
                    editWing_number.setVisibility(View.VISIBLE);
                    wing_number_alert.setVisibility(View.VISIBLE);
                }
            }
        }
        if (user_group == 1){
            editPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editComfirmPass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editPassword.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(4)});
            editComfirmPass.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(4)});
        }else {
            editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editComfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            tv_wing_account.setVisibility(View.VISIBLE);
            editWing_account.setVisibility(View.VISIBLE);
            wing_account_alert.setVisibility(View.VISIBLE);
            tv_wing_number.setVisibility(View.VISIBLE);
            editWing_number.setVisibility(View.VISIBLE);
            wing_number_alert.setVisibility(View.VISIBLE);
        }
        mEdit = intent.getStringExtra("edit");
        mProfile = intent.getStringExtra("Profile");
        location = intent.getStringExtra("road");
        lat_long = intent.getStringExtra("location");
        register_verify = intent.getStringExtra("Register_verify");
        product_id      = intent.getIntExtra("product_id",0);
        address = intent.getStringExtra("address");
        date1 = intent.getStringExtra("date");
        re_password = intent.getStringExtra("re_password");
        password = intent.getStringExtra("password");
        email1 = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        phone1 = intent.getStringExtra("phone1");
        phone2 = intent.getStringExtra("phone2");
        gender = intent.getStringExtra("gender");
        username = intent.getStringExtra("username");
        image = intent.getParcelableExtra("image");
        wing_account = intent.getStringExtra("wing_account_name");
        wing_number = intent.getStringExtra("wing_account_number");
        if (user_group != 1){
            editWing_number.setText(wing_number);
            editWing_account.setText(wing_account);
        }
        editUsername.setText(username);
        editGender.setText(gender);
        editPhone.setText(phone);
        editPhone1.setText(phone1);
        editPhone2.setText(phone2);
        editEmail.setText(email1);
        editPassword.setText(password);
        editComfirmPass.setText(re_password);
        editDate.setText(date1);
        editAddress.setText(address);

        Glide.with(Register.this).asBitmap().load(image).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgProfile.setImageBitmap(resource);
                bitmap = resource;
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        if (location != null) {
            if (location.length() > 30) {
                String loca = location.substring(0,30) + "...";
                editMap.setText(loca);
            }
        }else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            getLocation(true);
        }

        btUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,CreateShop.class));
            }
        });

        editComfirmPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnSubmit.performClick();
                }
                return false;
            }
        });
        TextView tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProfile!=null){
                    startActivity(new Intent(Register.this, SelectUserTypeActivity.class));
                }else if (pk != 0){
                    startActivity(new Intent(Register.this, AccountSettingActivity.class));
                }else {
                    finish();
                }
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
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
                tv_add.setVisibility(View.VISIBLE);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2000,01,01);
                YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(Register.this, calendar, new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                        editDate.setText(dateFormat.format(calendar.getTime()));

                    }
                });
                yearMonthPickerDialog.setMinYear(1960);
                yearMonthPickerDialog.setMaxYear(2020);
                yearMonthPickerDialog.show();
            }
        });
        genderListItems=getResources().getStringArray(R.array.genders_array);
        editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v);
            }
        });
        img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                }else {
                    Intent intent = new Intent(Register.this, FragmentMap.class);
                    intent.putExtra("register","register");
                    intent.putExtra("Profile",mProfile);
                    intent.putExtra("edit",mEdit);
                    intent.putExtra("Register_verify", register_verify);
                    intent.putExtra("group", user_group);
                    intent.putExtra("address", editAddress.getText().toString());
                    intent.putExtra("date", editDate.getText().toString());
                    intent.putExtra("re_password", editComfirmPass.getText().toString());
                    intent.putExtra("password", editPassword.getText().toString());
                    intent.putExtra("email", editEmail.getText().toString());
                    intent.putExtra("phone", editPhone.getText().toString());
                    intent.putExtra("phone1", editPhone1.getText().toString());
                    intent.putExtra("phone2", editPhone2.getText().toString());
                    intent.putExtra("gender", editGender.getText().toString());
                    intent.putExtra("username", editUsername.getText().toString());
                    intent.putExtra("image", image);
                    intent.putExtra("wing_account_number", editWing_account.getText().toString());
                    intent.putExtra("wing_account_name", editWing_number.getText().toString());
                    startActivity(intent);
                }
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editUsername.getText().toString().length()<3 || editPhone.getText().toString().length()<9 ){
                    if (editPhone.getText().toString().length()<9){
                        editPhone.requestFocus();
                    }
                    if (editUsername.getText().toString().length()<3){
                        editUsername.requestFocus();
                    }
                    mProgress.show();
                    PutData(url, Encode);
                }else {
                    mProgress.show();
                    PutData(url, Encode);
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass=editPassword.getText().toString();
                String firstname = editUsername.getText().toString();
                email = editEmail.getText().toString();
                address = editAddress.getText().toString();
                String birthday = editDate.getText().toString();
                String gender = editGender.getText().toString();
                map = editMap.getText().toString();
                wing_number = editWing_number.getText().toString();
                wing_account = editWing_account.getText().toString();
                String first_name = editUsername.getText().toString();
                String Number_Phone = editPhone.getText().toString();
                String Password = editPassword.getText().toString();
                String username = editPhone.getText().toString();
                String ComfirmPass = editComfirmPass.getText().toString();
                Pattern lowerCasePatten = Pattern.compile("[a-zA-Z]");
                Pattern digitCasePatten = Pattern.compile("[0-9 ]");
                Pattern white_space = Pattern.compile("[\\s]");
                Log.e("user_group",""+user_group);
                if (user_group == 1){
                    if (Number_Phone.length()<9 || Password.length() != 4  || !Password.equals(ComfirmPass)
                            || birthday.isEmpty() || gender.isEmpty()) {
                        if (first_name.isEmpty()){
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(getString(R.string.invalid_phone));
                        }
                        if (Number_Phone.isEmpty()) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhone);
                        } else if (Number_Phone.length() < 9) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhoneWrong);
                        } else {
                            PhoneError.setText("");
                        }

                        if (Password.isEmpty()) {
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.inputPassword);
                        } else if (Password.length() != 4) {
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.user_message);
                        } else {
                            PasswordError.setText("");
                        }

                        if (ComfirmPass.isEmpty()) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.inputComfirm);
                        } else if (!Password.equals(ComfirmPass)) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.wrongInputPasswordSecond);
                        } else {
                            ComfirmPassError.setText("");
                        }
                        if (gender.isEmpty()){
                            gender_alert.setTextColor(getColor(R.color.red));
                            gender_alert.setText(getString(R.string.invalid_gender));
                        }else {
                            gender_alert.setText("");
                        }
                        if (birthday.isEmpty()){
                            date_alert.setTextColor(getColor(R.color.red));
                            date_alert.setText(getString(R.string.invalid_date));
                        }else {
                            date_alert.setText("");
                        }
                        if (firstname.isEmpty()){
                            username_alert.setTextColor(getColor(R.color.red));
                            username_alert.setText(getString(R.string.invalid_username));
                        }else {
                            username_alert.setText("");
                        }
                        if (!term_privacy.isChecked()){

                        }
                    }else if (CheckNumber(ComfirmPass)) {
                        mProgress.show();
                        PhoneError.setText("");
                        ComfirmPassError.setText("");
                        PasswordError.setText("");
                        email_alert.setText("");
                        address_alert.setText("");
                        gender_alert.setText("");
                        date_alert.setText("");
                        map_alert.setText("");
                        username_alert.setText("");
                        registerAPIUser(username,email,Number_Phone,Password,user_group);
                    } else {
                        mProgress.dismiss();
                    }
                }else {
                    if (Number_Phone.length()<9 || white_space.matcher(Password).find() || !lowerCasePatten.matcher(Password).find() ||
                            Password.trim().length()<6 || !digitCasePatten.matcher(Password).find() || !Password.equals(ComfirmPass)
                           || birthday.isEmpty() || gender.isEmpty()) {
                        if (Number_Phone.isEmpty()) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhone);
                        } else if (Number_Phone.length() < 9) {
                            PhoneError.setTextColor(getColor(R.color.red));
                            PhoneError.setText(R.string.inputPhoneWrong);
                        } else {
                            PhoneError.setText("");
                        }

                        if (Password.isEmpty()) {
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.inputPassword);
                        } else if (Password.trim().length()<6){
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.user_message_dealer);
                        }
                        else if (!lowerCasePatten.matcher(Password).find() || !digitCasePatten.matcher(Password).find()){
                            PasswordError.setTextColor(getColor(R.color.red));
                            PasswordError.setText(R.string.valid_dealer_pass);
                        }
                        else if (white_space.matcher(Password).find()){
                            PasswordError.setText(R.string.no_whitespace_pass);
                            PasswordError.setTextColor(getColor(R.color.red));
                        }
                        else {
                            PasswordError.setText("");
                        }

                        if (ComfirmPass.isEmpty()) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.inputComfirm);
                        } else if (!Password.equals(ComfirmPass)) {
                            ComfirmPassError.setTextColor(getColor(R.color.red));
                            ComfirmPassError.setText(R.string.wrongInputPasswordSecond);
                        } else {
                            ComfirmPassError.setText("");
                        }
                        if (gender.isEmpty()){
                            gender_alert.setTextColor(getColor(R.color.red));
                            gender_alert.setText(getString(R.string.invalid_gender));
                        }else {
                            gender_alert.setText("");
                        }
                        if (birthday.isEmpty()){
                            date_alert.setTextColor(getColor(R.color.red));
                            date_alert.setText(getString(R.string.invalid_date));
                        }else {
                            date_alert.setText("");
                        }
                        if (wing_account.isEmpty()){
                            wing_account_alert.setTextColor(getColor(R.color.red));
                            wing_account_alert.setText(getString(R.string.invalid_wing_account));
                        }else {
                            wing_account_alert.setText("");
                        }
                        if (wing_number.isEmpty()){
                            wing_number_alert.setTextColor(getColor(R.color.red));
                            wing_number_alert.setText(getString(R.string.invalid_wing_number));
                        }else {
                            wing_number_alert.setText("");
                        }
                        if (firstname.isEmpty()){
                            username_alert.setTextColor(getColor(R.color.red));
                            username_alert.setText(getString(R.string.invalid_username));
                        }else {
                            username_alert.setText("");
                        }
                        if (!term_privacy.isChecked()){

                        }
                    }else {
                        mProgress.show();
                        PhoneError.setText("");
                        ComfirmPassError.setText("");
                        PasswordError.setText("");
                        email_alert.setText("");
                        address_alert.setText("");
                        gender_alert.setText("");
                        date_alert.setText("");
                        map_alert.setText("");
                        username_alert.setText("");
                        wing_number_alert.setText("");
                        wing_account_alert.setText("");
                        registerAPIUser(username,email,Number_Phone,Password,user_group);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mProfile!=null){
            startActivity(new Intent(Register.this, SelectUserTypeActivity.class));
        }else if (pk != 0){
            startActivity(new Intent(Register.this, AccountSettingActivity.class));
        }else {
            finish();
        }
    }

    private void registerAPIUser(String username, String email, String number_phone, String password, int group){

        String g = editGender.getText().toString().toLowerCase();
        String url=ConsumeAPI.BASE_URL+"api/v1/users/";
        OkHttpClient client=new OkHttpClient();
        JSONObject postdata=new JSONObject();
        JSONObject post_body=new JSONObject();
        try{
            postdata.put("username", username);
            postdata.put("first_name",editUsername.getText().toString());
            postdata.put("email",email);
            postdata.put("password", password);
            if (g.equals("female") || g.equals("ស្រី")){
                gender1 = "female";
            }else if (g.equals("male") || g.equals("ប្រុស")){
                gender1 = "male";
            }else if (g.equals("other") || g.equals("ផ្សេងទៀត")){
                gender1 = "other";
            }
            post_body.put("gender",gender1);
            post_body.put("address",address);
            post_body.put("responsible_officer",lat_long);
            post_body.put("date_of_birth",convertDateofBirth(editDate.getText().toString()));
            post_body.put("telephone", number_phone+","+editPhone1.getText().toString()+","+editPhone2.getText().toString());
            post_body.put("group",group);
            if (user_group == group){
                post_body.put("wing_account_number",editWing_account.getText().toString());
                post_body.put("wing_account_name",editWing_number.getText().toString());
            }
            //added by Rith
            bitmpaDefault=BitmapFactory.decodeResource(this.getResources(),R.drawable.logo_121);
            if(bitmapProfileImage==null){
                post_body.put("profile_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmpaDefault)));
            }else{
                post_body.put("profile_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapProfileImage)));
            }

            postdata.put("profile", post_body);
            postdata.put("groups",new JSONArray("[\"1\"]"));
        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        Log.d(TAG,"Data"+postdata);
        RequestBody body=RequestBody.create(ConsumeAPI.MEDIA_TYPE,postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.d(TAG,mMessage);
                convertUser(mMessage);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                mProgress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "failure Response:" + mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void convertUser(String mMessage){
        Gson gson =new  Gson();
        Convert_Json_Java convertJsonJava =new Convert_Json_Java();
        try {
            convertJsonJava = gson.fromJson(mMessage, Convert_Json_Java.class);
            int g=convertJsonJava.getGroup();
            int id = convertJsonJava.getId();
            String username=convertJsonJava.getUsername();
            String apiImageURL=convertJsonJava.getProfile().getProfile_photo();
            Log.e("TAG","Profile Image:"+apiImageURL);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (id!=0){
                        SharedPreferences.Editor editor =prefer.edit();
                        editor.putInt("id",id);
                        editor.putString("name",username);
                        editor.putString("pass",editComfirmPass.getText().toString());
                        editor.putString("groups",String.valueOf(g));
                        editor.commit();
                        String userEmail=ConsumeAPI.PREFIX_EMAIL+id+"@email.com";
                        if (user_group == 1) {
                            registerUserFirebase(userEmail,username, pass, String.valueOf(1),apiImageURL);
                        }else if (user_group == 3){
                            registerUserAccount(userEmail,username, pass, String.valueOf(3),id,apiImageURL);
                        }
                    }else {
                        AlertDialog alertDialog=new AlertDialog.Builder(Register.this).create();
                        alertDialog.setTitle(getString(R.string.register));
                        alertDialog.setMessage(getString(R.string.verify_code_message));
                        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        mProgress.dismiss();
                    }
                }
            });

        } catch (JsonParseException e) {
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog=new AlertDialog.Builder(Register.this).create();
                    alertDialog.setTitle(getString(R.string.register));
                    alertDialog.setMessage(getString(R.string.verify_code_message));
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    PhoneError.setText(R.string.verify_code_message);
                    PhoneError.setTextColor(getColor(R.color.red));
                    alertDialog.show();
                    mProgress.dismiss();
                }
            });

        }
    }

    private void registerUserFirebase(String email,String username, String pass1, String group,String imageURL){
        Log.d(TAG,"email: "+email+"  username:"+username+ " password "+pass1+" group :"+group);
        String password=group.equals("1")?pass1+"__":pass1; //if group=1 is public user
        //.createUserWithEmailAndPassword(email,password)
        auth.createUserWithEmailAndPassword(email,ConsumeAPI.DEFAULT_FIREBASE_PASSWORD_ACC)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userId=firebaseUser.getUid();
                            //save user information to firebase
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            if (imageURL.isEmpty()){
                                hashMap.put("imageURL","default");
                            }else {
                                //uploadImage(0);
                                hashMap.put("imageURL",imageURL);
                            }
                            //hashMap.put("imageURL","default");

                            hashMap.put("coverURL","default");
                            hashMap.put("status","online");
                            hashMap.put("email",email);
                            hashMap.put("search",username.toString());
                            hashMap.put("password",password);
                            hashMap.put("group",group);
                            reference.child("users").child(userId).setValue(hashMap);

                            //start send notification

                            String topic="/topics/"+ConsumeAPI.FB_Notification;
                            JSONObject notification=new JSONObject();
                            JSONObject notifcationBody=new JSONObject();

                            try
                            {
                                notifcationBody.put("title", "Register");
                                notifcationBody.put("message", "Register successfully.");  //Enter your notification message
                                notifcationBody.put("to",firebaseUser.getUid());
                                notification.put("to", topic);
                                notification.put("data", notifcationBody);
                                FBPostCommonFunction.submitNofitication(firebaseUser.getUid(),notifcationBody.toString());
                                Log.e("TAG", "try");
                            }catch (JSONException e){
                                Log.e("TAG","onCreate: "+e.getMessage());
                            }
                            sendNotification(notification);
                            //end send notification

                            Intent intent;
                            if(register_verify!=null) {
                                switch (register_verify) {
                                    case "notification":
                                        intent = new Intent(Register.this, NotificationActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "camera":
                                        intent = new Intent(Register.this, Camera.class);
                                        intent.putExtra("Register_verify",register_verify);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "message":
                                        intent = new Intent(Register.this, ChatMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "account":
                                        intent = new Intent(Register.this, Account.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "detail":
                                        intent = new Intent(Register.this, Detail_New_Post.class);
                                        intent.putExtra("ID", product_id);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        intent = new Intent(Register.this, Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
                            }else{
                                intent = new Intent(Register.this, Account.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                            mProgress.dismiss();
                        }else{
                            Toast.makeText(Register.this,"You cannot register with email or password."+task.getException(),Toast.LENGTH_SHORT).show();

                            Log.d(TAG,"Error "+task.getException()+" "+task.getResult());
                        }
                    }
                });
    }

    private void registerUserAccount(String email,String username, String pass1, String group, int id,String imageURL){
        String password=group.equals("3")?pass1+"__":pass1;
        //auth.createUserWithEmailAndPassword(email,password)
        auth.createUserWithEmailAndPassword(email,ConsumeAPI.DEFAULT_FIREBASE_PASSWORD_ACC)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userId=firebaseUser.getUid();
                            //save user information to firebase
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
//                            if (image == null){
//                                hashMap.put("imageURL","default");
//                            }else {
//                                uploadImage(0);
//                            }
                            if(imageURL.isEmpty())
                                hashMap.put("imageURL","default");
                            else
                                hashMap.put("imageURL",imageURL);
                            hashMap.put("coverURL","default");
                            hashMap.put("status","online");
                            hashMap.put("email",email);
                            hashMap.put("search",username.toString());
                            hashMap.put("password",password);
                            hashMap.put("group",group);
                            reference.child("users").child(userId).setValue(hashMap);

                            //start send notification

                            String topic="/topics/"+ConsumeAPI.FB_Notification;
                            JSONObject notification=new JSONObject();
                            JSONObject notifcationBody=new JSONObject();

                            try
                            {
                                notifcationBody.put("title", "Register");
                                notifcationBody.put("message", "Register successfully.");  //Enter your notification message
                                notifcationBody.put("to",firebaseUser.getUid());
                                notification.put("to", topic);
                                notification.put("data", notifcationBody);
                                FBPostCommonFunction.submitNofitication(firebaseUser.getUid(),notifcationBody.toString());
                                Log.e("TAG", "try");
                            }catch (JSONException e){
                                Log.e("TAG","onCreate: "+e.getMessage());
                            }
                            sendNotification(notification);
                            //end send notification


                            Intent intent = new Intent(Register.this, Account.class);
                            intent.putExtra("id_register",id);
                            intent.putExtra("ID",product_id);
                            intent.putExtra("Register_verify",register_verify);
                            startActivity(intent);
                            mProgress.dismiss();

                        }else{
                            Toast.makeText(Register.this,"You cannot register with email or password."+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
          }
    private Boolean CheckNumber(String st){
        Boolean check = false;
        String no = "\\d*\\.?\\d+";
        CharSequence inputStr = st;
        Pattern pte = Pattern.compile(no,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pte.matcher(inputStr);

        if (matcher.matches()){
            check = true;
        }
        return check;

    }

    private void sendNotification(JSONObject notification){
        Log.e("TAG", "sendNotification"+notification);
        RequestQueue requestQueue=Volley.newRequestQueue(this.getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(FCM_API, notification, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("TAG", "onResponse: "+response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Request error", Toast.LENGTH_LONG).show();
                Log.i("TAG", "onErrorResponse: Didn't work");
            }
        }){
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Authorization", serverKey);
                headers.put("Content-Type", contentType);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    private void selectImage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
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
                    image = data.getData();
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(image)));
                    bitmapProfileImage=BitmapFactory.decodeFile(mPhotoFile.getPath());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else if(requestCode==REQUEST_TAKE_PHOTO){
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapProfileImage=BitmapFactory.decodeFile(mPhotoFile.getPath());
                }catch (IOException e){
                    e.printStackTrace();
                }
                image=Uri.fromFile(mPhotoFile);
            }
            bitmap= BitmapFactory.decodeFile(mPhotoFile.getPath());
            Log.e("bitmap", String.valueOf(bitmap));
            Glide.with(Register.this).load(bitmap).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imgProfile);
            Log.d(TAG,"IMage "+image);
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
    private String convertDateofBirth(String year){
        String dd= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dd = Instant.now().toString();
        }
        String d[]=dd.split("-");
        for(int i=0;i<d.length;i++){
            Log.e(TAG,d[i]);
        }
        String dob=String.format("%s-%s-%s",year,d[1],d[2]);
        Log.d(TAG,dob);
        return dob;
    }
    public void showBottomSheet(View view) {
        BottomChooseGender addPhotoBottomDialogFragment = BottomChooseGender.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseGender.TAG);
    }

    @Override
    public void onItemClick(String item) {
        editGender.setText(item);
    }
    private String getFileExtenstion(Uri uri){
        ContentResolver contentResolver= Register.this.getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }
    private void initialUserInformation(String url,String encode){
        MediaType MEDIA_TYPE     =  MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e(TAG,mMessage);
                Gson gson = new Gson();
                Log.d(TAG,"User"+mMessage);
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            User convertJsonJava = new User();
                            convertJsonJava = gson.fromJson(mMessage,User.class);
                            int g=convertJsonJava.getProfile().getGroup();
                            if (g==1){
                                btUpgrade.setVisibility(View.VISIBLE);
                                tv_wing_account.setVisibility(View.GONE);
                                editWing_account.setVisibility(View.GONE);
                                wing_account_alert.setVisibility(View.GONE);
                                tv_wing_number.setVisibility(View.GONE);
                                editWing_number.setVisibility(View.GONE);
                                wing_number_alert.setVisibility(View.GONE);
                            }else if (g==3){
                                btUpgrade.setVisibility(View.GONE);
                                tv_wing_account.setVisibility(View.VISIBLE);
                                editWing_account.setVisibility(View.VISIBLE);
                                wing_account_alert.setVisibility(View.VISIBLE);
                                tv_wing_number.setVisibility(View.VISIBLE);
                                editWing_number.setVisibility(View.VISIBLE);
                                wing_number_alert.setVisibility(View.VISIBLE);
                            }
                            editUsername.setText(convertJsonJava.getFirst_name());
                            editEmail.setText(convertJsonJava.getEmail());

                            if(convertJsonJava.getProfile()!=null) {

                                if(convertJsonJava.getProfile().getTelephone()!=null){
                                    String phone = convertJsonJava.getProfile().getTelephone();
                                    String[] splitPhone = phone.split(",");

                                    if (splitPhone.length == 1){
                                        editPhone.setText(String.valueOf(splitPhone[0]));
                                        tv_add.setVisibility(View.VISIBLE);

                                    }else if(splitPhone.length == 2){
                                        tv_add1.setVisibility(View.VISIBLE);
                                        editPhone.setText(String.valueOf(splitPhone[0]));
                                        editPhone1.setText(String.valueOf(splitPhone[1]));
                                    }else if (splitPhone.length == 3){
                                        editPhone.setText(String.valueOf(splitPhone[0]));
                                        editPhone1.setText(String.valueOf(splitPhone[1]));
                                        editPhone2.setText(String.valueOf(splitPhone[2]));
                                    }

                                }
                                if(convertJsonJava.getProfile().getWing_account_number()!=null){
                                    editWing_number.setText(convertJsonJava.getProfile().getWing_account_number());
                                }
                                if(convertJsonJava.getProfile().getWing_account_name()!=null){
                                    editWing_account.setText(convertJsonJava.getProfile().getWing_account_name());
                                }
                                String s = convertJsonJava.getProfile().getGender();
                                if (s!=null ){
                                    if (s.equals("male")){
                                        editGender.setText(R.string.male);
                                    }else if (s.equals("female")){
                                        editGender.setText(R.string.female);
                                    }else if (s.equals("other")){
                                        editGender.setText(getString(R.string.other));
                                    }
                                }

                                if(convertJsonJava.getProfile().getDate_of_birth() !=null) {
                                    String d = convertJsonJava.getProfile().getDate_of_birth();
                                    String dd[] = d.split("-");
                                    String strDob=String.format("%s",dd[0]);
                                    editDate.setText(strDob);

                                    List<String> date = new ArrayList<>();
                                    date.add(0, d);
                                }

                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(getApplication(), Locale.getDefault());
                                String addr = convertJsonJava.getProfile().getResponsible_officer();
                                if (!addr.isEmpty()) {
                                    String add[] = addr.split(",");
                                    Double latetitude = Double.parseDouble(add[0]);
                                    Double longtitude = Double.parseDouble(add[1]);
                                    try {
                                        addresses = geocoder.getFromLocation(latetitude, longtitude, 1);
                                        String road = addresses.get(0).getAddressLine(0);
                                        if (road.length() > 30) {
                                            String loca = road.substring(0, 30) + "...";
                                            if (location != null) {
                                                if (location.length() > 30) {
                                                    String locate = location.substring(0, 30) + "...";
                                                    editMap.setText(locate);
                                                }
                                            } else {
                                                editMap.setText(loca);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(convertJsonJava.getProfile().getAddress()!=null) {
                                    String address = convertJsonJava.getProfile().getAddress();
                                    editAddress.setText(address);
                                }
                            }
                            storageReference= FirebaseStorage.getInstance().getReference("uploads");
                            fuser= FirebaseAuth.getInstance().getCurrentUser();
                            if(fuser!=null) {
                                reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        com.bt_121shoppe.motorbike.models.User user = dataSnapshot.getValue(com.bt_121shoppe.motorbike.models.User.class);
                                        if (user.getImageURL().equals("default")) {
                                            Glide.with(Register.this).load(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(imgProfile);
                                        } else {
                                            Glide.with(getBaseContext()).load(user.getImageURL()).placeholder(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(imgProfile);
                                            image = Uri.parse(user.getImageURL());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void PutData(String url,String encode) {
        String g = editGender.getText().toString().toLowerCase();
        MediaType media = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        JSONObject data = new JSONObject();
        JSONObject pro  = new JSONObject();
        try{
            data.put("username",name);
            data.put("password",pass1);
            data.put("email",editEmail.getText().toString());
            data.put("first_name",editUsername.getText().toString());
            pro.put("date_of_birth",convertDateofBirth(editDate.getText().toString()));
            pro.put("address",editAddress.getText().toString());
            pro.put("responsible_officer",lat_long);
            if (g.equals("female") || g.equals("ស្រី")){
                gender1 = "female";
            }else if (g.equals("male") || g.equals("ប្រុស")){
                gender1 = "male";
            }else if (g.equals("other") || g.equals("ផ្សេងទៀត")){
                gender1 = "other";
            }
            pro.put("gender",gender1);
            pro.put("telephone",editPhone.getText().toString()+","+editPhone1.getText().toString()+","+editPhone2.getText().toString());
            pro.put("wing_account_number",editWing_number.getText().toString());
            pro.put("wing_account_name",editWing_account.getText().toString());
            //added by Rith
            bitmpaDefault=BitmapFactory.decodeResource(this.getResources(),R.drawable.logo_121);
            if(bitmapProfileImage==null){
                pro.put("profile_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmpaDefault)));
            }else{
                pro.put("profile_photo", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapProfileImage)));
            }
            data.put("profile",pro);
            data.put("groups", new JSONArray("[\"1\"]"));

        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e){
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
                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Register.this).create();
                        alertDialog.setTitle(getString(R.string.title_edit_account));
                        alertDialog.setMessage(getString(R.string.edit_fail_message));
                        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        mProgress.dismiss();
                    }
                });
                mProgress.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.body().string();
                Log.d("Response EEEEE", message);
                Convert(message);
            }
        });
    }
    private void Convert(String mMessage){
        Gson gson =new  Gson();
        Convert_Json_Java convertJsonJava =new Convert_Json_Java();
        try {
            convertJsonJava = gson.fromJson(mMessage, Convert_Json_Java.class);
            int id = convertJsonJava.getId();
            String apiImageURL=convertJsonJava.getProfile().getProfile_photo();
//            Log.e("TAG","Profile Image:"+apiImageURL);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (id!=0){
                        UpdateData(apiImageURL);
                    }
                }
            });

        } catch (JsonParseException e) {
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

        }
    }
    private void UpdateData(String image){
        reference= FirebaseDatabase.getInstance().getReference();
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null) {
            Query query = reference.child("users").orderByChild(fuser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reference=FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("imageURL",image);
                    reference.updateChildren(map);
                    AlertDialog alertDialog = new AlertDialog.Builder(Register.this).create();
                    alertDialog.setTitle(getString(R.string.title_edit_account));
                    alertDialog.setMessage(getString(R.string.edit_success_message));
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> {
                        mProgress.dismiss();
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        dialog.dismiss();
                    });
                    alertDialog.show();
                    //mProgress.dismiss();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void Variable_Field(){
        editPhone        = (EditText) findViewById(R.id.et_phone);
        editPhone1       = (EditText) findViewById(R.id.et_phone1);
        editPhone2       = (EditText) findViewById(R.id.et_phone2);
        editPassword     = (EditText) findViewById(R.id.et_password);
        editComfirmPass  = (EditText) findViewById(R.id.et_re_password);
        editAddress      = (EditText) findViewById(R.id.et_address);
        editDate         = (EditText) findViewById(R.id.et_date);
        editUsername     = (EditText) findViewById(R.id.et_username);
        editEmail        = (EditText) findViewById(R.id.et_email);
        editGender       = (EditText) findViewById(R.id.et_gender);
        editMap          = (EditText) findViewById(R.id.et_map);
        editWing_account = (EditText) findViewById(R.id.et_wing_account);
        editWing_number  = (EditText) findViewById(R.id.et_wing_number);

        PhoneError         = (TextView) findViewById(R.id.phone_alert);
        PasswordError      = (TextView) findViewById(R.id.password_alert);
        ComfirmPassError   = (TextView) findViewById(R.id.re_password_alert);
        tv_privacy         = (TextView) findViewById(R.id.tv_privacy);
        tv_add             = (TextView) findViewById(R.id.tv_add);
        tv_add1            = (TextView) findViewById(R.id.tv_add1);
        tv_cancel          = (TextView) findViewById(R.id.tv_cancel);
        tv_password        = (TextView) findViewById(R.id.tv_password);
        tv_re_password     = (TextView) findViewById(R.id.tv_re_password);
        tv_wing_number     = (TextView) findViewById(R.id.tv_wing_number);
        tv_wing_account    = (TextView) findViewById(R.id.tv_wing_account);
        wing_account_alert = (TextView) findViewById(R.id.wing_account_acc_alert);
        wing_number_alert  = (TextView) findViewById(R.id.wing_number_alert);
        email_alert        = (TextView) findViewById(R.id.email_alert);
        username_alert     = (TextView) findViewById(R.id.username_alert);
        date_alert         = (TextView) findViewById(R.id.date_alert);
        wing_number_alert  = (TextView) findViewById(R.id.wing_number_alert);
        wing_account_alert = (TextView) findViewById(R.id.wing_account_acc_alert);
        address_alert      = (TextView) findViewById(R.id.address_alert);
        gender_alert       = (TextView) findViewById(R.id.gender_alert);
        map_alert          = (TextView) findViewById(R.id.map_alert);

        date       = (ImageView) findViewById(R.id.tv_cal);
        img_map    = (ImageView) findViewById(R.id.map);

        term_privacy  = (CheckBox) findViewById(R.id.term_privacy);
        layout_phone1 = (RelativeLayout) findViewById(R.id.layout_phone1);
        layout_phone2 = (RelativeLayout) findViewById(R.id.layout_phone2);
        imgProfile    = (CircleImageView) findViewById(R.id.imgProfile);

        btUpgrade   = (Button) findViewById(R.id.btn_upgrade);
        btUpdate    = (Button) findViewById(R.id.update);
        btnSubmit   = (Button) findViewById(R.id.sign_up);
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
                    lat_long = latitude+","+longtitude;
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
}
