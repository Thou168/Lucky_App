package com.bt_121shoppe.motorbike.stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.responses.APIShopResponse;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.ShopAdapter;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.adapters.StoreListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Button mButtonLocation,mButtonSort;
    private RecyclerView mRecyclerViewStoreList;
    private LinearLayoutManager mLayoutManager;
    private StoreListAdapter mAdapter;
    private List<ShopViewModel> mStoreList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        mButtonLocation=(Button) findViewById(R.id.button_location);
        mButtonSort=(Button)findViewById(R.id.button_sort);
        mRecyclerViewStoreList=(RecyclerView) findViewById(R.id.recyclerview_store_list);
        mSwipeRefreshLayout=findViewById(R.id.refresh);

        //Drawable imgleft = getApplicationContext().getResources().getDrawable( R.drawable.ic_place_black_24dp );
        //Drawable imgright = getApplicationContext().getResources().getDrawable( R.drawable.ic_arrow_drop_down_black_24dp );
        //mButtonLocation.setCompoundDrawablesWithIntrinsicBounds( imgleft, null, imgright, null);
        //mButtonSort.setCompoundDrawablesWithIntrinsicBounds( null, null, imgright, null);

        mSwipeRefreshLayout.setOnRefreshListener(this);
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
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
