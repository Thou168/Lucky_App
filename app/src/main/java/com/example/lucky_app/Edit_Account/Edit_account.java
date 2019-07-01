package com.example.lucky_app.Edit_Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucky_app.Api.ConsumeAPI;
import com.example.lucky_app.Api.Convert_Json_Java;
import com.example.lucky_app.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tiper.MaterialSpinner;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Edit_account extends AppCompatActivity {

    String[] country = { "India", "USA", "China", "Japan", "Other"};
    private static final String TAG = "Response";
    private LinearLayout layout_public_user,layout_121_dealer;
    private TextView tvType_121,tvBack;
    private EditText etShopName,etShopAddr,etResponsible,etWingNumber_121,etWingName_121,etPhone_121;
    private TextView tvType,tvGender,tvPob,tvLocation,tvAddress,tvMarried;
    private EditText etUsername,etDob,etJob,etWingNumber,etWingName,etPhone;
    private TextInputLayout tilusername,tildob,tiljob,tilwingnumber,tilwingname,tilphone,tilShopName,tilshopAddr,tilresponsible;
    private ImageView imgType,imgGender,imgPob,imgLocation,imgAddress,imgMarried,imgtilUsername,imgtilDob,imgtilWingNumber,
            imgtilWingName,imgtilPhone,imgtilShopName,imgtilShopAddr,imgtilResponsible;
    private Button btnsubmit;
    private String name,pass,Encode,user_id;
    private int pk ,id,g;
    private SharedPreferences prefer;
    private MaterialSpinner spinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

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
      //  Groups(url,Encode);
        DropList();

    } //create

    private void DropList() {
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,ANDROID_VERSIONS);
        spinner.setAdapter(aa);

    }

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

    private void Groups(String url,String encode){
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
                Convert_Json_Java convertJsonJava = new Convert_Json_Java();
                try{
                    convertJsonJava = gson.fromJson(mMessage,Convert_Json_Java.class);
                    int[] gg = convertJsonJava.getGroups();
                    g = gg[0];
                    Log.d(TAG,"Groups:" + g);
                    if (g==1){
                        tvType.setText("Public User");

                    }else if (g==2){
                        tvType.setText("121 Dealer");

                    }else if (g==3){
                        tvType.setText("Dealer");

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
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void ID_Field() {
        // public user textview
        spinner     =(MaterialSpinner) layout_public_user.findViewById(R.id.tvType);
        tvGender   = (TextView)layout_public_user.findViewById(R.id.tvGender);
        tvPob      =(TextView) layout_public_user.findViewById(R.id.tvPob);
        tvLocation = (TextView)layout_public_user.findViewById(R.id.tvLocation);
        tvMarried  = (TextView)layout_public_user.findViewById(R.id.tvMarried);
//public user edittext
        etUsername  = (EditText)layout_public_user.findViewById(R.id.etUsername);
        etDob       = (EditText)layout_public_user.findViewById(R.id.etDOB );
        etJob       = (EditText)layout_public_user.findViewById(R.id.etJob );
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
        imgGender          = (ImageView)findViewById(R.id.imgGender );
        imgPob             = (ImageView)findViewById(R.id.imgPob );
        imgLocation        = (ImageView)findViewById(R.id.imgLocation );
        imgAddress         = (ImageView)findViewById(R.id.imgAccount_Address );
        imgMarried         = (ImageView)findViewById(R.id.imgMarried );
        imgtilUsername     = (ImageView)findViewById(R.id.imgUsername);
        imgtilDob          = (ImageView)findViewById(R.id.imgDob );
        imgtilWingNumber   = (ImageView)findViewById(R.id.imgWingNumber );
        imgtilWingName     = (ImageView)findViewById(R.id.imgWingName );
        imgtilPhone        = (ImageView)findViewById(R.id.imgAccount_Phone );
        imgtilShopName     = (ImageView)findViewById(R.id.imgShop_name);
        imgtilShopAddr     = (ImageView)findViewById(R.id.imgShop_Addr);
        imgtilResponsible  = (ImageView)findViewById(R.id.imgResponsible );
    }
}
