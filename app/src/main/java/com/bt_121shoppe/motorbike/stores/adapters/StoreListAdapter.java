package com.bt_121shoppe.motorbike.stores.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.StoreDetailActivity;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG=StoreListAdapter.class.getSimpleName();

    private List<ShopViewModel> mShopList;

    public StoreListAdapter(List<ShopViewModel> shoplist){
        this.mShopList=shoplist;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if(mShopList!=null && mShopList.size()>0)
            return mShopList.size();
        return 0;
    }

    public void addItems(List<ShopViewModel> mShopList){
        this.mShopList.addAll(mShopList);

    }

    public class ViewHolder extends BaseViewHolder{
        TextView txtPostTitle;
        TextView txtShopLocation;
        TextView txtCountView;
        CircleImageView imgShopProfile;

        public ViewHolder(View itemView){
            super(itemView);
            txtPostTitle=itemView.findViewById(R.id.textview_posttitle);
            txtShopLocation=itemView.findViewById(R.id.textview_shoplocation);
            txtCountView=itemView.findViewById(R.id.textview_countview);
            imgShopProfile=itemView.findViewById(R.id.imageview_shopprofile);
        }

        @Override
        protected void clear() {
            txtPostTitle.setText("");
        }

        @Override
        public void onBind(int position){
            super.onBind(position);
            final ShopViewModel mShop=mShopList.get(position);

            txtPostTitle.setText(mShop.getShop_name());
            txtShopLocation.setText(mShop.getShop_address());
            Glide.with(itemView.getContext()).load(mShop.getShop_image()).placeholder(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(imgShopProfile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), StoreDetailActivity.class);
                    intent.putExtra("id",mShop.getId());
                    intent.putExtra("shopinfo",mShop.getShop_name());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
