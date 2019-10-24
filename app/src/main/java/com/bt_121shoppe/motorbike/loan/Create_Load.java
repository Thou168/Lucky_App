package com.bt_121shoppe.motorbike.loan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.child.AdapterNavigation.CustomViewPager;
import com.bt_121shoppe.motorbike.loan.child.one;

public class Create_Load extends AppCompatActivity {

    Button next,prev;
    TextView back;
    CustomViewPager viewPager;
    int count=0;
    AlertDialog dialog;
    boolean check = false,mCardID,mBook_familiy,mPhoto,mCard_work;
    RadioButton radio3;
    int product_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__load);
        Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id",0);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, one.newInstance(product_id)).commit();
        }

        back = findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_leaveLoan();
            }
        });

//        viewPager = findViewById(R.id.viewPager);
//        setupFm(getSupportFragmentManager(), viewPager); //Setup Fragment
//        next = findViewById(R.id.next);
//        prev = findViewById(R.id.back);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,
//                    new one()).commit();
//        }
//        viewPager.setCurrentItem(count);

//        next.setOnClickListener(v -> {
//            count++;
//            viewPager.setCurrentItem(count,true);
//        });
//        prev.setOnClickListener(v -> {
//            count--;
//            viewPager.setCurrentItem(count,true);
//        });
    }
    public void setBack(){
        onBackPressed();
    }
    public void loadFragment(Fragment fragment){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout,fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
    }
    public boolean AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle("Choose item");
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCardID = true;
            else mCardID = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
        return mCardID;
    }
    public String cuteString(String st, int indext){
        String[] separated = st.split(":");
        return separated[indext];
    }
    public boolean Checked(EditText editText){
        return (!editText.getText().toString().isEmpty() && editText.getText().toString().length()>2);
    }
    public boolean CheckedYear(EditText editText){
        return (!editText.getText().toString().isEmpty() && editText.getText().toString().length()>0);
    }
    public void Condition(ImageView imageView,EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String st = s.toString();
                    //do your work here
                if (!st.isEmpty()){
                    if (st.length()>2){
                        imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    } else {
                        imageView.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void ConditionYear(ImageView imageView,EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String st = s.toString();
                //do your work here
                if (!st.isEmpty()){
//                    if (st.length()>0){
                        imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                    } else {
//                        imageView.setImageResource(R.drawable.ic_error_black_24dp);
//                    }
                }else {
                    imageView.setImageResource(R.drawable.ic_error_black_24dp);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });

    }
    public boolean RadioCondition(ImageView imageView, RadioGroup radioGroup){
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = radioGroup.findViewById(checkedId);
            int index = radioGroup.indexOfChild(radioButton);
            radio3 = radioGroup.findViewById(checkedId);
            if (radio3.getText().toString().isEmpty()){
                imageView.setImageResource(R.drawable.ic_error_black_24dp);
                check = false;
            }else {
                imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
                check = true;
            }
        });
        return check;
    }

    private void dialog_leaveLoan() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
        final AlertDialog clearDialog = new AlertDialog.Builder(this).create();
        clearDialog.setView(clearDialogView);
        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
        Mssloan.setText(R.string.back_message);
        Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
        btnYes.setText(R.string.yes_leave);
        Button btnNo = (Button) clearDialogView.findViewById(R.id.button_negative);
        btnNo.setText(R.string.no_leave);
        clearDialogView.findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialog.dismiss();
            }
        });
        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clearDialog.show();
    }

    @Override
    public void onBackPressed() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
        final AlertDialog clearDialog = new AlertDialog.Builder(this).create();
        clearDialog.setView(clearDialogView);
        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
        Mssloan.setText(R.string.back_message);
        Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
        btnYes.setText(R.string.yes_leave);
        Button btnNo = (Button) clearDialogView.findViewById(R.id.button_negative);
        btnNo.setText(R.string.no_leave);
        clearDialogView.findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialog.dismiss();
            }
        });
        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_Load.super.onBackPressed();
            }
        });

        clearDialog.show();
    }

    //    public boolean Checked(ImageView imageView,EditText editText){
//        if (editText.getText().toString().isEmpty() || editText.getText().toString().length()<3){
//            editText.setError("wtf");
//            imageView.setImageResource(R.drawable.ic_error_black_24dp);
//            check = false;
//        }else {
//            check =true;
//        }
//        return check;
//    }
//    public void setViewPager (int pager){
//        viewPager.setCurrentItem(pager);
//    }
//    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager){
//        FragmentAdapter Adapter = new FragmentAdapter(fragmentManager);
//        //Add All Fragment To List
//        Adapter.add(new one(), "null");
//        Adapter.add(new two(), "null");
//        Adapter.add(new three(), "null");
//        viewPager.setAdapter(Adapter);
//    }
}
