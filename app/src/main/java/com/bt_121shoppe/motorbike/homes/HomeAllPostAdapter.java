package com.bt_121shoppe.motorbike.homes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.detail_shop;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapter;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class HomeAllPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG=HomeAllPostAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;
    private List<PostViewModel> mPostList;
    private String mView;

    public HomeAllPostAdapter(List<PostViewModel> postList,String view){
        this.mPostList=postList;
        this.mView=view;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
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

    public void addItems(List<PostViewModel> postlist){
        mPostList.addAll(postlist);
        notifyDataSetChanged();
    }
    public interface Callback{
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder{

        ImageView coverImageView;
        //        ImageView typeImageView;
        TextView typeView;
        TextView postTitle;
        //        TextView postLocationDT;
        TextView postPrice;
        TextView postOriginalPrice;
        TextView postView;
        TextView postLang;
        CircleImageView img_user;
        TextView cate;
        TextView tvColor1,tvColor2;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImageView=itemView.findViewById(R.id.image);
            typeView=itemView.findViewById(R.id.post_type);
            postTitle=itemView.findViewById(R.id.title);
            postPrice=itemView.findViewById(R.id.tv_price);
            postOriginalPrice=itemView.findViewById(R.id.tv_discount);
            postView=itemView.findViewById(R.id.user_view);
            postLang=itemView.findViewById(R.id.user_view1);
            img_user = itemView.findViewById(R.id.img_user);
            cate = itemView.findViewById(R.id.cate);
            tvColor1=itemView.findViewById(R.id.tv_color1);
            tvColor2=itemView.findViewById(R.id.tv_color2);
            cardView=itemView.findViewById(R.id.cardView);
        }

        @Override
        protected void clear() {
            coverImageView.setImageDrawable(null);
            postTitle.setText("");
            postPrice.setText("");
            postOriginalPrice.setText("");
            postView.setText("");
        }

        public void onBind(int position){
            super.onBind(position);
            final PostViewModel mPost=mPostList.get(position);
if(mPost!=null){
    String strPostTitle="";
    Glide.with(itemView.getContext()).load(mPost.getFront_image_path()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(coverImageView);
    String lang=postLang.getText().toString();
    if(lang.equals("View")) {
        if (mPost.getPost_type().equals("sell")) {
            typeView.setText(R.string.sell);
            typeView.setBackgroundResource(R.drawable.roundimage);
        }
        else if (mPost.getPost_type().equals("rent")) {
            typeView.setText(R.string.ren);
            typeView.setBackgroundResource(R.drawable.roundimage_rent);
        }
        strPostTitle = mPost.getPost_sub_title().split(",")[0];
    }else{
        if(mPost.getPost_type()!=null){
            if (mPost.getPost_type().equals("sell")) {
                typeView.setText(R.string.sell);
                typeView.setBackgroundResource(R.drawable.roundimage);
            }
            else if (mPost.getPost_type().equals("rent")) {
                typeView.setText(R.string.ren);
                typeView.setBackgroundResource(R.drawable.roundimage_rent);
            }
        }
if(mPost.getPost_sub_title()!=null)
        strPostTitle = mPost.getPost_sub_title().split(",").length>1?mPost.getPost_sub_title().split(",")[1]:mPost.getPost_sub_title().split(",")[0];
    }
    postTitle.setText(strPostTitle);

    String[] splitColor = new String[0];
    if(mPost.getMulti_color_code()!=null)
         splitColor=mPost.getMulti_color_code().split(",");

    if(splitColor.length>0){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[0])));
        tvColor1.setBackground(shape);
        tvColor2.setVisibility(View.GONE);
        if(splitColor.length>1){
            tvColor2.setVisibility(View.VISIBLE);
            GradientDrawable shape1 = new GradientDrawable();
            shape1.setShape(GradientDrawable.OVAL);
            shape1.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[1])));
            tvColor2.setBackground(shape1);
        }
    }

//            cate.setVisibility(View.GONE);

    double mPrice=0;
//            if(Double.parseDouble(mPost.getDiscount())>0) {
//                postOriginalPrice.setVisibility(View.VISIBLE);
//                postOriginalPrice.setText("$ "+mPost.getCost());
//                postOriginalPrice.setPaintFlags(postOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                Double cost=Double.parseDouble(mPost.getCost());
//                if(mPost.getDiscount_type().equals("amount")){
//                    cost=cost-Double.parseDouble(mPost.getDiscount());
//                }else if(mPost.getDiscount_type().equals("percent")){
//                    Double discountPrice=cost*(Double.parseDouble(mPost.getDiscount())/100);
//                    cost=cost-discountPrice;
//                }
//                mPrice=cost;
//                postPrice.setText("$ "+cost.toString());
//            }else{
//                postPrice.setText("$ "+mPost.getCost());
//            }

    postPrice.setText("$ "+mPost.getCost());

//            cate.setVisibility(View.VISIBLE);
    if(mPost.getCategory()==1){
        cate.setText(R.string.electronic);
    }else if(mPost.getCategory()==2){
        cate.setText(R.string.motor);
    }else
        cate.setText("");

    double finalMPrice = mPrice;
    itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(itemView.getContext(), Detail_new_post_java.class);
            intent.putExtra("Discount", finalMPrice);
            intent.putExtra("Price",mPost.getCost());
            intent.putExtra("ID",mPost.getId());
            itemView.getContext().startActivity(intent);
        }
    });
    // 05 09 19 thou, updated by TTerd Mar 30 2020
    if(mPost.getDealer_shops().size()==0){
        try{
            Service api = Client.getClient().create(Service.class);
            Call<User> call = api.getuser(Integer.parseInt(mPost.getCreated_by()));
            call.enqueue(new retrofit2.Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.isSuccessful()){
                        //Log.d("12122121", String.valueOf(response.code()));
                    }
                    CommomAPIFunction.getUserProfileFB(itemView.getContext(),img_user,response.body().getUsername());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });
        }catch (Exception e){ Log.d("TRY CATCH",e.getMessage());}
    }else{
        detail_shop shopViewModel=mPost.getDealer_shops().get(0);
        Service api=Client.getClient().create(Service.class);
        retrofit2.Call<ShopViewModel> call1=api.getDealerShop(shopViewModel.getShop());
        call1.enqueue(new retrofit2.Callback<ShopViewModel>() {
            @Override
            public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response) {
                if (response.body().getShop_image()==null) {
                    Glide.with(itemView.getContext()).load(R.drawable.group_2293).thumbnail(0.1f).into(img_user);
                } else {
                    Glide.with(itemView.getContext()).load(response.body().getShop_image()).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(img_user);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ShopViewModel> call, Throwable t) {

            }
        });
    }


    //count view
    try{
        Service apiServiece = Client.getClient().create(Service.class);
        Call<AllResponse> call1 = apiServiece.getCount(String.valueOf((int)mPost.getId()));
        call1.enqueue(new retrofit2.Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if(response.isSuccessful())
                postView.setText(String.valueOf(response.body().getCount()));
            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
        });
    }catch (Exception e){Log.d("Error e",e.getMessage());}
}

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
