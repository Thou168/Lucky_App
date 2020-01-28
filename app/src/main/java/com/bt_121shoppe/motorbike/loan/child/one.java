package com.bt_121shoppe.motorbike.loan.child;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.loan.child.two;
import com.bt_121shoppe.motorbike.loan.LoanSectionOneFragment;
import com.bt_121shoppe.motorbike.loan.model.Province;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.loan.model.province_Item;

import java.util.*;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;

public class one extends Fragment{
    private static final String ARG_NUMBER = "arg_number";
    private static final String PRICE = "price";
    private static final String LOANID = "loanid";
    private static final String FROMLOAN = "fromloan";
    private final Handler handler = new Handler();

    public SendItemOne SM;
    private Button mBtnNext;
    private LinearLayout relative_conspirator;
    private RadioButton radio1,radio2,radio3;
    private RadioGroup mCo_borrower;
    private EditText mName,mPhone_Number,mAddress,mJob,mRelationship,mCo_borrower_Job,mTotal_Income,mTotal_Expense,mNet_Income;
    private EditText mJob_Period,mCo_Job_Period,mDistrict,mCommune,mVillage;
    private TextView mName_alert,mPhone_alert,mAddress_alert,mRelationship_alert,Income_alert,Expense_alert,Netincome_alert;
    private TextView mJob_Period_alert,mCo_Job_alert,District_alert,mCommune_alert,mVillage_alert,mJob_alert,mCo_Job_Period_alert;
    private int mProductID;
    private String mPrice;
    private Create_Load createLoad;
    private item_one itemOne;
    private int index=3,indextJom,indexRela,indexCoborow_job,mProvinceID,mLoanID;
    boolean radioCheck = false,Co_borrower,mFromLoan;

    private SharedPreferences preferences;
    private String username,password,Encode;
    private int pk;
    private loan_item loanItem;
    private String basicEncode,currentLanguage;
    private List<province_Item> listData;
    private String[] provine = new String[28];
    private AlertDialog dialog;
    private String[] Job = {"seller","state staff","private company staff","service provider","other",""};
    private String[] Rela = {"husband", "wife", "father", "mother", "son","daugther","brother","sister","other",""};
    private String[] rJob ;
    private String[] rRela;
    private boolean bname,bphone,baddress,bJob,bJob_Period,bRelationship,bco_Relationship,bCo_borrower_Job,bCo_Job_Period,bTotal_Income,bmTotal_Expense;
    private boolean bNet_income,bDistrict,bCommune,bVillage;

    public static one newInstance(int number,String price,int loanid,boolean fromLoan) {
        one fragment = new one();
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
        View view = inflater.inflate(R.layout.activity_create__load_one, container, false);
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
            Log.e("Item",""+mProductID+","+mPrice+","+mLoanID+","+mFromLoan);
        }
        createLoad = (Create_Load)getActivity();
        rJob = getResources().getStringArray(R.array.job);
        rRela = getResources().getStringArray(R.array.relationship);
        initView(view);

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

        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");

        if (mFromLoan){
            GetLoan();
        }else {
            getDetailUser();
            mTotal_Income.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {
                        mTotal_Income.setFilters(new InputFilter[]{new InputFilterMinMax(0, 50000)});
                        if (mTotal_Expense.getText().toString().isEmpty()) {
                            mNet_Income.setText(s.toString());
                        } else {
                            if (Double.parseDouble(s.toString())<Double.parseDouble(mTotal_Expense.getText().toString())){
                                mTotal_Expense.setText(null);
                            }else {
                                mNet_Income.setText(Double.parseDouble(mTotal_Income.getText().toString()) - Double.parseDouble(mTotal_Expense.getText().toString()) + "");
                            }
                        }
                    } else {
                        mNet_Income.setText(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mTotal_Expense.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty() && !mTotal_Income.getText().toString().isEmpty()) {
                        mTotal_Expense.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(mTotal_Income.getText().toString()))});
                        mNet_Income.setText(Double.parseDouble(mTotal_Income.getText().toString()) - Double.parseDouble(s.toString()) + "");
                    } else {
                        if (!mTotal_Income.getText().toString().isEmpty())
                            mNet_Income.setText(Double.parseDouble(mTotal_Income.getText().toString()) + "");
                        else
                            mTotal_Expense.setFilters(new InputFilter[]{new InputFilterMinMax(0, 50000)});
                    }

                    int incom = 0;
                    if (s.length() == 0||mTotal_Income.getText().length() == 0){
                        s = "0";
                        mTotal_Income.setText("0");
                    }
                    incom = Integer.parseInt(mTotal_Income.getText().toString());
                    int borrow = Integer.parseInt(s.toString());
                    mNet_Income.setText(""+(incom-borrow));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        getprovince();
    }
    public String method(String str) {
        for (int i=0;i<str.length();i++){
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }
    private void getDetailUser(){
        Service api = Client.getClient().create(Service.class);
        Call<User_Detail> call = api.getDetailUser(pk,basicEncode);
        call.enqueue(new Callback<User_Detail>() {
            @Override
            public void onResponse(Call<User_Detail> call, Response<User_Detail> response) {
                if (!response.body().getFirst_name().isEmpty()){
                    mName.setText(response.body().getFirst_name());
                }
                String stphone = response.body().getProfile().getTelephone();

                mPhone_Number.setText(method(stphone));
//               mPhone_Number.setText(response.body().getProfile().getTelephone());
            }

            @Override
            public void onFailure(Call<User_Detail> call, Throwable t) {

            }
        });
    }
    private void getprovince(){
        Service api = Client.getClient().create(Service.class);
        Call<AllResponse> call = api.getProvince();
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("Error121211",response.code()+" ");
                }
                listData = response.body().getresults();
                Log.d("333333333333", String.valueOf(listData.size()));
                for (int i=0;i<listData.size();i++){
                    if (currentLanguage.equals("en")){
                        provine[i] = listData.get(i).getProvince();
                        Log.d("Province",listData.get(i).getProvince()+listData.get(i).getId());
                        Log.e("Pk",""+ pk + Encode+" user "+ username+"  pass  "+password+ " List " +listData.size());
                    }else {
                        Log.d("Province",listData.get(i).getProvince()+listData.get(i).getId());
                        provine[i] = listData.get(i).getProvince_kh();
                    }
                }
            }
            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void initView(View view) {

        Paper.init(getContext());
        String language = Paper.book().read("language");
        Log.e("90909090909","Current language is "+language);

        mName            = (EditText) view.findViewById(R.id.etName);
        mPhone_Number    = (EditText) view.findViewById(R.id.etPhone);
        mDistrict        = (EditText) view.findViewById(R.id.edDistrict);
        mCommune         = (EditText) view.findViewById(R.id.edCommune);
        mVillage         = (EditText) view.findViewById(R.id.edvillage);
        mAddress         = (EditText) view.findViewById(R.id.et_province);
        mJob             = (EditText) view.findViewById(R.id.et_Personal_Occupation);
        mJob_Period      = (EditText) view.findViewById(R.id.etTime_Practicing);
        mRelationship    = (EditText) view.findViewById(R.id.et_conspirator);
        mCo_borrower_Job = (EditText) view.findViewById(R.id.et_Contributors);
        mCo_Job_Period   = (EditText) view.findViewById(R.id.etTime_Practicing1);
        mTotal_Income    = (EditText) view.findViewById(R.id.etTotal_income_borrowers);
        mTotal_Expense   = (EditText) view.findViewById(R.id.etTotal_cost_borrowers);
        mNet_Income      = (EditText) view.findViewById(R.id.et_total);

        relative_conspirator = (LinearLayout) view.findViewById(R.id.relative_conspirator);

        mCo_borrower  = (RadioGroup)  view.findViewById(R.id.radio_group);
        radio1        = (RadioButton) view.findViewById(R.id.radio1);
        radio2        = (RadioButton) view.findViewById(R.id.radio2);

        mBtnNext         = (Button) view.findViewById(R.id.btn_next);

        mName_alert              = (TextView) view.findViewById(R.id.name_alert);
        mPhone_alert             = (TextView) view.findViewById(R.id.phone_alert);
        mAddress_alert           = (TextView) view.findViewById(R.id.province_alert);
        mJob_Period_alert        = (TextView) view.findViewById(R.id.Time_Practicing_alert);
        mCommune_alert           = (TextView) view.findViewById(R.id.commune_alert);
        mVillage_alert           = (TextView) view.findViewById(R.id.village_alert);
        Income_alert             = (TextView) view.findViewById(R.id.total_income_alert);
        Expense_alert            = (TextView) view.findViewById(R.id.expense_alert);
        Netincome_alert          = (TextView) view.findViewById(R.id.income_alert);
        District_alert           = (TextView) view.findViewById(R.id.district_alert);
        mRelationship_alert      = (TextView) view.findViewById(R.id.conspirator_alert);
        mCo_Job_alert            = (TextView) view.findViewById(R.id.Contributors_alert);
        mJob_alert               = (TextView) view.findViewById(R.id.Personal_Occupation_alert);
        mCo_Job_Period_alert     = (TextView) view.findViewById(R.id.Practicing1_alert);

        editext();

        mCo_borrower.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = mCo_borrower.findViewById(checkedId);
            index = mCo_borrower.indexOfChild(radioButton);
            radio3 = mCo_borrower.findViewById(checkedId);
            // dd logic here
            switch (index) {
                case 0: // first button
                    relative_conspirator.setVisibility(View.VISIBLE);
                    radioCheck = true;
                    Co_borrower = true;
                    break;
                case 1: // secondbutton
                    relative_conspirator.setVisibility(View.GONE);

                    mRelationship.setText(null);
                    mCo_borrower_Job.setText(null);
                    mCo_Job_Period.setText("0");
                    indexRela = 9;
                    indexCoborow_job = 5;
                    radioCheck = true;
                    Co_borrower = false;
                    break;
            }
            Log.e("Index",""+index);
        });
        mJob.setOnClickListener(v -> {
            createLoad.AlertDialog(rJob,mJob);
        });
        mRelationship.setOnClickListener(v -> {
            createLoad.AlertDialog(rRela,mRelationship);
        });
        mCo_borrower_Job.setOnClickListener(v -> {
            createLoad.AlertDialog(rJob,mCo_borrower_Job);
        });
        mAddress.setOnClickListener(v -> AlertDialog(provine,mAddress));
        mDistrict.setOnClickListener(v -> AlertDialog(provine,mDistrict));
        mVillage.setOnClickListener(v -> AlertDialog(provine,mVillage));
        mCommune.setOnClickListener(v -> AlertDialog(provine,mCommune));
        mJob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i=0;i<rJob.length;i++){
                    if (mJob.getText().toString().equals(rJob[i])){
                        indextJom = i;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        mRelationship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i=0;i<rRela.length;i++){
                    if (s.toString().toLowerCase().equals(rRela[i].toLowerCase())){
                        indexRela = i;
                        Log.d("1212121255555",Rela[indexRela]+"indext"+indexRela);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        mCo_borrower_Job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i=0;i<rJob.length;i++){
                    if (s.toString().toLowerCase().equals(rJob[i].toLowerCase())){
                        indexCoborow_job = i;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        mBtnNext.setOnClickListener(view3 -> {
//            boolean bCo_borrower = createLoad.RadioCondition(mCo_borrower);
            Log.d("343434343",String.valueOf(editext())+"    "+index);
            if (index == 0){
                createLoad.requstFocus(bco_Relationship,mRelationship,mRelationship_alert,"Invalid Relationship");
                createLoad.requstFocus(bCo_borrower_Job,mCo_borrower_Job,mCo_Job_alert,"Invalid Co-borrower Job");
                createLoad.requstFocus(bCo_Job_Period,mCo_Job_Period,mCo_Job_Period_alert,"Invalid Co-borrower Job Period");
            }
            createLoad.requstFocus(bname,mName,mName_alert,"Invalid Name");
            createLoad.requstFocus(baddress,mAddress,mAddress_alert,"Invalid Address");
            createLoad.requstFocus(bJob,mJob,mJob_alert,"Invalid Job");
            createLoad.requstFocus(bJob_Period,mJob_Period,mJob_Period_alert,"Invalid Job Period");
            createLoad.requstFocus(bTotal_Income,mTotal_Income,Income_alert,"Invalid Income");
            createLoad.requstFocus(bmTotal_Expense,mTotal_Expense,Expense_alert,"Invalid Expense");
            createLoad.requstFocus(bNet_income,mNet_Income,Netincome_alert,"Invalid NetIncome");
            createLoad.requstFocus(bDistrict,mDistrict,District_alert,"Invalid District");
            createLoad.requstFocus(bCommune,mCommune,mCommune_alert,"Invalid Commune");
            createLoad.requstFocus(bVillage,mVillage,mVillage_alert,"Invalid Village");
            if (editext()){
                itemOne = new item_one(
                        mName.getText().toString(),
                        mPhone_Number.getText().toString(),
                        mAddress.getText().toString(),
                        mDistrict.getText().toString(),
                        mCommune.getText().toString(),
                        mVillage.getText().toString(),
                        Job[indextJom],
                        Co_borrower,index,Rela[indexRela],Job[indexCoborow_job],
                        Float.parseFloat(mTotal_Income.getText().toString()),
                        Float.parseFloat(mTotal_Expense.getText().toString()),
                        mNet_Income.getText().toString(),
                        Integer.parseInt(mJob_Period.getText().toString()),
                        Integer.parseInt(mCo_Job_Period.getText().toString()),
                        mProductID,mProvinceID,mPrice,
                        mLoanID,mFromLoan);
                    SM.sendItemOne(itemOne,1);
            }
        });

    }

    private boolean editext(){
        bname = createLoad.Checked(mName);
        bphone = createLoad.Checked(mPhone_Number);
        baddress = createLoad.Checked(mAddress);
        bJob = createLoad.Checked(mJob);
        bJob_Period = createLoad.CheckedYear(mJob_Period);
        bRelationship = createLoad.Checked(mRelationship);
        bCo_borrower_Job = createLoad.CheckedYear(mCo_borrower_Job);
        bCo_Job_Period = createLoad.CheckedYear(mCo_Job_Period);
//        bco_Relationship = createLoad.RadioCondition(mCo_borrower);
        bTotal_Income = createLoad.CheckedYear(mTotal_Income);
        bmTotal_Expense = createLoad.CheckedYear(mTotal_Expense);
        bNet_income = createLoad.CheckedYear(mNet_Income);
        bCommune = createLoad.CheckedYear(mCommune);
        bDistrict = createLoad.CheckedYear(mDistrict);
        bVillage = createLoad.CheckedYear(mVillage);
        if (index == 0){
            return bname&&bphone&&baddress&&bJob&&bJob_Period&&radioCheck&&bTotal_Income&&bmTotal_Expense&&bRelationship&&bCo_borrower_Job;
        }else {
            return bname&&bphone&&baddress&&bJob&&bJob_Period&&radioCheck&&bTotal_Income&&bmTotal_Expense;
        }
    }
    public void AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0;
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            mProvinceID = which+1;
            editText.setText(items[which]);
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
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
                    Log.d("5555555555555555",response.code()+"");
                }
//                mName.setText(response.body());
                mName.setText(response.body().getUsername());
                mPhone_Number.setText(response.body().getTelephone());
                mDistrict.setText(response.body().getDistrmict());
                mCommune.setText(response.body().getCommune());
                mVillage.setText(response.body().getVillage());
                for (int i=0;i<Job.length;i++){
                    if (response.body().getJob().equals(Job[i])){
                        mJob.setText(rJob[i]);
                        indextJom = i;
                    }
                }
                mJob_Period.setText(String.valueOf(response.body().getBorrower_job_period()));
                if (response.body().ismIs_Co_borrower()){
                    mCo_borrower.check(R.id.radio1);
                    radio1.toggle();
                    for (int i=0;i<rRela.length;i++){
                        if (response.body().getmRelationship().equals(Rela[i].toLowerCase())) {
                            mRelationship.setText(rRela[i]);
                            indexRela = i;
                        }
                    }
                    for (int i=0;i<rJob.length;i++){
                        if (response.body().getmCoborrower_job().equals(Job[i].toLowerCase())){
                            mCo_borrower_Job.setText(rJob[i]);
                            indexCoborow_job = i;
                        }
                    }
                    mCo_Job_Period.setText(String.valueOf(response.body().getmCoborrower_job_period()));
                }else {
                    mCo_borrower.check(R.id.radio2);
                    radio2.toggle();
                    mRelationship.setText(null);
                    mCo_Job_Period.setText("0");
                }

                mTotal_Income.setText(String.valueOf(response.body().getAverage_income()));
                mTotal_Expense.setText(String.valueOf(response.body().getAverage_expense()));
                mNet_Income.setText(response.body().getAverage_income()- response.body().getAverage_expense()+"");
                mProvinceID = response.body().getProvince_id();
                Call<Province> call1 = api.getProvince(response.body().getProvince_id());
                call1.enqueue(new Callback<Province>() {
                    @Override
                    public void onResponse(Call<Province> call, Response<Province> response) {
                        if (!response.isSuccessful()){
                            Log.e("ONRESPONSE Province", String.valueOf(response.code()));
                        }
                        if (currentLanguage.equals("en"))
                        mAddress.setText(response.body().getProvince());
                        else  mAddress.setText(response.body().getProvince_kh());
                    }
                    @Override
                    public void onFailure(Call<Province> call, Throwable t) { }
                });

            }

            @Override
            public void onFailure(Call<loan_item> call, Throwable t) {

            }
        });
    }
    public interface SendItemOne {
        void sendItemOne(item_one item_one,int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendItemOne) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}
