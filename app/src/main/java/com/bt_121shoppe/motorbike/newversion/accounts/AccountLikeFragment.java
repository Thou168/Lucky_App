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
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_Likebyuser;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.LikebyUser;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountLikeFragment extends Fragment {

    private static final String TAG=AccountLikeFragment.class.getSimpleName();
    private List<LikebyUser> postListItems;
    private SharedPreferences prefer;
    private String name,pass,basic_encode;
    private int pk=0;

    public static AccountLikeFragment newInstance(int position) {
        // Required empty public constructor
        return new AccountLikeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_like, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name", "");
        pass = prefer.getString("pass", "");
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_encode = "Basic " + CommonFunction.getEncodedString(name, pass);

        TextView tvnoresult=view.findViewById(R.id.tv_no_result);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        RecyclerView recyclerView = view.findViewById(R.id.rv_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.divider_drawable);
        if (pk>0) {
            Service api = Client.getClient().create(Service.class);
            Call<AllResponse> call = api.getLikebyuser(basic_encode);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    Log.e(TAG,response.toString());
                    if(response.isSuccessful()) {
                        postListItems = response.body().getresults();

                        if (postListItems.size() == 0) {
                            tvnoresult.setVisibility(View.VISIBLE);
                        } else {
                            tvnoresult.setVisibility(View.GONE);
                            Adapter_Likebyuser mAdapter = new Adapter_Likebyuser(postListItems, getContext());
                            recyclerView.setAdapter(mAdapter);
                        }
                    }else{
                        tvnoresult.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) {

                }
            });
        }
    }

}
