package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.loan.model.Draft;
import com.bt_121shoppe.motorbike.loan.model.draft_Item;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.item_two;
import com.bt_121shoppe.motorbike.loan.model.loan_item;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;

public class three extends Fragment {

    private static final String ARG_NUMBER = "arg_number";
    private static final String PRICE = "price";
    private static final String LOANID = "loanid";
    private static final String FROMLOAN = "fromloan";
    private static final String DRAFT = "draft";

    private Button mBtnSubmit,mBtnSaveDraft;
    private RadioGroup etID_card,etFamily_book,etPhotos,etEmployment_card,etID_card1,etFamily_book1,etPhotos1,etEmployment_card1;
    private RadioButton rdID_card_yes,rdFamily_book_yes,rdPhotos_yes,rdEmployment_card_yes,rdID_card1_yes,rdFamily_book1_yes,rdPhotos1_yes,rdEmployment_card1_yes;
    private RadioButton rdID_card_no,rdFamily_book_no,rdPhotos_no,rdEmployment_card_no,rdID_card1_no,rdFamily_book1_no,rdPhotos1_no,rdEmployment_card1_no;
    private item_two itemTwo;
    private SharedPreferences preferences;
    private ProgressDialog mProgress;
    private String username,password,Encode,language;
    private LinearLayout layout_coborrower;
    private loan_item loanItem;
    private String basicEncode;
    private String mPrice,mDraft;
    private List<draft_Item> list_darft;
    private int mProductID,mLoanID;
    private int pk;
    private int index = 3;
    private boolean mFromLoan;
    private boolean mCard_ID,mFamily_Book,mPhoto,mCard_Work,mCard_ID1=false,mFamily_Book1=false,mPhoto1=false,mCard_Work1=false;
    public static three newInstance(int number,String price,int loanid,boolean fromLoan,String Draft) {
        three fragment = new three();
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
        View view = inflater.inflate(R.layout.activity_create__load_three, container, false);
        return view;
    }
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mProductID = args.getInt(ARG_NUMBER);
            mPrice = args.getString(PRICE);
            mLoanID = args.getInt(LOANID);
            mFromLoan = args.getBoolean(FROMLOAN);
            mDraft = args.getString(DRAFT);
            Log.e("Fragment Three",""+mProductID+","+mPrice+","+mLoanID+","+mFromLoan);
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
        if (itemTwo != null) {
            Log.e("co-borrower", "" + itemTwo.getItemOne().getIndex());
            if (itemTwo.getItemOne().getIndex() == 1) {
                layout_coborrower.setVisibility(View.GONE);
            } else {
                layout_coborrower.setVisibility(View.VISIBLE);
            }
        }
        initView(view);
        mBtnSaveDraft = view.findViewById(R.id.btn_back);
        mBtnSubmit = view.findViewById(R.id.btn_submit);
        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage(getString(R.string.update));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Paper.init(getContext());
        language = Paper.book().read("language");

        mBtnSaveDraft.setOnClickListener(view1 -> {
            LayoutInflater factory = LayoutInflater.from(getContext());
            final View clearDialogView = factory.inflate(R.layout.save_dialog,null);
            final AlertDialog clearDialog = new AlertDialog.Builder(getContext()).create();
            clearDialog.setView(clearDialogView);
            EditText draft_name = (EditText) clearDialogView.findViewById(R.id.et_draft);
            TextView title      = (TextView) clearDialogView.findViewById(R.id.textView_title);
            TextView name       = (TextView) clearDialogView.findViewById(R.id.tv_draft);
            Button btnSave      = (Button) clearDialogView.findViewById(R.id.btnSave);
            Button btnCancel    = (Button) clearDialogView.findViewById(R.id.btnCancel);
            title.setText(getString(R.string.save_draft));
            name.setText(getString(R.string.name));
            btnCancel.setText(getString(R.string.cancel));
            btnSave.setText(getString(R.string.save));
            clearDialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> clearDialog.dismiss());
            clearDialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
                Log.d("Pk",""+ pk + Encode+"  user "+ username+"  pass  "+password);
                if (itemTwo != null) {
                    Service api = Client.getClient().create(Service.class);
                    loanItem = new loan_item(itemTwo.getLoan_Amount(),0,Integer.parseInt(itemTwo.getLoan_Term()),
                            itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),9,1,
                            pk,itemTwo.getItemOne().getmProductId(),pk,pk,null,itemTwo.getItemOne().getName(),null,
                            0,itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getProvince(),
                            itemTwo.getItemOne().getDistrict(),itemTwo.getItemOne().getCommune(),itemTwo.getItemOne().getVillage(),mCard_ID,
                            mFamily_Book,mCard_Work,mPhoto,itemTwo.getItemOne().getmProvinceID(),itemTwo.getItemOne().getJob(),
                            itemTwo.getItemOne().getJob_Period(),itemTwo.getLoan_RepaymentType(),itemTwo.getDeposit_Amount(),itemTwo.getBuying_InsuranceProduct(),
                            itemTwo.getAllow_visito_home(),Integer.parseInt(itemTwo.getNumber_institution()),mCard_ID1,mFamily_Book1,mPhoto1,mCard_Work1,
                            itemTwo.getItemOne().getRelationship(),itemTwo.getItemOne().getCo_borrower_Job(),itemTwo.getItemOne().getCo_Job_Period(),
                            itemTwo.getItemOne().isCo_borrower(),Float.parseFloat(itemTwo.getMonthly_AmountPaid_Institurion()),draft_name.getText().toString(),true);
                    Log.e("loan item",""+loanItem);
                    Call<loan_item> call = api.setCreateLoan(loanItem,basicEncode);
                    call.enqueue(new Callback<loan_item>() {
                        @Override
                        public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                            if (response.isSuccessful()){
                                SaveDraftDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<loan_item> call, Throwable t) {
                            Log.d("ErroronFailure121212", t.getMessage());
                        }
                    });
                }else {
                    AlertDialog builder = new AlertDialog.Builder(getContext()).create();
                    builder.setMessage(getString(R.string.please_fill_information));
                    builder.setCancelable(false);
                    builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.back_ok), (dialogInterface, i) -> builder.dismiss());
                    builder.show();
                }
            });
            clearDialog.setCancelable(false);
            clearDialog.show();
        });
        mBtnSubmit.setOnClickListener(v -> {
                Log.e("From Loan",String.valueOf(mFromLoan));
                if (mFromLoan){
                    dialog_Editloan();
                }else {
                    Log.d("Pk",""+ pk + Encode+"  user "+ username+"  pass  "+password);
                    if (itemTwo != null) {
                        putapi();
                    }else {
                        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(getContext()).create();
                        builder.setMessage(getString(R.string.please_fill_information));
                        builder.setCancelable(false);
                        builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.back_ok), (dialogInterface, i) -> builder.dismiss());
                        builder.show();
                    }
                }
        });

    }

    private void putapi(){
        Service api1 = Client.getClient().create(Service.class);
        loanItem = new loan_item(itemTwo.getLoan_Amount(),0,Integer.parseInt(itemTwo.getLoan_Term()),
                itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),9,1,
                pk,itemTwo.getItemOne().getmProductId(),pk,pk,null,itemTwo.getItemOne().getName(),null,
                0,itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getProvince(),
                itemTwo.getItemOne().getDistrict(),itemTwo.getItemOne().getCommune(),itemTwo.getItemOne().getVillage(),mCard_ID,
                mFamily_Book,mCard_Work,mPhoto,itemTwo.getItemOne().getmProvinceID(),itemTwo.getItemOne().getJob(),
                itemTwo.getItemOne().getJob_Period(),itemTwo.getLoan_RepaymentType(),itemTwo.getDeposit_Amount(),itemTwo.getBuying_InsuranceProduct(),
                itemTwo.getAllow_visito_home(),Integer.parseInt(itemTwo.getNumber_institution()),mCard_ID1,mFamily_Book1,mPhoto1,mCard_Work1,
                itemTwo.getItemOne().getRelationship(),itemTwo.getItemOne().getCo_borrower_Job(),itemTwo.getItemOne().getCo_Job_Period(),
                itemTwo.getItemOne().isCo_borrower(),Float.parseFloat(itemTwo.getMonthly_AmountPaid_Institurion()));
//        loanItem = new loan_item(158,1200,0,3,"Test",1,1,"Thou","male",19,"Student",600,300,"1234567","st 273",true,false,
//        true,false,2,"185","null",185,null,null,null,null,202,7,null,null,null,"seller","2","1",false,true,4,"1234",false,false,true,false,true);
        Call<loan_item> call = api1.setCreateLoan(loanItem,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (response.isSuccessful()){
                    MaterialDialog();
                }
            }

            @Override
            public void onFailure(Call<loan_item> call, Throwable t) {
                Log.d("ErroronFailure121212", t.getMessage());
            }
        });
    }
    private void Editloan(){
        Service api = Client.getClient().create(Service.class);
        loanItem = new loan_item(itemTwo.getLoan_Amount(),0,Integer.parseInt(itemTwo.getLoan_Term()),
                itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),9,1,
                pk,itemTwo.getItemOne().getmProductId(),pk,pk,null,itemTwo.getItemOne().getName(),null,0,
                itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getProvince(),
                itemTwo.getItemOne().getDistrict(),itemTwo.getItemOne().getCommune(),itemTwo.getItemOne().getVillage(),mCard_ID,mFamily_Book,
                mCard_Work,mPhoto,itemTwo.getItemOne().getmProvinceID(),itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getJob_Period(),
                itemTwo.getLoan_RepaymentType(),itemTwo.getDeposit_Amount(),itemTwo.getBuying_InsuranceProduct(),itemTwo.getAllow_visito_home(),
                Integer.parseInt(itemTwo.getNumber_institution()),mCard_ID1,mFamily_Book1,mPhoto1,mCard_Work1,itemTwo.getItemOne().getRelationship(),
                itemTwo.getItemOne().getCo_borrower_Job(),itemTwo.getItemOne().getCo_Job_Period(),itemTwo.getItemOne().isCo_borrower(),
                Float.parseFloat(itemTwo.getMonthly_AmountPaid_Institurion()));
        Call<loan_item> call = api.getEditLoan(mLoanID,loanItem,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (!response.isSuccessful()){
                    Log.e("EditLoan Error",response.code()+"");
                    try {
                        Log.d("121212",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<loan_item> call, Throwable t) {
            }
        });
    }
    private void dialog_Editloan() {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
        final AlertDialog clearDialog = new AlertDialog.Builder(getContext()).create();
        clearDialog.setView(clearDialogView);
        TextView Mssloan = clearDialogView.findViewById(R.id.textView_message);
        Mssloan.setText(R.string.loan_edit_m);
        Button btnYes =  clearDialogView.findViewById(R.id.button_positive);
        btnYes.setText(R.string.yes_leave);
        Button btnNo = clearDialogView.findViewById(R.id.button_negative);
        btnNo.setText(R.string.no_leave);
        clearDialogView.findViewById(R.id.button_negative).setOnClickListener(v -> {
            clearDialog.dismiss();
        });
        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(v -> {
            Editloan();
            mProgress.show();
            getActivity().finish();
        });
        mProgress.dismiss();
        clearDialog.show();
    }

    private void initView(View view) {

        rdID_card_yes           = (RadioButton) view.findViewById(R.id.radio1_ID_card);
        rdID_card_no            = (RadioButton) view.findViewById(R.id.radio2_ID_card);
        rdFamily_book_yes       = (RadioButton) view.findViewById(R.id.radio1_Family_book);
        rdFamily_book_no        = (RadioButton) view.findViewById(R.id.radio2_Family_book);
        rdPhotos_yes            = (RadioButton) view.findViewById(R.id.radio1_Photos);
        rdPhotos_no             = (RadioButton) view.findViewById(R.id.radio2_Photos);
        rdEmployment_card_yes   = (RadioButton) view.findViewById(R.id.radio1_Employment_card);
        rdEmployment_card_no    = (RadioButton) view.findViewById(R.id.radio2_Employment_card);
        rdID_card1_yes          = (RadioButton) view.findViewById(R.id.radio1_ID_card1);
        rdID_card1_no           = (RadioButton) view.findViewById(R.id.radio2_ID_card1);
        rdFamily_book1_yes      = (RadioButton) view.findViewById(R.id.radio1_Family_book1);
        rdFamily_book1_no       = (RadioButton) view.findViewById(R.id.radio2_Family_book1);
        rdPhotos1_yes           = (RadioButton) view.findViewById(R.id.radio1_Photos1);
        rdPhotos1_no            = (RadioButton) view.findViewById(R.id.radio2_Photos1);
        rdEmployment_card1_yes  = (RadioButton) view.findViewById(R.id.radio1_Employment_card1);
        rdEmployment_card1_no   = (RadioButton) view.findViewById(R.id.radio2_Employment_card1);
        layout_coborrower       = (LinearLayout)view.findViewById(R.id.layout_co_borrower);

        etID_card = view.findViewById(R.id.radio_group_ID_card);
        etID_card.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etID_card.findViewById(checkedId);
            index = etID_card.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mCard_ID = true;
                    break;
                case 1:
                    mCard_ID = false;
                    break;
            }
        });

        etFamily_book = view.findViewById(R.id.radio_group_Family_book);
        etFamily_book.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etFamily_book.findViewById(checkedId);
            index = etFamily_book.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mFamily_Book = true;
                    break;
                case 1:
                    mFamily_Book = false;
                    break;
            }
        });

        etPhotos = view.findViewById(R.id.radio_group_Photos);
        etPhotos.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etPhotos.findViewById(checkedId);
            index = etPhotos.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mPhoto = true;
                    break;
                case 1:
                    mPhoto = false;
                    break;
            }
        });

        etEmployment_card = view.findViewById(R.id.radio_group_Employment_card);
        etEmployment_card.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etEmployment_card.findViewById(checkedId);
            index = etEmployment_card.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mCard_Work = true;
                    break;
                case 1:
                    mCard_Work = false;
                    break;
            }
        });

        etID_card1 = view.findViewById(R.id.radio_group_ID_card1);
        etID_card1.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etID_card1.findViewById(checkedId);
            index = etID_card1.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mCard_ID1 = true;
                    break;
                case 1:
                    mCard_ID1 = false;
                    break;
            }
        });

        etFamily_book1 = view.findViewById(R.id.radio_group_Family_book1);
        etFamily_book1.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etFamily_book1.findViewById(checkedId);
            index = etFamily_book1.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mFamily_Book1 = true;
                    break;
                case 1:
                    mFamily_Book1 = false;
                    break;
            }
        });

        etPhotos1 = view.findViewById(R.id.radio_group_Photos1);
        etPhotos1.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etPhotos1.findViewById(checkedId);
            index = etPhotos1.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mPhoto1 = true;
                    break;
                case 1:
                    mPhoto1 = false;
                    break;
            }
        });

        etEmployment_card1 = view.findViewById(R.id.radio_group_Employment_card1);
        etEmployment_card1.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = etEmployment_card1.findViewById(checkedId);
            index = etEmployment_card1.indexOfChild(radioButton);
            switch (index) {
                case 0:
                    mCard_Work1 = true;
                    break;
                case 1:
                    mCard_Work1 = false;
                    break;
            }
        });

        if (mFromLoan){
            GetLoan();
        }else if (mDraft != null){
            getLoan_draft();
        }

    }

    private void MaterialDialog(){
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.title_create_loan));
        alertDialog.setMessage(getString(R.string.loan_message));
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> {
            Intent intent = new Intent(getContext(), Detail_new_post_java.class);
            intent.putExtra("ID",mProductID);
            startActivity(intent);
            mProgress.show();
            getActivity().finish();
            dialog.dismiss();
        });
        alertDialog.show();
    }
    private void SaveDraftDialog(){
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getContext()).create();
        alertDialog.setMessage(getString(R.string.save_draft_message));
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> {
            Intent intent = new Intent(getContext(), Detail_new_post_java.class);
            intent.putExtra("ID",mProductID);
            startActivity(intent);
            mProgress.show();
            getActivity().finish();
            dialog.dismiss();
        });
        alertDialog.show();
    }
    private void GetLoan(){
        Service api = Client.getClient().create(Service.class);
        Call<loan_item> call = api.getDeailLoan(mLoanID,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (response.body() != null){
                    if (response.body().isState_id()){
                        mCard_ID = true;
                        etID_card.check(R.id.radio1_ID_card);
                        rdID_card_yes.toggle();
                    } else {
                        mCard_ID = false;
                        etID_card.check(R.id.radio2_ID_card);
                        rdID_card_no.toggle();
                    }

                    if (response.body().isFamily_book()){
                        mFamily_Book = true;
                        etFamily_book.check(R.id.radio1_Family_book);
                        rdFamily_book_yes.toggle();
                    } else{
                        mFamily_Book =false;
                        etFamily_book.check(R.id.radio2_Family_book);
                        rdFamily_book_no.toggle();
                    }
                    if (response.body().isIs_borrower_photo()){
                        mPhoto = true;
                        etPhotos.check(R.id.radio1_Photos);
                        rdPhotos_yes.toggle();
                    } else {
                        mPhoto = false;
                        etPhotos.check(R.id.radio2_Photos);
                        rdPhotos_no.toggle();
                    }

                    if (response.body().isStaff_id()){
                        mCard_Work = true;
                        etEmployment_card.check(R.id.radio1_Employment_card);
                        rdEmployment_card_yes.toggle();
                    } else{
                        mCard_Work = false;
                        etEmployment_card.check(R.id.radio2_Employment_card);
                        rdEmployment_card_no.toggle();
                    }

                    //co-borrow
                    if (response.body().isIs_coborrower_idcard()){
                        mCard_ID1 = true;
                        etID_card1.check(R.id.radio1_ID_card1);
                        rdID_card1_yes.toggle();
                    }else {
                        mCard_ID1 = false;
                        etID_card1.check(R.id.radio2_ID_card1);
                        rdID_card1_no.toggle();
                    }
                    if (response.body().isIs_coborrower_familybook()){
                        mFamily_Book1 = true;
                        etFamily_book1.check(R.id.radio1_Family_book1);
                        rdFamily_book1_yes.toggle();
                    }else {
                        mFamily_Book1 = false;
                        etFamily_book1.check(R.id.radio2_Family_book1);
                        rdFamily_book1_no.toggle();
                    }
                    if (response.body().isIs_coborrower_photo()){
                        mPhoto1= true;
                        etPhotos1.check(R.id.radio1_Photos1);
                        rdPhotos1_yes.toggle();
                    }else {
                        mPhoto1 = false;
                        etPhotos1.check(R.id.radio2_Photos1);
                        rdPhotos1_no.toggle();
                    }
                    if (response.body().isIs_coborrower_payslip()){
                        mCard_Work1= true;
                        etEmployment_card1.check(R.id.radio1_Employment_card1);
                        rdEmployment_card1_yes.toggle();
                    }else {
                        mCard_Work1= false;
                        etEmployment_card1.check(R.id.radio2_Employment_card1);
                        rdEmployment_card1_no.toggle();
                    }
                }
            }
            @Override
            public void onFailure(Call<loan_item> call, Throwable t) { }
        });
    }

    private void getLoan_draft(){
        Service apiService = Client.getClient().create(Service.class);
        Call<Draft> call = apiService.getList_draft(basicEncode);
        call.enqueue(new Callback<Draft>() {
            @Override
            public void onResponse(Call<Draft> call, Response<Draft> response) {
                list_darft = response.body().getresults();
                Log.e("list 3",""+list_darft.size());
                if (list_darft.size() >0){
                    for (int i=0;i<list_darft.size();i++){
                        Log.e("list draft",""+list_darft.get(i).isFamily_book()+list_darft.get(i).isStaff_id()+list_darft.get(i).isIs_borrower_photo());
                        if (list_darft.get(i).isState_id()){
                            mCard_ID = true;
                            etID_card.check(R.id.radio1_ID_card);
                            rdID_card_yes.toggle();
                        } else {
                            mCard_ID = false;
                            etID_card.check(R.id.radio2_ID_card);
                            rdID_card_no.toggle();
                        }

                        if (list_darft.get(i).isFamily_book()){
                            mFamily_Book = true;
                            etFamily_book.check(R.id.radio1_Family_book);
                            rdFamily_book_yes.toggle();
                        } else{
                            mFamily_Book =false;
                            etFamily_book.check(R.id.radio2_Family_book);
                            rdFamily_book_no.toggle();
                        }
                        if (list_darft.get(i).isIs_borrower_photo()){
                            mPhoto = true;
                            etPhotos.check(R.id.radio1_Photos);
                            rdPhotos_yes.toggle();
                        } else {
                            mPhoto = false;
                            etPhotos.check(R.id.radio2_Photos);
                            rdPhotos_no.toggle();
                        }

                        if (list_darft.get(i).isStaff_id()){
                            mCard_Work = true;
                            etEmployment_card.check(R.id.radio1_Employment_card);
                            rdEmployment_card_yes.toggle();
                        } else{
                            mCard_Work = false;
                            etEmployment_card.check(R.id.radio2_Employment_card);
                            rdEmployment_card_no.toggle();
                        }

                        //co-borrow
                        if (list_darft.get(i).isIs_coborrower_idcard()){
                            mCard_ID1 = true;
                            etID_card1.check(R.id.radio1_ID_card1);
                            rdID_card1_yes.toggle();
                        }else {
                            mCard_ID1 = false;
                            etID_card1.check(R.id.radio2_ID_card1);
                            rdID_card1_no.toggle();
                        }
                        if (list_darft.get(i).isIs_coborrower_familybook()){
                            mFamily_Book1 = true;
                            etFamily_book1.check(R.id.radio1_Family_book1);
                            rdFamily_book1_yes.toggle();
                        }else {
                            mFamily_Book1 = false;
                            etFamily_book1.check(R.id.radio2_Family_book1);
                            rdFamily_book1_no.toggle();
                        }
                        if (list_darft.get(i).isIs_coborrower_photo()){
                            mPhoto1= true;
                            etPhotos1.check(R.id.radio1_Photos1);
                            rdPhotos1_yes.toggle();
                        }else {
                            mPhoto1 = false;
                            etPhotos1.check(R.id.radio2_Photos1);
                            rdPhotos1_no.toggle();
                        }
                        if (list_darft.get(i).isIs_coborrower_payslip()){
                            mCard_Work1= true;
                            etEmployment_card1.check(R.id.radio1_Employment_card1);
                            rdEmployment_card1_yes.toggle();
                        }else {
                            mCard_Work1= false;
                            etEmployment_card1.check(R.id.radio2_Employment_card1);
                            rdEmployment_card1_no.toggle();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Draft> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public void getItemTwo(item_two item)
    {
        Log.e("item_two",""+item);
        itemTwo = item;
    }
}
