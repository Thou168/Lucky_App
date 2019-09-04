package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Account;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.LikebyUser;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_unlike;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_Likebyuser extends RecyclerView.Adapter<Adapter_Likebyuser.ViewHolder> {

    private List<LikebyUser> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_Likebyuser(List<LikebyUser> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list2,viewGroup,false);

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
        final LikebyUser model = datas.get(position);
        String iditem = String.valueOf(model.getPost()).substring(0, String.valueOf(model.getPost()).indexOf("."));
        String itemid_like = String.valueOf(model.getId()).substring(0, String.valueOf(model.getId()).indexOf("."));
        try{
            Service api = Client.getClient().create(Service.class);
            Call<Item> call = api.getDetailpost(iditem,basic_Encode);
            call.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Glide.with(mContext).load(response.body().getFront_image_path()).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.no_image_available)).into(view.imageView);
                    view.title.setText(response.body().getTitle());
                    Double rs_price=0.0;
                    if (response.body().getDiscount().equals("0.00")){
                        Double co_cost = Double.parseDouble(response.body().getCost());
                        view.cost.setText("$"+co_cost);
                        view.linearLayout.setOnClickListener(v -> {
                        Intent intent = new Intent(mContext, Detail_New_Post.class);
                        intent.putExtra("ID",Integer.parseInt(iditem));
                        mContext.startActivity(intent);
                    });
                    }else {
                        rs_price = Double.parseDouble(response.body().getCost());
                        if (response.body().getDiscount_type().equals("amount")){
                            rs_price = rs_price - Double.parseDouble(response.body().getDiscount());
                        }else if (response.body().equals("percent")){
                            Double per = Double.parseDouble(response.body().getCost()) *( Double.parseDouble(response.body().getDiscount())/100);
                            rs_price = rs_price - per;
                        }
                        Double finalRs_price = rs_price;
                        Log.d("565656","56"+finalRs_price);
                        view.linearLayout.setOnClickListener(v -> {
                            Intent intent = new Intent(mContext, Detail_New_Post.class);
                            intent.putExtra("Discount", finalRs_price);
                            intent.putExtra("ID",Integer.parseInt(iditem));
                            mContext.startActivity(intent);
                        });
                        view.cost.setText("$"+rs_price);
                        view.txt_discount.setVisibility(View.VISIBLE);
                        Double co_price = Double.parseDouble(response.body().getCost());
                        view.txt_discount.setText("$"+co_price);
                        view.txt_discount.setPaintFlags(view.txt_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
//                    view.linearLayout.setOnClickListener(v -> {
//                        Intent intent = new Intent(mContext, Detail_New_Post.class);
//                        intent.putExtra("Price", response.body().getCost());
//                        intent.putExtra("ID",Integer.parseInt(iditem));
//                        mContext.startActivity(intent);
//                    });
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
                    Log.d("545445",response.body().getCreated());
                    long date = 0;
                    try {
                        date = sdf.parse(response.body().getApproved_date()).getTime();
                        Long now = System.currentTimeMillis();
                        CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);
                        view.date.setText(ago);
                    } catch (ParseException e) { e.printStackTrace(); }
//Count View
                    try{
                        Service apiServiece = Client.getClient().create(Service.class);
                        Call<AllResponse> call_view = apiServiece.getCount(iditem,basic_Encode);
                        call_view.enqueue(new Callback<AllResponse>() {
                            @Override
                            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                view.txtview.setText(String.valueOf(response.body().getCount()));
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
                        });
                    }catch (Exception e){Log.d("Error e",e.getMessage());}
//Button Unlike
                    view.btn_unlike.setOnClickListener(v -> {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle(R.string.title_unlike)
                                .setMessage(R.string.unlike_message)
                                .setCancelable(false)
                                .setPositiveButton(R.string.yes_loan, (dialog1, which) -> {

                                    Service api1 = Client.getClient().create(Service.class);
                                    change_status_unlike unlike = new change_status_unlike(null,Integer.parseInt(iditem),pk,2);
//
                                    Call<change_status_unlike> call_unlike = api1.getputStatusUnlike(Integer.parseInt(itemid_like),unlike,basic_Encode);
                                    call_unlike.enqueue(new Callback<change_status_unlike>() {
                                        @Override
                                        public void onResponse(Call<change_status_unlike> call1, Response<change_status_unlike> response1) {
                                            Intent intent = new Intent(mContext, Account.class);
                                            mContext.startActivity(intent);
                                            ((Activity)mContext).finish();
                                        }

                                        @Override
                                        public void onFailure(Call<change_status_unlike> call1, Throwable t) {
                                        }
                                    });
                                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    });
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) { }
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
        TextView title,cost,item_type,txtview,date,txt_discount;
        ImageView imageView;
        ImageButton btn_unlike;
        LinearLayout linearLayout;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            date = view.findViewById(R.id.date);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.user_view);
            btn_unlike = view.findViewById(R.id.imgbtn_unlike);
            linearLayout = view.findViewById(R.id.linearLayout);
            txt_discount = view.findViewById(R.id.tv_discount);
        }
    }
}