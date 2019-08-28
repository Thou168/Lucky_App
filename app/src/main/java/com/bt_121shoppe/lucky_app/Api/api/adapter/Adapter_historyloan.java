package com.bt_121shoppe.lucky_app.Api.api.adapter;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.Api.api.AllResponse;
import com.bt_121shoppe.lucky_app.Api.api.Client;
import com.bt_121shoppe.lucky_app.Api.api.Service;
import com.bt_121shoppe.lucky_app.Api.api.model.Item;
import com.bt_121shoppe.lucky_app.Api.api.model.Item_loan;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.loan.LoanCreateActivity;
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
public class Adapter_historyloan extends RecyclerView.Adapter<Adapter_historyloan.ViewHolder> {

    private List<Item_loan> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_historyloan(List<Item_loan> datas, Context mContext) {
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

        view.txtview.setVisibility(View.GONE);
        view.userview.setVisibility(View.GONE);
        view.relativeLayout.setVisibility(View.GONE);
        view.item_type.setVisibility(View.GONE);
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

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    long date = 0;
                    try {
                        date = sdf.parse(response.body().getCreated()).getTime();
                        Long now = System.currentTimeMillis();
                        CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);
                        view.date.setText(ago);
                    } catch (ParseException e) { e.printStackTrace(); }
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
        TextView title,cost,date,item_type,txtview,userview,itemtype;
        ImageView imageView;
        RelativeLayout relativeLayout;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            date = view.findViewById(R.id.date);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.view);
            userview = view.findViewById(R.id.user_view);
            item_type = view.findViewById(R.id.item_type);
            relativeLayout = view.findViewById(R.id.relative);
        }
    }
}