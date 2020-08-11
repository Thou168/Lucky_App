package com.bt_121shoppe.motorbike.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Activity.Postbyuser_Class;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_ListStore;
import com.bt_121shoppe.motorbike.Api.responses.APIStorePostResponse;
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
import com.bt_121shoppe.motorbike.homes.HomeAllPostAdapter;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.StorePostViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPostActiveAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG=UserPostActiveAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private List<Item> mPostList;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    int pk=0,shopID=0,id=0;
    String key = "";

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
        Button btRenewal,btEdit,btSold,btRemove;
        RelativeLayout relativeLayout;
        TextView cate;
        TextView tvColor1,tvColor2;
        TextView pending_appprove;
        LinearLayout user_active;
        public ViewHolder(View itemView){
            super(itemView);
            ds_price=itemView.findViewById(R.id.ds_price);
            ivPostImage=itemView.findViewById(R.id.image);
            tvTitle=itemView.findViewById(R.id.title);
            tvCost=itemView.findViewById(R.id.tv_price);
            tvDiscount=itemView.findViewById(R.id.tv_discount);
            btRenewal=itemView.findViewById(R.id.btn_renew);
            btSold=itemView.findViewById(R.id.btnsold);
            btEdit=itemView.findViewById(R.id.btnedit_post);
            btRemove=itemView.findViewById(R.id.btn_remove);
            tvView=itemView.findViewById(R.id.user_view1);
            tvPostType=itemView.findViewById(R.id.item_type);
            tvCountView=itemView.findViewById(R.id.user_view);
            relativeLayout = itemView.findViewById(R.id.relative_view);
            cate = itemView.findViewById(R.id.cate);
            tvColor1=itemView.findViewById(R.id.tv_color1);
            tvColor2=itemView.findViewById(R.id.tv_color2);
            pending_appprove=itemView.findViewById(R.id.pending_appprove);
            user_active=itemView.findViewById(R.id.linear_userActive);
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
//            post title
            if (mPost.getCategory()==1){
                cate.setText(R.string.electronic);
            }else {
                cate.setText(R.string.motor);
            }
            if(mPost.getPost_sub_title()==null||mPost.getPost_sub_title().isEmpty()){}
            else{
                if(lang.equals("View"))
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

            //String[] splitColor=mPost.getColor().split(",");
            String[] splitColor=mPost.getMulti_color_code().split(",");
            //Log.e("TAG","Post Color "+splitColor[0]+" 2 "+splitColor[1]);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[0])));
            tvColor1.setBackground(shape);
            tvColor2.setVisibility(View.GONE);
            if(splitColor.length>1){
                tvColor2.setVisibility(View.VISIBLE);
                GradientDrawable shape1 = new GradientDrawable();
                shape1.setShape(GradientDrawable.OVAL);
                shape1.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[1])));
                tvColor2.setBackground(shape1);
            }

            //post price
            DecimalFormat f = new DecimalFormat("#,###.##");
            String NoDc,HaveDc,Price_Full_dis;
            double cost=0.0;
            if(mPost.getDiscount().equals("0.00")) {
                NoDc = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(mPost.getCost()));
//                tvCost.setText("$ " + f.format(Double.parseDouble(mPost.getCost())));
                tvCost.setText("$ " + NoDc);
                ds_price.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
            }
            else{
                cost=Double.parseDouble(mPost.getCost());
                relativeLayout.setVisibility(View.GONE);
                tvDiscount.setVisibility(View.VISIBLE);
                int per1 = (int) ( Double.parseDouble(mPost.getDiscount()));
                double co_price = Double.parseDouble(String.valueOf(per1 * cost))/100;
                double price = cost - co_price;
//                DecimalFormat formatter = new DecimalFormat("#0.00");
//                tvCost.setText("$"+formatter.format(price));
                HaveDc = NumberFormat.getNumberInstance(Locale.US).format(price);
                Price_Full_dis = NumberFormat.getNumberInstance(Locale.US).format(cost);
                tvCost.setText("$ "+HaveDc);
                tvDiscount.setText("$ "+Price_Full_dis);
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


            if((int) mPost.getStatus()==3){  // for pending
//                btRenewal.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
//                btRenewal.setTextColor(Color.parseColor("#FF9400"));
//                btRenewal.setText(R.string.pending);
                pending_appprove.setText(R.string.pending);
                pending_appprove.setTextColor(Color.parseColor("#CCCCCC"));
                btSold.setVisibility(View.GONE);
                btRenewal.setVisibility(View.GONE);
                user_active.setVisibility(View.GONE);
            }else if ((int)mPost.getStatus()==4){  // for approve
                //btRenewal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_autorenew_black_24dp, 0, 0, 0);
                pending_appprove.setText(R.string.approveval);
                pending_appprove.setTextColor(Color.parseColor("#43BF64"));
                btRenewal.setTextColor(Color.parseColor("#0A0909"));
                btRenewal.setText(R.string.renew);
                btRenewal.setOnClickListener((View.OnClickListener) view -> {
                    LayoutInflater factory = LayoutInflater.from(itemView.getContext());
                    final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
                    final AlertDialog clearDialog = new AlertDialog.Builder(itemView.getContext()).create();
                    clearDialog.setView(clearDialogView);
                    clearDialog.setCancelable(false);
                    TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
                    Mssloan.setText(R.string.renew_post);
                    TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
                    title.setText(R.string.Post_Renewal);
                    Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
                    btnYes.setText(R.string.ok);
                    Button btnNo = (Button) clearDialogView.findViewById(R.id.button_negative);
                    btnNo.setText(R.string.cancel);
                    clearDialogView.findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clearDialog.dismiss();
                        }
                    });
                    clearDialogView.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                            clearDialog.dismiss();
                        }
                    });
                    clearDialog.show();
                });

                btEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(itemView.getContext(), Camera.class);
                        intent.putExtra("process_type", 1);
                        intent.putExtra("id_product", Integer.parseInt(String.valueOf((int) mPost.getId())));
                        itemView.getContext().startActivity(intent);
                    }
                });
//                btRenewal.setOnClickListener(v ->
//                        new AlertDialog.Builder(itemView.getContext())
//                        .setTitle(R.string.Post_Renewal)
//                        .setMessage(R.string.renew_post)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setCancelable(false)
//                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                            String date = null;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                date = Instant.now().toString();
//                            }
//                            change_status_delete change_status = new change_status_delete(4,date,pk,"");
//                            Service api = Client.getClient().create(Service.class);
//                            Call<change_status_delete> call = api.getputStatus((int)mPost.getId(),change_status,basic_Encode);
//                            call.enqueue(new retrofit2.Callback<change_status_delete>() {
//                                @Override
//                                public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {
//                                    if (!response.isSuccessful()){
//                                        Log.d("10101", String.valueOf(response.code()));
//                                        return;
//                                    }
//                                    FBPostCommonFunction.renewalPost(String.valueOf((int)mPost.getId()));
//                                    Intent intent = new Intent(itemView.getContext(), Account.class);
//                                    itemView.getContext().startActivity(intent);
//                                }
//
//                                @Override
//                                public void onFailure(Call<change_status_delete> call, Throwable t) {
//                                    Log.d("Error12",t.getMessage());
//                                }
//                            });
//                        })
//                        .setNegativeButton(android.R.string.no,null)
//                        .show());
            }else {  // for rejected
                pending_appprove.setText(R.string.reject);
                pending_appprove.setTextColor(Color.parseColor("#CCCCCC"));

                btRenewal.setVisibility(View.GONE);
                btSold.setVisibility(View.GONE);

//                btEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(itemView.getContext(), Camera.class);
//                        intent.putExtra("process_type", 1);
//                        intent.putExtra("id_product", Integer.parseInt(String.valueOf((int) mPost.getId())));
//                        itemView.getContext().startActivity(intent);
//                    }
//                });
            }

            btRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater factory = LayoutInflater.from(itemView.getContext());
                    final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
                    final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(itemView.getContext()).create();
                    clearDialog.setView(clearDialogView);
                    clearDialog.setCancelable(false);
                    TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
                    Mssloan.setText(R.string.remove_po);
                    TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
                    title.setText(R.string.for_reomve_title);
                    Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
                    btnYes.setText(R.string.yes_leave);
                    Button btnNo = (Button) clearDialogView.findViewById(R.id.button_negative);
                    btnNo.setText(R.string.no_leave);
                    clearDialogView.findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clearDialog.dismiss();
                        }
                    });
                    clearDialogView.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String date = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                date = Instant.now().toString();
                            }
                            change_status_delete change_status = new change_status_delete(2,date,pk,"");
                            Service api = Client.getClient().create(Service.class);
                            Call<change_status_delete> call = api.getputStatus((int)mPost.getId(),change_status,basic_Encode);
                            call.enqueue(new retrofit2.Callback<change_status_delete>() {
                                @Override
                                public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {
                                    if (!response.isSuccessful()){
                                        Log.d("Error Remove", String.valueOf(response.code()));
                                        return;
                                    }
                                    //FBPostCommonFunction.renewalPost(String.valueOf((int)mPost.getId()));
                                    FBPostCommonFunction.deletePost(String.valueOf((int)mPost.getId()));
                                    Intent intent = new Intent(itemView.getContext(), Account.class);
                                    itemView.getContext().startActivity(intent);
                                    ((Activity)itemView.getContext()).finish();
                                }

                                @Override
                                public void onFailure(Call<change_status_delete> call, Throwable t) {

                                }
                            });
                            clearDialog.dismiss();
                        }
                    });
                    clearDialog.show();
                }
            });
//            intent.putExtra("remove",key);
//            intent.putExtra("sold",key);
//                btEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(itemView.getContext(), Camera.class);
//                        intent.putExtra("process_type", 1);
//                        intent.putExtra("id_product", Integer.parseInt(String.valueOf((int) mPost.getId())));
//                        itemView.getContext().startActivity(intent);
//                    }
//                });
            btSold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String[] delet_item = itemView.getContext().getResources().getStringArray(R.array.dailog_delete);
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(itemView.getContext());
//                    dialog.setItems(delet_item, (dialog1, which) -> {
//                        if (delet_item[which].equals(delet_item[3])){
//                            dialog1.dismiss();
//                        }else {
//                            String date = null;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                date = Instant.now().toString();
//                            }
//                            String item = delet_item[which];
//                            change_status_delete change_status = new change_status_delete(7,date,pk,item);
//                            Service api = Client.getClient().create(Service.class);
//                            Call<change_status_delete> call = api.getputStatus((int)mPost.getId(),change_status,basic_Encode);
//                            call.enqueue(new retrofit2.Callback<change_status_delete>() {
//                                @Override
//                                public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {
//
//                                    Intent intent = new Intent(itemView.getContext(), Account.class);
//                                    itemView.getContext().startActivity(intent);
//                                    ((Activity)itemView.getContext()).finish();
//                                }
//
//                                @Override
//                                public void onFailure(Call<change_status_delete> call, Throwable t) {
//
//                                }
//                            });
//                        }
//                    });
//                    dialog.create().show();
                    LayoutInflater factory = LayoutInflater.from(itemView.getContext());
                    final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
                    final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(itemView.getContext()).create();
                    clearDialog.setView(clearDialogView);
                    clearDialog.setCancelable(false);
                    TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
                    Mssloan.setText(R.string.sold_po);
//                    TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
//                    title.setText(R.string.for_reomve_title);
                    Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
                    btnYes.setText(R.string.ok);
                    Button btnNo = (Button) clearDialogView.findViewById(R.id.button_negative);
                    btnNo.setText(R.string.cancel);
                    clearDialogView.findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clearDialog.dismiss();
                        }
                    });
                    clearDialogView.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String date = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                date = Instant.now().toString();
                            }
                            change_status_delete change_status = new change_status_delete(7,date,pk,"");
                            Service api = Client.getClient().create(Service.class);
                            Call<change_status_delete> call = api.getputStatus((int)mPost.getId(),change_status,basic_Encode);
                            call.enqueue(new retrofit2.Callback<change_status_delete>() {
                                @Override
                                public void onResponse(Call<change_status_delete> call, Response<change_status_delete> response) {
                                    FBPostCommonFunction.soldPost(String.valueOf((int)mPost.getId()));
                                    Intent intent = new Intent(itemView.getContext(), Account.class);
                                    itemView.getContext().startActivity(intent);
                                    ((Activity)itemView.getContext()).finish();
                                }

                                @Override
                                public void onFailure(Call<change_status_delete> call, Throwable t) {

                                }
                            });
                            clearDialog.dismiss();
                        }
                    });
                    clearDialog.show();
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
                        intent.putExtra("postt", 2);
                    }else if (mPost.getStatus()!=2) {
                        intent.putExtra("postt", 1);
                    }
                    intent.putExtra("muticolor",splitColor);
                    intent.putExtra("ID",(int)mPost.getId());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
