package com.bt_121shoppe.lucky_app.useraccount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bt_121shoppe.lucky_app.Activity.Account;
import com.bt_121shoppe.lucky_app.Activity.Camera;
import com.bt_121shoppe.lucky_app.Activity.Home;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Api.User;
import com.bt_121shoppe.lucky_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.tiper.MaterialSpinner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
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
    private TextView tvType, tvType_121,tvAddress_account;
    private EditText etUsername,etShop_name,etWingNumber,etWingName,etPhone;
    private TextInputLayout tilusername,tildob,tiljob,tilwingnumber,tilwingname,tilphone,tilShopName,tilshopAddr,tilresponsible;
    private ImageView imgType,imgGender,imgPob,imgLocation,imgAddress,imgMarried,imgtilUsername,imgtilDob,imgtilWingNumber,
            imgtilWingName,imgtilPhone,imgtilShopName,imgtilShopAddr,imgtilResponsible;
    private Button btnsubmit,mp_Gender,mp_Married,mp_Dob,mp_Pob,mp_location;
    private String name,pass,Encode,user_id;
    private ArrayAdapter<Integer> ad_id;
    private int pk, id_pob=0,id_location=0;
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
    private String[] genderListItems,maritalStatusListItems,yearListItems,provinceListItems;
    private int[] provinceIdListItems,yearIdListItems;
    private String strGender,strMaritalStatus,strDob,strYob,strPob,strLocation;

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
        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",pk);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode =getEncodedString(name,pass);
        Log.e(TAG,name+" "+pass+" "+pk+" "+Encode+" "+url);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Updating...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        tvType = (TextView)findViewById(R.id.tvType);
        etUsername  =(EditText) findViewById(R.id.etUsername);
        etWingName  =(EditText) findViewById(R.id.etWingName);
        etWingNumber=(EditText) findViewById(R.id.etWingNumber);
        etPhone     =(EditText) findViewById(R.id.etPhone_account);
        mp_Dob      = (Button) findViewById(R.id.mp_Dob);
        mp_Pob      = (Button) findViewById(R.id.mp_Pob);
        mp_Married  = (Button) findViewById(R.id.mp_Married);
        mp_Gender   = (Button) findViewById(R.id.mp_Gender);
        mp_location = (Button) findViewById(R.id.mp_Location);
        tvAddress_account = (TextView) findViewById(R.id.tvAccount_Address);

        imgGender   =(ImageView) findViewById(R.id.imgGender);
        imgMarried  =(ImageView) findViewById(R.id.imgMarried);
        imgtilDob   =(ImageView) findViewById(R.id.imgDob);
        imgPob      =(ImageView) findViewById(R.id.imgPob);
        imgLocation =(ImageView) findViewById(R.id.imgLocation);
        imgAddress  = (ImageView) findViewById(R.id.imgAccount_Address);

        genderListItems=getResources().getStringArray(R.array.genders_array);
        mp_Gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle("Choose Gender");
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
                            case 2:
                                strGender="other";
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
                mBuilder.setTitle("Choose Marital Status");
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
                                strMaritalStatus="separated";
                                break;
                            case 3:
                                strMaritalStatus="divorced";
                                break;
                            case 4:
                                strMaritalStatus="windowed";
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
        initialUserInformation(url,Encode);

        mp_Pob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle("Choose Province");
                mBuilder.setSingleChoiceItems(provinceListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mp_Pob.setText(provinceListItems[i]);
                        imgPob.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        id_pob=provinceIdListItems[i];
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mp_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle("Choose Location");
                mBuilder.setSingleChoiceItems(provinceListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mp_location.setText(provinceListItems[i]);
                        imgLocation.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        id_location=provinceIdListItems[i];
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mp_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Edit_account.this);
                mBuilder.setTitle("Choose Year");
                mBuilder.setSingleChoiceItems(yearListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mp_Dob.setText(yearListItems[i]);
                        imgtilDob.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        //id_location=provinceIdListItems[i];
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        Button btSubmit=(Button) findViewById(R.id.btn_EditAccount);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //summitUserInformation();
                mProgress.show();
                PutData(url,Encode);
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
                            int[] gg = convertJsonJava.getGroups();
                            final int g=gg[0];

                            if (g==2){
                                tvType.setText("121 Shoppe");
                            }else if (g==3){
                                tvType.setText("Other Dealer");
                            }else {
                                tvType.setText("Public User");
                            }

                            etUsername.setText(convertJsonJava.getUsername());

                            if(convertJsonJava.getProfile()!=null) {
                                etPhone.setText(convertJsonJava.getProfile().getTelephone());
                                etWingNumber.setText(convertJsonJava.getProfile().getWing_account_number());
                                etWingName.setText(convertJsonJava.getProfile().getWing_account_name());
                                String s = convertJsonJava.getProfile().getGender();
                                mp_Gender.setText(s);
                                String d = convertJsonJava.getProfile().getDate_of_birth();
                                List<String> date = new ArrayList<>();
                                date.add(0,d);


                                 String addr = convertJsonJava.getProfile().getAddress();
                                 if (addr.isEmpty()) {
                                     get_location(true);
                                 }else {
                                 String[] splitAddr = addr.split(",");
                                 latitude = Double.valueOf(splitAddr[0]);
                                 longtitude = Double.valueOf(splitAddr[1]);
                                 get_location(false);
                                 //get_location(false);
                                 SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                         .findFragmentById(R.id.map_Account);
                                 mapFragment.getMapAsync(Edit_account.this::onMapReady);

                             }

                                String m = convertJsonJava.getProfile().getMarital_status();
                                mp_Married.setText(m);

                                if(convertJsonJava.getProfile().getPlace_of_birth()!=null) {
                                    int p = Integer.parseInt(convertJsonJava.getProfile().getPlace_of_birth());
                                    Log.d(TAG,"province Id "+p);
                                    getProvinceName(p,true);
                                }
                                if(convertJsonJava.getProfile().getProvince()!=null) {
                                    int l = Integer.parseInt(convertJsonJava.getProfile().getProvince());
                                    Log.d(TAG,"Location Id "+l);
                                    getProvinceName(l,false);
                                }
                            }
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
            data.put("first_name",etUsername.getText().toString());
            //pro.put("gender",gender);
            pro.put("data_of_birth", strDob);
            pro.put("address",latlng);
            pro.put("shop_name","");
            pro.put("responsible_officer","");
            pro.put("job","");

            pro.put("gender",strGender);
            pro.put("marital_status",strMaritalStatus);
            pro.put("shop_address","");
            pro.put("telephone",etPhone.getText().toString().toLowerCase());
            pro.put("wing_account_number",etWingNumber.getText().toString());
            pro.put("wing_account_name",etWingName.getText().toString());
            if(id_location>0)
                pro.put("province",id_location);
            if(id_pob>0)
                pro.put("place_of_birth",id_pob);
            pro.put("user_status",1);
            pro.put("record_status",1);
   //         pro.put("modified", Instant.now().toString());
            data.put("profile",pro);
            data.put("groups", new JSONArray("[\"1\"]"));
            //data.put("groups",new JSONArray("["+id_type+"]"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        //Log.d(TAG,data.toString());
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
                        alertDialog.setTitle("Edit Account");
                        alertDialog.setMessage("Account information has been error while submiting.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.dismiss();
                        Log.d("Response", message);
                        AlertDialog alertDialog = new AlertDialog.Builder(Edit_account.this).create();
                        alertDialog.setTitle("Edit Account");
                        alertDialog.setMessage("Account information has been successfully submitted.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
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
                    provinceListItems=new String[jsonArray.length()];
                    provinceIdListItems=new int[jsonArray.length()];

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String pro = object.getString("province");
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
                    String pro=jsonObject.getString("province");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isPob)
                                mp_Pob.setText(pro);
                            else
                                mp_location.setText(pro);
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


    private void get_location(boolean isCurrent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
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
                latlng = latitude+","+longtitude;

                try{
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addressList = null;
                    addressList = geocoder.getFromLocation(latitude,longtitude,1);
//                    String country = addressList.get(0).getCountryName();
//                    String city    = addressList.get(0).getLocality();
                    String road = addressList.get(0).getAddressLine(0);

                    tvAddress_account.setText( road );
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
        builder.setMessage("Please Turn On your PGS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
        mMap = googleMap;
        LatLng current_location = new LatLng(latitude, longtitude);
  //      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(old,10));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5),2000,null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current_location)
                .zoom(18)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longtitude)));
    }
}
