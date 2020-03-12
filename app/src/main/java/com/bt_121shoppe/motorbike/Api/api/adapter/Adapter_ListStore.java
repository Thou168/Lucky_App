package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_delete;
import com.bt_121shoppe.motorbike.Api.responses.APIStorePostResponse;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.DealerStoreActivity;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.dealerstores.DealerStoreDetailActivity;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.StorePostViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;

public class Adapter_ListStore extends RecyclerView.Adapter<Adapter_ListStore.ViewHolder> {

    private List<ShopViewModel> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    int pk=0;

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
        prefer = viewGroup.getContext().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");

        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_Encode = "Basic "+ CommonFunction.getEncodedString(name,pass);
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
            Intent intent=new Intent(mContext, DealerStoreDetailActivity.class);
            intent.putExtra("shopId",shopId);
            mContext.startActivity(intent);
        });
        view.txtRate.setText(model.getShop_rate());
        view.view1.setText(String.valueOf(model.getShop_view()));

        view.btRemoveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(view.getContext());
                final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
                final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(view.getContext()).create();
                clearDialog.setView(clearDialogView);
                clearDialog.setCancelable(false);
                TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
                Mssloan.setText(R.string.remove_shop_message_confirmation);
                Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
                btnYes.setText(R.string.yes_leave);
                Button btnNo = (Button) clearDialogView.findViewById(R.id.button_negative);
                btnNo.setText(R.string.no_leave);
                clearDialogView.findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearDialog.dismiss();
                    }
                });
                clearDialogView.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.e("TAG","Selected Shop id is "+model.getId());

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
                                            //Start remove active post in shop
                                            retrofit2.Call<APIStorePostResponse> call1=api.GetStoreActivePost(model.getId());
                                            call1.enqueue(new retrofit2.Callback<APIStorePostResponse>() {
                                                @Override
                                                public void onResponse(retrofit2.Call<APIStorePostResponse> call, Response<APIStorePostResponse> response1) {
                                                    if(response1.isSuccessful()){
                                                        ArrayList<StorePostViewModel> results=response1.body().getResults();
                                                        for(int i=0;i<results.size();i++){
                                                            int rPostId=results.get(i).getPost();
                                                            String date = null;
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                date = Instant.now().toString();
                                                            }
                                                            change_status_delete removePost=new change_status_delete(2,date,pk,"");
                                                            retrofit2.Call<change_status_delete> call2=api.getputStatus(rPostId,removePost,basic_Encode);
                                                            call2.enqueue(new retrofit2.Callback<change_status_delete>() {
                                                                @Override
                                                                public void onResponse(retrofit2.Call<change_status_delete> call, Response<change_status_delete> response) {

                                                                }

                                                                @Override
                                                                public void onFailure(retrofit2.Call<change_status_delete> call, Throwable t) {


                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(retrofit2.Call<APIStorePostResponse> call, Throwable t) {

                                                }
                                            });

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


                        clearDialog.dismiss();
                    }
                });
                clearDialog.show();
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