package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.R;

import java.util.List;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_history_store extends RecyclerView.Adapter<Adapter_history_store.ViewHolder> {

    private List<ShopViewModel> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_history_store(List<ShopViewModel> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history_store,viewGroup,false);

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

        ViewHolder(View view){
            super(view);

        }
    }
}