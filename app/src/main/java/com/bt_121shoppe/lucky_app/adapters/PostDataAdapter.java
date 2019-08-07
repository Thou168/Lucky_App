package com.bt_121shoppe.lucky_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.chats.ChatRentFragment;
import com.bt_121shoppe.lucky_app.interfaces.OnLoadMoreListener;
import com.bt_121shoppe.lucky_app.models.PostProduct;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostDataAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM=1;
    private final int VIEW_PROG=0;
    private int visibleThreshold=5;
    private int lastVisibleItem,totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private List<PostProduct> postProductList;
    private String gallaryType;
    private RecyclerView mRecyclerView;

    public PostDataAdapter(List<PostProduct> posts,RecyclerView recyclerView,String gallryType){
        postProductList=posts;
        this.gallaryType=gallryType;
        this.mRecyclerView=recyclerView;
        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount=linearLayoutManager.getItemCount();
                    lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                    if(!loading && totalItemCount<=(lastVisibleItem+visibleThreshold)){
                        if(onLoadMoreListener!=null){
                            onLoadMoreListener.onLaodMore();
                        }
                        loading=true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType==VIEW_ITEM){
            View v;
            if(gallaryType.equals("List")){
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
            }else if(gallaryType.equals("Grid")){
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid,parent,false);
            }else{
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
            }
            vh=new PostProductViewHolder(v);
        }
        else{
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar,parent,false);
            vh=new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PostProductViewHolder){
            PostProduct singlePost=(PostProduct) postProductList.get(position);
            Glide.with(mRecyclerView.getContext()).load("https://media.kmall24.com/media/catalog/product/cache/12/image/9df78eab33525d08d6e5fb8d27136e95/0/_/0_1e5v5bvgemjckn9g.jpg").into(((PostProductViewHolder)holder).postImage);
            ((PostProductViewHolder)holder).postProduct=singlePost;
        }
        else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return postProductList.size();
    }

    @Override
    public int getItemViewType(int position){
        return postProductList.get(position)!=null ? VIEW_ITEM:VIEW_PROG;
    }

    public void setLoaded(){loading=false;}
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener=onLoadMoreListener;
    }


    //
    public static class PostProductViewHolder extends RecyclerView.ViewHolder{
        ImageView postType,postImage;
        TextView postTitle,postPrice,locationDuration,countView,discountPrice;
        PostProduct postProduct;

        public PostProductViewHolder( View v) {
            super(v);
            postType=v.findViewById(R.id.post_type);
            postImage=v.findViewById(R.id.image);
            postTitle=v.findViewById(R.id.title);
            postPrice=v.findViewById(R.id.tv_price);
            locationDuration=v.findViewById(R.id.location);
            countView=v.findViewById(R.id.user_view);
            discountPrice=v.findViewById(R.id.tv_discount);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"on click: "+postProduct.getPostTitle(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    //
    public static class ProgressViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar=(ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}

