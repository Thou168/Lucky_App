package com.bt_121shoppe.motorbike.useraccount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bt_121shoppe.motorbike.Activity.Account;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.date.YearMonthPickerDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Validator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Edit_account extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = Edit_account.class.getSimpleName();
    private LinearLayout layout_public_user,layout_121_dealer;
    private SearchView tvAddress_account;
    private EditText etUsername,etShop_name,etWingNumber,etWingName,etPhone,etPhone2,etPhone3,etEmail;
    private TextInputLayout tilShop_name,tilPhone2,tilPhone3;
    private ImageButton btnImagePhone1,btnImagePhone2;
    private ImageView imgType,imgGender,imgPob,imgLocation,imgAddress,imgMarried,imgUsername,imgDob,imgWingNumber,
            imgWingName,imgPhone,imgPhone2,imgPhone3,imgEmail,imgShopName,imgShopAddr,imgResponsible;
    private EditText btnsubmit,mp_Gender,mp_Married,mp_Dob,mp_Pob,mp_location,tvType;
    private String name,pass,Encode,user_id;
    private ArrayAdapter<Integer> ad_id;
    private int pk, id_pob=0,id_location=0,id_type=0;
    private SharedPreferences prefer;
    private List<Integer> provinceIdArrayList=new ArrayList<>();
    private List<String> provinceNameArrayList=new ArrayList<>();
    private RequestQueue mQueue;
    private ProgressDialog mProgress;
    ArrayAdapter<CharSequence> adapter;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double latitude,longtitude;
    String latlng;
    GoogleMap mMap;
    int mMonth,mYear,mDay;
    private String[] genderListItems,genderListItemkh,maritalStatusListItems,yearListItems,provinceListItems,provinceItemkh,type_userListItem,usertpyeItem;
    private int[] provinceIdListItems,yearIdListItems,type_userid;
    private String strGender,strMaritalStatus,strDob,strYob,strPob,strLocation;

    private TextInputLayout input_user, input_wingname,input_wingnum;
    private Button btUpgrade;

    private Validator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        TextView back = (TextView) findViewById(R.id.tv_Back);
//        layout_public_user = (LinearLayout)findViewById(R.id.layout_type_public_user);
//        layout_121_dealer  = (LinearLayout)findViewById(R.id.layout_type_121_dealer);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
//check active and deactive account by samang 2/09/19
        Active_user activeUser = new Active_user();
        String active;
        active = activeUser.isUserActive(pk,this);
        if (active.equals("false")){
            activeUser.clear_session(this);
        }
//end
        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode =getEncodedString(name,pass);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.update));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");

        tvType      = findViewById(R.id.tvType);
        etUsername  =(EditText) findViewById(R.id.etUsername);
        etShop_name =(EditText) findViewById(R.id.etShop_name);
        etWingName  =(EditText) findViewById(R.id.etWingName);
        etWingNumber=(EditText) findViewById(R.id.etWingNumber);
        etPhone     =(EditText) findViewById(R.id.etPhone_account);
        etPhone2    =(EditText) findViewById(R.id.etPhone_account2);
        etPhone3    =(EditText) findViewById(R.id.etPhone_account3);
        etEmail     =(EditText) findViewById(R.id.etEmail_account);

        btnImagePhone1 = (ImageButton)findViewById(R.id.btnPhone_account);
        btnImagePhone2 = (ImageButton)findViewById(R.id.btnPhone_account2);

        mp_Dob      = (EditText) findViewById(R.id.mp_Dob);
        mp_Pob      = (EditText) findViewById(R.id.mp_Pob);
        mp_Married  = (EditText) findViewById(R.id.mp_Married);
        mp_Gender   = (EditText) findViewById(R.id.mp_Gender);
        mp_location = (EditText) findViewById(R.id.mp_Location);
        tvAddress_account = (SearchView) findViewById(R.id.tvAccount_Address);

        imgType        =(ImageView) findViewById(R.id.imgType);
        imgUsername =(ImageView) findViewById(R.id.imgUsername);
        imgShopName =(ImageView) findViewById(R.id.imgShop_name);
        imgWingName =(ImageView) findViewById(R.id.imgWingName);
        imgWingNumber=(ImageView) findViewById(R.id.imgWingNumber);
        imgPhone    =(ImageView) findViewById(R.id.imgPhone_account);
        imgPhone2   =(ImageView) findViewById(R.id.imgPhone_account2);
        imgPhone3   =(ImageView) findViewById(R.id.imgPhone_account3);
        imgEmail    =(ImageView) findViewById(R.id.imgEmail_account);
        imgDob      =(ImageView) findViewById(R.id.imgDob);
        imgPob         =(ImageView) findViewById(R.id.imgPob);
        imgMarried     =(ImageView) findViewById(R.id.imgMarried);
        imgGender      =(ImageView) findViewById(R.id.imgGender);
        imgLocation    =(ImageView) findViewById(R.id.imgLocation);
        imgAddress     = (ImageView) findViewById(R.id.imgAccount_Address);

        input_user = (TextInputLayout)findViewById(R.id.tilUsername);
        input_wingname = (TextInputLayout)findViewById(R.id.tilWingName);
        input_wingnum = (TextInputLayout)findViewById(R.id.tilWingNumber);
        tilShop_name  = (TextInputLayout)findViewById(R.id.tilShop_name);
        tilPhone2     = (TextInputLayout)findViewById(R.id.tilPhone_account2);
        tilPhone3     = (TextInputLayout)findViewById(R.id.tilPhone_account3);
        btUpgrade = findViewById(R.id.btn_upgrade);

        genderListItems=getResources().getStringArray(R.array.genders_array);
        mp_Gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle(getString(R.string.choose_gender));
                mBuilder.setSingleChoiceItems(genderListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mp_Gender.setText(genderListItems[i]);
                        imgGender.setImageResource(R.drawable.ic_check_circle_black_24dp);

                        switch (i){
                            case 0:
                                strGender="male";
                                break;
                            case 1:
                                strGender="female";
                                break;
                        }

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        maritalStatusListItems=getResources().getStringArray(R.array.marital_status_array);
        mp_Married.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle(getString(R.string.choose_status));
                mBuilder.setSingleChoiceItems(maritalStatusListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mp_Married.setText(maritalStatusListItems[i]);
                        imgMarried.setImageResource(R.drawable.ic_check_circle_black_24dp);

                        switch (i){
                            case 0:
                                strMaritalStatus="single";
                                break;
                            case 1:
                                strMaritalStatus="married";
                                break;
                            case 2:
                                strMaritalStatus="other";
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Province();
        getYears();
        getUserType();
        initialUserInformation(url,Encode);

        mp_Pob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle(getString(R.string.choose_province));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(provinceItemkh, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mp_Pob.setText(provinceItemkh[i]);
                            imgPob.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            id_pob=provinceIdListItems[i];
                            dialogInterface.dismiss();
                        }
                    });
                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(provinceListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mp_Pob.setText(provinceListItems[i]);
                            imgPob.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            id_pob=provinceIdListItems[i];
                            dialogInterface.dismiss();
                            Log.d("PLACE OF BIRTH", String.valueOf(id_pob));
                        }
                    });
                }
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mp_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle(getString(R.string.choose_location));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(provinceItemkh, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mp_location.setText(provinceItemkh[i]);
                            imgLocation.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            id_location=provinceIdListItems[i];
                            dialogInterface.dismiss();
                        }
                    });
                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(provinceListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mp_location.setText(provinceListItems[i]);
                            imgLocation.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            id_location=provinceIdListItems[i];
                            dialogInterface.dismiss();
                            Log.d("LOCATION", String.valueOf(id_location));
                        }
                    });
                }

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        //Close option usertype by Raksmey 11/09/2019
//        usertpyeItem=getResources().getStringArray(R.array.usertype);
//        tvType.setOnClickListener(v -> {
//            AlertDialog.Builder mbuilder = new AlertDialog.Builder(Edit_account.this);
//            mbuilder.setTitle(getString(R.string.user_type));
//            if (language.equals("km")){
//                mbuilder.setSingleChoiceItems(usertpyeItem, -1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        tvType.setText(usertpyeItem[i]);
//                        id_type = type_userid[i];
//                        if (id_type == 1){
//                            imgShopName.setVisibility(View.GONE);
//                            tilShop_name.setVisibility(View.GONE);
//                        }else {
//                            imgShopName.setVisibility(View.VISIBLE);
//                            tilShop_name.setVisibility(View.VISIBLE);
//                        }
//
//                        dialog.dismiss();
//                    }
//                });
//            }else if (language.equals("en")){
//                mbuilder.setSingleChoiceItems(type_userListItem, -1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        tvType.setText(type_userListItem[i]);
//                        id_type = type_userid[i];
//                        if (id_type == 1){
//                            imgShopName.setVisibility(View.GONE);
//                            tilShop_name.setVisibility(View.GONE);
//                        }else {
//                            imgShopName.setVisibility(View.VISIBLE);
//                            tilShop_name.setVisibility(View.VISIBLE);
//                        }
//
//                        dialog.dismiss();
//                    }
//                });
//            }
//            AlertDialog mDialog = mbuilder.create();
//            mDialog.show();
//
//        });
        mp_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
//                mBuilder.setTitle(getString(R.string.choose_year));
//                mBuilder.setSingleChoiceItems(yearListItems, -1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        strDob=yearListItems[i];
//                        mp_Dob.setText(yearListItems[i]);
//                        imgDob.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                        //id_location=provinceIdListItems[i];
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                AlertDialog mDialog = mBuilder.create();
//                mDialog.show();
                Calendar calendar = Calendar.getInstance();
                calendar.set(2000,01,01);
                YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(Edit_account.this, calendar, new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, month);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

                        mp_Dob.setText(dateFormat.format(calendar.getTime()));
                        strDob = mp_Dob.getText().toString();

                    }
                });
                yearMonthPickerDialog.setMinYear(1960);
                yearMonthPickerDialog.setMaxYear(2020);
                yearMonthPickerDialog.show();
            }
        });

        tvAddress_account.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Seach_Address();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    imgAddress.setImageResource(R.drawable.icon_null);
                }else {
                    imgAddress.setImageResource(R.drawable.ic_check_circle_black_24dp);
                }
                return false;
            }
        });
        addPhone_number();
        Text_Action();
        Button btSubmit=(Button) findViewById(R.id.btn_EditAccount);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //summitUserInformation();
                if(tvType.getText().toString().length()<3){
                    tvType.requestFocus();
                    imgType.setImageResource(R.drawable.ic_error_black_24dp);
                }else if (etShop_name.getText().toString().length()<3){
                    etShop_name.requestFocus();
                    imgShopName.setImageResource(R.drawable.ic_error_black_24dp);
                } else if (etUsername.getText().toString().length()<3){
                    etUsername.requestFocus();
                    imgUsername.setImageResource(R.drawable.ic_error_black_24dp);
                }else {
                    mProgress.show();
                    PutData(url, Encode);
                }
            }
        });
        //add new button Upgrade by Raksmey
        btUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<type_userid.length;i++){
                    id_type = type_userid[i];
                    if(id_type == 1){
                        imgShopName.setVisibility(View.VISIBLE);
                        tilShop_name.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    } // oncreate



    private void showDatePickerDialog(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerFragmentDialog datePickerFragmentDialog=DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
              //  tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            }
        },mYear, mMonth, mDay);
        datePickerFragmentDialog.show(getSupportFragmentManager(),null);
        datePickerFragmentDialog.setMaxDate(System.currentTimeMillis());
        datePickerFragmentDialog.setYearRange(1900,mYear);
        datePickerFragmentDialog.setCancelColor(getResources().getColor(R.color.colorPrimaryDark));
        datePickerFragmentDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
        datePickerFragmentDialog.setAccentColor(getResources().getColor(R.color.colorAccent));
        datePickerFragmentDialog.setOkText(getResources().getString(R.string.ok_dob));
        datePickerFragmentDialog.setCancelText(getResources().getString(R.string.cancel_dob));
    }

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

    private void initialUserInformation(String url,String encode){
        MediaType MEDIA_TYPE     =  MediaType.parse("application/json");
        Log.d(TAG,"tt"+url);
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

                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            User convertJsonJava = new User();

                            convertJsonJava = gson.fromJson(mMessage,User.class);
                            int g=convertJsonJava.getProfile().getGroup();

                            id_type = g;
                            Log.d(TAG,"GROUP "+g);
                            if (g==2){
                                tvType.setText(getString(R.string.shoppe));
                            }else if (g==3){
                                tvType.setText(getString(R.string.other_dealer));
                                btUpgrade.setVisibility(View.GONE);
                                imgShopName.setVisibility(View.VISIBLE);
                                tilShop_name.setVisibility(View.VISIBLE);
                                etShop_name.setText(convertJsonJava.getProfile().getShop_name());
                            }else {
                                tvType.setText(getString(R.string.public_user));
                                btUpgrade.setVisibility(View.VISIBLE);
                                imgShopName.setVisibility(View.GONE);
                                tilShop_name.setVisibility(View.GONE);
                            }

                            etUsername.setText(convertJsonJava.getFirst_name());
                            etEmail.setText(convertJsonJava.getEmail());

                            if(convertJsonJava.getProfile()!=null) {


                                if(convertJsonJava.getProfile().getTelephone()!=null){
                                    String phone = convertJsonJava.getProfile().getTelephone();
                                    String[] splitPhone = phone.split(",");

                                    Log.d("SPLIT:::", String.valueOf(splitPhone.length));
                                    if (splitPhone.length == 1){
                                        etPhone.setText(String.valueOf(splitPhone[0]));
                                        btnImagePhone1.setVisibility(View.VISIBLE);

                                    }else if(splitPhone.length == 2){
                                        imgPhone2.setVisibility(View.VISIBLE);
                                        tilPhone2.setVisibility(View.VISIBLE);
                                        btnImagePhone2.setVisibility(View.VISIBLE);
                                        btnImagePhone1.setVisibility(View.GONE);

                                        etPhone.setText(String.valueOf(splitPhone[0]));
                                        etPhone2.setText(String.valueOf(splitPhone[1]));
                                    }else if (splitPhone.length == 3){
                                        imgPhone2.setVisibility(View.VISIBLE);
                                        tilPhone2.setVisibility(View.VISIBLE);
                                        imgPhone3.setVisibility(View.VISIBLE);
                                        tilPhone3.setVisibility(View.VISIBLE);
                                        btnImagePhone2.setVisibility(View.GONE);
                                        btnImagePhone1.setVisibility(View.GONE);

                                        etPhone.setText(String.valueOf(splitPhone[0]));
                                        etPhone2.setText(String.valueOf(splitPhone[1]));
                                        etPhone3.setText(String.valueOf(splitPhone[2]));
                                    }

                                }
                                if(convertJsonJava.getProfile().getWing_account_number()!=null){
                                    etWingNumber.setText(convertJsonJava.getProfile().getWing_account_number());
                                }
                                if(convertJsonJava.getProfile().getWing_account_name()!=null){
                                    etWingName.setText(convertJsonJava.getProfile().getWing_account_name());
                                }
                                String s = convertJsonJava.getProfile().getGender();
                                Log.d(TAG,"Gender:"+s);
                                if (s!=null ){
                                    if (s.equals("male")){
                                        mp_Gender.setText(R.string.male);
                                    }else if (s.equals("female")){
                                        mp_Gender.setText(R.string.female);
                                    }else {
                                        Log.d(TAG,s);
                                    }
                                }

                                if(convertJsonJava.getProfile().getDate_of_birth() !=null) {
                                    String d = convertJsonJava.getProfile().getDate_of_birth();
                                    String dd[] = d.split("-");
                                    strDob = dd[0];
                                    mp_Dob.setText(strDob);

                                    List<String> date = new ArrayList<>();
                                    date.add(0, d);
                                }

                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map_Account);

                                 String addr = convertJsonJava.getProfile().getAddress();
                                 if (addr.isEmpty()) {
                                     get_location(true);
                                     mapFragment.getMapAsync(Edit_account.this::onMapReady);
                                 }else {
                                     String[] splitAddr = addr.split(",");
                                     Log.d("Split Location::", String.valueOf(splitAddr.length));
                                     latitude = Double.valueOf(splitAddr[0]);
                                     longtitude = Double.valueOf(splitAddr[1]);

                                     get_location(false);
                                     if (convertJsonJava.getProfile().getResponsible_officer()!=null){
                                         String search_title = convertJsonJava.getProfile().getResponsible_officer().toString();
                                         tvAddress_account.setQuery(search_title,false);
                                     }
                                     mapFragment.getMapAsync(Edit_account.this);
                                 }
                                String m = convertJsonJava.getProfile().getMarital_status();
                                 if (m!=null){
                                     if (m.equals("single")){
                                         mp_Married.setText(R.string.single);
                                     }else if (m.equals("married")){
                                         mp_Married.setText(R.string.marriedd);
                                     }else if (m.equals("other")){
                                         mp_Married.setText(R.string.other);
                                     }
                                 }


                                if(convertJsonJava.getProfile().getPlace_of_birth()!=null) {
                                    int p = Integer.parseInt(convertJsonJava.getProfile().getPlace_of_birth());
                                    getProvinceName(p,true);
                                }
                                if(convertJsonJava.getProfile().getProvince()!=null) {
                                    int l = Integer.parseInt(convertJsonJava.getProfile().getProvince());
                                    //Log.d(TAG,"Location Id "+l);
                                    getProvinceName(l,false);
                                }
                            }
                            //}
                        }
                    });
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void InitialProvinceDatas(){
        String API_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/provinces/";
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, API_ENDPOINT, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            provinceListItems=new String[jsonArray.length()];
                            provinceIdListItems=new int[jsonArray.length()];
                            for (int i=0;i< jsonArray.length(); i++){
                                JSONObject  field_province = jsonArray.getJSONObject(i);
                                //Log.d(TAG,"Result "+field_province);
                                int id = field_province.getInt("id");
                                String province = field_province.getString("province");
                                provinceListItems[i]=province;
                                provinceIdListItems[i]=id;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void PutData(String url,String encode) {
        MediaType media = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        JSONObject data = new JSONObject();
        JSONObject pro  = new JSONObject();
        try{
            data.put("username",name);
            data.put("password",pass);
            data.put("email",etEmail.getText().toString());
            data.put("first_name",etUsername.getText().toString());

            //pro.put("gender",gender);
            if(!strDob.isEmpty() && strDob!=null)
                if (Build.VERSION.SDK_INT >= 26) {
                    pro.put("date_of_birth", convertDateofBirth(strDob));
                }

            pro.put("data_of_birth", strDob);
            pro.put("address",latlng);
            pro.put("shop_name",etShop_name.getText().toString());
            pro.put("responsible_officer",tvAddress_account.getQuery().toString());
            pro.put("job","");

            pro.put("gender",strGender);
            pro.put("marital_status",strMaritalStatus);
            pro.put("shop_address","");
            pro.put("telephone",etPhone.getText().toString()+","+etPhone2.getText().toString()+","+etPhone3.getText().toString());
            pro.put("wing_account_number",etWingNumber.getText().toString());
            pro.put("wing_account_name",etWingName.getText().toString());
            if(id_location>0)
                pro.put("province",id_location);
            if(id_pob>0)
                pro.put("place_of_birth",id_pob);
            pro.put("user_status",1);
            pro.put("record_status",1);
            pro.put("group",id_type);
   //         pro.put("modified", Instant.now().toString());
            data.put("profile",pro);
            data.put("groups", new JSONArray("[\"1\"]"));
            //data.put("groups",new JSONArray("["+id_type+"]"));
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
                        AlertDialog alertDialog = new AlertDialog.Builder(Edit_account.this).create();
                        alertDialog.setTitle(getString(R.string.title_edit_account));
                        alertDialog.setMessage(getString(R.string.edit_fail_message));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        mProgress.dismiss();
                        //Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
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
                        Log.d("Response EEEEE", message);
                        AlertDialog alertDialog = new AlertDialog.Builder(Edit_account.this).create();
                        alertDialog.setTitle(getString(R.string.title_edit_account));
                        alertDialog.setMessage(getString(R.string.edit_success_message));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
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
                //finish();
            }
        });
    }

    public void Province(){
        final String rl = ConsumeAPI.BASE_URL+"api/v1/provinces/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(rl)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String province = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(province);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    provinceItemkh=new String[jsonArray.length()];
                    provinceListItems=new String[jsonArray.length()];
                    provinceIdListItems=new int[jsonArray.length()];

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String pro = object.getString("province");
                        String prokh=object.getString("province_kh");
                        provinceItemkh[i]=prokh;
                        provinceListItems[i]=pro;
                        provinceIdListItems[i]=id;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //mp_location.setAdapter(ad_name);
                                //mp_Pob.setAdapter(ad_name);
                            }
                        });
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }

        });
    }

    public void getProvinceName(int id,boolean isPob){
        final String rl = ConsumeAPI.BASE_URL+"api/v1/provinces/"+id+"/";
        String auth="Basic "+ Encode;
        //Log.d("URLLLLLLLL", rl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(rl)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String province = response.body().string();
                Log.d(TAG,"Province "+province);
                try{
                    SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                    String language = preferences.getString("My_Lang", "");
                    JSONObject jsonObject = new JSONObject(province);
                    String pro=jsonObject.getString("province");
                    String prokh=jsonObject.getString("province_kh");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                if (language.equals("km")){
                                    if(isPob)
                                        mp_Pob.setText(prokh);
                                    else
                                        mp_location.setText(prokh);
                                }else if (language.equals("en")){
                                    if(isPob)
                                        mp_Pob.setText(pro);
                                    else
                                        mp_location.setText(pro);
                                }
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(Call call, IOException e) {

            }

        });
    }

    public void getYears(){
        final String rl = ConsumeAPI.BASE_URL+"api/v1/years/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(rl)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String province = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(province);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    yearListItems=new String[jsonArray.length()];
                    yearIdListItems=new int[jsonArray.length()];

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String year = object.getString("year");
                        yearListItems[i]=year;
                        yearIdListItems[i]=id;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //mp_location.setAdapter(ad_name);
                                //mp_Pob.setAdapter(ad_name);
                            }
                        });
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }

        });

    }

    public void getUserType(){
        final String rl = ConsumeAPI.BASE_URL+"api/v1/groups/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(rl)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userType = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(userType);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    type_userListItem=new String[jsonArray.length()-1];
                    type_userid=new int[jsonArray.length()-1];
                    int c=0;
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String group = object.getString("name");
                        if(id!=2){
                            type_userListItem[c]=group;
                            type_userid[c]=id;
                            c++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mp_location.setAdapter(ad_name);
                                    //mp_Pob.setAdapter(ad_name);
                                }
                            });
                        }

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });

    }

    private String convertDateofBirth(String year){
        String dd= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dd = Instant.now().toString();
        }
        //String dd = DateFormat.getDateTimeInstance().format(new Date());
        String d[]=dd.split("-");
        for(int i=0;i<d.length;i++){
            Log.e(TAG,d[i]);
        }
        String dob=String.format("%s-%s-%s",year,d[1],d[2]);
        Log.d(TAG,dob);
        return dob;
    }

    private void addPhone_number() {
        btnImagePhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPhone2.setVisibility(View.VISIBLE);
                tilPhone2.setVisibility(View.VISIBLE);
                btnImagePhone2.setVisibility(View.VISIBLE);
                btnImagePhone1.setVisibility(View.GONE);
            }
        });

        btnImagePhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPhone3.setVisibility(View.VISIBLE);
                tilPhone3.setVisibility(View.VISIBLE);
                btnImagePhone2.setVisibility(View.GONE);
            }
        });
    }

    private void Text_Action() {
        tvType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgType.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgType.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgType.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgUsername.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgUsername.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgUsername.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_user.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_user.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etShop_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgShopName.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgShopName.setImageResource(R.drawable.ic_error_black_24dp);
                }else {
                    imgShopName.setImageResource(R.drawable.ic_check_circle_black_24dp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etWingName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgWingName.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgWingName.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgWingName.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_wingname.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_wingname.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etWingNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgWingNumber.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgWingNumber.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgWingNumber.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_wingnum.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_wingnum.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgPhone.setImageResource(R.drawable.icon_null);
                }else if (s.length()<8){
                    imgPhone.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgPhone.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPhone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgPhone2.setImageResource(R.drawable.icon_null);
                }else if (s.length()<8){
                    imgPhone2.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgPhone2.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPhone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgPhone3.setImageResource(R.drawable.icon_null);
                }else if (s.length()<8){
                    imgPhone3.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgPhone3.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgEmail.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgEmail.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgEmail.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mp_Dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgDob.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgDob.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgDob.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mp_Pob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgPob.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgPob.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgPob.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mp_Married.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgMarried.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgMarried.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgMarried.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mp_Gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgGender.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgGender.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgGender.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mp_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    imgLocation.setImageResource(R.drawable.icon_null);
                }else if (s.length()<3){
                    imgLocation.setImageResource(R.drawable.ic_error_black_24dp);
                }else imgLocation.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        tvAddress_account.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length()==0){
//                    imgAddress.setImageResource(R.drawable.icon_null);
//                }else if (s.length()<3){
//                    imgAddress.setImageResource(R.drawable.ic_error_black_24dp);
//                }else imgAddress.setImageResource(R.drawable.ic_check_circle_black_24dp);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    } // text change

    private void Seach_Address(){
        String loca = tvAddress_account.getQuery().toString();
        List<Address> address_Search = null;
        if (loca!=null || !loca.equals("")){
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try{
                address_Search = geocoder.getFromLocationName(loca,1);
            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                LatLng ll = new LatLng(address_Search.get(0).getLatitude(),address_Search.get(0).getLongitude());
                mMap.addMarker(new MarkerOptions().position(ll));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(ll));
                List<Address> address_S = address_Search;
                latitude = address_S.get(0).getLatitude();
                longtitude = address_S.get(0).getLongitude();
                latlng = latitude+","+longtitude;
                Log.d("LATITUDE and LONGTITUDE",latlng);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void get_location(boolean isCurrent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            buildAlertMessageNoGps();
//        }else
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocation(isCurrent);
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
//                    String country = addressList.get(0).getCountryName();
//                    String city    = addressList.get(0).getLocality();
                    String road = addressList.get(0).getAddressLine(0);

                    Log.d("Name road",road);
                    tvAddress_account.setQuery( road , false );
                }catch (IOException e){
                    e.printStackTrace();
                }


            }else {
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }  //

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
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_map_pin_48px_3));

        mMap = googleMap;
        LatLng current_location = new LatLng(latitude, longtitude);
  //      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(old,10));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5),2000,null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current_location)
                .zoom(10)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longtitude)));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_map_pin_48px_3));
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                latitude = latLng.latitude;
                longtitude = latLng.longitude;

                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location!=null) {
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        List<Address> addressList = null;
                        addressList = geocoder.getFromLocation(latitude, longtitude, 1);
                        String country = addressList.get(0).getCountryName();
                        String city = addressList.get(0).getLocality();
                        String road = addressList.get(0).getAddressLine(0);

                        tvAddress_account.setQuery(road,false);
                        latlng = latitude+","+longtitude;
                        Log.d("LATITUDE",latitude+","+longtitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }
}
