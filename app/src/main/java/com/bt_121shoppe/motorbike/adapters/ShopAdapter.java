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

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import java.util.*;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private List<ShopViewModel> moviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.name);
            genre = (TextView) view.findViewById(R.id.idshop);
//            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public ShopAdapter(Context mContext,List<ShopViewModel> moviesList) {
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
        ShopViewModel movie = moviesList.get(position);
        holder.title.setText(movie.getShop_name());
        holder.genre.setText("Shop "+(position+1));
//        holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}