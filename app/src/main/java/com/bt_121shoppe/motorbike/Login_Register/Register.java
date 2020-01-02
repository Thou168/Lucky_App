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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
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
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.fragments.FragmentMap;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.UserShopViewModel;
import com.bt_121shoppe.motorbike.stores.CreateShop;
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

    private Button btnSubmit;
    private EditText editPhone,editComfirmPass,editPassword,editUsername,editEmail,editAddress,editGender,editMap,editDate,editWing_number,editWing_account;
    private EditText editPhone1,editPhone2;
    private static final String TAG = Register.class.getSimpleName();
    private TextView PhoneError,PasswordError,ComfirmPassError,date_alert,username_alert,gender_alert,map_alert,email_alert,address_alert;
    private TextView tv_wing_number,tv_wing_account,wing_number_alert,wing_account_alert,tv_add,tv_add1,tv_cancel,tv_password,tv_re_password,tv_privacy;
    private CheckBox term_privacy;
    private ProgressDialog mProgress;
    SharedPreferences prefer;
    Button btUpgrade,btUpdate;
    String pass,map,gender,email,address,url;
    ImageView date,img_map;
    String[] photo_select;
    private String register_verify,location,lat_long;
    private int product_id,user_group,mYear,mMonth,mDay,pk=0;
    private CircleImageView imgProfile;
    private static final int REQUEST_LOCATION = 1;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String[] genderListItems;
    static final int REQUEST_TAKE_PHOTO=1;
    static final int REQUEST_GALLERY_PHOTO=2;
    private File mPhotoFile;
    private RelativeLayout layout_phone1,layout_phone2;
    Bitmap bitmap;
    Uri image;
    private String date1,re_password,password,email1,phone,phone1,phone2,gender1,username,image1,wing_account,wing_number,mProfile;
    private FileCompressor mCompressor;
    private String pass1,name,Encode;
    private FirebaseUser fuser;

    private String FCM_API="https://fcm.googleapis.com/fcm/send";
    private String serverKey="key=AAAAc-OYK_o:APA91bFUyiHEPdYUjVatqxaVzfLPwVcd090bMY5emPPh-ubQtu76mEDAdmthgR03jYwhClbDqy0lqbSr_HAAvD0vnTqigM16YH4x-Xr1TMb3q_sz9PLtjNLpfnLi6NdCI-v6dyX6-5jB";
    private String contentType = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        editPhone = (EditText) findViewById(R.id.et_phone);
        editPhone1 = (EditText) findViewById(R.id.et_phone1);
        editPhone2 = (EditText) findViewById(R.id.et_phone2);
        editPassword = (EditText)findViewById(R.id.et_password);
        editComfirmPass = findViewById(R.id.et_re_password);
        editAddress = findViewById(R.id.et_address);
        editDate = findViewById(R.id.et_date);
        editUsername = findViewById(R.id.et_username);
        editEmail = findViewById(R.id.et_email);
        editGender = findViewById(R.id.et_gender);
        editMap = findViewById(R.id.et_map);
        PhoneError = findViewById(R.id.phone_alert);
        PasswordError = findViewById(R.id.password_alert);
        ComfirmPassError = findViewById(R.id.re_password_alert);
        date = findViewById(R.id.tv_cal);
        img_map = findViewById(R.id.map);
        term_privacy = findViewById(R.id.term_privacy);
        tv_privacy = findViewById(R.id.tv_privacy);
        tv_add = findViewById(R.id.tv_add);
        tv_add1 = findViewById(R.id.tv_add1);
        tv_cancel = findViewById(R.id.tv_cancel);
        layout_phone1 = findViewById(R.id.layout_phone1);
        layout_phone2 = findViewById(R.id.layout_phone2);
        imgProfile = findViewById(R.id.imgProfile);
        tv_password = findViewById(R.id.tv_password);
        tv_re_password = findViewById(R.id.tv_re_password);
        tv_wing_number = findViewById(R.id.tv_wing_number);
        tv_wing_account = findViewById(R.id.tv_wing_account);
        editWing_account = findViewById(R.id.et_wing_account);
        editWing_number = findViewById(R.id.et_wing_number);
        wing_account_alert = findViewById(R.id.wing_account_acc_alert);
        wing_number_alert = findViewById(R.id.wing_number_alert);
        email_alert = findViewById(R.id.email_alert);
        username_alert = findViewById(R.id.username_alert);
        date_alert = findViewById(R.id.date_alert);
        wing_number_alert = findViewById(R.id.wing_number_alert);
        wing_account_alert = findViewById(R.id.wing_account_acc_alert);
        address_alert = findViewById(R.id.address_alert);
        gender_alert = findViewById(R.id.gender_alert);
        map_alert = findViewById(R.id.map_alert);
        btUpgrade = findViewById(R.id.btn_upgrade);
        btUpdate = findViewById(R.id.update);
        btnSubmit = (Button)findViewById(R.id.sign_up);
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

        Intent intent = getIntent();
        mProfile = intent.getStringExtra("Profile");
        location = intent.getStringExtra("road");
        lat_long = intent.getStringExtra("location");
        register_verify = intent.getStringExtra("Register_verify");
        product_id      = intent.getIntExtra("product_id",0);
        user_group = intent.getIntExtra("user_group",0);
        address = intent.getStringExtra("address");
        date1 = intent.getStringExtra("date");
        re_password = intent.getStringExtra("re_password");
        password = intent.getStringExtra("password");
        email1 = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        phone1 = intent.getStringExtra("phone1");
        phone2 = intent.getStringExtra("phone2");
        gender1 = intent.getStringExtra("gender");
        username = intent.getStringExtra("username");
        image1 = intent.getStringExtra("image");
        Log.e("imageeeeeee",""+image1);
        wing_account = intent.getStringExtra("wing_account_name");
        wing_number = intent.getStringExtra("wing_account_number");
        if (intent != null){
            if (user_group != 1){
                editWing_number.setText(wing_number);
                editWing_account.setText(wing_account);
            }
            editUsername.setText(username);
            editGender.setText(gender1);
            editPhone.setText(phone);
            editPhone1.setText(phone1);
            editPhone2.setText(phone2);
            editEmail.setText(email1);
            editPassword.setText(password);
            editComfirmPass.setText(re_password);
            editDate.setText(date1);
            editAddress.setText(address);
            Glide.with(Register.this).asBitmap().load(image1).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Log.e("Resource",""+resource);
                    imgProfile.setImageBitmap(resource);
                    bitmap = resource;
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }

        if (location != null) {
            if (location.length() > 30) {
                String loca = location.substring(0,30) + "...";
                editMap.setText(loca);
            }
        }

        if (user_group == 1){
            editPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editComfirmPass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editPassword.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(4)});
            editComfirmPass.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(4)});
        }else {
            tv_wing_account.setVisibility(View.VISIBLE);
            editWing_account.setVisibility(View.VISIBLE);
            wing_account_alert.setVisibility(View.VISIBLE);
            tv_wing_number.setVisibility(View.VISIBLE);
            editWing_number.setVisibility(View.VISIBLE);
            wing_number_alert.setVisibility(View.VISIBLE);
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
                }else{
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

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.set(2000,1,1);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerFragmentDialog datePickerFragmentDialog=DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                        editDate.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear, year));
                    }
                },mYear, mMonth, mDay);
                datePickerFragmentDialog.show(getSupportFragmentManager(),null);
                datePickerFragmentDialog.setMaxDate(System.currentTimeMillis());
                datePickerFragmentDialog.setCancelColor(getResources().getColor(R.color.colorPrimaryDark));
                datePickerFragmentDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
                datePickerFragmentDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
                datePickerFragmentDialog.setOkText(getResources().getString(R.string.ok_dob));
                datePickerFragmentDialog.setCancelText(getResources().getString(R.string.cancel_dob));
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
                    try {
                        if (bitmap == null){
                            intent.putExtra("image",image);
                        }else {
                            intent.putExtra("image", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(Register.this, bitmap)));
                            Log.e("push image", "" + ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(getApplicationContext(), bitmap)));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (user_group == 1){
                    if (Number_Phone.length()<9 || Password.length() != 4  || !Password.equals(ComfirmPass)) {
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
                        if (email.isEmpty()){
                            email_alert.setTextColor(getColor(R.color.red));
                            email_alert.setText(getString(R.string.invalid_email));
                        }
                        if (email.matches(emailPattern)){
                            email_alert.setTextColor(getColor(R.color.red));
                            email_alert.setText(getString(R.string.invalid_email));
                        }
                        if (digitCasePatten.matcher(email).find()){
                            email_alert.setTextColor(getColor(R.color.red));
                            email_alert.setText(getString(R.string.invalid_email));
                        }
                        if (gender.isEmpty()){
                            gender_alert.setTextColor(getColor(R.color.red));
                            gender_alert.setText(getString(R.string.invalid_gender));
                        }
                        if (birthday.isEmpty()){
                            date_alert.setTextColor(getColor(R.color.red));
                            date_alert.setText(getString(R.string.invalid_date));
                        }
                        if (address.isEmpty()){
                            address_alert.setTextColor(getColor(R.color.red));
                            address_alert.setText(getString(R.string.invalid_address));
                        }
                        if (map.isEmpty()){
                            map_alert.setTextColor(getColor(R.color.red));
                            map_alert.setText(getString(R.string.invalid_map));
                        }
                        if (firstname.isEmpty()){
                            username_alert.setTextColor(getColor(R.color.red));
                            username_alert.setText(getString(R.string.invalid_username));
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
                            Password.trim().length()<6 || !digitCasePatten.matcher(Password).find() || !Password.equals(ComfirmPass)) {
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
                        }
                        else if (Password.trim().length()<6){
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
                        if (email.isEmpty()){
                            email_alert.setTextColor(getColor(R.color.red));
                            email_alert.setText(getString(R.string.invalid_email));
                        }
                        if (email.matches(emailPattern)){
                            email_alert.setTextColor(getColor(R.color.red));
                            email_alert.setText(getString(R.string.invalid_email));
                        }
                        if (gender.isEmpty()){
                            gender_alert.setTextColor(getColor(R.color.red));
                            gender_alert.setText(getString(R.string.invalid_gender));
                        }
                        if (birthday.isEmpty()){
                            date_alert.setTextColor(getColor(R.color.red));
                            date_alert.setText(getString(R.string.invalid_date));
                        }
                        if (address.isEmpty()){
                            address_alert.setTextColor(getColor(R.color.red));
                            address_alert.setText(getString(R.string.invalid_address));
                        }
                        if (map.isEmpty()){
                            map_alert.setTextColor(getColor(R.color.red));
                            map_alert.setText(getString(R.string.invalid_map));
                        }
                        if (wing_account.isEmpty()){
                            wing_account_alert.setTextColor(getColor(R.color.red));
                            wing_account_alert.setText(getString(R.string.invalid_wing_account));
                        }
                        if (wing_number.isEmpty()){
                            wing_number_alert.setTextColor(getColor(R.color.red));
                            wing_number_alert.setText(getString(R.string.invalid_wing_number));
                        }
                        if (firstname.isEmpty()){
                            username_alert.setTextColor(getColor(R.color.red));
                            username_alert.setText(getString(R.string.invalid_username));
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
    private void registerAPIUser(String username,String email,String number_phone,String password,int group){
        String url=ConsumeAPI.BASE_URL+"api/v1/users/";
        OkHttpClient client=new OkHttpClient();
        JSONObject postdata=new JSONObject();
        JSONObject post_body=new JSONObject();
        try{
            postdata.put("username", username);
            postdata.put("first_name",editUsername.getText().toString());
            postdata.put("email",email);
            postdata.put("password", password);
            post_body.put("gender",gender);
            post_body.put("address",address);
            post_body.put("responsible_officer",lat_long);
            post_body.put("date_of_birth",convertDateofBirth(editDate.getText().toString()));
            post_body.put("telephone", number_phone+","+editPhone1.getText().toString()+","+editPhone2.getText().toString());
            post_body.put("group",group);
            if (user_group == group){
                post_body.put("wing_account_number",editWing_account.getText().toString());
                post_body.put("wing_account_name",editWing_number.getText().toString());
            }
            postdata.put("profile", post_body);
            postdata.put("groups",new JSONArray("[\"1\"]"));
        }catch (JSONException e){
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

                        if (user_group == 1) {
                            registerUserFirebase(username, pass, String.valueOf(1));
                        }else if (user_group == 3){
                            registerUserAccount(username, pass, String.valueOf(3),id);
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

    private void registerUserFirebase(String username, String pass1, String group){
        String password=group.equals("1")?pass1+"__":pass1; //if group=1 is public user
        auth.createUserWithEmailAndPassword(email,password)
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
                            if (image == null){
                                hashMap.put("imageURL","default");
                            }else {
                                uploadImage(image);
                            }
                            hashMap.put("coverURL","default");
                            hashMap.put("status","online");
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

    private void registerUserAccount(String username, String pass1, String group, int id){
        String password=group.equals("3")?pass1+"__":pass1;
        auth.createUserWithEmailAndPassword(email,password)
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
                            if (image == null){
                                hashMap.put("imageURL","default");
                            }else {
                                uploadImage(image);
                            }
                            hashMap.put("coverURL","default");
                            hashMap.put("status","online");
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
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else if(requestCode==REQUEST_TAKE_PHOTO){
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
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
    private String convertDateofBirth(String bithdate){
        String dd= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dd = Instant.now().toString();
        }
        String d[]=dd.split("-");
        for(int i=0;i<d.length;i++){
            Log.e("current",d[i]);
        }
        String d1[]=bithdate.split("/");
        for(int i=0;i<d1.length;i++){
            Log.e("Date",d1[i]);
        }
        String dob=String.format("%s-%s-%s",d1[2],d1[1],d[2]);
        Log.e("result",""+dob);
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
    private void uploadImage(Uri image){
        Log.e(TAG,"RUN "+image);
        if(image!=null){
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtenstion(image));
            uploadTask=fileReference.putFile(image);
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
                        FirebaseUser firebaseUser=auth.getCurrentUser();
                        String userId=firebaseUser.getUid();
                        reference=FirebaseDatabase.getInstance().getReference("users").child(userId);
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);
                    }else{
                        Toast.makeText(Register.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(Register.this,"No image selected.",Toast.LENGTH_LONG).show();
        }
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
                                    String strDob=String.format("%s-%s-%s",dd[0],dd[1],dd[2]);
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
                                            String loca = road.substring(0,30) + "...";
                                            if (location != null){
                                                if (location.length() > 30) {
                                                    String locate = location.substring(0,30) + "...";
                                                    editMap.setText(locate);
                                                }
                                            }else {
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
        MediaType media = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        JSONObject data = new JSONObject();
        JSONObject pro  = new JSONObject();
        try{
            data.put("username",name);
            data.put("password",pass1);
            data.put("email",editEmail.getText().toString());
            data.put("first_name",editUsername.getText().toString());
            pro.put("data_of_birth", editDate.getText().toString());
            pro.put("address",editAddress.getText().toString());
            pro.put("responsible_officer",lat_long);
            pro.put("gender",editGender.getText().toString());
            pro.put("telephone",editPhone.getText().toString()+","+editPhone1.getText().toString()+","+editPhone2.getText().toString());
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.dismiss();
                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Register.this).create();
                        alertDialog.setTitle(getString(R.string.title_edit_account));
                        alertDialog.setMessage(getString(R.string.edit_success_message));
                        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mProgress.dismiss();
                                        startActivity(new Intent(getApplicationContext(), Account.class));
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }
        });
    }
}
