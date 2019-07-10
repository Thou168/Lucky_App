package com.example.lucky_app.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lucky_app.R;

public class TextItemViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;
    public TextItemViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.list_item);
    }
    public void bind(String text) {
        textView.setText(text);
    }
}
