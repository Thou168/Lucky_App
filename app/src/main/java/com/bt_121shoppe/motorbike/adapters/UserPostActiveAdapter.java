package com.bt_121shoppe.motorbike.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Activity.Postbyuser_Class;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_delete;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.time.Instant;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserPostActiveAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG=UserPostActiveAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private List<Item> mPostList;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    int pk=0;

    public UserPostActiveAdapter(List<Item> postlist){
        this.mPostList=postlist;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        prefer = parent.getContext().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");

        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_Encode = "Basic "+ CommonFunction.getEncodedString(name,pass);
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list1t,parent,false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position){
        if(mPostList!=null && mPostList.size()>0)
            return VIEW_TYPE_NORMAL;
        else
            return VIEW_TYPE_EMPTY;
    }

    @Override
    public int getItemCount() {
        if(mPostList!=null && mPostList.size()>0)
            return mPostList.size();
        else
            return 0;
    }

    public void addItems(List<Item> postList){
        mPostList.addAll(postList);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder{

        TextView tvTitle,tvCost,tvDiscount,tvView,tvPostType,tvCountView,ds_price;
        ImageView ivPostImage;
        Button btRenewal,btEdit,btDelete;
        RelativeLayout relativeLayout;
        TextView cate;

        public ViewHolder(View itemView){
            super(itemView);
            ds_price=itemView.findViewById(R.id.ds_price);
            ivPostImage=itemView.findViewById(R.id.image);
            tvTitle=itemView.findViewById(R.id.title);
            tvCost=itemView.findViewById(R.id.tv_price);
            tvDiscount=itemView.findViewById(R.id.tv_discount);
            btRenewal=itemView.findViewById(R.id.btn_renew);
            btDelete=itemView.findViewById(R.id.btndelete);
            btEdit=itemView.findViewById(R.id.btnedit_post);
            tvView=itemView.findViewById(R.id.user_view1);
            tvPostType=itemView.findViewById(R.id.item_type);
            tvCountView=itemView.findViewById(R.id.user_view);
            relativeLayout = itemView.findViewById(R.id.relative_view);
            cate = itemView.findViewById(R.id.cate);
        }

        @Override
        protected void clear() {
            ivPostImage.setImageDrawable(null);
            tvTitle.setText("");
            tvCost.setText("");
            tvDiscount.setText("");
        }

        public void onBind(int position){
            super.onBind(position);
            final Item mPost=mPostList.get(position);
            String lang=tvView.getText().toString();
            String strPostTitle="";
            //post image
            Glide.with(itemView.getContext()).load(mPost.getFront_image_path()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).centerCrop().into(ivPostImage);
            //post title
//            if (mPost.getCategory()==1){
//                cate.setText(R.string.electronic);
//            }else {
//                cate.setText(R.string.motor);
//            }
            if(mPost.getPost_sub_title()==null||mPost.getPost_sub_title().isEmpty()){}
            else{
                if(lang.equals("View:"))
                    strPostTitle=mPost.getPost_sub_title().split(",")[0];
                else
                    strPostTitle=mPost.getPost_sub_title().split(",")[1];
            }
            Log.e(TAG,mPost.getPost_sub_title()+" " +strPostTitle );
            tvTitle.setText(strPostTitle);
            //post type icon
            if(mPost.getPost_type().equals("sell")){
                tvPostType.setText(R.string.sell_t);
//                tvPostType.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.color_sell));
                tvPostType.setBackgroundResource(R.drawable.roundimage);
            }else if(mPost.getPost_type().equals("rent")){
                tvPostType.setText(R.string.rent);
                tvPostType.setBackgroundResource(R.drawable.roundimage_rent);
//                tvPostType.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.color_rent));
            }

            //post price
            Double cost=0.0;
            if(mPost.getDiscount().equals("0.00")) {
                tvCost.setText("$ " + mPost.getCost());
                ds_price.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
            }
            else{
                cost=Double.parseDouble(mPost.getCost());
                if(mPost.getDiscount_type().equals("amount")) {
                    cost = cost - Double.parseDouble(mPost.getDiscount());
//                    double ds = Double.parseDouble(mPost.getDiscount());
//                    ds_price.setText("$"+ds);
//                    relativeLayout.setVisibility(View.GONE);
                }
                else if(mPost.getDiscount_type().equals("percent")){
                    Double per = Double.parseDouble(mPost.getCost()) *( Double.parseDouble(mPost.getDiscount())/100);
//                    int per1 = (int) ( Double.parseDouble(mPost.getDiscount()));
                    cost = cost - per;
//                    ds_price.setText(per1+"%");
                }
                tvCost.setText("$"+cost);
                tvDiscount.setVisibility(View.VISIBLE);
                Double co_price = Double.parseDouble(mPost.getCost());
                tvDiscount.setText("$"+co_price);
                tvDiscount.setPaintFlags(tvDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            //count view
            try{
                Service apiServiece = Client.getClient().create(Service.class);
                Call<AllResponse> call = apiServiece.getCount(String.valueOf((int)mPost.getId()),basic_Encode);
                //Log.d("111111",basic_Encode);
                call.enqueue(new retrofit2.Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        tvCountView.setText(String.valueOf(response.body().getCount()));
                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
                });
            }catch (Exception e){Log.d("Error e",e.getMessage());}


            if((int) mPost.getStatus()==3){
                btRenewal.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                btRenewal.setTextColor(Color.parseColor("#FF9400"));
                btRenewal.setText(R.string.pending);
            }else{
                //btRenewal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_autorenew_black_24dp, 0, 0, 0);
                btRenewal.setTextColor(Color.parseColor("#0A0909"));
                btRenewal.setText(R.string.renew);

                btRenewal.setOnClickListener(v -> new AlertDialog.Builder(itemView.getContext())
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
                            Call<change_status_delete> call = api.getputStatus((int)mPost.getId(),change_status,basic_Encode);
                            call.enqueue(new retrofit2.Callback<change_status_delete>() {
                                @Override
                                public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {
                                    if (!response.isSuccessful()){
                                        Log.d("10101", String.valueOf(response.code()));
                                        return;
                                    }
                                    FBPostCommonFunction.renewalPost(String.valueOf((int)mPost.getId()));
                                    Intent intent = new Intent(itemView.getContext(), Account.class);
                                    itemView.getContext().startActivity(intent);
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

            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), Camera.class);
                    intent.putExtra("id_product",Integer.parseInt(String.valueOf((int)mPost.getId())));
                    itemView.getContext().startActivity(intent);
                }
            });

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] delet_item = itemView.getContext().getResources().getStringArray(R.array.dailog_delete);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(itemView.getContext());
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
                            Call<change_status_delete> call = api.getputStatus((int)mPost.getId(),change_status,basic_Encode);
                            call.enqueue(new retrofit2.Callback<change_status_delete>() {
                                @Override
                                public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {

                                    Intent intent = new Intent(itemView.getContext(), Account.class);
                                    itemView.getContext().startActivity(intent);
                                    ((Activity)itemView.getContext()).finish();
                                }

                                @Override
                                public void onFailure(Call<change_status_delete> call, Throwable t) {

                                }
                            });
                        }
                    });
                    dialog.create().show();
                }
            });
            Double finalPrice=cost;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), Postbyuser_Class.class);
                    intent.putExtra("Price", mPost.getCost());
                    intent.putExtra("Discount", finalPrice);
                    if (mPost.getStatus()==3){
                        intent.putExtra("postt", 1);
                    }
                    intent.putExtra("ID",(int)mPost.getId());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
