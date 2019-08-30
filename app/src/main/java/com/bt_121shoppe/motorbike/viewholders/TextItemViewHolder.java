package com.bt_121shoppe.motorbike.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.R;

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
