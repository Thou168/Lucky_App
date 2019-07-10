package com.example.lucky_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.lucky_app.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    private ArrayList<String> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;

    }

    public CustomAdapter(ArrayList data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public String getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(getItem(position));
        // Return the completed view to render on screen
        return convertView;
    }
}
