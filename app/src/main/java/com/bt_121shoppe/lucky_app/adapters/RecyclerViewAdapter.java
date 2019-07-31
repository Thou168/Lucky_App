package com.bt_121shoppe.lucky_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.holders.TextItemViewHolder;

public class RecyclerViewAdapter extends RecyclerView.Adapter<TextItemViewHolder> {

    String[] items;

    public RecyclerViewAdapter(String[] items) {
        this.items = items;
    }

    @Override
    public TextItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_item, parent, false);
        return new TextItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextItemViewHolder holder, int position) {
        holder.bind(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder {
    }
}
