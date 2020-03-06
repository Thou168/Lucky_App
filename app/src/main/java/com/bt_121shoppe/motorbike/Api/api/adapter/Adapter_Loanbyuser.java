package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_Loanbyuser extends RecyclerView.Adapter<Adapter_Loanbyuser.ViewHolder> {

    private List<Item_loan> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_Loanbyuser(List<Item_loan> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loan,viewGroup,false);

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
        String loanid = String.valueOf((int)model.getId());
        int postid=(int)model.getPost();
        try{
            Service api = Client.getClient().create(Service.class);
            Call<Item> itemCall = api.getDetailpost(postid,basic_Encode);
            itemCall.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Glide.with(mContext).load(response.body().getFront_image_path()).apply(new RequestOptions().placeholder(R.drawable.no_image_available)).thumbnail(0.1f).into(view.imageView);
                    //dd by Raksmey
                    String strPostTitle="";
                    String lang = view.txtview.getText().toString();
                    if(response.body().getPost_sub_title()== null){
//                        String fullTitle=CommonFunction.generatePostSubTitle(response.body().getModeling(),year,response.body().getColor());
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
                    view.title.setText(strPostTitle);
                    view.cost.setText("$"+model.getLoan_amount());

                    if (response.body().getCategory()==1){
                        view.cate.setText(R.string.electronic);
                    }else {
                        view.cate.setText(R.string.motor);
                    }

                    //date
                    view.date.setVisibility(View.VISIBLE);
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
                                    Log.e("===============", "======currentData======" + tt);
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

                    view.textViewStatus.setVisibility(View.VISIBLE);
                    int status=(int)response.body().getStatus();
                    switch (status){
                        case 9:
                            view.textViewStatus.setText(R.string.pending);
                            view.textViewStatus.setTextColor(Color.parseColor("#CCCCCC"));
                            break;
                        case 10:
                            view.textViewStatus.setText(R.string.approveval);
                            view.textViewStatus.setTextColor(Color.parseColor("#43BF64"));
                            break;
                    }

                    try{
                        Service apiServiece = Client.getClient().create(Service.class);
                        Call<AllResponse> call_view = apiServiece.getCount(String.valueOf(postid),basic_Encode);
                        call_view.enqueue(new Callback<AllResponse>() {
                            @Override
                            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                view.textview1.setText(String.valueOf(response.body().getCount()));
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

                    view.btn_cancel.setOnClickListener(v -> {
                        LayoutInflater factory = LayoutInflater.from(mContext);
                        final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
                        final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(mContext).create();
                        clearDialog.setView(clearDialogView);
                        clearDialog.setCancelable(false);
                        clearDialog .setIcon(android.R.drawable.ic_dialog_alert);
                        TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
                        title.setText(R.string.cancel_loan);
                        TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
                        Mssloan.setText(R.string.delete_loan);
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
                                try{
                                    Service api1 = Client.getClient().create(Service.class);
                                    String post = String.valueOf(model.getPost()).substring(0, String.valueOf(model.getPost()).indexOf("."));
                                    int Post_by = Integer.parseInt(post);
                                    Item_loan item_loan = new Item_loan(model.getLoan_to(),model.getLoan_amount(),model.getLoan_interest_rate(),model.getLoan_duration(),model.getLoan_purpose(),2,12,model.getUsername(),model.getGender(),model.getAge(),model.getJob(),model.getAverage_income(),model.getAverage_expense(),model.getTelephone(),model.getAddress(),true,model.isFamily_book(),model.isStaff_id(),model.isHouse_plant(),model.getMfi(),model.getCreated(),model.getCreated_by(),model.getModified(),model.getModified_by(),model.getReceived_date(),model.getReceived_by(),model.getRejected_date(),model.getRejected_by(),model.getRejected_comments(),Post_by);
//                                               Item_loan item_loan = new Item_loan(2,12);
                                    Call<Item_loan> call_loan = api1.getputcancelloan(Integer.parseInt(loanid),item_loan,basic_Encode);
                                    call_loan.enqueue(new Callback<Item_loan>() {
                                        @Override
                                        public void onResponse(Call<Item_loan> call1, Response<Item_loan> response1) {
                                            if (!response1.isSuccessful()){
                                                Log.d("444444", String.valueOf(response1.code()));
                                            }
                                            Intent intent = new Intent(mContext, Account.class);
                                            mContext.startActivity(intent);
                                            ((Activity)mContext).finish();
                                            // delete item withou intent by samang 9/9/19
//                                                        datas.remove(position);
//                                                        notifyItemRemoved(position);
//                                                        notifyItemRangeChanged(position, datas.size());

                                            if (response1.code() == 400) {
                                                try {
                                                    Log.v("Error code 400", response1.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Item_loan> call1, Throwable t) {
                                            Log.d("ERROR",t.getMessage());
                                        }
                                    });
                                }catch (Exception e){Log.d("CAtchMessage",e.getMessage());}
                            }
                        });
                        clearDialog.show();
                    });

                    view.linearLayout.setOnClickListener(v -> {
                        Intent intent = new Intent(mContext, Create_Load.class);
                        intent.putExtra("LoanEdit",true);
                        intent.putExtra("LoanID",Integer.parseInt(loanid));
                        intent.putExtra("product_id",postid);
                        mContext.startActivity(intent);
//                        Intent intent = new Intent(mContext, Detail_new_post_java.class);
//                        intent.putExtra("id",Integer.parseInt(loanid));
//                        intent.putExtra("LoanEdit",true);
//                        intent.putExtra("ID",postid);
//                        mContext.startActivity(intent);
                    });

                    view.btn_edit.setOnClickListener(v -> {
                        Intent intent = new Intent(mContext, Create_Load.class);
                        intent.putExtra("LoanEdit",true);
                        intent.putExtra("LoanID",Integer.parseInt(loanid));
                        intent.putExtra("product_id",postid);
                        mContext.startActivity(intent);
                    });

                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

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
        TextView title,cost,date,item_type,txtview,textview1,textViewStatus;
        ImageView imageView;
        Button btn_edit,btn_cancel;
        LinearLayout linearLayout;
        CircleImageView imgUserProfile;
        TextView tvColor1,tvColor2;
        TextView cate;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
            date = view.findViewById(R.id.txt_date);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.view);
            btn_edit = view.findViewById(R.id.btnedit);
            btn_cancel = view.findViewById(R.id.btndelete);
            linearLayout = view.findViewById(R.id.linearLayout);
            textview1 = view.findViewById(R.id.user_view);
            textViewStatus=view.findViewById(R.id.pending_appprove);
            imgUserProfile=view.findViewById(R.id.img_user);
            tvColor1=view.findViewById(R.id.tv_color1);
            tvColor2=view.findViewById(R.id.tv_color2);
            cate=view.findViewById(R.id.cate);
        }
    }
}