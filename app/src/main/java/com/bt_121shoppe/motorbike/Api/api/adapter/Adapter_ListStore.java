package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_delete;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.DealerStoreActivity;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.DetailStoreActivity;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.R;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
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
        int shopId = model.getId();
        //Log.e("Shop","Image "+model.getShop_image());
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

        view.btRemoveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Log.e("TAG","Selected Shop id is "+model.getId());
                            Service api=Client.getClient().create(Service.class);
                            retrofit2.Call<ShopViewModel> call=api.getDealerShop(model.getId());
                            call.enqueue(new retrofit2.Callback<ShopViewModel>() {
                                @Override
                                public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response) {
                                    if(response.isSuccessful()){
                                        Log.e("TAG","Shop Detail "+response.body().getId());
                                        ShopViewModel shop=response.body();
                                        shop.setRecord_status(2);
                                        String surl=ConsumeAPI.BASE_URL+"api/v1/shop/"+shop.getId()+"/";
                                        OkHttpClient client=new OkHttpClient();
                                        JSONObject obj=new JSONObject();
                                        try{
                                            obj.put("user",shop.getUser());
                                            obj.put("record_status",2);
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                        RequestBody body=RequestBody.create(ConsumeAPI.MEDIA_TYPE,obj.toString());
                                        Request request = new Request.Builder()
                                                .url(surl)
                                                .put(body)
                                                .header("Accept", "application/json")
                                                .header("Content-Type", "application/json")
                                                .build();
                                        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(okhttp3.Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                                Log.e("TAG","Remove Store Successfully. "+response.message());
                                                Intent intent = new Intent(view.getContext(), DealerStoreActivity.class);
                                                view.getContext().startActivity(intent);
                                                ((Activity)view.getContext()).finish();
                                            }
                                        });


//                                        retrofit2.Call<ShopViewModel> call1=api.updateShopCountView(shop.getId(),shop);
//
//                                        call1.enqueue(new retrofit2.Callback<ShopViewModel>() {
//                                            @Override
//                                            public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response1) {
//                                                Log.e("TAG","Remove Store Successfully. "+response1.message());
//                                                Intent intent = new Intent(view.getContext(), DealerStoreActivity.class);
//                                                view.getContext().startActivity(intent);
//                                                ((Activity)view.getContext()).finish();
//                                            }
//
//                                            @Override
//                                            public void onFailure(retrofit2.Call<ShopViewModel> call, Throwable t) {
//                                                Log.e("TAG","fail submit count view "+t.getMessage());
//                                            }
//                                        });
                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<ShopViewModel> call, Throwable t) {

                                }
                            });


                            dialog.dismiss();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.remove_shop_message_confirmation).setPositiveButton(R.string.yes_remove, dialogClickListener)
                        .setNegativeButton(R.string.no_remove, dialogClickListener).show();
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
        TextView txtview1,shopname,view1,address,txtRate;
        CircleImageView img_user;
        ImageView btRemoveStore;
        ViewHolder(View view){
            super(view);
            txtview1 = view.findViewById(R.id.view1);
            shopname = view.findViewById(R.id.tv_dealer);
            view1 = view.findViewById(R.id.view);
            address = view.findViewById(R.id.location_store);
            img_user = view.findViewById(R.id.img_user);
            txtRate=view.findViewById(R.id.rate);
            btRemoveStore=view.findViewById(R.id.bt_clear);
        }
    }
}