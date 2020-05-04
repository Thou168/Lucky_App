package com.bt_121shoppe.motorbike.loan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.loan.child.one;
import com.bt_121shoppe.motorbike.loan.child.two;
import com.bt_121shoppe.motorbike.loan.child.three;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.item_two;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Locale;

public class Create_Load extends AppCompatActivity implements one.SendItemOne,two.SendItemTwo {

    private TextView back;
    private TabLayout tabs;
    private ViewPager viewPager;
    private Pager pager;
    private AlertDialog dialog;
    private boolean From_Loan;
    private int product_id,mCardID,mLoandID;
    private String price,draft,loan_history;
    private Button btnList_Draft;
    String currentLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = prefer.getString("My_Lang", "");
        setContentView(R.layout.activity_create__load);
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pagerLoan);
        tabs.setupWithViewPager(viewPager);
        pager = new Pager(getSupportFragmentManager());
        viewPager.setAdapter(pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Intent intent = getIntent();
        LinearLayout tabStrip = ((LinearLayout)tabs.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        product_id = intent.getIntExtra("product_id",0);
        mLoandID = intent.getIntExtra("LoanID",0);
        Log.e("loan id",""+mLoandID);
        From_Loan = intent.getBooleanExtra("LoanEdit",false);
        price = intent.getStringExtra("price");
        draft = intent.getStringExtra("draft");
        loan_history = intent.getStringExtra("loan_history");

        back = findViewById(R.id.tv_back);
        back.setOnClickListener(v -> onBackPressed());

        btnList_Draft = findViewById(R.id.list_draft);
        btnList_Draft.setOnClickListener(view -> {
            Intent intent1 = new Intent(Create_Load.this,Draft_loan.class);
            intent1.putExtra("product_id",product_id);
            intent1.putExtra("price",price);
            intent1.putExtra("LoanEdit",From_Loan);
            intent1.putExtra("LoanID",mLoandID);
            startActivity(intent1);
        });
        if (loan_history != null){
            btnList_Draft.setVisibility(View.GONE);
        }
        if (draft != null){
            btnList_Draft.setVisibility(View.GONE);
        }

    }
    public void setBack(int position){
        viewPager.setCurrentItem(position);
    }

    public int AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0;
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
                mCardID = which;
            editText.setText(items[which]);
            dialog.dismiss();
        });
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
    public boolean CheckedRdioButton(RadioGroup radioGroup){
        if (radioGroup.getCheckedRadioButtonId()==-1) {
            return false;
        }
        return true;
    }
    public void request(boolean b,RadioGroup radioGroup,TextView textView,String text){
        textView.setText("");
        if (!b) {
            if(radioGroup.getCheckedRadioButtonId()==-1) {
                textView.setText(text);
                textView.setTextColor(getColor(R.color.red));
            }else {
                textView.setText("");
            }
        }else {
            textView.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
        final AlertDialog clearDialog = new AlertDialog.Builder(this).create();
        clearDialog.setView(clearDialogView);
        clearDialog.setCancelable(false);
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
                if (draft != null){
                    finish();
                }else if (mLoandID != 0){
                    Intent intent1 = new Intent(Create_Load.this,Account.class);
                    startActivity(intent1);
                }else if (product_id != 0) {
                    Intent intent1 = new Intent(Create_Load.this,Detail_new_post_java.class);
                    intent1.putExtra("ID",product_id);
                    startActivity(intent1);
                }else {
                    finish();
                    clearDialog.dismiss();
                }
            }
        });
        clearDialog.show();
    }
    public void requstFocus(boolean b,EditText editText,TextView textView,String text){
        textView.setText("");
        if (!b) {
            if(!(editText ==null)) {
                editText.requestFocus();
                textView.setText(text);
                textView.setTextColor(getColor(R.color.red));
            }
        }else {
            textView.setText("");
        }
    }

    private static String makeFragmentName(int viewId , long id){
        return "android:switcher:"+viewId+":"+id;
    }

    @Override
    public void sendItemOne(item_one item,int position) {
        Log.e("item one",""+item);
        two f = (two) getSupportFragmentManager().findFragmentByTag(makeFragmentName(viewPager.getId(),1));
        if (f != null) {
            f.getItemOne(item);
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public void sendItemTwo(item_two item,int position) {
        //Log.e("item two",""+item);
        three f = (three) getSupportFragmentManager().findFragmentByTag(makeFragmentName(viewPager.getId(),2));
        if (f != null) {
            f.getItemTwo(item);
            viewPager.setCurrentItem(position);
        }
    }

    public class Pager extends FragmentPagerAdapter {

        public Pager(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new one().newInstance(product_id,price,mLoandID,From_Loan,draft,loan_history);

            } else if (position == 1) {
                fragment = new two().newInstance(product_id,price,mLoandID,From_Loan,draft,loan_history);

            } else if (position == 2) {
                fragment = new three().newInstance(product_id,price,mLoandID,From_Loan,draft,loan_history);
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position==0) {
//                title = getApplicationContext().getString(R.string.private_info);
                if (currentLanguage.equals("en")) {
                    title = "Private Info";
                } else title = "ព័ត៌មានឯកជន";
            } else if (position==1) {
//                title =  getApplicationContext().getString(R.string.tab_loan_info);
                if (currentLanguage.equals("en")) {
                    title = "Loan Info";
                }else title = "ព័ត៌មានឥណទាន";
            }else if (position == 2){
//                title = getApplicationContext().getString(R.string.borrow_info);
                if (currentLanguage.equals("en")) {
                    title = "Borrow Info";
                }else title = "ព័ត៌មានអ្នកខ្ចី";
            }
            return title;
        }
    }
}
