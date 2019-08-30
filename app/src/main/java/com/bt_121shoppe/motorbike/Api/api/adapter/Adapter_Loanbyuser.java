package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Account;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.LoanCreateActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_Loanbyuser extends RecyclerView.Adapter<Adapter_Loanbyuser.ViewHolder> {

    private List<Item_loan> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_Loanbyuser(List<Item_loan> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loan,viewGroup,false);

        prefer = mContext.getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");

        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_Encode = "Basic "+getEncodedString(name,pass);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder view, final int position) {
        final Item_loan model = datas.get(position);
        String postid = String.valueOf(model.getPost()).substring(0, String.valueOf(model.getPost()).indexOf("."));
        String loanid = String.valueOf(model.getId()).substring(0, String.valueOf(model.getId()).indexOf("."));

        view.linearLayout.setOnClickListener(v -> {
            Log.d("idpost",postid);
            Log.d("idloan",loanid);
            Intent intent = new Intent(mContext, LoanCreateActivity.class);
            intent.putExtra("id",Integer.parseInt(loanid));
            intent.putExtra("post",Integer.parseInt(postid));
            mContext.startActivity(intent);
        });
        view.btn_edit.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, LoanCreateActivity.class);
            intent.putExtra("id",Integer.parseInt(loanid));
            intent.putExtra("post",Integer.parseInt(postid));
            mContext.startActivity(intent);
        });

        try{
            Service api = Client.getClient().create(Service.class);
            Call<Item> itemCall = api.getDetailpost(postid,basic_Encode);
            itemCall.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
//                    String postid = String.valueOf(response.body().g).substring(0, String.valueOf(model.getPost()).indexOf("."));
                    Glide.with(mContext).load(response.body().getFront_image_path()).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.no_image_available)).into(view.imageView);
                    view.title.setText(response.body().getTitle());
                    view.cost.setText("$"+model.getLoan_amount());

                    SimpleDateFormat sdf;
                    String create,approved_date,date_time;
                    create = model.getCreated();
                    approved_date = model.getApproved_date();
                    Log.d("131313",create+"44444"+approved_date);
                    if (approved_date == null){
                        date_time = create;
                        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    }else {
                        date_time = approved_date;
                        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    }
                    Log.d("5050505",date_time);

//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    long date = 0;
                    try {
                        date = sdf.parse(date_time).getTime();
                        Long now = System.currentTimeMillis();
                        CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);
                        view.date.setText(ago);
                    } catch (ParseException e) { e.printStackTrace(); }

                    view.txtview.setVisibility(View.GONE);
                    view.item_type.setVisibility(View.GONE);
                    view.textview1.setVisibility(View.GONE);
                    if (response.body().getPost_type().equals("sell")){
                        view.item_type.setText(R.string.sell_t);
                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_sell));
                    }else if (response.body().getPost_type().equals("buy")){
                        view.item_type.setText(R.string.buy_t);
                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_buy));
                    }else {
                        view.item_type.setText(R.string.rent);
                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_rent));
                    }
                    view.btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            dialog.setTitle(R.string.cancel_loan)
                                    .setMessage(R.string.delete_loan)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try{
                                                Service api = Client.getClient().create(Service.class);
//                                                String created_by = String.valueOf(model.getCreated_by()).substring(0, String.valueOf(model.getCreated_by()).indexOf("."));
//                                                int Created_by = Integer.parseInt(created_by);
                                                String post = String.valueOf(model.getPost()).substring(0, String.valueOf(model.getPost()).indexOf("."));
                                                int Post_by = Integer.parseInt(post);
                                                Item_loan item_loan = new Item_loan(model.getLoan_to(),model.getLoan_amount(),model.getLoan_interest_rate(),model.getLoan_duration(),model.getLoan_purpose(),2,12,model.getUsername(),model.getGender(),model.getAge(),model.getJob(),model.getAverage_income(),model.getAverage_expense(),model.getTelephone(),model.getAddress(),true,model.isFamily_book(),model.isStaff_id(),model.isHouse_plant(),model.getMfi(),model.getCreated(),model.getCreated_by(),model.getModified(),model.getModified_by(),model.getReceived_date(),model.getReceived_by(),model.getRejected_date(),model.getRejected_by(),model.getRejected_comments(),Post_by);
//                                               Item_loan item_loan = new Item_loan(2,12);
                                                Call<Item_loan> call_loan = api.getputcancelloan(Integer.parseInt(loanid),item_loan,basic_Encode);
                                                call_loan.enqueue(new Callback<Item_loan>() {
                                                    @Override
                                                    public void onResponse(Call<Item_loan> call, Response<Item_loan> response) {
                                                        if (!response.isSuccessful()){
                                                            Log.d("444444", String.valueOf(response.code()));
                                                        }
                                                        Intent intent = new Intent(mContext, Account.class);
                                                        mContext.startActivity(intent);
                                                        ((Activity)mContext).finish();
                                                        if (response.code() == 400) {
                                                            try {
                                                                Log.v("Error code 400",response.errorBody().string());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Item_loan> call, Throwable t) {
                                                        Log.d("ERROR",t.getMessage());
                                                    }
                                                });
                                            }catch (Exception e){Log.d("CAtchMessage",e.getMessage());}
                                        }
                                    }).setNegativeButton(android.R.string.no, null).show();
                        }
                    });

//count View
//                    try{
//                        Service apiServiece = Client.getClient().create(Service.class);
//                        Call<AllResponse> call_view = apiServiece.getCount(postid,basic_Encode);
//                        call_view.enqueue(new Callback<AllResponse>() {
//                            @Override
//                            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
//                                view.txtview.setText(String.valueOf(response.body().getCount()));
//                            }
//
//                            @Override
//                            public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
//                        });
//                    }catch (Exception e){Log.d("Error e",e.getMessage());}

                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

                }
            });
        }catch (Exception e){}
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,cost,date,item_type,txtview,textview1;
        ImageView imageView;
        Button btn_edit,btn_cancel;
        LinearLayout linearLayout;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            date = view.findViewById(R.id.date);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.view);
            btn_edit = view.findViewById(R.id.btnedit);
            btn_cancel = view.findViewById(R.id.btndelete);
            linearLayout = view.findViewById(R.id.linearLayout);
            textview1 = view.findViewById(R.id.user_view);
        }
    }
}