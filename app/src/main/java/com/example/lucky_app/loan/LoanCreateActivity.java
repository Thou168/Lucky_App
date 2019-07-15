package com.example.lucky_app.loan;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lucky_app.Api.ConsumeAPI;
import com.example.lucky_app.R;
import com.example.lucky_app.models.LoanViewModel;
import com.example.lucky_app.utils.CommonFunction;
import com.example.lucky_app.utils.LoanCalculator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoanCreateActivity extends AppCompatActivity {

    private static final String TAG=LoanCreateActivity.class.getSimpleName();
    private SharedPreferences preferences;
    private String username,password,encodeAuth;
    private int pk;
    private Boolean status_card,status_family,status_staff,status_title = null;
    private Boolean status_borrower = null;

    //loan_information
    private Button btSubmit;
    EditText job_loan_information,monthly_income_loan_information,monthly_expense;
    MaterialSpinner co_borrower_loan_information;
    EditText loan_purpose,loan_amount,loan_term;
//    EditText price_loancreate,interest_rate,deposit_loancreate,term_loancreate;
    MaterialSpinner id_card,family_book,staff_id_or_salary_slip,land_tile;

    TextView txtBack;
    boolean estadoCadastro = true;

 //   final String[] co_borrower = getResources().getStringArray(R.array.co_borrower);
//    String[] card_id = getResources().getStringArray(R.array.ID_card);
//    String[] book_family = getResources().getStringArray(R.array.Family_book);
//    String[] staff_id = getResources().getStringArray(R.array.Staff_id);
//    String[] land_title = getResources().getStringArray(R.array.Land_Tile);



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

        job_loan_information = (EditText)findViewById(R.id.etJob);
        co_borrower_loan_information = (MaterialSpinner) findViewById(R.id.etCoBorrower);
        monthly_income_loan_information = (EditText)findViewById(R.id.etMonthlyIncome);
        monthly_expense = (EditText)findViewById(R.id.etMonthlyExpense);

        loan_purpose = (EditText)findViewById(R.id.etLoanPurpose);
        loan_amount = (EditText)findViewById(R.id.etLoanAmount);
        loan_term = (EditText)findViewById(R.id.etLoanTerm);

        id_card = (MaterialSpinner) findViewById(R.id.tvIDCard);
        family_book = (MaterialSpinner) findViewById(R.id.tvFamilyBook);
        staff_id_or_salary_slip = (MaterialSpinner) findViewById(R.id.tvStaffID);
        land_tile = (MaterialSpinner) findViewById(R.id.tvLandTitle);

//        price_loancreate = (EditText)findViewById(R.id.ed_loan_price);
//        interest_rate = (EditText)findViewById(R.id.ed_loan_interest_rate);
//        deposit_loancreate = (EditText)findViewById(R.id.ed_loan_deposit);
//        term_loancreate = (EditText)findViewById(R.id.ed_loan_term);

        preferences=getSharedPreferences("Register",MODE_PRIVATE);
        username=preferences.getString("name","");
        password=preferences.getString("pass","");
        encodeAuth="Basic "+ CommonFunction.getEncodedString(username,password);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }
        Log.d(TAG,String.valueOf(LoanCalculator.getLoanMonthPayment(2340,1.5,12)));

        btSubmit=(Button) findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumeLoanCreateApi();
            }
        });

        String[] co_borrower = getResources().getStringArray(R.array.co_borrower);
        ArrayAdapter<String> borrower = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,co_borrower);
        co_borrower_loan_information.setAdapter(borrower);
        co_borrower_loan_information.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String item = String.valueOf(borrower.getItem(i));
                Log.d("Borrower", item);
                if(!item.equals(null)){
                    if (item.equals("Yes")){
                        status_borrower = true;
                        Log.d("Value borrow ",status_borrower.toString());
                    }else {
                        status_borrower = false;
                        Log.d("Value borrow ",status_borrower.toString());
                    }

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        String[] card_id = getResources().getStringArray(R.array.ID_card);
        ArrayAdapter<String> card_state = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,card_id);
        id_card.setAdapter(card_state);
        id_card.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String item = String.valueOf(card_state.getItem(i));
                Log.d("ID", item);
                if(!item.equals(null)){
                    if (item.equals("Yes")){
                        status_card = true;
                        Log.d("Value card ",status_card.toString());
                    }else {
                        status_card = false;
                        Log.d("Value card ",status_card.toString());
                    }

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        String[] family = getResources().getStringArray(R.array.Family_book);
        ArrayAdapter<String> book_family = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,family);
        family_book.setAdapter(book_family);
        family_book.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String item = String.valueOf(book_family.getItem(i));
                Log.d("BOOK", item);
                if(!item.equals(null)){
                    if (item.equals("Yes")){
                        status_family = true;
                        Log.d("Value family ",status_family.toString());
                    }else {
                        status_family = false;
                        Log.d("Value family ",status_family.toString());
                    }

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        String[] staff_id = getResources().getStringArray(R.array.Staff_id);
        ArrayAdapter<String> staff_id_loan = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,staff_id);
        staff_id_or_salary_slip.setAdapter(staff_id_loan);
        staff_id_or_salary_slip.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String item = String.valueOf(staff_id_loan.getItem(i));
                Log.d("Staff ID", item);
                if(!item.equals(null)){
                    if (item.equals("Yes")){
                        status_staff = true;
                        Log.d("Staff id ",status_staff.toString());
                    }else {
                        status_staff = false;
                        Log.d("Staff id ",status_staff.toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        String[] land_title = getResources().getStringArray(R.array.Land_Tile);
        ArrayAdapter<String> land_title_loan = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,land_title);
        land_tile.setAdapter(land_title_loan);
        land_tile.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String item = String.valueOf(land_title_loan.getItem(i));
                Log.d("Land Title", item);
                if(!item.equals(null)){
                    if (item.equals("Yes")){
                        status_title = true;
                        Log.d("Land ",status_title.toString());
                    }else {
                        status_title = false;
                        Log.d("Land ",status_title.toString());
                    }

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });


    } //oncreate

    private void consumeLoanCreateApi(){
        String urlAPIEndpoint=ConsumeAPI.BASE_URL+"loanbyuser/";

        OkHttpClient client=new OkHttpClient();
        JSONObject data=new JSONObject();
        try{
            data.put("loan_to",pk);
            data.put("loan_amount",loan_amount.getText().toString().toLowerCase());
            data.put("loan_interest_rate",loan_term); //loan term
            data.put("loan_duration","24");
            data.put("loan_purpose",loan_purpose.getText().toString().toLowerCase());
            data.put("loan_status",9);
            data.put("record_status",1);
            data.put("username","Bye");
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
            data.put("post",20);
            data.put("created_by",pk);

            Log.d(TAG," d"+data);
        }catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(ConsumeAPI.MEDIA_TYPE,data.toString());
        Request request=new Request.Builder()
                .url(urlAPIEndpoint)
                .post(body)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
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
                        alertDialog.setTitle("Loan");
                        alertDialog.setMessage("Loan request has been error while submiting.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
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
                Log.d("Response",result);
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
                                    alertDialog.setTitle("Loan");
                                    alertDialog.setMessage("Loan request has been successfully submitted.");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
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
                                alertDialog.setTitle("Loan");
                                alertDialog.setMessage("Loan request has been error while submiting.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
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

}
