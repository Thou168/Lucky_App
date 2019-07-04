package com.example.lucky_app.Startup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lucky_app.Product_New_Post.Detail_New_Post;
import com.example.lucky_app.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Created by Hp on 3/17/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyHolder> implements Filterable {

    Context c;
    ArrayList<Item> items,filterList;
    CustomFilter filter;


    public MyAdapter(Context ctx, ArrayList<Item> players)
    {
        this.c=ctx;
        this.items =players;
        this.filterList=players;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //CONVERT XML TO VIEW ONBJ
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null);

        //HOLDER
        MyHolder holder=new MyHolder(v);

        return holder;
    }

    //DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        //BIND DATA
//        holder.posTxt.setText(Double.toString(items.get(position).getCast()));
        if(items.get(position).getPost_type().equals("Buy")){
            holder.type_post.setImageResource(R.drawable.buy);
        }else if(items.get(position).getPost_type().equals("Sell")){
            holder.type_post.setImageResource(R.drawable.sell);
        }else{
            holder.type_post.setImageResource(R.drawable.rent);
        }
        holder.nameTxt.setText(items.get(position).getTitle());
        holder.img.setImageResource(items.get(position).getImage());

        //IMPLEMENT CLICK LISTENET
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(v.getContext(), Detail_New_Post.class);
                intent.putExtra("Image","https://i.redd.it/obx4zydshg601.jpg");
                v.getContext().startActivity(intent);
            }
        });

    }

    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return items.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }
}
