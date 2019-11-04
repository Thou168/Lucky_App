package com.bt_121shoppe.motorbike.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.homes.HomeFilterResultFragment;
import com.bt_121shoppe.motorbike.models.FilterConditionViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.nio.file.DirectoryStream;
import java.util.List;

public class FilterConditionAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG=FilterConditionAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NORMAL=1;
    private List<FilterConditionViewModel> mFilterItemsList;
    private Callback mCallback;
    private int mOptionValue,mFilterType;
    private FilterConditionViewModel mFilterItem;

    public FilterConditionAdapter(List<FilterConditionViewModel> filterItemsList, int optionValue,int filterType, FilterConditionViewModel mFilterItem){
        this.mFilterItemsList=filterItemsList;
        this.mOptionValue=optionValue;
        this.mFilterItem=mFilterItem;
        this.mFilterType=filterType;
    }

    public void setCallBack(Callback callBack){
        mCallback=callBack;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_condition,parent,false));
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
        if (mFilterItemsList != null && mFilterItemsList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if(mFilterItemsList!=null && mFilterItemsList.size()>0)
            return mFilterItemsList.size();
        else
            return 1;
    }

    public void addItems(List<FilterConditionViewModel> itemsList){
        mFilterItemsList.addAll(itemsList);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder{

        TextView titleTextView;
        ImageView optionImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.filterConditionTextView);
            optionImageView=itemView.findViewById(R.id.filterOptionImageView);
        }

        @Override
        protected void clear() {
            titleTextView.setText("");
            Glide.with(itemView.getContext()).load(R.drawable.ic_circle_outline_24).thumbnail(0.1f).into(optionImageView);
        }

        public void onBind(int position){
            super.onBind(position);
            final FilterConditionViewModel mFilterCondition=mFilterItemsList.get(position);

            titleTextView.setText(mFilterCondition.getName());

            if(mFilterCondition.getId()==mOptionValue)
                Glide.with(itemView.getContext()).load(R.drawable.ic_circle_slice_24).thumbnail(0.1f).into(optionImageView);
            else
                Glide.with(itemView.getContext()).load(R.drawable.ic_circle_outline_24).thumbnail(0.1f).into(optionImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Bundle bundle=new Bundle();
                    if(mFilterType== CommonFunction.FILTERPOSTTYPE) {
                        bundle.putInt("postTypeId", mFilterCondition.getId());
                        bundle.putInt("categoryId", mFilterItem.getCategoryId());
                        bundle.putInt("brandId", mFilterItem.getBrandId());
                        bundle.putInt("yearId", mFilterItem.getYearId());
                        bundle.putDouble("minPrice", mFilterItem.getMinPrice());
                        bundle.putDouble("maxPrice", mFilterItem.getMaxPrice());
                    }else if(mFilterType==CommonFunction.FILTERCATEGORY){
                        bundle.putInt("postTypeId", mFilterItem.getPostType());
                        bundle.putInt("categoryId", mFilterCondition.getId());
                        bundle.putInt("brandId", mFilterItem.getBrandId());
                        bundle.putInt("yearId", mFilterItem.getYearId());
                        bundle.putDouble("minPrice", mFilterItem.getMinPrice());
                        bundle.putDouble("maxPrice", mFilterItem.getMaxPrice());
                    }else if(mFilterType==CommonFunction.FILTERBRAND){
                        bundle.putInt("postTypeId", mFilterItem.getPostType());
                        bundle.putInt("categoryId", mFilterItem.getCategoryId());
                        bundle.putInt("brandId", mFilterCondition.getId());
                        bundle.putInt("yearId", mFilterItem.getYearId());
                        bundle.putDouble("minPrice", mFilterItem.getMinPrice());
                        bundle.putDouble("maxPrice", mFilterItem.getMaxPrice());
                    }else if(mFilterType==CommonFunction.FILTERYEAR){
                        bundle.putInt("postTypeId", mFilterItem.getPostType());
                        bundle.putInt("categoryId", mFilterItem.getCategoryId());
                        bundle.putInt("brandId", mFilterItem.getBrandId());
                        bundle.putInt("yearId", mFilterCondition.getId());
                        bundle.putDouble("minPrice", mFilterItem.getMinPrice());
                        bundle.putDouble("maxPrice", mFilterItem.getMaxPrice());
                    }
                    Fragment myFragment = new HomeFilterResultFragment();
                    myFragment.setArguments(bundle);
                    FragmentManager fm=activity.getFragmentManager();
                    FragmentTransaction fragmentTransaction=fm.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,myFragment);
                    fragmentTransaction.commit();
                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).commit();
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
