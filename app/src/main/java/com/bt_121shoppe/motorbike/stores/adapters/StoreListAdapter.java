package com.bt_121shoppe.motorbike.stores.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.model.Province;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.StoreDetailActivity;
import com.bt_121shoppe.motorbike.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG=StoreListAdapter.class.getSimpleName();

    private ArrayList<ShopViewModel> mShopList;
    private String currentLanguage;
    private int mProvinceID;

    public StoreListAdapter(ArrayList<ShopViewModel> shoplist){
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
        TextView txtRateNumber;
        RatingBar ratingBar;

        public ViewHolder(View itemView){
            super(itemView);
            txtPostTitle=itemView.findViewById(R.id.textview_posttitle);
            txtShopLocation=itemView.findViewById(R.id.textview_shoplocation);
            txtCountView=itemView.findViewById(R.id.view);
            imgShopProfile=itemView.findViewById(R.id.imageview_shopprofile);
            txtRateNumber= itemView.findViewById(R.id.number_of_rate);
            ratingBar= itemView.findViewById(R.id.rating_star);
        }

        @Override
        protected void clear() {
            txtPostTitle.setText("");
        }

        @Override
        public void onBind(int position){
            super.onBind(position);
            final ShopViewModel mShop=mShopList.get(position);
            SharedPreferences preferences = itemView.getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            currentLanguage = preferences.getString("My_Lang", "");

            txtPostTitle.setText(mShop.getShop_name());
            mProvinceID = mShop.getShop_province();
            try{
                Service apiServiece = Client.getClient().create(Service.class);
                Call<Province> call = apiServiece.getProvince(mProvinceID);
                call.enqueue(new retrofit2.Callback<Province>() {
                    @Override
                    public void onResponse(Call<Province> call, Response<Province> response) {
                        if (!response.isSuccessful()){
                            Log.e("ONRESPONSE Province", String.valueOf(response.code()));
                        }else {
                            if (currentLanguage.equals("en"))
                                txtShopLocation.setText(response.body().getProvince());
                            else txtShopLocation.setText(response.body().getProvince_kh());
                        }
                    }

                    @Override
                    public void onFailure(Call<Province> call, Throwable t) { Log.d("Error",t.getMessage()); }
                });
            }catch (Exception e){Log.d("Error e",e.getMessage());}

            Glide.with(itemView.getContext()).load(mShop.getShop_image()).placeholder(R.mipmap.ic_launcher_round).thumbnail(0.1f).into(imgShopProfile);
            txtCountView.setText(String.valueOf(mShop.getShop_view()));

//            double d = Double.parseDouble(mShop.getShop_rate());
//            DecimalFormat precision = new DecimalFormat("0.0");

            txtRateNumber.setText(mShop.getShop_rate());
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                    double numStar = Double.parseDouble(precision.format(d));
//                    numStar = ratingBar.getNumStars();
                }
            });
//            txtRateNumber.setText(d);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), StoreDetailActivity.class);
                    intent.putExtra("id",mShop.getId());
                    intent.putExtra("shopinfo",mShop.getShop_name());
                    intent.putExtra("shop_location",mShop.getShop_province());
                    intent.putExtra("shop_image", String.valueOf((mShop.getShop_image())));
                    intent.putExtra("shop_view",String.valueOf(mShop.getShop_view()));
                    intent.putExtra("shop_rate_num",String.valueOf(mShop.getShop_rate()));
                    intent.putExtra("shop_phonenumber",mShop.getShop_phonenumber());
                    itemView.getContext().startActivity(intent);

                }
            });
        }
    }

}
