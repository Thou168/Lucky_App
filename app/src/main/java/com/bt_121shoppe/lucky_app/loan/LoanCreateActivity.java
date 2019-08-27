package com.bt_121shoppe.lucky_app.loan;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.models.LoanViewModel;
import com.bt_121shoppe.lucky_app.utils.LoanCalculator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

//import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoanCreateActivity extends AppCompatActivity {

    private static final String TAG=LoanCreateActivity.class.getSimpleName();
    private SharedPreferences preferences;
    private String username,password,Encode;
    private int encode = 0;
    private int pk , id_edit=0,id_cancel=0,cancelid;
    private int postid;
    private String pk_create;
    private Boolean status_card,status_family,status_staff,status_title = null;
    private String status_borrower = null;
    //    private int postID;
    //loan_information
    private Button btSubmit;
    EditText job_loan_information,monthly_income_loan_information,monthly_expense;

    EditText co_borrower_loan_information;
    EditText loan_purpose,loan_amount,loan_term;
    EditText id_card,family_book,staff_id_or_salary_slip,land_tile;
    TextView txtBack;
    boolean estadoCadastro = true;
    SharedPreferences  pre_id;
    Bundle bundle;
    ImageView icAddress_job,icAddress_co,icAddress_income,icAddress_expense,icAddress_purpose,icAddress_amount,icAddress_term,icAddress_card,icAddress_book,icAddress_staff,icAddress_title;

    private TextInputLayout   input_income, input_expense, input_purpose, input_amount, input_term ;
    private  TextInputLayout input_job;
    String[] yesNos;

    int st;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_create);

        txtBack = (TextView)findViewById(R.id.tvBack_account);
        txtBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//image
        icAddress_co = (ImageView) findViewById(R.id.imgCoBorrower);
        icAddress_card = (ImageView) findViewById(R.id.imgIDCard);
        icAddress_book = (ImageView) findViewById(R.id.imgFamilyBook);
        icAddress_staff = (ImageView) findViewById(R.id.imgStaffID);
        icAddress_title = (ImageView) findViewById(R.id.imgLandTitle);
        icAddress_job = (ImageView)findViewById(R.id.imgJob);
        icAddress_income = (ImageView)findViewById(R.id.imgMonthlyIncome) ;
        icAddress_expense = (ImageView)findViewById(R.id.imgMonthlyExpense);
        icAddress_purpose = (ImageView)findViewById(R.id.imgPurpose);
        icAddress_amount = (ImageView)findViewById(R.id.imgLoanAmount);
        icAddress_term = (ImageView)findViewById(R.id.imgLoanTerm);

//input
        input_job = (TextInputLayout)findViewById(R.id.tilJob);
        input_expense = (TextInputLayout)findViewById(R.id.tilMonthlyExpense);
        input_income = (TextInputLayout)findViewById(R.id.tilMonthlyIncome);
        input_purpose = (TextInputLayout)findViewById(R.id.tilLoanPurpose);
        input_amount = (TextInputLayout)findViewById(R.id.tilLoanAmount);
        input_term = (TextInputLayout)findViewById(R.id.tilLoanTerm);

//ID item
        job_loan_information = (EditText)findViewById(R.id.etJob);
        co_borrower_loan_information = (EditText) findViewById(R.id.etCoBorrower);
        monthly_income_loan_information = (EditText)findViewById(R.id.etMonthlyIncome);
        monthly_expense = (EditText)findViewById(R.id.etMonthlyExpense);
        loan_purpose = (EditText)findViewById(R.id.etLoanPurpose);
        loan_amount = (EditText)findViewById(R.id.etLoanAmount);
        loan_term = (EditText)findViewById(R.id.etLoanTerm);
        id_card = (EditText) findViewById(R.id.tvIDCard);
        family_book = (EditText) findViewById(R.id.tvFamilyBook);
        staff_id_or_salary_slip = (EditText) findViewById(R.id.tvStaffID);
        land_tile = (EditText) findViewById(R.id.tvLandTitle);

        preferences=getSharedPreferences("Register",MODE_PRIVATE);
        username=preferences.getString("name","");
        password=preferences.getString("pass","");
        Encode=getEncodedString(username,password);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }
        Log.d("Pk",""+ pk);
        yesNos=getResources().getStringArray(R.array.co_borrower);
//        ButterKnife.bind(this);
        Log.d(TAG,String.valueOf(LoanCalculator.getLoanMonthPayment(2340,0.0,1.5,12)));

        bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            postid=bundle.getInt("post",0);
            Log.d("Post id",String.valueOf(postid));
        }

        bundle = getIntent().getExtras();
        if (bundle!=null) {
            id_edit = bundle.getInt("id",0);
            Log.d("Loan edit id",String.valueOf(id_edit));
            getLoan_user(Encode);
        }

        pre_id = getSharedPreferences("id",MODE_PRIVATE);
        btSubmit=(Button) findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    if (id_edit != 0) {
                        Edit_loan(Encode, id_edit);
                    }
                    else {
                        st = getIntent().getIntExtra("PutIDLoan",0);
                        Log.d("Log", String.valueOf(st));
                        consumeLoanCreateApi(Encode);
                        signUp();
                    }
            }
        });

        String[] co_borrower = getResources().getStringArray(R.array.co_borrower);
        co_borrower_loan_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoanCreateActivity.this);

                mBuilder.setSingleChoiceItems(co_borrower, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        co_borrower_loan_information.setText(co_borrower[i]);
                        status_borrower=co_borrower[i];
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        String[] card_id = getResources().getStringArray(R.array.ID_card);
        id_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoanCreateActivity.this);
                //mBuilder.setTitle("Choose Category");
                mBuilder.setSingleChoiceItems(card_id, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        id_card.setText(card_id[i]);
                        switch (i){
                            case 0:
                                status_card=true;
                                break;
                            case 1:
                                status_card=false;
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        String[] family = getResources().getStringArray(R.array.Family_book);
        family_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoanCreateActivity.this);
                //mBuilder.setTitle("Choose Category");
                mBuilder.setSingleChoiceItems(family, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        family_book.setText(family[i]);
                        switch (i){
                            case 0:
                                status_family=true;
                                break;
                            case 1:
                                status_family=false;
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        String[] staff_id = getResources().getStringArray(R.array.Staff_id);
        staff_id_or_salary_slip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoanCreateActivity.this);
                //mBuilder.setTitle("Choose Category");
                mBuilder.setSingleChoiceItems(staff_id, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        staff_id_or_salary_slip.setText(staff_id[i]);
                        switch (i){
                            case 0:
                                status_staff=true;
                                break;
                            case 1:
                                status_staff=false;
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        String[] land_title = getResources().getStringArray(R.array.Land_Tile);
        land_tile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoanCreateActivity.this);
                //mBuilder.setTitle("Choose Category");
                mBuilder.setSingleChoiceItems(land_title, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        land_tile.setText(land_title[i]);
                        switch (i){
                            case 0:
                                status_title=true;
                                break;
                            case 1:
                                status_title=false;
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        textChange();

//        loan_term =
    }

    private void signUp(){
        boolean isValid = true;

        if (job_loan_information.getText().toString().isEmpty() || job_loan_information.getText().toString() == null) {
            icAddress_job.setImageResource(R.drawable.ic_error_black_24dp);
            isValid = false;
        } else {
            input_job.setErrorEnabled(false);
        }

        if (co_borrower_loan_information.getText().toString().isEmpty()){
            icAddress_co.setImageResource(R.drawable.ic_error_black_24dp);
            co_borrower_loan_information.setHintTextColor(getResources().getColor(R.color.red));
            isValid = false;
        }else {

        }

        if (monthly_income_loan_information.getText().toString().isEmpty()) {
            icAddress_income.setImageResource(R.drawable.ic_error_black_24dp);
            input_income.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            isValid = false;
        } else {
            input_income.setErrorEnabled(false);
        }

        if (monthly_expense.getText().toString().isEmpty()) {
            icAddress_expense.setImageResource(R.drawable.ic_error_black_24dp);
            input_expense.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            isValid = false;
        } else {
            input_expense.setErrorEnabled(false);
        }

        if (loan_purpose.getText().toString().isEmpty()) {
            icAddress_purpose.setImageResource(R.drawable.ic_error_black_24dp);
            isValid = false;
        } else {
            input_purpose.setErrorEnabled(false);
        }

        if (loan_amount.getText().toString().isEmpty()) {
            icAddress_amount.setImageResource(R.drawable.ic_error_black_24dp);
            input_amount.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            isValid = false;
        } else {
            input_amount.setErrorEnabled(false);
        }

        if (loan_term.getText().toString().isEmpty()) {
            icAddress_term.setImageResource(R.drawable.ic_error_black_24dp);
            input_term.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            isValid = false;
        } else {
            input_term.setErrorEnabled(false);
        }

        if (id_card.getText().toString().isEmpty()) {
            icAddress_card.setImageResource(R.drawable.ic_error_black_24dp);
            id_card.setHintTextColor(getResources().getColor(R.color.red));
            isValid = false;
        } else {

        }

        if (family_book.getText().toString().isEmpty()){
            icAddress_book.setImageResource(R.drawable.ic_error_black_24dp);
            family_book.setHintTextColor(getResources().getColor(R.color.red));
            isValid = false;
        }else {

        }

        if (staff_id_or_salary_slip.getText().toString().isEmpty()){
            icAddress_staff.setImageResource(R.drawable.ic_error_black_24dp);
            staff_id_or_salary_slip.setHintTextColor(getResources().getColor(R.color.red));
            isValid = false;
        }else {

        }

        if (land_tile.getText().toString().isEmpty()){
            icAddress_title.setImageResource(R.drawable.ic_error_black_24dp);
            land_tile.setHintTextColor(getResources().getColor(R.color.red));
            isValid = false;
        }else {

        }

        if (isValid) {

        }

    }

    private void textChange(){
        job_loan_information.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_job.setImageResource(R.drawable.icon_null);

                }
//                else if (s.length() < 2) {
//                    icAddress_job.setImageResource(R.drawable.ic_error_black_24dp);
//                }
                else if
                (s.length()>0) {
                    icAddress_job.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    input_job.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                    input_job.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        co_borrower_loan_information.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_co.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_co.setImageResource(R.drawable.ic_error_black_24dp);

                } else icAddress_co.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        monthly_income_loan_information.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_income.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_income.setImageResource(R.drawable.ic_error_black_24dp);
                } else icAddress_income.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_income.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_income.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        monthly_expense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_expense.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_expense.setImageResource(R.drawable.ic_error_black_24dp);
                } else icAddress_expense.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_expense.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_expense.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loan_purpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_purpose.setImageResource(R.drawable.icon_null);
                }
                else icAddress_purpose.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_purpose.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_purpose.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loan_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_amount.setImageResource(R.drawable.icon_null);
                }
//                } else if (s.length() < 2) {
//                    icAddress_amount.setImageResource(R.drawable.ic_error_black_24dp);
//                }
                else
                    icAddress_amount.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_amount.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_amount.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loan_term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_term.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_term.setImageResource(R.drawable.ic_error_black_24dp);
                } else icAddress_term.setImageResource(R.drawable.ic_check_circle_black_24dp);
                input_term.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
                input_term.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray_active_icon)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        id_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_card.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_card.setImageResource(R.drawable.ic_error_black_24dp);
                } else icAddress_card.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        family_book.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_book.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_book.setImageResource(R.drawable.ic_error_black_24dp);
                } else icAddress_book.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        staff_id_or_salary_slip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_staff.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_staff.setImageResource(R.drawable.ic_error_black_24dp);
                } else icAddress_staff.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        land_tile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    icAddress_title.setImageResource(R.drawable.icon_null);
                } else if (s.length() < 1) {
                    icAddress_title.setImageResource(R.drawable.ic_error_black_24dp);
                } else icAddress_title.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void consumeLoanCreateApi(String encode){

        String urlAPIEndpoint=ConsumeAPI.BASE_URL+"api/v1/loan/?record_status=1";
        OkHttpClient client=new OkHttpClient();
        JSONObject data=new JSONObject();
        try{
            data.put("loan_to",pk);
            data.put("loan_amount",loan_amount.getText().toString().toLowerCase());
            data.put("loan_interest_rate",0);
            data.put("loan_duration",loan_term.getText().toString().toLowerCase());//loan term
            data.put("loan_purpose",loan_purpose.getText().toString().toLowerCase());
            data.put("loan_status",1);
            data.put("record_status",1);
            data.put("username",status_borrower);
            data.put("gender","female");
            data.put("age",0);
            data.put("job",job_loan_information.getText().toString().toLowerCase());
            data.put("average_income",monthly_income_loan_information.getText().toString().toLowerCase());
            data.put("average_expense",monthly_expense.getText().toString().toLowerCase());
            data.put("telephone","011308281");
            data.put("address","Wat Phnom");
            data.put("state_id",status_card);   //id_card
            data.put("family_book",status_family);    //family_book
            data.put("staff_id",status_staff);   //staff_id
            data.put("house_plant",status_title);  //land_title
            data.put("post",getIntent().getIntExtra("PutIDLoan",0));
            data.put("created_by",pk);

            Log.d(TAG," d"+data);
        }catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(ConsumeAPI.MEDIA_TYPE,data.toString());
        String auth = "Basic " + encode;
        Request request=new Request.Builder()
                .url(urlAPIEndpoint)
                .post(body)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String result=e.getMessage().toString();
                Log.d(TAG,"Fail:"+result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                        alertDialog.setTitle(getString(R.string.title_create_loan));
                        alertDialog.setMessage(getString(R.string.loan_fail_message));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Log.d("Response ",result);
                Gson gson=new Gson();
                LoanViewModel loanObj=new LoanViewModel();
                try{
                    loanObj=gson.fromJson(result,LoanViewModel.class);
                    if(loanObj!=null){
                        int statusCode=loanObj.getStatus();
                        if(statusCode==201){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                                    alertDialog.setTitle(getString(R.string.title_create_loan));
                                    alertDialog.setMessage(getString(R.string.loan_message));
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });

                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                                alertDialog.setTitle(getString(R.string.title_create_loan));
                                alertDialog.setMessage(getString(R.string.loan_fail_message));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                    }
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getLoan_user(String encode){

        if(id_edit !=0){
            final String url = String.format("%s%s/", ConsumeAPI.BASE_URL,"api/v1/loan/"+id_edit);
            Log.d("Url",url);
            OkHttpClient client = new OkHttpClient();
            String auth = "Basic "+ encode;
            Request request = new Request.Builder()
                    .url(url)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    .header("Authorization",auth)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respone = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(respone);
                                String job = object.getString("job");
                                String coborrow= object.getString("username");
                                Double income = object.getDouble("average_income");
                                Double expense = object.getDouble("average_expense");
                                String purpose = object.getString("loan_purpose");
                                Double amount = object.getDouble("loan_amount");
                                int term = object.getInt("loan_duration");
                                Boolean stateId = object.getBoolean("state_id");
                                Boolean family = object.getBoolean("family_book");
                                Boolean staffId = object.getBoolean("staff_id");
                                Boolean house = object.getBoolean("house_plant");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        job_loan_information.setText(job);
                                        monthly_income_loan_information.setText(String.valueOf(income));
                                        monthly_expense.setText(String.valueOf(expense));
                                        loan_purpose.setText(purpose);
                                        loan_amount.setText(String.valueOf(amount));
                                        loan_term.setText(String.valueOf(term));

                                        if(stateId == true ) {
                                            id_card.setText(yesNos[0]);
                                        }else if (stateId == false){
                                            id_card.setText(yesNos[1]);
                                        }
                                        if(family == true ) {
                                            family_book.setText(yesNos[0]);
                                        }else if (family == false){
                                            family_book.setText(yesNos[1]);
                                        }
                                        if(staffId == true ) {
                                            staff_id_or_salary_slip.setText(yesNos[0]);
                                        }else if (staffId == false){
                                            staff_id_or_salary_slip.setText(yesNos[1]);
                                        }
                                        if(house == true ) {
                                            land_tile.setText(yesNos[0]);
                                        }else if (house == false){
                                            land_tile.setText(yesNos[1]);
                                        }

                                        if(coborrow.equals("Yes") || coborrow.equals("មាន") ) {
                                            co_borrower_loan_information.setText(yesNos[0]);
                                        }else if (coborrow.equals("No") || coborrow.equals("មិនមាន")){
                                            co_borrower_loan_information.setText(yesNos[1]);
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    Log.d("Edit", respone);
                }
            });

        }
    }
    private void Edit_loan(String encode,int id_edit){

        final String url = String.format("%s%s/", ConsumeAPI.BASE_URL,"api/v1/loan/"+id_edit);
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient();
        JSONObject data=new JSONObject();
        try{
            data.put("loan_to",pk);
            data.put("loan_amount",loan_amount.getText().toString().toLowerCase());
            data.put("loan_interest_rate",0);
            data.put("loan_duration",loan_term.getText().toString().toLowerCase());//loan term
            data.put("loan_purpose",loan_purpose.getText().toString().toLowerCase());
            data.put("loan_status",1);
            data.put("record_status",1);
            data.put("username",status_borrower);
            data.put("gender","female");
            data.put("age",0);
            data.put("job",job_loan_information.getText().toString().toLowerCase());
            data.put("average_income",monthly_income_loan_information.getText().toString().toLowerCase());
            data.put("average_expense",monthly_expense.getText().toString().toLowerCase());
            data.put("telephone","011308281");
            data.put("address","Wat Phnom");
            data.put("state_id",status_card);   //id_card
            data.put("family_book",status_family);    //family_book
            data.put("staff_id",status_staff);   //staff_id
            data.put("house_plant",status_title);  //land_title
            data.put("post",pk);
            data.put("created_by",pk);

            Log.d(TAG," d"+data);
            RequestBody body=RequestBody.create(MEDIA_TYPE,data.toString());
            String auth = "Basic "+ encode;
            Request request=new Request.Builder()
                    .url(url)
                    .put(body)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    .header("Authorization",auth)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    Log.d("Response",result);
                    Gson gson=new Gson();
                    LoanViewModel loanObj=new LoanViewModel();
                    try{
                        loanObj=gson.fromJson(result,LoanViewModel.class);
                        if(loanObj!=null){
                            int statusCode=loanObj.getStatus();
                            if(statusCode == 201){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                                        alertDialog.setTitle(getString(R.string.title_edit_loan));
                                        alertDialog.setMessage(getString(R.string.loan_feedback1));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                });
                            } else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                                        alertDialog.setTitle(getString(R.string.title_edit_loan));
                                        alertDialog.setMessage(getString(R.string.loan_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                });
                            }
                        }
                    }catch (JsonParseException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    String result=e.getMessage().toString();
                    Log.d(TAG,"Fail:"+result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(LoanCreateActivity.this).create();
                            alertDialog.setTitle(getString(R.string.title_edit_loan));
                            alertDialog.setMessage(getString(R.string.loan_feedback));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                }
            });

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP);
    }
}
