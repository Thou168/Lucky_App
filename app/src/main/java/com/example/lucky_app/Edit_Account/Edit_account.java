package com.example.lucky_app.Edit_Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucky_app.Api.ConsumeAPI;
import com.example.lucky_app.Api.Convert_Json_Java;
import com.example.lucky_app.Api.User;
import com.example.lucky_app.R;
import com.example.lucky_app.utils.CalendarConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.tiper.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Edit_account extends AppCompatActivity implements OnMapReadyCallback {

    String[] country = {"India", "USA", "China", "Japan", "Other"};
    private static final String TAG = "Response";
    private LinearLayout layout_public_user, layout_121_dealer;
    private TextView tvType_121, tvBack;
    private EditText etShopName, etShopAddr, etResponsible, etWingNumber_121, etWingName_121, etPhone_121;
    //private TextView tvType,genderSpinner,pobSpinner,locationSpinner,tvAddress,maritalStatusSpinner;
    private EditText etUsername, etDob, etJob, etWingNumber, etWingName, etPhone;
    private TextInputLayout tilusername, tildob, tiljob, tilwingnumber, tilwingname, tilphone, tilShopName, tilshopAddr, tilresponsible;
    private ImageView imgType, imgGender, imgPob, imgLocation, imgAddress, imgMarried, imgtilUsername, imgtilDob, imgtilWingNumber,
            imgtilWingName, imgtilPhone, imgtilShopName, imgtilShopAddr, imgtilResponsible;
    private Button btnsubmit;
    private String name, pass, Encode, user_id;
    private int pk, id, g;
    private SharedPreferences prefer;
    private MaterialSpinner spinner;
    private Spinner genderSpinner, pobSpinner, locationSpinner, maritalStatusSpinner, groupsSpinner;

    private List<Integer> provinceIdArrayList = new ArrayList<>();
    private List<String> provinceNameArrayList = new ArrayList<>();
    private RequestQueue mQueue;

    private Spinner gender, place, status, location;
    private EditText date;
    int mMonth, mYear, mDay;

//    private static final String TAG = Edit_account.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;


    private static final String[] ANDROID_VERSIONS = {
            "Cupcake",
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow",
            "Nougat",
            "Oreo"
    };

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        TextView back = (TextView) findViewById(R.id.tv_Back);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_Account);
        mapFragment.getMapAsync(this);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        layout_public_user = (LinearLayout)findViewById(R.id.layout_type_public_user);
//        layout_121_dealer  = (LinearLayout)findViewById(R.id.layout_type_121_dealer);
//
//        prefer = getSharedPreferences("Register",MODE_PRIVATE);
//        if (prefer.contains("token")) {
//            pk = prefer.getInt("Pk",0);
//            user_id = String.valueOf(pk);
//            Log.d(TAG, user_id);
//        }else if (prefer.contains("id")) {
//            id = prefer.getInt("id", 0);
//            user_id = String.valueOf(id);
//            Log.d(TAG, user_id);
//        }
//        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",user_id);
//
//        name = prefer.getString("name","");
//        pass = prefer.getString("pass","");
//        Encode = getEncodedString(name,pass);


//        ID_Field();
        //  Groups(url,Encode);
//        DropList();
//Dropdown
        gender = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        place = (Spinner) findViewById(R.id.place);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.place, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place.setAdapter(adapter1);
        status = (Spinner) findViewById(R.id.status);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter2);
        location = (Spinner) findViewById(R.id.location);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter3);
//Date
        date = (EditText) findViewById(R.id.birth);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    } //create

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerFragmentDialog datePickerFragmentDialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            }
        }, mYear, mMonth, mDay);
        datePickerFragmentDialog.show(getSupportFragmentManager(), null);
        datePickerFragmentDialog.setMaxDate(System.currentTimeMillis());
        datePickerFragmentDialog.setYearRange(1900, mYear);
        datePickerFragmentDialog.setCancelColor(getResources().getColor(R.color.colorPrimaryDark));
        datePickerFragmentDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
        datePickerFragmentDialog.setAccentColor(getResources().getColor(R.color.colorAccent));
        datePickerFragmentDialog.setOkText(getResources().getString(R.string.ok_dob));
        datePickerFragmentDialog.setCancelText(getResources().getString(R.string.cancel_dob));
    }

//    private void DropList() {
//        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,ANDROID_VERSIONS);
//        spinner.setAdapter(aa);
//
//    }

    private String getEncodedString(String username, String password) {
        final String userpass = username + ":" + password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

    private void Groups(String url, String encode) {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        Log.d(TAG, "tt" + url);

        OkHttpClient client = new OkHttpClient();

        String auth = "Basic " + encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", auth)
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
                Log.e(TAG, mMessage);
                Gson gson = new Gson();
                User convertJsonJava = new User();
                try {
                    convertJsonJava = gson.fromJson(mMessage, User.class);
                    int[] gg = convertJsonJava.getGroups();
                    g = gg[0];
                    Log.d(TAG, "initialUserInformation:" + g);
                    if (g == 1) {
                        //groupsSpinner.setSelection(0);

                    } else if (g == 2) {
                        //tvType.setText("121 Dealer");

                    } else if (g == 3) {
                        //tvType.setText("Dealer");

                    }
//                    etUsername.setText(convertJsonJava.getUsername());
//                    tvGender.setText(convertJsonJava.getGender());
//                    etDob   .setText(convertJsonJava.getData_of_birth());
//                    tvPob.setText(convertJsonJava.getPlace_of_birth());
//                    tvMarried.setText(convertJsonJava.getMarital_status());
//                    etWingNumber.setText(convertJsonJava.getWing_account_number());
//                    etWingName.setText(convertJsonJava.getWing_account_name());
//                    etJob.setText(convertJsonJava.getJob());
//
//                    etShopAddr.setText(convertJsonJava.getShop_address());
//                    etShopName.setText(convertJsonJava.getShop_name());
//                    etResponsible.setText(convertJsonJava.getResponsible_officer());
//                    etWingNumber_121.setText(convertJsonJava.getWing_account_number());
//                    etWingName_121.setText(convertJsonJava.getWing_account_name());
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
//    private void ID_Field() {
//        // public user textview
//        spinner     =(MaterialSpinner) layout_public_user.findViewById(R.id.tvType);
//        tvGender   = (TextView)layout_public_user.findViewById(R.id.tvGender);
//        tvPob      =(TextView) layout_public_user.findViewById(R.id.tvPob);
//        tvLocation = (TextView)layout_public_user.findViewById(R.id.tvLocation);
//        tvMarried  = (TextView)layout_public_user.findViewById(R.id.tvMarried);
////public user edittext
//        etUsername  = (EditText)layout_public_user.findViewById(R.id.etUsername);
//        etDob       = (EditText)layout_public_user.findViewById(R.id.ed_date_of_birth );
//        etJob       = (EditText)layout_public_user.findViewById(R.id.edJob );
//        etWingNumber= (EditText)layout_public_user.findViewById(R.id.etWingNumber );
//        etWingName  = (EditText)layout_public_user.findViewById(R.id.etWingName );
//        etPhone     = (EditText)layout_public_user.findViewById(R.id.etAccount_Phone );
//        etShopName  = (EditText)layout_public_user.findViewById(R.id.etShop_Name);
//        etShopAddr  = (EditText)layout_public_user.findViewById(R.id. etShop_Addr);
//        etResponsible= (EditText)layout_public_user.findViewById(R.id.etResponsible );
//
//        // 121 dealer TextView
//        tvType_121 = (TextView) layout_121_dealer.findViewById(R.id.tvType);
//        // 121 dealer EditText
//        etShopName       =(EditText)layout_121_dealer.findViewById(R.id.etShop_Name);
//        etShopAddr       =(EditText)layout_121_dealer.findViewById(R.id.etShop_Addr );
//        etWingName_121   =(EditText)layout_121_dealer.findViewById(R.id.etWingName );
//        etWingNumber_121 =(EditText)layout_121_dealer.findViewById(R.id.etWingNumber );
//        etResponsible    =(EditText)layout_121_dealer.findViewById(R.id.etResponsible );
//        etPhone_121      =(EditText)layout_121_dealer.findViewById(R.id.etAccount_Phone );
//        btnsubmit = (Button)findViewById(R.id.btn_EditAccount);
//        tvBack = (TextView)findViewById(R.id.tvBack_account);
//        tvBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//// textinputlayout
////      tilusername= (TextInputLayout)findViewById(R.id.tilUsername );
////      tildob     = (TextInputLayout)findViewById(R.id.tilDob );
////      tiljob     = (TextInputLayout)findViewById(R.id.tilJob );
////      tilwingnumber= (TextInputLayout)findViewById(R.id.tilWingNumber );
////      tilwingname= (TextInputLayout)findViewById(R.id.tilWingName );
////      tilphone   = (TextInputLayout)findViewById(R.id.tilAccount_phone );
////      tilShopName= (TextInputLayout)findViewById(R.id.tilShop_Name);
////      tilshopAddr= (TextInputLayout)findViewById(R.id.tilShop_Addr );
////      tilresponsible= (TextInputLayout)findViewById(R.id.tilResponsible );
//
////        imgType            = (ImageView)findViewById(R.id.imgType );
////        imgGender          = (ImageView)findViewById(R.id.imgGender );
////        imgPob             = (ImageView)findViewById(R.id.imgPob );
////        imgLocation        = (ImageView)findViewById(R.id.imgLocation );
////        imgAddress         = (ImageView)findViewById(R.id.imgAccount_Address );
////        imgMarried         = (ImageView)findViewById(R.id.imgMarried );
////        imgtilUsername     = (ImageView)findViewById(R.id.imgUsername);
////        imgtilDob          = (ImageView)findViewById(R.id.imgDob );
////        imgtilWingNumber   = (ImageView)findViewById(R.id.imgWingNumber );
////        imgtilWingName     = (ImageView)findViewById(R.id.imgWingName );
////        imgtilPhone        = (ImageView)findViewById(R.id.imgAccount_Phone );
////        imgtilShopName     = (ImageView)findViewById(R.id.imgShop_name);
////        imgtilShopAddr     = (ImageView)findViewById(R.id.imgShop_Addr);
////        imgtilResponsible  = (ImageView)findViewById(R.id.imgResponsible );
//    }

    private void InitialProvinceDatas() {
        String API_ENDPOINT = ConsumeAPI.BASE_URL + "api/v1/provinces/";
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, API_ENDPOINT, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject field_province = jsonArray.getJSONObject(i);
                                Log.d(TAG, "Result " + field_province);

                                int id = field_province.getInt("id");
                                String province = field_province.getString("province");
                                provinceNameArrayList.add(province);
                                provinceIdArrayList.add(id);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        getLocationPermission();

        updateLocationUI();

        getDeviceLocation();
    }
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(Edit_account.this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
