package com.example.lucky_app.Edit_Account;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tiper.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Edit_account extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] country = { "India", "USA", "China", "Japan", "Other"};
    private static final String TAG = "Response";
    private LinearLayout layout_public_user,layout_121_dealer;
    private TextView tvType_121,tvBack;
    private EditText etShopName,etShopAddr,etResponsible,etWingNumber_121,etWingName_121,etPhone_121;
    //private TextView tvType,genderSpinner,pobSpinner,locationSpinner,tvAddress,maritalStatusSpinner;
    private EditText etUsername,etDob,etJob,etWingNumber,etWingName,etPhone;
    private TextInputLayout tilusername,tildob,tiljob,tilwingnumber,tilwingname,tilphone,tilShopName,tilshopAddr,tilresponsible;
    private ImageView imgType,imgGender,imgPob,imgLocation,imgAddress,imgMarried,imgtilUsername,imgtilDob,imgtilWingNumber,
            imgtilWingName,imgtilPhone,imgtilShopName,imgtilShopAddr,imgtilResponsible;
    private Button btnsubmit;
    private String name,pass,Encode,user_id;
    private int pk ,id,g;
    private SharedPreferences prefer;
    private MaterialSpinner spinner;
    private Spinner genderSpinner, pobSpinner, locationSpinner, maritalStatusSpinner,groupsSpinner;

    private List<Integer> provinceIdArrayList=new ArrayList<>();
    private List<String> provinceNameArrayList=new ArrayList<>();
    private RequestQueue mQueue;

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

        mQueue= Volley.newRequestQueue(this);

        layout_public_user = (LinearLayout)findViewById(R.id.layout_type_public_user);
        layout_121_dealer  = (LinearLayout)findViewById(R.id.layout_type_121_dealer);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
            user_id = String.valueOf(pk);
            Log.d(TAG, user_id);
        }else if (prefer.contains("id")) {
            id = prefer.getInt("id", 0);
            user_id = String.valueOf(id);
            Log.d(TAG, user_id);
        }
        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL,"api/v1/users/",user_id);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);

        ID_Field();
        initialUserInformation(url,Encode);
        DropList();
        InitialProvinceDatas();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.groups_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapterGender=ArrayAdapter.createFromResource(this,R.array.genders_array,android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterGender);

        ArrayAdapter<CharSequence> adapterMaritalStatus=ArrayAdapter.createFromResource(this,R.array.marital_status_array,android.R.layout.simple_spinner_item);
        adapterMaritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalStatusSpinner.setAdapter(adapterMaritalStatus);

        pobSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterPOB=new ArrayAdapter(this, android.R.layout.simple_spinner_item, provinceNameArrayList);
        adapterPOB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pobSpinner.setAdapter(adapterPOB);



        ArrayAdapter<String> adapterLocation=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinceNameArrayList);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapterLocation);


        //Initial Date of birth calendar
        final Calendar myCalendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            }
        };

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Edit_account.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                etDob.setText(CalendarConverter.dateConvertMMDDYY(myCalendar));
            }
        });

    } //create
    @SuppressWarnings("unchecked")
    private void DropList() {
        /*
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,ANDROID_VERSIONS);
        spinner.setAdapter(aa);
        */
    }
    @SuppressWarnings("unchecked")
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }
    @SuppressWarnings("unchecked")
    private void initialUserInformation(String url, String encode){
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
                User convertJsonJava = new User();
                try{
                    convertJsonJava = gson.fromJson(mMessage,User.class);
                    int[] gg = convertJsonJava.getGroups();
                    g = gg[0];
                    Log.d(TAG,"initialUserInformation:" + g);
                    if (g==1){
                        //groupsSpinner.setSelection(0);

                    }else if (g==2){
                        //tvType.setText("121 Dealer");

                    }else if (g==3){
                        //tvType.setText("Dealer");

                    }
                    etUsername.setText(convertJsonJava.getUsername());
//                    genderSpinner.setText(convertJsonJava.getGender());
//                    etDob   .setText(convertJsonJava.getData_of_birth());
//                    pobSpinner.setText(convertJsonJava.getPlace_of_birth());
//                    maritalStatusSpinner.setText(convertJsonJava.getMarital_status());
//                    etWingNumber.setText(convertJsonJava.getWing_account_number());
//                    etWingName.setText(convertJsonJava.getWing_account_name());
//                    etJob.setText(convertJsonJava.getJob());
//
//                    etShopAddr.setText(convertJsonJava.getShop_address());
//                    etShopName.setText(convertJsonJava.getShop_name());
//                    etResponsible.setText(convertJsonJava.getResponsible_officer());
//                    etWingNumber_121.setText(convertJsonJava.getWing_account_number());
//                    etWingName_121.setText(convertJsonJava.getWing_account_name());
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }
    @SuppressWarnings("unchecked")
    private void ID_Field() {
        // public user textview
        /*
        spinner     =(MaterialSpinner) layout_public_user.findViewById(R.id.tvType);
        genderSpinner   = (TextView)layout_public_user.findViewById(R.id.genderSpinner);
        pobSpinner      =(TextView) layout_public_user.findViewById(R.id.pobSpinner);
        locationSpinner = (TextView)layout_public_user.findViewById(R.id.locationSpinner);
        maritalStatusSpinner  = (TextView)layout_public_user.findViewById(R.id.mar);
        */
        //spinner     =(MaterialSpinner) layout_public_user.findViewById(R.id.tvType);
        groupsSpinner=(Spinner) findViewById(R.id.groups_spinner);
        genderSpinner = (Spinner)layout_public_user.findViewById(R.id.tvGender);
        pobSpinner =(Spinner) layout_public_user.findViewById(R.id.pob_spinner);
        locationSpinner = (Spinner) layout_public_user.findViewById(R.id.location_spinner);
        maritalStatusSpinner = (Spinner) layout_public_user.findViewById(R.id.marital_status_spinner);
        //public user edittext
        etUsername  = (EditText)layout_public_user.findViewById(R.id.etUsername);
        etDob       = (EditText)layout_public_user.findViewById(R.id.ed_date_of_birth );
        etJob       = (EditText)layout_public_user.findViewById(R.id.edJob );
        etWingNumber= (EditText)layout_public_user.findViewById(R.id.etWingNumber );
        etWingName  = (EditText)layout_public_user.findViewById(R.id.etWingName );
        etPhone     = (EditText)layout_public_user.findViewById(R.id.etAccount_Phone );
        etShopName  = (EditText)layout_public_user.findViewById(R.id.etShop_Name);
        etShopAddr  = (EditText)layout_public_user.findViewById(R.id. etShop_Addr);
        etResponsible= (EditText)layout_public_user.findViewById(R.id.etResponsible );

        // 121 dealer TextView
        tvType_121 = (TextView) layout_121_dealer.findViewById(R.id.tvType);
        // 121 dealer EditText
        etShopName       =(EditText)layout_121_dealer.findViewById(R.id.etShop_Name);
        etShopAddr       =(EditText)layout_121_dealer.findViewById(R.id.etShop_Addr );
        etWingName_121   =(EditText)layout_121_dealer.findViewById(R.id.etWingName );
        etWingNumber_121 =(EditText)layout_121_dealer.findViewById(R.id.etWingNumber );
        etResponsible    =(EditText)layout_121_dealer.findViewById(R.id.etResponsible );
        etPhone_121      =(EditText)layout_121_dealer.findViewById(R.id.etAccount_Phone );
        btnsubmit = (Button)findViewById(R.id.btn_EditAccount);
        tvBack = (TextView)findViewById(R.id.tvBack_account);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // textinputlayout
        //      tilusername= (TextInputLayout)findViewById(R.id.tilUsername );
        //      tildob     = (TextInputLayout)findViewById(R.id.tilDob );
        //      tiljob     = (TextInputLayout)findViewById(R.id.tilJob );
        //      tilwingnumber= (TextInputLayout)findViewById(R.id.tilWingNumber );
        //      tilwingname= (TextInputLayout)findViewById(R.id.tilWingName );
        //      tilphone   = (TextInputLayout)findViewById(R.id.tilAccount_phone );
        //      tilShopName= (TextInputLayout)findViewById(R.id.tilShop_Name);
        //      tilshopAddr= (TextInputLayout)findViewById(R.id.tilShop_Addr );
        //      tilresponsible= (TextInputLayout)findViewById(R.id.tilResponsible );

        imgType            = (ImageView)findViewById(R.id.imgType );
        //imgGender          = (ImageView)findViewById(R.id.imgGender );
        //imgPob             = (ImageView)findViewById(R.id.imgPob );
        //imgLocation        = (ImageView)findViewById(R.id.imgLocation );
        imgAddress         = (ImageView)findViewById(R.id.imgAccount_Address );
        //imgMarried         = (ImageView)findViewById(R.id.imgMarried );
        //imgtilUsername     = (ImageView)findViewById(R.id.imgUsername);
        //imgtilDob          = (ImageView)findViewById(R.id.imgDob );
        imgtilWingNumber   = (ImageView)findViewById(R.id.imgWingNumber );
        imgtilWingName     = (ImageView)findViewById(R.id.imgWingName );
        imgtilPhone        = (ImageView)findViewById(R.id.imgAccount_Phone );
        imgtilShopName     = (ImageView)findViewById(R.id.imgShop_name);
        imgtilShopAddr     = (ImageView)findViewById(R.id.imgShop_Addr);
        imgtilResponsible  = (ImageView)findViewById(R.id.imgResponsible );
    }

    private void InitialProvinceDatas(){
        String API_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/provinces/";
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, API_ENDPOINT, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i=0;i< jsonArray.length(); i++){
                                JSONObject  field_province = jsonArray.getJSONObject(i);
                                Log.d(TAG,"Result "+field_province);

                                int id = field_province.getInt("id");
                                String province = field_province.getString("province");
                                provinceNameArrayList.add(province);
                                provinceIdArrayList.add(id);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
        ((TextView) parent.getChildAt(0)).setTextSize(5);
        Toast.makeText(getApplicationContext(), provinceIdArrayList.get(position) , Toast.LENGTH_LONG).show();
        Log.d(TAG, provinceIdArrayList.get(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
