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

import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.loan.model.draft_Item;
import com.bt_121shoppe.motorbike.loan.model.Draft;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_list_draft extends RecyclerView.Adapter<Adapter_list_draft.ViewHolder> {

    private List<draft_Item> datas;
    SharedPreferences prefer;
    private Context mContext;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_list_draft(Context mContex,List<draft_Item> datas) {
        this.mContext=mContex;
        this.datas = datas;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_draft,viewGroup,false);

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
        final draft_Item model = datas.get(position);
        int loanid = (int)model.getId();
        int post_by = model.getPost();
        String price = String.valueOf(model.getLoan_amount());
        view.draft_name.setText(model.getDraft_name());
        Log.e("item",""+loanid+price+post_by);
        view.itemView.setOnClickListener(view1 -> {
            Intent intent = new Intent(view1.getContext(),Create_Load.class);
            intent.putExtra("LoanID",loanid);
            intent.putExtra("product_id",post_by);
            intent.putExtra("price",price);
            intent.putExtra("draft","draft");
            view1.getContext().startActivity(intent);
        });
        view.cancel.setOnClickListener(view1 -> {
            LayoutInflater factory = LayoutInflater.from(view1.getContext());
            final View clearDialogView = factory.inflate(R.layout.layout_warnning_dialog, null);
            final android.app.AlertDialog clearDialog = new android.app.AlertDialog.Builder(view1.getContext()).create();
            clearDialog.setView(clearDialogView);
            clearDialog.setIcon(R.drawable.tab_message_selector);
            clearDialog.setCancelable(false);
            TextView title = (TextView) clearDialogView.findViewById(R.id.textView_title);
            TextView Mssloan = (TextView) clearDialogView.findViewById(R.id.textView_message);
            Mssloan.setText(R.string.delete_loan_draft);
            title.setText(R.string.cancel_loan_draft);
            Button btnYes = (Button) clearDialogView.findViewById(R.id.button_positive);
            btnYes.setText(R.string.ok);
            clearDialogView.findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Service api = Client.getClient().create(Service.class);
                        Item_loan item_loan = new Item_loan(model.getLoan_to(),String.valueOf(model.getLoan_amount()),String.valueOf(model.getLoan_interest_rate()),model.getLoan_duration(),model.getLoan_purpose(),2,12,model.getUsername(),model.getGender(),model.getAge(),model.getJob(),String.valueOf(model.getAverage_income()),String.valueOf(model.getAverage_expense()),model.getTelephone(),model.getAddress(),true,model.isFamily_book(),model.isStaff_id(),model.isHouse_plant(),model.getCreated_by(),model.getModified(),String.valueOf(model.getModified_by()),model.getReceived_date(),model.getRejected_date(),model.getRejected_by(),model.getRejected_comments(),post_by);

                        Call<Item_loan> call_loan = api.getputcancelloan(loanid,item_loan,basic_Encode);
                        call_loan.enqueue(new Callback<Item_loan>() {
                            @Override
                            public void onResponse(Call<Item_loan> call1, Response<Item_loan> response1) {
                                if (!response1.isSuccessful()){
                                    Log.d("444444", String.valueOf(response1.code()));
                                }
                                Intent intent = new Intent(view1.getContext(), Account.class);
                                view1.getContext().startActivity(intent);

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
                    }catch (Exception e){Log.e("CatchMessage",e.getMessage());}
                }
            });
            clearDialog.show();
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
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView draft_name;
        ImageView cancel;
        ViewHolder(View view){
            super(view);
            draft_name = view.findViewById(R.id.name);
            cancel = view.findViewById(R.id.bt_clear);
        }
    }
}