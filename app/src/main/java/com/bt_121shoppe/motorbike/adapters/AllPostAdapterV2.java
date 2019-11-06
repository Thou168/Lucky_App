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

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class AllPostAdapterV2 extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG=AllPostAdapterV2.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private AllPostAdapter.Callback callback;
    private List<PostViewModel> mPostList;
    private String mView;
    private String jok;

    public AllPostAdapterV2(List<PostViewModel> postList,String view){
        this.mPostList=postList;
        this.mView=view;
    }

    public void setCallback(AllPostAdapter.Callback callback){
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

    public void addItems(List<PostViewModel> postList){
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
        TextView postDiscount;
        TextView postOriginalPrice;
        TextView postView;
        CircleImageView img_user;
        TextView postLang;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImageView=itemView.findViewById(R.id.image);
            typeImageView=itemView.findViewById(R.id.post_type);
            postTitle=itemView.findViewById(R.id.title);
            postLocationDT=itemView.findViewById(R.id.location);
            postPrice=itemView.findViewById(R.id.tv_price);
            postOriginalPrice=itemView.findViewById(R.id.tv_discount);
            postView=itemView.findViewById(R.id.user_view);
            img_user = itemView.findViewById(R.id.img_user);
            postLang=itemView.findViewById(R.id.user_view1);
        }

        @Override
        protected void clear() {
            coverImageView.setImageDrawable(null);
            typeImageView.setImageDrawable(null);
            postTitle.setText("");
            postLocationDT.setText("");
            postOriginalPrice.setText("");
            postView.setText("");
            //postLang.setText("");
        }

        public void onBind(int position){
            super.onBind(position);
            final PostViewModel mPost=mPostList.get(position);

            //postView.setText(String.valueOf(mPost.getCountView()));

            Glide.with(itemView.getContext()).load(mPost.getFront_image_path()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(coverImageView);
            String lang=postLang.getText().toString();
            if(lang.equals("View:")) {
                if (mPost.getPost_type().equals("sell"))
                    Glide.with(itemView.getContext()).load(R.drawable.sell).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPost_type().equals("rent"))
                    Glide.with(itemView.getContext()).load(R.drawable.rent).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPost_type().equals("buy"))
                    Glide.with(itemView.getContext()).load(R.drawable.buy).thumbnail(0.1f).into(typeImageView);
            }else{
                if (mPost.getPost_type().equals("sell"))
                    Glide.with(itemView.getContext()).load(R.drawable.sell_kh).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPost_type().equals("rent"))
                    Glide.with(itemView.getContext()).load(R.drawable.rent_kh).thumbnail(0.1f).into(typeImageView);
                else if (mPost.getPost_type().equals("buy"))
                    Glide.with(itemView.getContext()).load(R.drawable.buy_kh).thumbnail(0.1f).into(typeImageView);
            }

            String strPostTitle="";
//            typeImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            if(mPost.getPost_sub_title().isEmpty()){
//                String fullTitle=CommonFunction.generatePostSubTitle(mPost.getModeling(),mPost.getYear(),mPost.getColor());
//                if(lang.equals("View:"))
//                    strPostTitle=fullTitle.split(",")[0];
//                else
//                    strPostTitle=fullTitle.split(",")[1];
            }else {
                if (lang.equals("View:")) {
                    strPostTitle = mPost.getPost_sub_title().split(",")[0];
                } else {
                    strPostTitle = mPost.getPost_sub_title().split(",")[1];
                }
            }

//            jok=strPostTitle;
//            if (jok.length()>36){
//                jok=jok.substring(0,36)+"...";
//                postTitle.setText(jok);
//            }else {
//                postTitle.setText(jok);
//
//            }
            postTitle.setText(strPostTitle);
            double mPrice=0;
            if(Double.parseDouble(mPost.getDiscount())>0) {
                postOriginalPrice.setVisibility(View.INVISIBLE);
                postOriginalPrice.setText("$ "+mPost.getCost());
                postOriginalPrice.setPaintFlags(postOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Double cost=Double.parseDouble(mPost.getCost());
                if(mPost.getDiscount_type().equals("amount")){
                    cost=cost-Double.parseDouble(mPost.getDiscount());
                }else if(mPost.getDiscount_type().equals("percent")){
                    Double discountPrice=cost*(Double.parseDouble(mPost.getDiscount())/100);
                    cost=cost-discountPrice;
                }
                mPrice=cost;
                postPrice.setText("$ "+cost.toString());
            }else{
                postPrice.setText("$ "+mPost.getCost());
                postOriginalPrice.setVisibility(View.GONE);
            }

            int countView=0;
            try{
                String response= CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"countview/?post="+mPost.getId());
                APIResponse APIResponse =new APIResponse();
                Gson gson=new Gson();
                APIResponse =gson.fromJson(response, APIResponse.class);
                countView= APIResponse.getCount();
            }catch (IOException io){
                io.printStackTrace();
            }
            postView.setText(String.valueOf(countView));

            double finalMPrice = mPrice;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(), Detail_New_Post.class);
                    intent.putExtra("Discount", finalMPrice);
                    intent.putExtra("Price",mPost.getCost());
                    intent.putExtra("ID",mPost.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
            // 05 09 19 thou

            try{
                Service api = Client.getClient().create(Service.class);
                Call<User> call = api.getuser(Integer.parseInt(mPost.getCreated_by()));
                call.enqueue(new retrofit2.Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.isSuccessful()){
                            Log.d("12122121", String.valueOf(response.code()));
                        }
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
