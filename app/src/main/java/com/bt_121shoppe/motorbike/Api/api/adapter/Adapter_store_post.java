package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bt_121shoppe.motorbike.models.ShopViewModel;

import com.bt_121shoppe.motorbike.Activity.Postbyuser_Class;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_delete;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.time.Instant;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Adapter_store_post extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG= Adapter_store_post.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private List<ShopViewModel> mPostList;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    int pk=0;

    public Adapter_store_post(List<ShopViewModel> postlist){
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

    public void addItems(List<ShopViewModel> postList){
        mPostList.addAll(postList);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder{


        public ViewHolder(View itemView){
            super(itemView);

        }

        @Override
        protected void clear() {

        }

        public void onBind(int position){
            super.onBind(position);
            final ShopViewModel mPost=mPostList.get(position);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), Postbyuser_Class.class);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
