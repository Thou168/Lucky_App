package com.bt_121shoppe.motorbike.adapters;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG=AllPostAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private Callback callback;
    private List<PostProduct> mPostList;
    private String mView;

    public AllPostAdapter(List<PostProduct> postList,String view){
        this.mPostList=postList;
        this.mView=view;
    }

    public void setCallback(Callback callback){
        this.callback=callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                switch (mView){
                    case "Grid":
                        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid,parent,false));
                    case "Image":
                        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false));
                    case "List":
                        default:
                            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false));
                }
            case VIEW_TYPE_EMPTY:
                default:
                    return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_loading,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mPostList != null && mPostList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if(mPostList!=null && mPostList.size()>0){
            return mPostList.size();
        }
        else {
            return 1;
        }
    }

    public void addItems(List<PostProduct> postList){
        mPostList.addAll(postList);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder{

        ImageView coverImageView;
        ImageView typeImageView;
        TextView postTitle;
        TextView postLocationDT;
        TextView postPrice;
        TextView postOriginalPrice;
        TextView postView;
        TextView postLang;
        CircleImageView img_user;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImageView=itemView.findViewById(R.id.image);
            typeImageView=itemView.findViewById(R.id.post_type);
            postTitle=itemView.findViewById(R.id.title);
            postLocationDT=itemView.findViewById(R.id.location);
            postPrice=itemView.findViewById(R.id.tv_price);
            postOriginalPrice=itemView.findViewById(R.id.tv_discount);
            postView=itemView.findViewById(R.id.user_view);
            postLang=itemView.findViewById(R.id.user_view1);
            img_user = itemView.findViewById(R.id.img_user);
        }

        @Override
        protected void clear() {
            coverImageView.setImageDrawable(null);
            typeImageView.setImageDrawable(null);
            postTitle.setText("");
            postLocationDT.setText("");
            postPrice.setText("");
            postOriginalPrice.setText("");
            postView.setText("");
            //postLang.setText("");
        }

        public void onBind(int position){
            super.onBind(position);
            final PostProduct mPost=mPostList.get(position);

            Glide.with(itemView.getContext()).load(mPost.getPostImage()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).centerCrop().into(coverImageView);
            String lang=postLang.getText().toString();
            if(lang.equals("View:")) {
                if (mPost.getPostType().equals("sell"))
                    Glide.with(itemView.getContext()).load(R.drawable.sell).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPostType().equals("rent"))
                    Glide.with(itemView.getContext()).load(R.drawable.rent).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPostType().equals("buy"))
                    Glide.with(itemView.getContext()).load(R.drawable.buy).thumbnail(0.1f).into(typeImageView);
            }else{
                if (mPost.getPostType().equals("sell"))
                    Glide.with(itemView.getContext()).load(R.drawable.sell_kh).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPostType().equals("rent"))
                    Glide.with(itemView.getContext()).load(R.drawable.rent_kh).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPostType().equals("buy"))
                    Glide.with(itemView.getContext()).load(R.drawable.buy_kh).thumbnail(0.1f).into(typeImageView);
            }
            postTitle.setText(mPost.getPostTitle());
            postLocationDT.setText(mPost.getLocationDuration());

            double mPrice=0;
            if(Double.parseDouble(mPost.getDiscountAmount())>0) {
                postOriginalPrice.setVisibility(View.VISIBLE);
                postOriginalPrice.setText("$ "+mPost.getPostPrice());
                postOriginalPrice.setPaintFlags(postOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Double cost=Double.parseDouble(mPost.getPostPrice());
                if(mPost.getDiscountType().equals("amount")){
                    cost=cost-Double.parseDouble(mPost.getDiscountAmount());
                }else if(mPost.getDiscountType().equals("percent")){
                    Double discountPrice=cost*(Double.parseDouble(mPost.getDiscountAmount())/100);
                    cost=cost-discountPrice;
                }
                mPrice=cost;
                postPrice.setText("$ "+cost.toString());
            }else{
                postPrice.setText("$ "+mPost.getPostPrice());
                postOriginalPrice.setVisibility(View.GONE);
            }

            postView.setText(String.valueOf(mPost.getCountView()));

            double finalMPrice = mPrice;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(), Detail_New_Post.class);
                    intent.putExtra("Discount", finalMPrice);
                    intent.putExtra("Price",mPost.getPostPrice());
                    intent.putExtra("ID",mPost.getPostId());
                    itemView.getContext().startActivity(intent);
                }
            });
// 05 09 19 thou
            try{
                Service api = Client.getClient().create(Service.class);
                Call<User> call = api.getuser(mPost.getUser_id());
                call.enqueue(new retrofit2.Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        CommomAPIFunction.getUserProfileFB(itemView.getContext(),img_user,response.body().getUsername());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
            }catch (Exception e){ Log.d("TRY CATCH",e.getMessage());}
        }

    }

    public class LoadingViewHolder extends BaseViewHolder{

        ProgressBar progressBar;
        public LoadingViewHolder(View itemView){
            super(itemView);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

        @Override
        protected void clear() {

        }
    }
}
