package com.bt_121shoppe.motorbike.stores;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.responses.APIShopResponse;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapter;
import com.bt_121shoppe.motorbike.adapters.ShopAdapter;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.adapters.StoreListAdapter;
import com.bt_121shoppe.motorbike.useraccount.EditAccountActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private TextView mButtonLocation,mButtonSort;
    private RecyclerView mRecyclerViewStoreList;
    private LinearLayoutManager mLayoutManager;
    private StoreListAdapter mAdapter;
    private List<ShopViewModel> mStoreList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView ads;
    int index = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        mButtonLocation= findViewById(R.id.button_location);
        mButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(StoreListActivity.this);
                mBuilder.setTitle(getString(R.string.choose_location));
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
//        mButtonSort=(Button)findViewById(R.id.button_sort);
        ads = findViewById(R.id.best_match);
        ads.setOnClickListener(v -> {
            View dialogView = StoreListActivity.this.getLayoutInflater().inflate(R.layout.best_match_dialog,null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StoreListActivity.this);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.show();
            ImageView close = dialogView.findViewById(R.id.icon_close);
            RadioGroup group = dialogView.findViewById(R.id.radio_group);
            Button ok = dialogView.findViewById(R.id.btn_ok);
            close.setOnClickListener(v1 -> bottomSheetDialog.dismiss());
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = group.findViewById(checkedId);
                    index = group.indexOfChild(radioButton);
//                        best_m = bestm[index];
                    switch (checkedId){
                        case 0:
                            index=0;
                            break;
                        case 1:
                            index=1;
                            break;
                        case 2:
                            index=2;
                            break;
                        case 3:
                            index=3;
                            break;
                    }
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if (index == 0) {
                            ads.setText(R.string.new_ads);
                        } else if (index == 1) {
                            ads.setText(R.string.most_hit_ads);
                        } else if (index == 2) {
                            ads.setText(R.string.low_to_high);
                        } else if (index == 3) {
                            ads.setText(R.string.high_to_low);
                        }
                        bottomSheetDialog.dismiss();
                }
            });
        });
        mRecyclerViewStoreList=(RecyclerView) findViewById(R.id.recyclerview_store_list);
//        mSwipeRefreshLayout=findViewById(R.id.refresh);

        //Drawable imgleft = getApplicationContext().getResources().getDrawable( R.drawable.ic_place_black_24dp );
        //Drawable imgright = getApplicationContext().getResources().getDrawable( R.drawable.ic_arrow_drop_down_black_24dp );
        //mButtonLocation.setCompoundDrawablesWithIntrinsicBounds( imgleft, null, imgright, null);
        //mButtonSort.setCompoundDrawablesWithIntrinsicBounds( null, null, imgright, null);

//        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerViewStoreList.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerViewStoreList.setLayoutManager(mLayoutManager);
        mStoreList=new ArrayList<>();
        mAdapter=new StoreListAdapter(mStoreList);
        //get storelist from api here
        Service apiService= Client.getClient().create(Service.class);
        Call<APIShopResponse> call=apiService.GetStoreList();
        call.enqueue(new Callback<APIShopResponse>() {
            @Override
            public void onResponse(Call<APIShopResponse> call, Response<APIShopResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("TAG","Get Shop Result failure:"+response.code());
                }else{
                    Log.e("TAG",response.body().getresults().toString());
                    mStoreList=response.body().getresults();
                    mAdapter.addItems(mStoreList);
                    mRecyclerViewStoreList.setAdapter(mAdapter);
                    ViewCompat.setNestedScrollingEnabled(mRecyclerViewStoreList,false);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<APIShopResponse> call, Throwable t) {
                Log.e("TAG","Get Filter Result failure:"+t.getMessage());
            }
        });


    }


    @Override
    public void onRefresh() {
//        mSwipeRefreshLayout.setRefreshing(false);
    }
}
