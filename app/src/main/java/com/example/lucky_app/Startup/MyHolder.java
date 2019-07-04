package com.example.lucky_app.Startup;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lucky_app.R;

/**
 * Created by Hp on 3/17/2016.
 */
public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //OUR VIEWS
    ImageView type_post;
    ImageView img;
    TextView nameTxt,posTxt;

    ItemClickListener itemClickListener;


    public MyHolder(View itemView) {
        super(itemView);
//
        this.img= (ImageView) itemView.findViewById(R.id.image);
        this.nameTxt= (TextView) itemView.findViewById(R.id.title);
        this.type_post = (ImageView) itemView.findViewById(R.id.post_type);
//        this.posTxt= (TextView) itemView.findViewById(R.id.tv_price);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());

    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}
