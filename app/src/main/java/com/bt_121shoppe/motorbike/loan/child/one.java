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
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_list_draft;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.loan.child.two;
import com.bt_121shoppe.motorbike.loan.LoanSectionOneFragment;
import com.bt_121shoppe.motorbike.loan.model.Draft;
import com.bt_121shoppe.motorbike.loan.model.Province;
import com.bt_121shoppe.motorbike.loan.model.District;
import com.bt_121shoppe.motorbike.loan.model.Commune;
import com.bt_121shoppe.motorbike.loan.model.Village;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.loan.model.draft_Item;
import com.bt_121shoppe.motorbike.loan.model.province_Item;
import com.bt_121shoppe.motorbike.loan.model.district_Item;
import com.bt_121shoppe.motorbike.loan.model.commune_Item;
import com.bt_121shoppe.motorbike.loan.model.village_Item;
import com.google.android.gms.common.util.ArrayUtils;

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
    private static final String DRAFT = "draft";

    public  SendItemOne SM;
    private Button mBtnNext;
    private LinearLayout relative_conspirator;
    private RadioButton radio1,radio2,radio3;
    private RadioGroup mCo_borrower;
    private EditText mName,mPhone_Number,mAddress,mJob,mRelationship,mCo_borrower_Job,mTotal_Income,mTotal_Expense,mNet_Income;
    private EditText mJob_Period,mCo_Job_Period,mDistrict,mCommune,mVillage;
    private TextView mName_alert,mPhone_alert,mAddress_alert,mRelationship_alert,Income_alert,Expense_alert,Netincome_alert;
    private TextView mJob_Period_alert,mCo_Job_alert,District_alert,mCommune_alert,mVillage_alert,mJob_alert,mCo_Job_Period_alert;
    private Create_Load createLoad;
    private AlertDialog dialog;
    private item_one itemOne;
    private List<draft_Item> list_darft;
    private List<province_Item> listData;
    private List<district_Item> listDistrict;
    private List<commune_Item> list_Commmune;
    private List<village_Item> list_village;

    private SharedPreferences preferences;
    private String username,password,Encode;
    private String basicEncode,currentLanguage;
    private String mPrice,mDraft;
    private String[] provine,district,commune,village;
    private String[] Job = {"seller","state staff","private company staff","service provider","other",""};
    private String[] Rela = {"husband", "wife", "father", "mother", "son","daugther","brother","sister","other",""};
    private String[] rJob ;
    private String[] rRela;
    private int [] provinceId,districtId,communeId,villageId;
    private int index=3,indextJom,indexRela,indexCoborow_job,mProductID,mLoanID;
    private int mProvinceID,mDistrictID,mCommuneID,mVillageID;
    private int pk;
    private boolean radioCheck = false,Co_borrower,mFromLoan;
    private boolean bname,bphone,baddress,bJob,bJob_Period,bRelationship,bco_Relationship,bCo_borrower_Job,bCo_Job_Period,bTotal_Income,bmTotal_Expense;
    private boolean bNet_income,bDistrict,bCommune,bVillage;

    public static one newInstance(int number,String price,int loanid,boolean fromLoan,String Draft) {
        one fragment = new one();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        args.putString(PRICE,price);
        args.putInt(LOANID,loanid);
        args.putBoolean(FROMLOAN,fromLoan);
        args.putString(DRAFT,Draft);
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
            mDraft = args.getString(DRAFT);
            Log.e("Item",""+mProductID+","+mPrice+","+mLoanID+","+mFromLoan);
        }
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
        createLoad = (Create_Load)getActivity();
        rJob = getResources().getStringArray(R.array.job);
        rRela = getResources().getStringArray(R.array.relationship);
        initView(view);

        Log.d("Pk",""+ pk + basicEncode+"  user "+ username+"  pass  "+password);

        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");
        getprovince();
        if (mFromLoan){
            GetLoan();
        }else if (mDraft!= null){
            getLoan_draft();
        } else {
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
                if (response.body().getresults() != null) {
                    listData = response.body().getresults();
                    provine = new String[listData.size()];
                    provinceId=new int[listData.size()];
//                    Log.d("333333333333", String.valueOf(listData.size()));
                    for (int i = 0; i < listData.size(); i++) {
                        provinceId[i]=(int)listData.get(i).getId();
                        if (currentLanguage.equals("en")) {
                            provine[i] = listData.get(i).getProvince();
                            Log.d("Province", listData.get(i).getProvince() + listData.get(i).getId());
                        } else {
                            Log.d("Province", listData.get(i).getProvince_kh() + listData.get(i).getId());
                            provine[i] = listData.get(i).getProvince_kh();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }
    private void getdistrict(int provinceID){
        Log.e("Province ID",""+provinceID);
        Service api = Client.getClient().create(Service.class);
        Call<District> call = api.getDistrict();
        call.enqueue(new Callback<District>() {
            @Override
            public void onResponse(Call<District> call, Response<District> response) {
                if (!response.isSuccessful()){
                    Log.d("Error121211",response.code()+" ");
                }
                if (response.body().getresults() != null) {
                    listDistrict = response.body().getresults();
                    int count=0,ccount=0;
                    for (int i = 0; i < listDistrict.size(); i++) {
                        if (listDistrict.get(i).getProvinceId() == provinceID) {
                            count++;
                        }
                    }
                    district = new String[count];
                    districtId=new int[count];
                    for (int i = 0; i < listDistrict.size(); i++) {
                        if (listDistrict.get(i).getProvinceId() == provinceID) {
                            districtId[ccount]=(int)listDistrict.get(i).getId();
                            if (currentLanguage.equals("en")) {
                                district[ccount] = listDistrict.get(i).getDistrict();
                            } else {
                                district[ccount] = listDistrict.get(i).getDistrict_kh();
                            }
                            districtId[ccount] = (int) listDistrict.get(i).getId();
                            ccount++;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<District> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }
    private void getCommune(int districtID){
        Log.e("District ID",""+districtID);
        Service api = Client.getClient().create(Service.class);
        Call<Commune> call = api.getCommune();
        call.enqueue(new Callback<Commune>() {
            @Override
            public void onResponse(Call<Commune> call, Response<Commune> response) {
                if (!response.isSuccessful()){
                    Log.d("Error121211",response.code()+" ");
                }
                if (response.body().getresults() != null) {
                    list_Commmune = response.body().getresults();
                    int count=0,ccount=0;
                    for (int i = 0; i < list_Commmune.size(); i++) {
                        if (list_Commmune.get(i).getDistrictId() == districtID) {
                            count++;
                        }
                    }
                    commune = new String[count];
                    communeId=new int[count];
                    for (int i = 0; i < list_Commmune.size(); i++) {
                        if (list_Commmune.get(i).getDistrictId() == districtID) {
                            communeId[ccount]=(int)list_Commmune.get(i).getId();
                            if (currentLanguage.equals("en")) {
                                commune[ccount] = list_Commmune.get(i).getCommune();
                                Log.d("Commune", list_Commmune.get(i).getCommune() + list_Commmune.get(i).getId());
                            } else {
                                Log.d("Commune", list_Commmune.get(i).getCommune_kh() + list_Commmune.get(i).getId());
                                commune[ccount] = list_Commmune.get(i).getCommune_kh();
                            }
                            communeId[ccount] = (int) list_Commmune.get(i).getId();
                            ccount++;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Commune> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }
    private void getVillage(int communeID){
        Log.e("Commune ID",""+communeID);
        Service api = Client.getClient().create(Service.class);
        Call<Village> call = api.getVillage();
        call.enqueue(new Callback<Village>() {
            @Override
            public void onResponse(Call<Village> call, Response<Village> response) {
                if (!response.isSuccessful()){
                    Log.d("Error121211",response.code()+" ");
                }
                if (response.body().getresults() != null) {
                    list_village = response.body().getresults();
                    int count=0,ccount=0;
                    for (int i = 0; i < list_village.size(); i++) {
                        if (list_village.get(i).getCommuneId() == communeID) {
                            count++;
                        }
                    }
                    village = new String[count];
                    villageId=new int[count];
                    for (int i = 0; i < list_village.size(); i++) {
                        if (list_village.get(i).getCommuneId() == communeID) {
                            villageId[ccount]=(int)list_village.get(i).getId();
                            if (currentLanguage.equals("en")) {
                                village[ccount] = list_village.get(i).getVillage();
                                Log.d("Village", list_village.get(i).getVillage() + list_village.get(i).getId());
                            } else {
                                Log.d("Village", list_village.get(i).getVillage_kh() + list_village.get(i).getId());
                                village[ccount] = list_village.get(i).getVillage_kh();
                            }
                            ccount++;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Village> call, Throwable t) {
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

        mBtnNext      = (Button) view.findViewById(R.id.btn_next);

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
        mAddress.setOnClickListener(v -> {
            AlertDialog(provine, mAddress);
        });
        mDistrict.setOnClickListener(v -> {
            AlertDialog_District(district,mDistrict);
        });
        mVillage.setOnClickListener(v -> {
            AlertDialog_village(village,mVillage);
        });
        mCommune.setOnClickListener(v -> {
            AlertDialog_Commune(commune,mCommune);
        });
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
            editext();
            if (index == 0){
                createLoad.requstFocus(bco_Relationship,mRelationship,mRelationship_alert,getString(R.string.invalid_relationship));
                createLoad.requstFocus(bCo_borrower_Job,mCo_borrower_Job,mCo_Job_alert,getString(R.string.invalid_co_borrower_job));
                createLoad.requstFocus(bCo_Job_Period,mCo_Job_Period,mCo_Job_Period_alert,getString(R.string.invalid_co_borrower_job_period));
            }
            createLoad.requstFocus(bname,mName,mName_alert,getString(R.string.invalid_name));
            createLoad.requstFocus(baddress,mAddress,mAddress_alert,getString(R.string.invalid_province));
            createLoad.requstFocus(bJob,mJob,mJob_alert,getString(R.string.invalid_job));
            createLoad.requstFocus(bJob_Period,mJob_Period,mJob_Period_alert,getString(R.string.invalid_job_period));
            createLoad.requstFocus(bTotal_Income,mTotal_Income,Income_alert,getString(R.string.invalid_income));
            createLoad.requstFocus(bmTotal_Expense,mTotal_Expense,Expense_alert,getString(R.string.invalid_expense));
            createLoad.requstFocus(bNet_income,mNet_Income,Netincome_alert,getString(R.string.invalid_net_income));
            createLoad.requstFocus(bDistrict,mDistrict,District_alert,getString(R.string.invalid_district));
            createLoad.requstFocus(bCommune,mCommune,mCommune_alert,getString(R.string.invalid_commune));
            createLoad.requstFocus(bVillage,mVillage,mVillage_alert,getString(R.string.invalid_village));
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
            //mProvinceID = which+1;

            mProvinceID=provinceId[which];
            Log.e("TAG","Selected Province id "+which+" "+mProvinceID);
            getdistrict(mProvinceID);
            editText.setText(items[which]);
            mDistrict.setText("");
            mCommune.setText("");
            mVillage.setText("");
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog_District(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0;
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            //mDistrictID = which+1;
            mDistrictID=districtId[which];
           getCommune(mDistrictID);
            editText.setText(items[which]);
            mCommune.setText("");
            mVillage.setText("");
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog_Commune(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0;
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            //mCommuneID = which+1;
            mCommuneID=communeId[which];
            getVillage(mCommuneID);
            editText.setText(items[which]);
            mVillage.setText("");
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog_village(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0;
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            //mVillageID = which+1;
            mVillageID=villageId[which];
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
    private void getLoan_draft(){
        Service apiService = Client.getClient().create(Service.class);
        Call<Draft> call = apiService.getList_draft(basicEncode);
        call.enqueue(new Callback<Draft>() {
            @Override
            public void onResponse(Call<Draft> call, Response<Draft> response) {
                list_darft = response.body().getresults();
                Log.e("list 1",""+list_darft.size());
                if (list_darft.size() >0){
                    for (int i=0;i<list_darft.size();i++){
                        mName.setText(list_darft.get(i).getUsername());
                        mPhone_Number.setText(list_darft.get(i).getTelephone());
                        mDistrict.setText(list_darft.get(i).getDistrmict());
                        mCommune.setText(list_darft.get(i).getCommune());
                        mVillage.setText(list_darft.get(i).getVillage());
                        for (int j=0;j<Job.length;j++){
                            if (list_darft.get(i).getJob().equals(Job[j])){
                                mJob.setText(rJob[j]);
                                indextJom = j;
                            }
                        }
                        mJob_Period.setText(String.valueOf(list_darft.get(i).getBorrower_job_period()));
                        if (list_darft.get(i).ismIs_Co_borrower()){
                            mCo_borrower.check(R.id.radio1);
                            radio1.toggle();
                            for (int j=0;j<rRela.length;j++){
                                if (list_darft.get(i).getmRelationship().equals(Rela[j].toLowerCase())) {
                                    mRelationship.setText(rRela[j]);
                                    indexRela = j;
                                }
                            }
                            for (int j=0;j<rJob.length;j++){
                                if (list_darft.get(i).getmCoborrower_job().equals(Job[j].toLowerCase())){
                                    mCo_borrower_Job.setText(rJob[j]);
                                    indexCoborow_job = j;
                                }
                            }
                            mCo_Job_Period.setText(String.valueOf(list_darft.get(i).getmCoborrower_job_period()));
                        }else {
                            mCo_borrower.check(R.id.radio2);
                            radio2.toggle();
                            mRelationship.setText(null);
                            mCo_Job_Period.setText("0");
                        }
                        mTotal_Income.setText(String.valueOf(list_darft.get(i).getAverage_income()));
                        mTotal_Expense.setText(String.valueOf(list_darft.get(i).getAverage_expense()));
                        mNet_Income.setText(list_darft.get(i).getAverage_income()- list_darft.get(i).getAverage_expense()+"");
                        mProvinceID = list_darft.get(i).getProvince_id();
                        Call<Province> call1 = apiService.getProvince(list_darft.get(i).getProvince_id());
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
                }
            }
            @Override
            public void onFailure(Call<Draft> call, Throwable t) {
                Log.d("Error",t.getMessage());
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
