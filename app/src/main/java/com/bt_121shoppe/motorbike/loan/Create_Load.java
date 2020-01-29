package com.bt_121shoppe.motorbike.loan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.child.one;
import com.bt_121shoppe.motorbike.loan.child.two;
import com.bt_121shoppe.motorbike.loan.child.three;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.item_two;
import com.google.android.material.tabs.TabLayout;

public class Create_Load extends AppCompatActivity implements one.SendItemOne,two.SendItemTwo {

    private TextView back;
    private TabLayout tabs;
    private ViewPager viewPager;
    private Pager pager;
    private AlertDialog dialog;
    private boolean From_Loan;
    private int product_id,mCardID,mLoandID;
    private String price;
    private Button btnList_Draft;
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
        pager = new Pager(getSupportFragmentManager());
        viewPager.setAdapter(pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id",0);
        mLoandID = intent.getIntExtra("LoanID",0);
        From_Loan = intent.getBooleanExtra("LoanEdit",false);
        price = intent.getStringExtra("price");

        back = findViewById(R.id.tv_back);
        back.setOnClickListener(v -> onBackPressed());

        btnList_Draft = findViewById(R.id.list_draft);
        btnList_Draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Create_Load.this,Draft_loan.class);
                startActivity(intent);
            }
        });

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
    public void requstFocus(boolean b,EditText editText,TextView textView,String text){
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
        Log.e("item two",""+item);
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
                fragment = new one().newInstance(product_id,price,mLoandID,From_Loan);

            } else if (position == 1) {
                fragment = new two().newInstance(product_id,price,mLoandID,From_Loan);

            } else if (position == 2) {
                fragment = new three().newInstance(product_id,price,mLoandID,From_Loan);
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
                title = getApplicationContext().getString(R.string.private_info);
            } else if (position==1) {
                title =  getApplicationContext().getString(R.string.tab_loan_info);
            }else if (position == 2){
                title = getApplicationContext().getString(R.string.borrow_info);
            }
            return title;
        }
    }
}
