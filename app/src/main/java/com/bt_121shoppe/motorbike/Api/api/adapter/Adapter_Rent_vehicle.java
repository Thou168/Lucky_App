package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_Rent_vehicle extends RecyclerView.Adapter<Adapter_Rent_vehicle.ViewHolder> {

    private List<Item> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;
    public Adapter_Rent_vehicle(List<Item> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_rent,viewGroup,false);
//
//        prefer = mContext.getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE);
//        name = prefer.getString("name","");
//        pass = prefer.getString("pass","");
//
//        if (prefer.contains("token")) {
//            pk = prefer.getInt("Pk", 0);
//        } else if (prefer.contains("id")) {
//            pk = prefer.getInt("id", 0);
//        }
//        basic_Encode = "Basic "+getEncodedString(name,pass);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder view, final int position) {
        final Item model = datas.get(position);
        String iditem = String.valueOf(model.getId()).substring(0, String.valueOf(model.getId()).indexOf("."));

        view.title.setText(model.getTitle());
        Glide.with(mContext).load(model.getFront_image_path()).apply(new RequestOptions().placeholder(R.drawable.no_image_available)).into(view.imageView);
        view.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Detail_new_post_java.class);
            intent.putExtra("Price", model.getCost());
//            intent.putExtra("postt", 1);
            intent.putExtra("ID",Integer.parseInt(iditem));
            mContext.startActivity(intent);
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        long date = 0;
        try {
            date = sdf.parse(model.getCreated()).getTime();
            Long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);
            Log.d("131312", String.valueOf(ago));
            Log.d("131312", model.getCreated());
            view.date.setText(ago);
        } catch (ParseException e) { e.printStackTrace(); }
        view.title.setText(model.getTitle());
        DecimalFormat f = new DecimalFormat("#,###.##");
        view.cost.setText("$ "+f.format(Double.parseDouble(model.getCost())));
        Glide.with(mContext).load(model.getFront_image_path()).apply(new RequestOptions().placeholder(R.drawable.no_image_available)).into(view.imageView);
        if (model.getPost_type().equals("sell")){
            view.item_type.setText(R.string.sell_t);
//            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_sell));
            view.item_type.setBackgroundResource(R.drawable.roundimage);
        }else if (model.getPost_type().equals("buy")){
            view.item_type.setText(R.string.buy_t);
            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_buy));
        }else {
            view.item_type.setText(R.string.rent);
//            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_rent));
            view.item_type.setBackgroundResource(R.drawable.roundimage_rent);
        }
        try{
            Service apiServiece = Client.getClient().create(Service.class);
            Call<AllResponse> call = apiServiece.getCount(iditem,basic_Encode);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    view.txtview.setText(String.valueOf(response.body().getCount()));
                    CommomAPIFunction.getUserProfileFB(mContext,view.img_user,"086595985");
                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
            });
        }catch (Exception e){Log.d("Error e",e.getMessage());}
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
        TextView title,cost,date,item_type,txtview;
        ImageView imageView;
        LinearLayout linearLayout;
        CircleImageView img_user;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            date = view.findViewById(R.id.location);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.user_view);
            linearLayout = view.findViewById(R.id.linearLayout);
            img_user = view.findViewById(R.id.img_user);
        }
    }
}