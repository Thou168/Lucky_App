package com.bt_121shoppe.motorbike.loan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.child.one;
import com.bt_121shoppe.motorbike.loan.child.two;
import com.bt_121shoppe.motorbike.loan.child.three;
import com.google.android.material.tabs.TabLayout;

public class Create_Load extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    Button next,prev;
    TextView back;
    private TabLayout tabs;
    private ViewPager viewPager;
    int count=0;
    AlertDialog dialog;
    boolean check=false,mBook_familiy,mPhoto,mCard_work,From_Loan;
    RadioButton radio3;
    int product_id,mCardID,mLoandID;
    String price;
    Boolean dialogpress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__load);
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pagerLoan);
        tabs.setupWithViewPager(viewPager);
        setUpPager();
        Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id",0);
        mLoandID = intent.getIntExtra("LoanID",0);
        From_Loan = intent.getBooleanExtra("LoanEdit",false);
        price = intent.getStringExtra("price");

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, one.newInstance(product_id,price,mLoandID,From_Loan)).commit();
//        }

        back = findViewById(R.id.tv_back);
        back.setOnClickListener(v -> onBackPressed());

    }
    public void setBack(){
        dialogpress = true;
        super.onBackPressed();
    }
    public void loadFragment(Fragment fragment){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout,fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
    }
    public int AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
                mCardID = which;
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
                if (!st.isEmpty()&&s.toString().length()>2){
                    imageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
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
                }
                else {
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
        TextView Mssloan = clearDialogView.findViewById(R.id.textView_message);
        Mssloan.setText(R.string.back_message);
        Button btnYes =  clearDialogView.findViewById(R.id.button_positive);
        btnYes.setText(R.string.yes_leave);
        Button btnNo = clearDialogView.findViewById(R.id.button_negative);
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
                finish();
                clearDialog.dismiss();
            }
        });
        clearDialog.show();
    }
    public void requstFocus(boolean b,ImageView img,EditText editText){
        if (!b) {
            img.setImageResource(R.drawable.ic_error_black_24dp);
            if(!(editText ==null))
                editText.requestFocus();
        }
    }

    private void setUpPager(){
        tabs.addTab(tabs.newTab().setText(R.string.private_info));
        tabs.addTab(tabs.newTab().setText(R.string.tab_loan_info));
        tabs.addTab(tabs.newTab().setText(R.string.borrow_info));
        tabs.setOnTabSelectedListener(this);
        Pager adapter = new Pager(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    public class Pager extends FragmentStatePagerAdapter {
        int tabCount;
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    return new one();
                case 1:
                    return new two();
                case 2:
                    return new three();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0) {
                return getApplicationContext().getString(R.string.private_info);
            } else if (position==1) {
                return getApplicationContext().getString(R.string.tab_loan_info);
            }else if (position == 2){
                return getApplicationContext().getString(R.string.borrow_info);
            } else {
                return null;
            }
        }
    }
}
