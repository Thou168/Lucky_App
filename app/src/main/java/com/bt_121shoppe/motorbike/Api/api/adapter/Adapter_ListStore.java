package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.DetailStoreActivity;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Adapter_ListStore extends RecyclerView.Adapter<Adapter_ListStore.ViewHolder> {

    private List<ShopViewModel> datas;
    private Context mContext;
    public Adapter_ListStore(List<ShopViewModel> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dealer_item,viewGroup,false);
        Paper.init(mContext);
        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language","km");

        return new ViewHolder(view);
    }
    private void updateView(String language,ViewHolder view) {
        Context context = LocaleHapler.setLocale(mContext, language);
        Resources resources = context.getResources();
        view.txtview1.setText(resources.getString(R.string.countview));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder view, final int position) {
        final ShopViewModel model = datas.get(position);
        updateView(Paper.book().read("language"),view);
        int shopId = model.getId();
        Log.e("Shop","Image "+model.getShop_image());
        String imageUrl="";
        if(model.getShop_image()!=null){
            imageUrl= ConsumeAPI.BASE_URL_IMG+model.getShop_image();
        }
        //Log.e("AD","Shop ID in Adapter "+shopId);
        view.address.setText(model.getShop_address());
        view.shopname.setText(model.getShop_name());
        Glide.with(mContext).load(imageUrl).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(view.img_user);
        view.itemView.setOnClickListener(view1 -> {
            Intent intent=new Intent(mContext, DetailStoreActivity.class);
            intent.putExtra("shopId",shopId);
            mContext.startActivity(intent);
        });
        view.txtRate.setText(model.getShop_rate());
        view.view1.setText(String.valueOf(model.getShop_view()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtview1,shopname,view1,address,txtRate;
        CircleImageView img_user;
        ViewHolder(View view){
            super(view);
            txtview1 = view.findViewById(R.id.view1);
            shopname = view.findViewById(R.id.tv_dealer);
            view1 = view.findViewById(R.id.view);
            address = view.findViewById(R.id.location_store);
            img_user = view.findViewById(R.id.img_user);
            txtRate=view.findViewById(R.id.rate);
        }
    }
}