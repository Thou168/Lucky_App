package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.item_two;
import com.bt_121shoppe.motorbike.loan.model.province_Item;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class two extends Fragment {
    private static final String ARG_NUMBER = "arg_number";

    private Toolbar mToolbar;
    private TextView mTvName;
    private Button mBtnNext, mBtnNextWithFinish, mBtnback;
    private item_one itemOne;
    private three fragment;
    private Create_Load createLoad;
    private EditText mLoan_amount,mLoan_Term,mloan_RepaymentType,mLoan_Contributions,mNumber_institution,mMonthly_Amount_Paid;
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;
    private RadioButton radio1,radio2,radio3;
    private RadioGroup rgBuying_product_insurance,mAllowto_visit_home;
    private boolean radioCheck1 = false,radioCheck2 = false;
    private item_two itemTwo;
    private boolean mVisit_home,mBuy_product_insurance;
    AlertDialog dialog;
    public static two newInstance(item_one itemOne) {
        Bundle args = new Bundle();
//        args.putParcelable(ARG_NUMBER,itemOne);
        args.putParcelable(ARG_NUMBER, itemOne);
        two fragment = new two();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
//            itemOne = getArguments().getParcelable(ARG_NUMBER);
             itemOne =  args.getParcelable(ARG_NUMBER);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create__load_two, container, false);

        createLoad = (Create_Load)getActivity();
        initView(view);
        checkEd();
//        fragment = new three();
        createLoad = (Create_Load)this.getActivity();
        mBtnNext = view.findViewById(R.id.btn_next);
        mBtnback = view.findViewById(R.id.btn_back);

        mBtnback.setOnClickListener(view1 -> { createLoad.setBack(); });
        mBtnNext.setOnClickListener(view12 -> {
//            Bundle bundle=new Bundle();
//            fragment.setArguments(bundle);
            if (checkEd()){
                itemTwo = new item_two(Float.parseFloat(mLoan_amount.getText().toString()),mLoan_Term.getText().toString(),mloan_RepaymentType.getText().toString(),mLoan_Contributions.getText().toString(),
                        mBuy_product_insurance,mVisit_home,mNumber_institution.getText().toString(),mMonthly_Amount_Paid.getText().toString(),itemOne);
                fragment = three.newInstance(itemTwo);
                createLoad.loadFragment(fragment);
            }
        });

        return view;
    }
    private void AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mVisit_home = true;
            else mVisit_home = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void initView(View view) {

        String[] values = getResources().getStringArray(R.array.repayment);
        String[] institution = getResources().getStringArray(R.array.institute);

        mLoan_amount = view.findViewById(R.id.etLoan_amount);
        mLoan_Term = view.findViewById(R.id.etBorrowing_period);
        mloan_RepaymentType =view.findViewById(R.id.etPayment_Method);
        mloan_RepaymentType.setOnClickListener(v -> {
            createLoad.AlertDialog(values,mloan_RepaymentType);
        });
        mLoan_Contributions = view.findViewById(R.id.etLoan_contributions);
//        mBtnNextWithFinish = view.findViewById(R.id.btn_next_with_finish);
        rgBuying_product_insurance = view.findViewById(R.id.radio_group);
        rgBuying_product_insurance.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = rgBuying_product_insurance.findViewById(checkedId);
            int index = rgBuying_product_insurance.indexOfChild(radioButton);
            radio2 = rgBuying_product_insurance.findViewById(checkedId);
            img5.setImageResource(R.drawable.ic_check_circle_black_24dp);
            // Add logic here
            switch (index) {
                case 0: // first button
                    mBuy_product_insurance = true;
                    radioCheck1 = true;
                    break;
                case 1: // secondbutton
                    radioCheck1 = true;
                    mBuy_product_insurance = false;
                    break;
            }
        });
        mAllowto_visit_home = view.findViewById(R.id.radio_group1);
        mAllowto_visit_home.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = mAllowto_visit_home.findViewById(checkedId);
            int index = mAllowto_visit_home.indexOfChild(radioButton);
            radio3 = mAllowto_visit_home.findViewById(checkedId);
            img6.setImageResource(R.drawable.ic_check_circle_black_24dp);
            // Add logic here
            switch (index) {
                case 0: // first button
                    mVisit_home = true;
                    radioCheck2 = true;
                    break;
                case 1: // secondbutton
                    mVisit_home = false;
                    radioCheck2 = true;
                    break;
            }
        });
        mNumber_institution = view.findViewById(R.id.etNumber_debt);
        mNumber_institution.setOnClickListener(v -> {
            createLoad.AlertDialog(institution,mNumber_institution);
        });
        mMonthly_Amount_Paid = view.findViewById(R.id.et_monthly_payment);

        img1 = view.findViewById(R.id.img_1);
        img2 = view.findViewById(R.id.img_2);
        img3 = view.findViewById(R.id.img_3);
        img4 = view.findViewById(R.id.img_4);
        img5 = view.findViewById(R.id.img_5);
        img6 = view.findViewById(R.id.img_6);
        img7 = view.findViewById(R.id.img_7);
        img8 = view.findViewById(R.id.img_8);
        img9 = view.findViewById(R.id.img_9);
        img10 = view.findViewById(R.id.img_10);
    }
    private boolean checkEd(){
        boolean bLoand_amount,bLoan_Period,bPayment_Method,bLoan_Contributions,bNumber_institution,bMonthly_Amount_Paid;

        bLoand_amount = createLoad.CheckedYear(mLoan_amount);
        bLoan_Period = createLoad.CheckedYear(mLoan_Term);
        bPayment_Method = createLoad.Checked(mloan_RepaymentType);
        bLoan_Contributions = createLoad.CheckedYear(mLoan_Contributions);
        bNumber_institution = createLoad.CheckedYear(mNumber_institution);
        bMonthly_Amount_Paid = createLoad.CheckedYear(mMonthly_Amount_Paid);

        createLoad.ConditionYear(img1,mLoan_amount);
        createLoad.ConditionYear(img2,mLoan_Term);
        createLoad.Condition(img3,mloan_RepaymentType);
        createLoad.ConditionYear(img4,mLoan_Contributions);
        createLoad.ConditionYear(img7,mNumber_institution);
        createLoad.ConditionYear(img8,mMonthly_Amount_Paid);

        return bLoand_amount&&bLoan_Period&&bPayment_Method&&bLoan_Contributions&&radioCheck1&&radioCheck2&&bNumber_institution&&bMonthly_Amount_Paid;
    }
}
