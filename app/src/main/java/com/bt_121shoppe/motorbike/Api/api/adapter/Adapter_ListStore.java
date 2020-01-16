package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.Detail_store;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_unlike;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        view.address.setText(model.getShop_address());
        view.shopname.setText(model.getShop_name());
        Glide.with(mContext).load(model.getShop_image()).placeholder(R.drawable.group_2293).thumbnail(0.1f).into(view.img_user);
        view.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, Detail_store.class);
                intent.putExtra("shop_name",model.getShop_name());
                intent.putExtra("address",model.getShop_address());
                intent.putExtra("shop_image",model.getShop_image());
                mContext.startActivity(intent);
            }
        });
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
        TextView txtview1,shopname,view1,address;
        CircleImageView img_user;
        ViewHolder(View view){
            super(view);
            txtview1 = view.findViewById(R.id.view1);
            shopname = view.findViewById(R.id.tv_dealer);
            view1 = view.findViewById(R.id.view);
            address = view.findViewById(R.id.location_store);
            img_user = view.findViewById(R.id.img_user);

        }
    }
}