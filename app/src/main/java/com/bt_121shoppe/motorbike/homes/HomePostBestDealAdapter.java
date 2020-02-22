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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.PostBestDealAdapter;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class HomePostBestDealAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG=HomePostBestDealAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;

    private List<PostViewModel> mPostList;

    public HomePostBestDealAdapter(List<PostViewModel> postlist){
        this.mPostList=postlist;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount,parent,false));
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
        TextView typeView;
        TextView postTitle;
        TextView postLocationDT;
        TextView postPrice;
        TextView postOriginalPrice;
        TextView postView;
        TextView postLang;
        CircleImageView img_user;
        RelativeLayout relativeLayout;
        TextView ds_price;
        TextView tvColor1,tvColor2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView=itemView.findViewById(R.id.image);
            typeView=itemView.findViewById(R.id.post_type);
            postTitle=itemView.findViewById(R.id.title);
            postLocationDT=itemView.findViewById(R.id.location);
            postPrice=itemView.findViewById(R.id.tv_discount);
            postOriginalPrice=itemView.findViewById(R.id.tv_price);
            postView=itemView.findViewById(R.id.view);
            postLang=itemView.findViewById(R.id.user_view);
            img_user = itemView.findViewById(R.id.img_user);
            relativeLayout = itemView.findViewById(R.id.relative_view);
            ds_price = itemView.findViewById(R.id.ds_price);
            tvColor1=itemView.findViewById(R.id.tv_color1);
            tvColor2=itemView.findViewById(R.id.tv_color2);
        }

        @Override
        protected void clear() {

        }

        public void onBind(int position){
            super.onBind(position);
            final PostViewModel mPost=mPostList.get(position);

            Glide.with(itemView.getContext()).load(mPost.getFront_image_path()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(coverImageView);
            if(mPost.getPost_type().equals("sell")) {
                typeView.setText(R.string.sell);
                typeView.setBackgroundResource(R.drawable.roundimage);
            }
            else if(mPost.getPost_type().equals("rent")) {
                typeView.setText(R.string.ren);
                typeView.setBackgroundResource(R.drawable.roundimage_rent);
            }
            String strPostTitle="";
            if(postLang.getText().toString().equals("View"))
                strPostTitle=mPost.getPost_sub_title().split(",")[0];
            else
            if(strPostTitle.length()>1)
                strPostTitle=mPost.getPost_sub_title().split(",")[1];
            else
                strPostTitle=mPost.getPost_sub_title().split(",")[0];
            postTitle.setText(strPostTitle);
            String[] splitColor=mPost.getMulti_color_code().split(",");
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            if(!splitColor[0].isEmpty())
                shape.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[0])));
            tvColor1.setBackground(shape);
            tvColor2.setVisibility(View.GONE);
            if (splitColor.length > 1) {
                tvColor2.setVisibility(View.VISIBLE);
                GradientDrawable shape1 = new GradientDrawable();
                shape1.setShape(GradientDrawable.OVAL);
                shape1.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[1])));
                tvColor2.setBackground(shape1);
            }

            double mPrice=0;
            if(Double.parseDouble(mPost.getDiscount())>0) {
                Double cost=Double.parseDouble(mPost.getCost());
                Double discountPrice=cost*(Double.parseDouble(mPost.getDiscount())/100);
                int per1 = (int) ( Double.parseDouble(mPost.getDiscount()));
                cost=cost-discountPrice;
                mPrice=cost;
                postPrice.setText("$ "+cost.toString());
                postOriginalPrice.setText("$ "+mPost.getCost());
                ds_price.setText(per1+"%");
                relativeLayout.setVisibility(View.VISIBLE);
                postOriginalPrice.setPaintFlags(postOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                postOriginalPrice.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
            }

            /* post profile */
            try{
                Service api = Client.getClient().create(Service.class);
                Call<User> call = api.getuser(Integer.parseInt(mPost.getCreated_by()));
                call.enqueue(new retrofit2.Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.isSuccessful()){

                        }
                        CommomAPIFunction.getUserProfileFB(itemView.getContext(),img_user,response.body().getUsername());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
            }catch (Exception e){ Log.d("TRY CATCH",e.getMessage());}

            //count view
            try{
                Service apiServiece = Client.getClient().create(Service.class);
                Call<AllResponse> call1 = apiServiece.getCount(String.valueOf((int)mPost.getId()));
                call1.enqueue(new retrofit2.Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        postView.setText(String.valueOf(response.body().getCount()));
                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
                });
            }catch (Exception e){Log.d("Error e",e.getMessage());}

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
