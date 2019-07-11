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
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lucky_app.Activity.Account;
import com.example.lucky_app.Api.ConsumeAPI;
import com.example.lucky_app.Api.Convert_Json_Java;
import com.example.lucky_app.Api.User;
import com.example.lucky_app.R;
import com.example.lucky_app.utils.CalendarConverter;
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
import org.w3c.dom.Text;

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
import okhttp3.RequestBody;
import okhttp3.Response;

public class Edit_account extends AppCompatActivity {

    String[] country = { "India", "USA", "China", "Japan", "Other"};
    private static final String TAG = "Response";
    private LinearLayout layout_public_user,layout_121_dealer;
    private TextView tvType, tvType_121,tvBack;
    private EditText etUsername,etShop_name,etWingNumber,etWingName,etPhone;
    private TextInputLayout tilusername,tildob,tiljob,tilwingnumber,tilwingname,tilphone,tilShopName,tilshopAddr,tilresponsible;
    private ImageView imgType,imgGender,imgPob,imgLocation,imgAddress,imgMarried,imgtilUsername,imgtilDob,imgtilWingNumber,
            imgtilWingName,imgtilPhone,imgtilShopName,imgtilShopAddr,imgtilResponsible;
    private Button btnsubmit;
    private String name,pass,Encode,user_id;
    private ArrayAdapter<Integer> ad_id;
    private int pk, id_pob,id_location;
    private SharedPreferences prefer;
    private List<Integer> provinceIdArrayList=new ArrayList<>();
    private List<String> provinceNameArrayList=new ArrayList<>();
    private RequestQueue mQueue;

    private MaterialSpinner mp_Gender,mp_Dob,mp_Married,mp_Pob,mp_location;

    int mMonth,mYear,mDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        TextView back = (TextView) findViewById(R.id.tv_Back);


//        layout_public_user = (LinearLayout)findViewById(R.id.layout_type_public_user);
//        layout_121_dealer  = (LinearLayout)findViewById(R.id.layout_type_121_dealer);

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




        tvType = (TextView)findViewById(R.id.tvType);

        etUsername  =(EditText) findViewById(R.id.etUsername);
        etWingName  =(EditText) findViewById(R.id.etWingName);
        etWingNumber=(EditText) findViewById(R.id.etWingNumber);
        etPhone     =(EditText) findViewById(R.id.etPhone_account);



        mp_Dob     = (MaterialSpinner) findViewById(R.id.mp_Dob);
        mp_Pob     = (MaterialSpinner) findViewById(R.id.mp_Pob);
        mp_Married = (MaterialSpinner) findViewById(R.id.mp_Married);
        mp_Gender  = (MaterialSpinner) findViewById(R.id.mp_Gender);
        mp_location= (MaterialSpinner) findViewById(R.id.mp_Location);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mp_Gender.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mp_Dob.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.marital_status_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mp_Married.setAdapter(adapter2);

        //Date


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Province();
        initialUserInformation(url,Encode);
        Button btSubmit=(Button) findViewById(R.id.btn_EditAccount);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //summitUserInformation();
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
                                List<String> sex = new ArrayList<>();
                                sex.add(0,s);
                                mp_Gender.setSelection(0);

                                String d = convertJsonJava.getProfile().getDate_of_birth();
                                List<String> date = new ArrayList<>();
                                date.add(0,d);
                                mp_Dob.setSelection(0);

                                String m = convertJsonJava.getProfile().getMarital_status();
                                List<String> married = new ArrayList<>();
                                married.add(0,m);
                                mp_Married.setSelection(0);

                                int p = Integer.parseInt(convertJsonJava.getProfile().getPlace_of_birth());
                                int pob=ad_id.getPosition(p);
                                Log.d(TAG,"r"+pob);
                                mp_Pob.setSelection(pob);

                                int l = Integer.parseInt(convertJsonJava.getProfile().getProvince());
                                int prov = ad_id.getPosition(l);
                                mp_location.setSelection(prov);
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
    private void summitUserInformation(){
        String URL_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/users/"+pk;
        OkHttpClient client=new OkHttpClient();
        JSONObject user=new JSONObject();
        JSONObject profile=new JSONObject();
        JSONObject profile_detail=new JSONObject();
        try{

            profile_detail.put("gender","male");
            //profile_detail.put("date_of_birth","");
            profile_detail.put("telephone","");
            profile_detail.put("address","");
            profile_detail.put("wing_account_number",etWingNumber.getText().toString());
            profile_detail.put("wing_account_name",etWingName.getText().toString());
            profile_detail.put("job","");
            profile.put("profile",profile_detail);

            user.put("username",etUsername.getText().toString());
            user.put("password",pass);
            user.put("profile",profile_detail);
            RequestBody body=RequestBody.create(ConsumeAPI.MEDIA_TYPE,user.toString());
            Request request=new Request.Builder()
                    .url(URL_ENDPOINT)
                    .put(body)
                    .header("Content-Type","application/json")
                    .header("Accept","application/json")
                    .header("Authorization","Basic "+Encode)
                    .build();
            Log.e(TAG,URL_ENDPOINT+" "+user);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String result=e.getMessage().toString();
                    Log.d(TAG,"Fail:"+result);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    Log.d(TAG,result);
                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void PutData(String url,String encode) {

//        String t = tvType.getText().toString();
//        username  = etUsername.getText().toString();
//        gender    = tvGender.getText().toString().toLowerCase();
//        dob       = etDob.getText().toString();
//        pob       = tvPob.getText().toString();
//        married   = tvMarried.getText().toString().toLowerCase();
//        phone     = etPhone.getText().toString();
//        location  = tvLocation.getText().toString();
//        job       = etJob.getText().toString();
//        shop_name        = etShopName.getText().toString();
//        shop_addr        = etShopAddr.getText().toString();
//        responsible_name = etResponsible.getText().toString();
//
//        if (t.equals("Public User") || t.equals("Dealer")){
//            wingnumber = etWingNumber.getText().toString();
//            wingname   = etWingName.getText().toString();
//        }else if (t.equals("121 Dealer")){
//            wingnumber = etWingNumber_121.getText().toString();
//            wingname   = etWingName_121.getText().toString();
//        }

        MediaType media = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();
        JSONObject data = new JSONObject();
        JSONObject pro  = new JSONObject();

        try{
            data.put("username",name);
            data.put("password",pass);
            data.put("first_name",etUsername.getText().toString());

            //pro.put("gender",gender);

            pro.put("data_of_birth", mp_Dob.getSelectedItem().toString().toLowerCase());
            pro.put("address",""); /////////////
            pro.put("shop_name","");
            pro.put("responsible_officer","");
            pro.put("job","");
            pro.put("province",id_location); /////////////
            pro.put("gender",mp_Gender.getSelectedItem().toString().toLowerCase());
            pro.put("marital_status",mp_Married.getSelectedItem().toString().toLowerCase());
            pro.put("shop_address","");
            pro.put("telephone",etPhone.getText().toString().toLowerCase());

            pro.put("wing_account_number",etWingNumber.getText().toString());
            pro.put("wing_account_name",etWingName.getText().toString());

           pro.put("place_of_birth",id_pob);
            data.put("profile",pro);
            data.put("groups", new JSONArray("[\"1\"]"));
            //data.put("groups",new JSONArray("["+id_type+"]"));
        }catch (JSONException e){
            e.printStackTrace();
        }
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String message = response.body().string();
                    Log.d("Response", message);
                    startActivity(new Intent(getApplicationContext(), Account.class));
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //finish();
            }
        });
    }

    public void Province(){
        final String rl = "http://103.205.26.103:8000/api/v1/provinces/";
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
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String pro = object.getString("province");
                        provinceIdArrayList.add(id);
                        provinceNameArrayList.add(pro);
                    ad_id = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,provinceIdArrayList);
                    ArrayAdapter<String> ad_name = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,provinceNameArrayList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mp_location.setAdapter(ad_name);
                            mp_Pob.setAdapter(ad_name);
                        }
                    });
                    }

                    mp_Pob.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                            id_pob = ad_id.getItem(i);
                        }

                        @Override
                        public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

                        }
                    });

                    mp_location.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                            id_location = ad_id.getItem(i);
                        }

                        @Override
                        public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

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
}
