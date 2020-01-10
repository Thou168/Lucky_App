package com.bt_121shoppe.motorbike.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import com.bt_121shoppe.motorbike.activities.Camera;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bt_121shoppe.motorbike.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends BaseAdapter {
    private int[] item;
    private Context context;
    public List<Integer> selectedPositions = new ArrayList<>();

    public ColorAdapter(int[] item,Context context) {
        this.item = item;
        this.context = context;
    }

    @Override
    public int getCount() {
        return item.length;
    }

    @Override
    public Object getItem(int position) {
        return item[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomView customView;
        if (convertView == null) {
            customView = new CustomView(context);
            customView.display(item[position], selectedPositions.contains(position));
        } else {
            customView = (CustomView) convertView;
            customView.display(item[position], selectedPositions.contains(position));
        }
        return customView;
    }
}
