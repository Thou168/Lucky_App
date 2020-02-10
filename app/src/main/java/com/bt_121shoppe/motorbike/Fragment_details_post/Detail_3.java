package com.bt_121shoppe.motorbike.Fragment_details_post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.utils.LoanCalculator;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Detail_3 extends Fragment {
    public static final String TAG = "3 Fragement";
    private EditText edPrice,edTerm,edDeposit;
    private TextView tvSB,tvSBRate;
    private TextView tvMonthly;
    private SeekBar sbDeposit,sbInterestRate;


    private int pt=0;
    private int postId = 0;
    PostViewModel postDetail = new PostViewModel();
    private String postPrice;
    Double discount = 0.0;
    SharedPreferences prefer;
    private String name,pass,Encode;
    float correct;
    String term = "0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_3, container, false);

        edPrice = view.findViewById(R.id.edPrice);
        edDeposit = view.findViewById(R.id.edDeposit);
        edTerm = view.findViewById(R.id.edTerm);
        tvMonthly = view.findViewById(R.id.tvMonthlyPayment);
        sbDeposit = view.findViewById(R.id.sbDeposit);
        tvSB = view.findViewById(R.id.tvSb);
        sbInterestRate = view.findViewById(R.id.sbInterestRate);
        tvSBRate = view.findViewById(R.id.tvSb_Rate);

        pt = getActivity().getIntent().getIntExtra("postt",0);
        postId = getActivity().getIntent().getIntExtra("ID",0);
        discount = getActivity().getIntent().getDoubleExtra("Discount",0.0);
        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        detail_fragment3(Encode);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edPrice.setHint("$ 0.00");
        edPrice.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                calculateLoanMonthlyPayment();
                return true;
            }
            return false;
        });
        edPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateLoanMonthlyPayment();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edDeposit.setText("$ 0.00");
        edTerm.setText("24");
        edTerm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        edTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateLoanMonthlyPayment();
                String zero = s.toString();
                if (zero.equals(term)){
                    tvMonthly.setText("$0.00");
                }else if (zero.isEmpty()){
                    tvMonthly.setText("$0.00");
                    edTerm.setHint("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvMonthly.setHint("Monthly");
        edPrice.setFocusable(false);
        edDeposit.setFocusable(false);
    }

    private void detail_fragment3(String encode){
        String url;
        Request request;
        String auth = "Basic" + encode;
        if (pt==1){
            url = ConsumeAPI.BASE_URL + "postbyuser/" + postId;
            request = new  Request.Builder()
                    .url(url)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    .header("Authorization",auth)
                    .build();
        }
        else {
            url = ConsumeAPI.BASE_URL + "detailposts/" + postId;
            request = new  Request.Builder()
                    .url(url)
                    .header("Accept","application/json")
                    .header("Content-Type", "application/json")
                    .build();
        }
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Request",mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.d(TAG+"3333",mMessage);
                Gson json = new Gson();
                try {
                    if (getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postDetail = json.fromJson(mMessage, PostViewModel.class);
                                Log.e(TAG, "D" + mMessage);
                                postPrice=discount.toString();
                                float post_price_dc = Float.parseFloat(postDetail.getDiscount());
                                edPrice.setText(postPrice);
                                //get value of percent/100 and then mul
                                sbDeposit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        calculateLoanMonthlyPayment();
                                        tvSB.setText(progress + "%");
                                        int x = seekBar.getThumb().getBounds().centerX();
                                        tvSB.setX(x);
                                        correct = progress;
                                        float percent = correct / 100;
                                        float viewPrice = percent * post_price_dc;
                                        BigDecimal bigDecimal = new BigDecimal(viewPrice);
                                        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
                                        edDeposit.setText(bigDecimal+"$");
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });

                                //interestrate
                                sbInterestRate.setProgress(10);
                                sbInterestRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        calculateLoanMonthlyPayment();
                                        tvSBRate.setText(progress + "%");
                                        int x = seekBar.getThumb().getBounds().left;
                                        tvSBRate.setX(x);
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });
                                if (discount==0.00){
                                    edPrice.setText(postDetail.getCost());
                                    float post_price = Float.parseFloat(postDetail.getCost());
                                    sbDeposit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                            calculateLoanMonthlyPayment();
                                            tvSB.setText(progress + "%");
                                            int x = seekBar.getThumb().getBounds().centerX();
                                            tvSB.setX(x);
                                            correct = progress;
                                            float percent = correct / 100;
                                            float viewPrice = percent * post_price;
                                            BigDecimal bigDecimal = new BigDecimal(viewPrice);
                                            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
                                            edDeposit.setText(bigDecimal+"$");
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });

                                    //interestrate
                                    sbInterestRate.setProgress(10);
                                    sbInterestRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                            calculateLoanMonthlyPayment();
                                            tvSBRate.setText(progress + "%");
                                            int x = seekBar.getThumb().getBounds().left;
                                            tvSBRate.setX(x);
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                }catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void calculateLoanMonthlyPayment(){
        String loanPrice = edPrice.getText().toString();
        String loanDeposit = String.valueOf(sbDeposit.getProgress());
        String loanInterestRate = String.valueOf(sbInterestRate.getProgress());
        String loanTerm = edTerm.getText().toString();

        double aPrice = 0.0;
        double aDeposit = 0.0;
        double aInterestRate = 0.0;
        int aLoanTerm = 0;

        if (loanPrice.isEmpty()){
            aPrice = 0.0;
        }else {
            aPrice= Double.parseDouble(loanPrice);
        }

        if (loanDeposit.isEmpty()){
            aDeposit = 0.0;
        }else {
            aDeposit = Double.parseDouble(loanDeposit);
        }

        if (loanInterestRate.isEmpty()){
            aInterestRate = 0.0;
        }else {
            aInterestRate = Double.parseDouble(loanInterestRate);
        }

        if (loanTerm.isEmpty()){
        }else {
            aLoanTerm = Integer.parseInt(loanTerm);
        }

        double monthlypayment = LoanCalculator.getLoanMonthPayment(aPrice,aDeposit,aInterestRate,aLoanTerm);
//        DecimalFormatSymbols df = new  DecimalFormatSymbols(Locale.US);
//        DecimalFormat me = new  DecimalFormat("#.##", df);
//        me.setRoundingMode(RoundingMode.HALF_EVEN);
//        tvMonthly.setText(me.format(monthlypayment) + " $ ");
        DecimalFormat bd = new DecimalFormat("#.##");
        tvMonthly.setText("$"+bd.format(monthlypayment));
    }
}
