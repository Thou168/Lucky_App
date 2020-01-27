package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.bt_121shoppe.motorbike.loan.LoanActivity;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.item_two;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.loan.model.province_Item;

import java.io.Serializable;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;
import static com.facebook.AccessTokenManager.TAG;


public class two extends Fragment {
    private static final String ARG_NUMBER = "arg_number";
    private static final String PRICE = "price";
    private static final String LOANID = "loanid";
    private static final String FROMLOAN = "fromloan";

    public SendItemTwo SM;
    private SharedPreferences preferences;
    private String username,password,Encode;
    private int pk;
    private Toolbar mToolbar;
    private TextView mTvName;
    private Button mBtnNext, mBtnNextWithFinish, mBtnback;
    private item_one itemOne;
    private three fragment;
    private Create_Load createLoad;
    private EditText mLoan_amount,mLoan_Term,mloan_RepaymentType,mLoan_Contributions,mNumber_institution,mMonthly_Amount_Paid;
    private TextView mLoan_amount_alert,mLoan_Term_alert,mloan_RepaymentType_alert,mLoan_Contributions_alert,mNumber_institution_alert,mMonthly_Amount_Paid_alert;
    private RadioButton radio1,radio2,radio3,radio4;
    private RadioGroup rgBuying_product_insurance,mAllowto_visit_home;
    private boolean radioCheck1 = false,radioCheck2 = false;
    private item_two itemTwo;
    private boolean mVisit_home,mBuy_product_insurance,check_return;
    private AlertDialog dialog;
    private int index=3,index1 = 3;
    private String basicEncode;
    private int mProductID,mLoanID;
    private String mPrice;
    private String[] values1 = {"monthly annuity repayment","monthly declining repayment"};
    private boolean mFromLoan,bLoand_amount,bLoan_Period,bPayment_Method,bLoan_Contributions,bNumber_institution,bMonthly_Amount_Paid;
    public static two newInstance(int number,String price,int loanid,boolean fromLoan) {
        two fragment = new two();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        args.putString(PRICE,price);
        args.putInt(LOANID,loanid);
        args.putBoolean(FROMLOAN,fromLoan);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create__load_two, container, false);
        return view;
    }
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mProductID = args.getInt(ARG_NUMBER);
            mPrice = args.getString(PRICE);
            mLoanID = args.getInt(LOANID);
            mFromLoan = args.getBoolean(FROMLOAN);
            Log.e("Fragment Two",""+mProductID+","+mPrice+","+mLoanID+","+mFromLoan);
        }
        createLoad = (Create_Load)getActivity();
        initView(view);
        checkEd();
        preferences= getContext().getSharedPreferences("Register",MODE_PRIVATE);
        username=preferences.getString("name","");
        password=preferences.getString("pass","");
        Encode = getEncodedString(username,password);
        basicEncode = "Basic "+Encode;
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }
        Log.d("Pk",""+ pk + basicEncode+"  user "+ username+"  pass  "+password);
//        fragment = new three();
        createLoad = (Create_Load)this.getActivity();
        mBtnNext = view.findViewById(R.id.btn_next);
        mBtnback = view.findViewById(R.id.btn_back);

        mBtnback.setOnClickListener(view1 -> createLoad.setBack(0));
        mBtnNext.setOnClickListener(view12 -> {

            createLoad.requstFocus(bLoand_amount,mLoan_amount,mLoan_amount_alert,"Invalid Loan Amount");
            createLoad.requstFocus(bLoan_Period,mLoan_Term,mLoan_Term_alert,"Invalid Loan Term");
            createLoad.requstFocus(bPayment_Method,mloan_RepaymentType,mloan_RepaymentType_alert,"Invalid Repayment Type");
            createLoad.requstFocus(bLoan_Contributions,mLoan_Contributions,mLoan_Contributions_alert,"Invalid Contributions");
            createLoad.requstFocus(bNumber_institution,mNumber_institution,mNumber_institution_alert,"Invalid Number Institutions");
            createLoad.requstFocus(bMonthly_Amount_Paid,mMonthly_Amount_Paid,mMonthly_Amount_Paid_alert,"Invalid Monthly Amount");

//            if (checkEd()){
            itemTwo = new item_two(Float.parseFloat(
                    mLoan_amount.getText().toString()),
                    mLoan_Term.getText().toString(),
                    values1[index],
                    Float.parseFloat(mLoan_Contributions.getText().toString()),
                    mBuy_product_insurance,mVisit_home,
                    mNumber_institution.getText().toString(),
                    mMonthly_Amount_Paid.getText().toString(),itemOne);
            SM.sendItemTwo(itemTwo,2);
//            }
        });
//        mLoan_amount.setText(cuteString(mPrice,0));
    }
    private void initView(View view) {

        Paper.init(getContext());
        String language = Paper.book().read("language");
        Log.e("90909090909","Current language is "+language);

        String[] values = getResources().getStringArray(R.array.repayment);

        mLoan_amount = view.findViewById(R.id.etLoan_amount);
        mLoan_Term = view.findViewById(R.id.etBorrowing_period);
        mLoan_Term.setFilters(new InputFilter[]{new InputFilterMinMax(1,36)});
        mloan_RepaymentType =view.findViewById(R.id.etPayment_Method);

        mLoan_amount_alert = view.findViewById(R.id.loan_amount_alert);
        mloan_RepaymentType_alert = view.findViewById(R.id.Payment_Method_alert);
        mLoan_Term_alert = view.findViewById(R.id.Borrowing_period_alert);
        mLoan_Contributions_alert = view.findViewById(R.id.Loan_contributions_alert);
        mNumber_institution_alert = view.findViewById(R.id.Number_debt_alert);
        mMonthly_Amount_Paid_alert = view.findViewById(R.id.monthly_payment_alert);


        mloan_RepaymentType.setOnClickListener(v -> {
           createLoad.AlertDialog(values,mloan_RepaymentType);
        });
        mloan_RepaymentType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (language.equals("km")||language.equals("en")){
                    for (int i=0;i<values.length;i++){
                        if (mloan_RepaymentType.getText().toString().equals(values[i])){
                            index = i;
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        mLoan_Contributions = view.findViewById(R.id.etLoan_contributions);

        if (mFromLoan){
            GetLoan();
        }else {
            mLoan_amount.setText(cuteString(mPrice,0));
            mLoan_amount.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(cuteString(mPrice,0)))});
            mLoan_Contributions.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(cuteString(mPrice,0)))});
            mLoan_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()&& s.toString().equals(mLoan_amount.getText().toString())){
                        mLoan_Contributions.setText("0");
                    }else mLoan_Contributions.setText(null);
                }
                @Override
                public void afterTextChanged(Editable s) { }
            });
        }

        mNumber_institution = view.findViewById(R.id.etNumber_debt);
        mMonthly_Amount_Paid = view.findViewById(R.id.et_monthly_payment);
//        mNumber_institution.setOnClickListener(v -> {
//            createLoad.AlertDialog(institution,mNumber_institution);
//        });
        mNumber_institution.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNumber_institution.getText().toString().equals("0")||mNumber_institution.getText().toString().equals("០")){
                    mMonthly_Amount_Paid.setText("0");
                    check_return = false;
                }else {
                    check_return = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
    }
    public int AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            index = which;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
        return index;
    }
    public String cuteString(String st, int indext){
        String[] separated = st.split("\\.");
        return separated[indext];
    }
    private boolean checkEd(){

        bLoand_amount = createLoad.CheckedYear(mLoan_amount);
        bLoan_Period = createLoad.CheckedYear(mLoan_Term);
        bPayment_Method = createLoad.Checked(mloan_RepaymentType);
        bLoan_Contributions = createLoad.CheckedYear(mLoan_Contributions);
        bNumber_institution = createLoad.CheckedYear(mNumber_institution);
        bMonthly_Amount_Paid = createLoad.CheckedYear(mMonthly_Amount_Paid);

        if (check_return){
            return bLoand_amount&&bLoan_Period&&bPayment_Method&&bLoan_Contributions&&radioCheck1&&radioCheck2&&bNumber_institution&&bMonthly_Amount_Paid;
        }else {
            return bLoand_amount&&bLoan_Period&&bPayment_Method&&bLoan_Contributions&&radioCheck1&&radioCheck2&&bNumber_institution;
        }
    }
    public class InputFilterMinMax implements InputFilter {
        private int min;
        private int max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //noinspection EmptyCatchBlock
            try {
                int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length()));
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
    private void GetLoan(){
        Service api = Client.getClient().create(Service.class);
        Call<loan_item> call = api.getDeailLoan(mLoanID,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (!response.isSuccessful()){
                    Log.e("ONRESPONSE ERROR", String.valueOf(response.code()));
                }

                mLoan_amount.setText(cuteString(String.valueOf(response.body().getLoan_amount()),0));
                mLoan_Term.setText(String.valueOf(response.body().getLoan_duration()));
                mLoan_Contributions.setText(String.valueOf(response.body().getLoan_deposit_amount()));
                mLoan_amount.setFilters(new InputFilter[]{new InputFilterMinMax(0, (int)response.body().getLoan_amount()+(int)response.body().getLoan_deposit_amount())});
                mLoan_amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().isEmpty()&& s.toString().equals(mLoan_amount.getText().toString())){
                            mLoan_Contributions.setText("0");
                        }else mLoan_Contributions.setText(null);
                    }
                    @Override
                    public void afterTextChanged(Editable s) { }
                });
                double price = response.body().getLoan_amount() + response.body().getLoan_deposit_amount();
                mLoan_amount.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(cuteString(String.valueOf(price),0)))});
                mLoan_Contributions.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(cuteString(String.valueOf(price),0)))});

                mLoan_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            mLoan_Contributions.setText(cuteString(String.valueOf(price - Double.parseDouble(mLoan_amount.getText().toString())),0));
                        }
                    }
                });

                mLoan_Contributions.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (!s.toString().isEmpty()&&!s.toString().equals("0")){
                            mLoan_Contributions.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
                            mLoan_Contributions.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(cuteString(String.valueOf(price),0)))});
                            mLoan_amount.setText(cuteString(String.valueOf(price - Double.parseDouble(s.toString())),0));
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().equals("0") || s.toString().equals("0.0")){
                            mLoan_Contributions.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                        }
                    }
                });
                mloan_RepaymentType.setText(response.body().getLoan_repayment_type());
                String repay = response.body().getLoan_repayment_type();
                if (repay.equals("monthly annuity repayment")){
                    mloan_RepaymentType.setText(getString(R.string.monthly_annuity_repay));
                }else mloan_RepaymentType.setText(getString(R.string.monlthly_declining_repay));
                mLoan_Contributions.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().isEmpty()&&!mLoan_amount.getText().toString().isEmpty()){
                            mLoan_Contributions.setFilters(new InputFilter[]{new InputFilterMinMax(0,(int)response.body().getLoan_amount()+(int)response.body().getLoan_deposit_amount()-Integer.parseInt(mLoan_amount.getText().toString()))});
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) { }
                });
                mNumber_institution.setText(String.valueOf(response.body().getLending_intitution_owned()));
                if (!(response.body().getLending_intitution_owned()==0)){
                    mMonthly_Amount_Paid.setText(String.valueOf(response.body().getAmount_paid_intitution()));
                }
            }
            @Override
            public void onFailure(Call<loan_item> call, Throwable t) { }
        });
    }
    public void getItemOne(item_one item)
    {
        Log.e("item_one",""+item);
        itemOne = item;
    }
    public interface SendItemTwo {

        void sendItemTwo(item_two item_two,int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendItemTwo) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}
