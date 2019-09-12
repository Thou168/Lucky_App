package com.bt_121shoppe.motorbike.Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.R;

public class SelectUserType extends AppCompatActivity {
    private RadioButton radioPublic_User,radioOther_dealer;
    private RadioGroup radioGroup;
    private Button Next;
    private String register_verify;
    private int group_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
// select account type by samang 10/09/19
        radioPublic_User = findViewById(R.id.radio_publicUser);
        radioOther_dealer= findViewById(R.id.radio_otherDealer);
        radioGroup = findViewById(R.id.radio_group);
        Next    = findViewById(R.id.btn_userType);

        Intent intent = getIntent();
        register_verify = intent.getStringExtra("Register_verify");

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
                if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(v.getContext(),"Please select account type",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(v.getContext(), Register.class);
                    intent.putExtra("Register_verify",register_verify);
                    intent.putExtra("user_group", group_user);
                    startActivity(intent);
                }
            }
        });
    }
}
