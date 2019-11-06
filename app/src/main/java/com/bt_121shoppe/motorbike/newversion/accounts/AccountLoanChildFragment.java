package com.bt_121shoppe.motorbike.newversion.accounts;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_Loanbyuser;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_historyloan;
import com.bt_121shoppe.motorbike.Api.api.model.Item_loan;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountLoanChildFragment extends Fragment {

    private static final String TAG=AccountLoanFragment.class.getSimpleName();
    private static final String ARGUMENT_POSITION="arugment_position";

    private SharedPreferences prefer;
    private String name,pass,basic_encode;
    private int pk=0;

    public static AccountLoanChildFragment newInstance(int position){
        Bundle args=new Bundle();
        args.putInt(ARGUMENT_POSITION,position);
        AccountLoanChildFragment fragment=new AccountLoanChildFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_loan_child, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_encode="Basic "+ CommonFunction.getEncodedString(name,pass);

        TextView tvnoresult=view.findViewById(R.id.tv_no_result);
        LinearLayoutManager layoutManager=new GridLayoutManager(getContext(),1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        RecyclerView recyclerView=view.findViewById(R.id.rv_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable= ContextCompat.getDrawable(getContext(),R.drawable.divider_drawable);

        int position=getArguments().getInt(ARGUMENT_POSITION,-1);
        switch (position){
            case 0:
                Service apiService = Client.getClient().create(Service.class);
                Call<AllResponse> call = apiService.getLoanbyuser(basic_encode);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        if(response.isSuccessful()) {
                            List<Item_loan> listData = response.body().getresults();
                            if (listData.size() == 0) {
                                tvnoresult.setVisibility(View.VISIBLE);
                            }else{
                                tvnoresult.setVisibility(View.GONE);
                                Adapter_Loanbyuser mAdapter = new Adapter_Loanbyuser(listData, getContext());
                                recyclerView.setAdapter(mAdapter);
                            }
                        }else{
                            tvnoresult.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
                break;
            case 1:
                Service apiService1 = Client.getClient().create(Service.class);
                Call<AllResponse> call1 = apiService1.getloanhistory(basic_encode);
                call1.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        if(response.isSuccessful()) {
                            List<Item_loan> listData = response.body().getresults();
                            if (listData.size() == 0) {
                                tvnoresult.setVisibility(View.VISIBLE);
                            }else {
                                //progressBar.setVisibility(View.GONE);
                                tvnoresult.setVisibility(View.GONE);
                                Adapter_historyloan mAdapter = new Adapter_historyloan(listData, getContext());
                                recyclerView.setAdapter(mAdapter);
                            }
                        }else{
                            tvnoresult.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
                break;
        }

    }

}
