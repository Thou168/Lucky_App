package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
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


public class two extends Fragment {
    private static final String ARG_NUMBER = "arg_number";

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
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;
    private RadioButton radio1,radio2,radio3,radio4;
    private RadioGroup rgBuying_product_insurance,mAllowto_visit_home;
    private boolean radioCheck1 = false,radioCheck2 = false;
    private item_two itemTwo;
    private boolean mVisit_home,mBuy_product_insurance,check_return;
    AlertDialog dialog;
    RelativeLayout relati_Contributors;
    private int index;
    String basicEncode;
    String[] values1 = {"monthly annuity repayment","monthly declining repayment"};

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

        mBtnback.setOnClickListener(view1 -> createLoad.setBack());
        mBtnNext.setOnClickListener(view12 -> {
//            Bundle bundle=new Bundle();
//            fragment.setArguments(bundle);
            if (checkEd()){
                itemTwo = new item_two(Float.parseFloat(mLoan_amount.getText().toString()),mLoan_Term.getText().toString(),values1[index],Float.parseFloat(mLoan_Contributions.getText().toString()),
                        mBuy_product_insurance,mVisit_home,mNumber_institution.getText().toString(),mMonthly_Amount_Paid.getText().toString(),itemOne);
                fragment = three.newInstance(itemTwo);
                createLoad.loadFragment(fragment);
            }
        });
//        mLoan_amount.setText(cuteString(itemOne.getPrice(),0));
        return view;
    }
    private void initView(View view) {

        Paper.init(getContext());
        String language = Paper.book().read("language");
        Log.e("90909090909","Current language is "+language);

        String[] values = getResources().getStringArray(R.array.repayment);
        String[] institution = getResources().getStringArray(R.array.institute);

        mLoan_amount = view.findViewById(R.id.etLoan_amount);
        mLoan_Term = view.findViewById(R.id.etBorrowing_period);
        mloan_RepaymentType =view.findViewById(R.id.etPayment_Method);
        relati_Contributors = view.findViewById(R.id.relati_Contributors);

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

        if (itemOne.isFromLoan()){
            GetLoan();

//            mLoan_Contributions.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(cuteString(itemOne.getPrice(),0)))});
        }else {
            img1.setImageResource(R.drawable.ic_check_circle_black_24dp);
            mLoan_amount.setText(cuteString(itemOne.getPrice(),0));
            mLoan_Contributions.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(cuteString(itemOne.getPrice(),0)))});
        }

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
        mMonthly_Amount_Paid = view.findViewById(R.id.et_monthly_payment);
        mNumber_institution.setOnClickListener(v -> {
            createLoad.AlertDialog(institution,mNumber_institution);
        });
        mNumber_institution.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNumber_institution.getText().toString().equals("0")||mNumber_institution.getText().toString().equals("០")){
                    relati_Contributors.setVisibility(View.GONE);
                    mMonthly_Amount_Paid.setText("0");
                    check_return = false;
                }else {
                    relati_Contributors.setVisibility(View.VISIBLE);
                    check_return = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
        radio3 = view.findViewById(R.id.radio3);
        radio4 = view.findViewById(R.id.radio4);
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
        Call<loan_item> call = api.getDeailLoan(itemOne.getmLoanID(),basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (!response.isSuccessful()){
                    Log.e("ONRESPONSE ERROR", String.valueOf(response.code()));
                }
                mLoan_amount.setText(String.valueOf(response.body().getLoan_amount()));
                mLoan_Term.setText(String.valueOf(response.body().getLoan_duration()));
//                mloan_RepaymentType.setText(response.body().getLoan_repayment_type());
                String repay = response.body().getLoan_repayment_type();
                if (repay.equals("monthly annuity repayment")){
                    mloan_RepaymentType.setText(getString(R.string.monthly_annuity_repay));
                }else mloan_RepaymentType.setText(getString(R.string.monlthly_declining_repay));
                mLoan_Contributions.setText(String.valueOf(response.body().getLoan_deposit_amount()));
                if (response.body().isIs_product_insurance()){
                    rgBuying_product_insurance.check(R.id.radial);
                    radio1.toggle();
                }else {
                    rgBuying_product_insurance.check(R.id.radio2);
                    radio2.toggle();
                }
                if (response.body().isIs_home_visit()){
                    radio3.toggle();
                    mAllowto_visit_home.check(R.id.radio3);
                }else {
                    radio4.toggle();
                    mAllowto_visit_home.check(R.id.radio4);
                }
                mNumber_institution.setText(String.valueOf(response.body().getLending_intitution_owned()));
                if (!(response.body().getLending_intitution_owned()==0)){
                    mMonthly_Amount_Paid.setText(String.valueOf(response.body().getAmount_paid_intitution()));
                }
            }
            @Override
            public void onFailure(Call<loan_item> call, Throwable t) { }
        });
    }
}
