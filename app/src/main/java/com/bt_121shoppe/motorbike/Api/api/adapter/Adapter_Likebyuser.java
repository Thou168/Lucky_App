package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.LikebyUser;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_unlike;
import com.bt_121shoppe.motorbike.Language.LocaleHapler;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_Likebyuser extends RecyclerView.Adapter<Adapter_Likebyuser.ViewHolder> {

    private List<LikebyUser> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_Likebyuser(List<LikebyUser> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list2,viewGroup,false);

        prefer = mContext.getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");

        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_Encode = "Basic "+getEncodedString(name,pass);

        Paper.init(mContext);
        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language","km");

        return new ViewHolder(view);
    }
    private void updateView(String language,ViewHolder view) {
        Context context = LocaleHapler.setLocale(mContext, language);
        Resources resources = context.getResources();
        view.txtview1.setText(resources.getString(R.string.view));
    }

    @Override
    public void onBindViewHolder(final ViewHolder view, final int position) {
        final LikebyUser model = datas.get(position);

        updateView(Paper.book().read("language"),view);
        String iditem=String.valueOf((int)model.getPost());
        String itemid_like=String.valueOf((int)model.getId());

        try{
            Service api = Client.getClient().create(Service.class);
            Call<Item> call = api.getDetailpost(Integer.parseInt(iditem),basic_Encode);
            call.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Glide.with(mContext).load(response.body().getFront_image_path()).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.no_image_available)).thumbnail(0.1f).into(view.imageView);
                    if (response.body().getCategory()==1){
                        view.cate.setText(R.string.electronic);
                    }else {
                        view.cate.setText(R.string.motor);
                    }
                    //dd by Raksmey
                    String strPostTitle="";
                    String lang = view.txtview1.getText().toString();
                    if(response.body().getPost_sub_title()== null){
//                        String fullTitle=CommonFunction.generatePostSubTitle(response.body().getModeling(),year,response.body().getColor());
//                        if(lang.equals("View:"))
//                            strPostTitle=fullTitle.split(",")[0];
//                        else
//                            strPostTitle=fullTitle.split(",")[1];
                    }else {
                        if (lang.equals("View")) {
                            strPostTitle = response.body().getPost_sub_title().split(",")[0];
                        } else {
                            String[] splitTitle=response.body().getPost_sub_title().split(",");
//                            if(strPostTitle.length()>1)
//                                strPostTitle = response.body().getPost_sub_title().split(",")[1];
//                            else
//                                strPostTitle = response.body().getPost_sub_title().split(",")[0];
                            strPostTitle = splitTitle.length==1?splitTitle[0]:splitTitle[1];
                        }
                    }
                    view.title.setText(strPostTitle);
//                    String jok=strPostTitle;
//                    if (jok.length()>37){
//                        jok=jok.substring(0,37)+"...";
//                        view.title.setText(jok);
//                    }else {
//                        view.title.setText(jok);
//                    }
//
//End
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

                    Double rs_price=0.0;
                    DecimalFormat f = new DecimalFormat("#,###.##");
                    String NoDc,HaveDc,Price_Full_dis;
                    if (response.body().getDiscount().equals("0.00")){
                        double co_cost = Double.parseDouble(response.body().getCost());
                        NoDc = NumberFormat.getNumberInstance(Locale.US).format(co_cost);
                        view.cost.setText("$ "+NoDc);
                        view.linearLayout.setOnClickListener(v -> {
                            Intent intent = new Intent(mContext, Detail_new_post_java.class);
                            intent.putExtra("ID",Integer.parseInt(iditem));
                            intent.putExtra("intent_like","like");
                            mContext.startActivity(intent);
                        });
                    }else {
//                        rs_price = Double.parseDouble(response.body().getCost());
//                        if (response.body().getDiscount_type().equals("amount")){
//                            rs_price = rs_price - Double.parseDouble(response.body().getDiscount());
//                        }else if (response.body().equals("percent")){

//                            rs_price = rs_price - per;
//                        }
//                        Double per = Double.parseDouble(response.body().getCost()) *( Double.parseDouble(response.body().getDiscount())/100);
//                        if (response.body().getDiscount_type().equals("percent")) {
//                            view.txt_discount.setVisibility(View.VISIBLE);
//                            Double co_price = Double.parseDouble(response.body().getCost());
//                            double result = co_price - per;
//                            view.cost.setText("$" + result);
//                            view.txt_discount.setText("$" + co_price);
//                            view.txt_discount.setPaintFlags(view.txt_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        }else {
                            double per = Double.parseDouble(response.body().getCost()) *( Double.parseDouble(response.body().getDiscount())/100);
                            double co_price = Double.parseDouble(response.body().getCost());
                            double result = co_price - per;
                        HaveDc = NumberFormat.getNumberInstance(Locale.US).format(result);
                            view.cost.setText("$ " + HaveDc);
                            view.txt_discount.setVisibility(View.VISIBLE);
                        Price_Full_dis = NumberFormat.getNumberInstance(Locale.US).format(co_price);
                            view.txt_discount.setText("$ "+Price_Full_dis);
                            view.txt_discount.setPaintFlags(view.txt_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        }
                        view.linearLayout.setOnClickListener(v -> {
                            Intent intent = new Intent(mContext, Detail_new_post_java.class);
                            intent.putExtra("Discount", per);
                            intent.putExtra("ID", Integer.parseInt(iditem));
                            intent.putExtra("intent_like","like");
                            mContext.startActivity(intent);
                        });
                    }
//                    view.linearLayout.setOnClickListener(v -> {
//                        Intent intent = new Intent(mContext, Detail_New_Post.class);
//                        intent.putExtra("Price", response.body().getCost());
//                        intent.putExtra("ID",Integer.parseInt(iditem));
//                        mContext.startActivity(intent);
//                    });
                    view.date.setVisibility(View.GONE);
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
//Close by Raksmey
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//                    Log.d("545445",response.body().getCreated());
//                    long date = 0;
//                    try {
//                        date = sdf.parse(response.body().getApproved_date()).getTime();
//                        Long now = System.currentTimeMillis();
//                        CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);
//                        view.date.setText(ago);
//                    } catch (ParseException e) { e.printStackTrace(); }
//End
//Count View
                    try{
                        Service apiServiece = Client.getClient().create(Service.class);
                        Call<AllResponse> call_view = apiServiece.getCount(iditem,basic_Encode);
                        call_view.enqueue(new Callback<AllResponse>() {
                            @Override
                            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                view.txtview.setText(String.valueOf(response.body().getCount()));
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
                        });
                    }catch (Exception e){Log.d("Error e",e.getMessage());}
                    view.imgUserProfile.setVisibility(View.VISIBLE);
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
//Button Unlike
                    view.btn_unlike.setOnClickListener(v -> {
                        LayoutInflater factory = LayoutInflater.from(mContext);
                        final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
                        final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(mContext).create();
                        clearDialog.setView(clearDialogView);
                        clearDialog.setCancelable(false);
                        TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
                        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
                        title.setText(R.string.title_unlike);
                        Mssloan.setText(R.string.unlike_message);
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
                                clearDialog.dismiss();
                                Service api1 = Client.getClient().create(Service.class);
                                change_status_unlike unlike = new change_status_unlike(null,Integer.parseInt(iditem),pk,2);
//
                                Call<change_status_unlike> call_unlike = api1.getputStatusUnlike(Integer.parseInt(itemid_like),unlike,basic_Encode);
                                call_unlike.enqueue(new Callback<change_status_unlike>() {
                                    @Override
                                    public void onResponse(Call<change_status_unlike> call1, Response<change_status_unlike> response1) {
//                                            Intent intent = new Intent(mContext, Account.class);
//                                            mContext.startActivity(intent);
//                                            ((Activity)mContext).finish();
                                        String respone = String.valueOf(response.body());
                                        Log.d("Respone unlike",respone);

// delete item without intent by samang 9/9/19
                                        datas.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, datas.size());
                                    }

                                    @Override
                                    public void onFailure(Call<change_status_unlike> call1, Throwable t) {
                                    }
                                });
                            }
                        });
                        clearDialog.show();
                    });
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) { }
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
        TextView title,cost,item_type,txtview,txtview1,date,txt_discount;
        ImageView imageView;
        CircleImageView imgUserProfile;
        ImageButton btn_unlike;
        LinearLayout linearLayout;
        TextView tvColor1,tvColor2;
        TextView cate;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            date = view.findViewById(R.id.txt_date);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.user_view);
            txtview1 = view.findViewById(R.id.user_view1);
            btn_unlike = view.findViewById(R.id.imgbtn_unlike);
            linearLayout = view.findViewById(R.id.linearLayout);
            txt_discount = view.findViewById(R.id.tv_discount);
            imgUserProfile=view.findViewById(R.id.img_user);
            tvColor1=view.findViewById(R.id.tv_color1);
            tvColor2=view.findViewById(R.id.tv_color2);
            cate = view.findViewById(R.id.cate);
        }
    }
}