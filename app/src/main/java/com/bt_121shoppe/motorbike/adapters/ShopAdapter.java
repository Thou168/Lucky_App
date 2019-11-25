package com.bt_121shoppe.motorbike.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.detail_shop;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private List<detail_shop> moviesList;
    private List<detail_shop> moviesList1;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.name);
            genre = view.findViewById(R.id.idshop);
//            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public ShopAdapter(Context mContext,List<detail_shop> moviesList) {
        this.moviesList = moviesList;
        this.context = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_shop, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        detail_shop shop = moviesList.get(position);
//        holder.title.setText(shop.getShop_name());
        String text = holder.itemView.getContext().getString(R.string.shop);
        holder.genre.setText(text+(position+1));
        Service api = Client.getClient().create(Service.class);
        Call<ShopViewModel> call = api.getDealerShop(shop.getShop());
        call.enqueue(new Callback<ShopViewModel>() {
            @Override
            public void onResponse(Call<ShopViewModel> call, Response<ShopViewModel> response) {
                if (!response.isSuccessful()){
                    Log.d("323232323232","32 "+response.code());
                }
                Log.d("323232323232","32 "+response.body().getShop_name());
                holder.title.setText(response.body().getShop_name());
            }

            @Override
            public void onFailure(Call<ShopViewModel> call, Throwable t) {
                Log.d("323232323232","Failure"+t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}