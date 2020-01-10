package com.bt_121shoppe.motorbike.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.R;

public class CustomView extends FrameLayout {

    ImageView textView,done;

    public CustomView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.backgroundiem, this);
        textView = (ImageView) getRootView().findViewById(R.id.textView);
        done = (ImageView) getRootView().findViewById(R.id.done);
    }

    public void display(int text, boolean isSelected) {
        textView.setImageResource(text);
        display(isSelected);
    }
    public void display(boolean isSelected) {
        if (isSelected) {
            done.setVisibility(VISIBLE);
        }
        else {
            done.setVisibility(GONE);
        }
    }
}
