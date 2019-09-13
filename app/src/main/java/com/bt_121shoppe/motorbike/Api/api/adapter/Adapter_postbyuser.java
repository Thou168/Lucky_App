package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
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
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import com.bt_121shoppe.motorbike.Activity.Account;
import com.bt_121shoppe.motorbike.Activity.Camera;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_delete;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_postbyuser extends RecyclerView.Adapter<Adapter_postbyuser.ViewHolder> implements View.OnClickListener{

    private List<Item> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;
    private OnItemClickListener onItemClickListener;

    public Adapter_postbyuser(List<Item> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list1t,viewGroup,false);

        prefer = mContext.getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE);
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
        final Item model = datas.get(position);
        String iditem = String.valueOf(model.getId()).substring(0, String.valueOf(model.getId()).indexOf("."));
        String type_status = null;
        if (model.getSales().size() == 1){
            for (int i=0;i<model.getSales().size();i++){
                type_status = String.valueOf(model.getSales().get(i).getSale_status()).substring(0,String.valueOf(model.getSales().get(i).getSale_status()).indexOf("."));
            }
        }
        else if (model.getBuy().size() == 1){
            for (int i=0;i<model.getBuy().size();i++){
                type_status = String.valueOf(model.getBuy().get(i).getBuy_status()).substring(0,String.valueOf(model.getBuy().get(i).getBuy_status()).indexOf("."));
            }
        }else  {
            for (int i = 0; i < model.getRent().size(); i++) {
                type_status = String.valueOf(model.getRent().get(i).getRent_status()).substring(0, String.valueOf(model.getRent().get(i).getRent_status()).indexOf("."));
            }
        }

        if(type_status.equals("3")){
            view.btn_renewal.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            view.btn_renewal.setTextColor(Color.parseColor("#FF9400"));
            view.btn_renewal.setText(R.string.pending);
            view.btn_renewal.setOnClickListener(v -> { });
        }
        else {
            view.btn_renewal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.refresh, 0, 0, 0);
            view.btn_renewal.setTextColor(Color.parseColor("#0A0909"));
            view.btn_renewal.setText(R.string.renew);
            view.btn_renewal.setOnClickListener(v -> new AlertDialog.Builder(mContext)
                   .setTitle(R.string.Post_Renewal)
                   .setMessage(R.string.renew_post)
                   .setIcon(android.R.drawable.ic_dialog_alert)
                   .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                       String date = null;
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                           date = Instant.now().toString();
                       }
                       change_status_delete change_status = new change_status_delete(4,date,pk,"");
                       Service api = Client.getClient().create(Service.class);
                       Call<change_status_delete> call = api.getputStatus(Integer.parseInt(iditem),change_status,basic_Encode);
                       call.enqueue(new Callback<change_status_delete>() {
                           @Override
                           public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {
                               if (!response.isSuccessful()){
                                   Log.d("10101", String.valueOf(response.code()));
                                   return;
                               }
                               FBPostCommonFunction.renewalPost(iditem);
                               Intent intent = new Intent(mContext, Account.class);
                               mContext.startActivity(intent);
                           }

                           @Override
                           public void onFailure(Call<change_status_delete> call, Throwable t) {
                               Log.d("Error12",t.getMessage());
                           }
                       });
                   })
                   .setNegativeButton(android.R.string.no,null)
                   .show());
        }

        view.btn_edit.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Camera.class);
            intent.putExtra("id_product",Integer.parseInt(iditem));
            mContext.startActivity(intent);
        });
        Double rs_price = 0.0;
        String[] delet_item = mContext.getResources().getStringArray(R.array.dailog_delete);
        view.btn_delete.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setItems(delet_item, (dialog1, which) -> {
                if (delet_item[which].equals(delet_item[3])){
                    dialog1.dismiss();
                }else {
                    String date = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        date = Instant.now().toString();
                    }
                    String item = delet_item[which];
                    change_status_delete change_status = new change_status_delete(2,date,pk,item);
                    Service api = Client.getClient().create(Service.class);
                    Call<change_status_delete> call = api.getputStatus(Integer.parseInt(iditem),change_status,basic_Encode);
                    call.enqueue(new Callback<change_status_delete>() {
                        @Override
                        public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {

                            Intent intent = new Intent(mContext, Account.class);
                            mContext.startActivity(intent);
                            ((Activity)mContext).finish();
//                 // delete item without intent by samang 9/9/19
//                            datas.remove(position);
//                            notifyItemRemoved(position);
//                            notifyItemRangeChanged(position, datas.size());
                        }

                        @Override
                        public void onFailure(Call<change_status_delete> call, Throwable t) {

                        }
                    });
                }
            });
            dialog.create().show();
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long date = 0;
        try {
            date = sdf.parse(model.getCreated()).getTime();
            Long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);

            view.date.setText(ago);
        } catch (ParseException e) { e.printStackTrace(); }
        view.title.setText(model.getTitle());
        if (model.getDiscount().equals("0.00")){
            view.cost.setText("$"+model.getCost());
        }else {
            rs_price = Double.parseDouble(model.getCost());
            if (model.getDiscount_type().equals("amount")){
                rs_price = rs_price - Double.parseDouble(model.getDiscount());
            }else if (model.getDiscount_type().equals("percent")){
                Double per = Double.parseDouble(model.getCost()) *( Double.parseDouble(model.getDiscount())/100);
                rs_price = rs_price - per;
            }
            view.cost.setText("$"+rs_price);
            view.txt_discount.setVisibility(View.VISIBLE);
            Double co_price = Double.parseDouble(model.getCost());
            view.txt_discount.setText("$"+co_price);
            view.txt_discount.setPaintFlags(view.txt_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        Double finalRs_price = rs_price;
        view.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Detail_New_Post.class);
            intent.putExtra("Price", model.getCost());
            intent.putExtra("Discount", finalRs_price);
            if (model.getStatus()==3){
                intent.putExtra("postt", 1);
            }
            intent.putExtra("ID",Integer.parseInt(iditem));
            mContext.startActivity(intent);
        });

        Glide.with(mContext).load(model.getFront_image_path()).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.no_image_available)).into(view.imageView);
        if (model.getPost_type().equals("sell")){
            view.item_type.setText(R.string.sell_t);
            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_sell));
        }else if (model.getPost_type().equals("buy")){
            view.item_type.setText(R.string.buy_t);
            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_buy));
        }else {
            view.item_type.setText(R.string.rent);
            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_rent));
        }
        try{
            Service apiServiece = Client.getClient().create(Service.class);
            Call<AllResponse> call = apiServiece.getCount(iditem,basic_Encode);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    view.txtview.setText(String.valueOf(response.body().getCount()));
                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
            });
        }catch (Exception e){Log.d("Error e",e.getMessage());}
    }
    public void updateData(List<Item> viewModels) {
        datas.clear();
        datas.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
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

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickListener.onItemClick(v, (ViewModel) v.getTag());
                }
            }, 0);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, ViewModel viewModel);

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,cost,date,item_type,txtview,txt_discount;
        ImageView imageView;
        Button btn_renewal,btn_edit,btn_delete;
        LinearLayout linearLayout;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            txt_discount = view.findViewById(R.id.tv_discount);
            date = view.findViewById(R.id.date);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.user_view);
            btn_renewal = view.findViewById(R.id.btn_renew);
            btn_edit = view.findViewById(R.id.btnedit_post);
            btn_delete = view.findViewById(R.id.btndelete);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }
}