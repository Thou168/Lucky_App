package com.bt_121shoppe.motorbike.Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

public class SelectUserTypeActivity extends AppCompatActivity {

    private static final String TAG=SelectUserTypeActivity.class.getSimpleName();
    private RadioButton radioPublic_User,radioOther_dealer;
    private RadioGroup radioGroup;
    private Button Next;
    private String register_verify,product_id;
    private int group_user;
    private Intent intent;
    private String processType="",facebooktokenkey,facebookid,facebookname,imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        // select account type by samang 10/09/19
        radioPublic_User = findViewById(R.id.radio_publicUser);
        radioOther_dealer= findViewById(R.id.radio_otherDealer);
        radioGroup = findViewById(R.id.radio_group);
        Next    = findViewById(R.id.btn_userType);

        intent=getIntent();
        if (intent!=null) {
            processType = intent.getStringExtra("processtype");
            facebooktokenkey = intent.getStringExtra("facebooktokenkey");
            facebookid = intent.getStringExtra("facebookid");
            facebookname = intent.getStringExtra("facebookname");
            imageurl = intent.getStringExtra("imageurl");
            register_verify = intent.getStringExtra("Register_verify");
        }
//        Log.d("Facebook register",processType+","+facebooktokenkey+","+facebookid+","+facebookname+","+imageurl);
//        Log.d("11111","facebook is "+CommonFunction.ProcessType.FacebookRegister.toString());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_publicUser:
                        group_user = 1;
                        break;
                    case R.id.radio_otherDealer:
                        group_user = 3;
                        break;
                }
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(v.getContext(), "Please select account type", Toast.LENGTH_SHORT).show();
                } else {
                    if (processType.equals(CommonFunction.ProcessType.FacebookRegister.toString())) {
                        Intent intent = new Intent(SelectUserTypeActivity.this, ConfirmMobileNumberActivity.class);
                        intent.putExtra("facebooktokenkey", facebooktokenkey);
                        intent.putExtra("facebookid", facebookid);
                        intent.putExtra("facebookname", facebookname);
                        intent.putExtra("imageurl", imageurl);
                        intent.putExtra("usergroup", group_user);
                        intent.putExtra("gender", "");
                        intent.putExtra("birthday", "");
                        intent.putExtra("Register_verify", register_verify);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                        intent.putExtra("user_group", group_user);
                        intent.putExtra("Register_verify", register_verify);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
