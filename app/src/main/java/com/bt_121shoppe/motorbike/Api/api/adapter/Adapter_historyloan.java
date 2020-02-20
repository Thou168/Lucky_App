package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_historyloan extends RecyclerView.Adapter<Adapter_historyloan.ViewHolder> {

    private List<Item_loan> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_historyloan(List<Item_loan> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loan_history,viewGroup,false);

        prefer = mContext.getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");

        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_Encode = "Basic "+getEncodedString(name,pass);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder view, final int position) {
        final Item_loan model = datas.get(position);

        int postid=(int)model.getPost();
        //view.txtview.setVisibility(View.GONE);
        //view.userview.setVisibility(View.GONE);
        view.relativeLayout.setVisibility(View.GONE);
        //view.item_type.setVisibility(View.GONE);
        try{
            Service api = Client.getClient().create(Service.class);
            Call<Item> itemCall = api.getDetailpost(postid,basic_Encode);
            itemCall.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Glide.with(mContext).load(response.body().getFront_image_path()).apply(new RequestOptions().placeholder(R.drawable.no_image_available)).thumbnail(0.1f).into(view.imageView);

                    String strPostTitle="";
                    String lang = view.userview.getText().toString();

                    if(response.body().getPost_sub_title()==null){
//                        String fullTitle= CommonFunction.generatePostSubTitle(response.body().getModeling(),year,response.body().getColor());
//                        if(lang.equals("View:"))
//                            strPostTitle=fullTitle.split(",")[0];
//                        else
//                            strPostTitle=fullTitle.split(",")[1];
                    }else {
                        String[] splitTitle=response.body().getPost_sub_title().split(",");
                        if (lang.equals("View")) {
                            strPostTitle = splitTitle[0];
                        } else {
                            strPostTitle = splitTitle.length==1?splitTitle[0]:splitTitle[1];
                        }
                    }

                    //date
                    view.date.setVisibility(View.GONE);

                    String inputPattern = "yyyy-MM-dd";
                    String outputPattern = "MMM dd, yyyy";
                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                    Service apiService = Client.getClient().create(Service.class);
                    Call<AllResponse> call_date = apiService.getLoanbyuser(basic_Encode);
                    call_date.enqueue(new Callback<AllResponse>() {
                        @Override
                        public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                            Date dd;
                            String tt;
                            datas = response.body().getresults();
                            if (datas.size()==0){
                                view.date.setVisibility(View.GONE);
                            }
                            for (int i = 0;i<datas.size();i++) {
                                try {
                                    dd = inputFormat.parse(datas.get(i).getCreated());
                                    tt = outputFormat.format(dd);
                                    view.date.setText(tt);
                                    Log.e("===============", "Loan history" + tt);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AllResponse> call, Throwable t) {

                        }
                    });
                    //end

                    if (response.body().getCategory()==1){
                        view.cate.setText(R.string.electronic);
                    }else {
                        view.cate.setText(R.string.motor);
                    }

                    String[] splitColor=response.body().getMulti_color_code().split(",");

                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.OVAL);
                    shape.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[0])));
                    view.tvColor1.setBackground(shape);
                    view.tvColor2.setVisibility(View.GONE);
                    if(splitColor.length>1){
                        view.tvColor2.setVisibility(View.VISIBLE);
                        GradientDrawable shape1 = new GradientDrawable();
                        shape1.setShape(GradientDrawable.OVAL);
                        shape1.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[1])));
                        view.tvColor2.setBackground(shape1);
                    }

                    view.title.setText(strPostTitle);
                    view.cost.setText("$"+model.getLoan_amount());
                    if (response.body().getPost_type().equals("sell")){
                        view.item_type.setText(R.string.sell_t);
//                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_sell));
                        view.item_type.setBackgroundResource(R.drawable.roundimage);
                    }else if (response.body().getPost_type().equals("buy")){
                        view.item_type.setText(R.string.buy_t);
                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_buy));
                    }else {
                        view.item_type.setText(R.string.rent);
//                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_rent));
                        view.item_type.setBackgroundResource(R.drawable.roundimage_rent);
                    }

                    int status=model.getLoan_status();
                    switch (status){
                        case 2:
                            view.textViewStatus.setText("Removed");
                            break;
                        case 9:
                            view.textViewStatus.setText(R.string.pending);
                            break;
                        case 10:
                            view.textViewStatus.setText("Approved");
                            break;
                        case 11:
                            view.textViewStatus.setText("Rejected");
                            break;
                        case 12:
                            view.textViewStatus.setText("Cancelled");
                            break;
                    }

                    try{
                        Service apiServiece = Client.getClient().create(Service.class);
                        Call<AllResponse> call_view = apiServiece.getCount(String.valueOf(postid),basic_Encode);
                        call_view.enqueue(new Callback<AllResponse>() {
                            @Override
                            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                view.userview.setText(String.valueOf(response.body().getCount()));
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
                        });
                    }catch (Exception e){Log.d("Error e",e.getMessage());}

                    try{
                        Service api = Client.getClient().create(Service.class);
                        Call<User> call1 = api.getuser(response.body().getCreated_by());
                        call1.enqueue(new retrofit2.Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (!response.isSuccessful()){
                                    //Log.d("12122121", String.valueOf(response.code()));
                                }
                                CommomAPIFunction.getUserProfileFB(mContext,view.imgUserProfile,response.body().getUsername());
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.d("Error",t.getMessage());
                            }
                        });
                    }catch (Exception e){ Log.d("TRY CATCH",e.getMessage());}

//                    if (response.body().getPost_type().equals("sell")){
//                        view.item_type.setText(R.string.sell_t);
//                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_sell));
//                    }else if (response.body().getPost_type().equals("buy")){
//                        view.item_type.setText(R.string.buy_t);
//                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_buy));
//                    }else {
//                        view.item_type.setText(R.string.rent);
//                        view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_rent));
//                    }
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {
                    Log.d("OnFailur Run",t.getMessage());
                }
            });
        }catch (Exception e){}
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,cost,date,item_type,txtview,userview,itemtype,textViewStatus;
        ImageView imageView;
        RelativeLayout relativeLayout;
        CircleImageView imgUserProfile;
        TextView tvColor1,tvColor2;
        TextView cate;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.view);
            userview = view.findViewById(R.id.user_view);
            item_type = view.findViewById(R.id.item_type);
            relativeLayout = view.findViewById(R.id.relative);
            imgUserProfile=view.findViewById(R.id.img_user);
            textViewStatus=view.findViewById(R.id.pending_appprove);
            tvColor1=view.findViewById(R.id.tv_color1);
            tvColor2=view.findViewById(R.id.tv_color2);
            cate=view.findViewById(R.id.cate);
            date=view.findViewById(R.id.txt_date);
        }
    }
}