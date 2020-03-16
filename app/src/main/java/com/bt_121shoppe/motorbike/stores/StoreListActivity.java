package com.bt_121shoppe.motorbike.stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Api.responses.APIShopResponse;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseProvince;
import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.CheckGroup;
import com.bt_121shoppe.motorbike.activities.DealerStoreActivity;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.stores.adapters.StoreListAdapter;
import com.bt_121shoppe.motorbike.utils.API;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,BottomChooseProvince.ItemClickListener{

    private TextView mButtonLocation,mButtonSort;
    private RecyclerView mRecyclerViewStoreList;
    private StoreListAdapter mAdapter;
    private ArrayList<ShopViewModel> mStoreList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView ads;
    int index = 4;
    int index_province = 26;
    BottomNavigationView bnaviga,bnaviga1;
    private BottomSheetBehavior sheetBehavior;
    private int pk=0;
    SharedPreferences prefer;
    private String name,pass,Encode;
    int g = 0;
    int selectedProvinceId=0;
    private RelativeLayout rlNoResult;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        rlNoResult=findViewById(R.id.rl_noResult);
        bnaviga=findViewById(R.id.bnaviga);
        bnaviga1=findViewById(R.id.bottom_nav);
        mButtonLocation= findViewById(R.id.button_location);
        mButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomProvince(view);
            }
        });

//        mButtonLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View dialogView = StoreListActivity.this.getLayoutInflater().inflate(R.layout.province_string_array,null);
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StoreListActivity.this);
//                bottomSheetDialog.setContentView(dialogView);
////                bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                bottomSheetDialog.show();
//                ImageView close = dialogView.findViewById(R.id.close);
//                RadioGroup group = dialogView.findViewById(R.id.radiogroup);
//                Button ok = dialogView.findViewById(R.id.click_btn);
//                close.setOnClickListener(v1 -> bottomSheetDialog.dismiss());
//                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        View radioButton = group.findViewById(checkedId);
//                        index = group.indexOfChild(radioButton);
////                        best_m = bestm[index];
//                        switch (checkedId){
//                            case 0:
//                                index=0;
//                                break;
//                            case 1:
//                                index=1;
//                                break;
//                            case 2:
//                                index=2;
//                                break;
//                            case 3:
//                                index=3;
//                                break;
//                        }
//                    }
//                });
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (index==0){
//                            mButtonLocation.setText(R.string.phnompenh);
//                        }else if (index==1){
//                            mButtonLocation.setText(R.string.pursat);
//                        }else if (index==2){
//                            mButtonLocation.setText(R.string.banteay_meanchey);
//                        }else if (index==3){
//                            mButtonLocation.setText(R.string.kampong_cham);
//                        }else if (index==4){
//                            mButtonLocation.setText(R.string.kampong_chnang);
//                        }else if (index==5){
//                            mButtonLocation.setText(R.string.kampong_spue);
//                        }else if (index==6){
//                            mButtonLocation.setText(R.string.kampot);
//                        }else if (index==7){
//                            mButtonLocation.setText(R.string.kandal);
//                        }else if (index==8){
//                            mButtonLocation.setText(R.string.Kep);
//                        }else if (index==9){
//                            mButtonLocation.setText(R.string.koh_kong);
//                        }else if (index==10){
//                            mButtonLocation.setText(R.string.kratie);
//                        }else if (index==11){
//                            mButtonLocation.setText(R.string.mondulkiri);
//                        }else if (index==12){
//                            mButtonLocation.setText(R.string.oddor_meanchey);
//                        }else if (index==13){
//                            mButtonLocation.setText(R.string.pailin);
//                        }else if (index==14){
//                            mButtonLocation.setText(R.string.preyveng);
//                        }else if (index==15){
//                            mButtonLocation.setText(R.string.rattanakiri);
//                        }else if (index==16){
//                            mButtonLocation.setText(R.string.siem_reap);
//                        }else if (index==17){
//                            mButtonLocation.setText(R.string.sihanouk);
//                        }else if (index==18){
//                            mButtonLocation.setText(R.string.stung_treng);
//                        }else if (index==19){
//                            mButtonLocation.setText(R.string.svay_reang);
//                        }else if (index==20){
//                            mButtonLocation.setText(R.string.Takeo);
//                        }else if (index==21){
//                            mButtonLocation.setText(R.string.kompongthom);
//                        }else if (index==22){
//                            mButtonLocation.setText(R.string.preah_vihear);
//                        }else if (index==23){
//                            mButtonLocation.setText(R.string.tbong_khmum);
//                        }else if (index==24){
//                            mButtonLocation.setText(R.string.battambang);
//                        }
//                        bottomSheetDialog.dismiss();
//                    }
//                });
//            }
//        });
//        mButtonSort=(Button)findViewById(R.id.button_sort);
        ads = findViewById(R.id.best_match);
        ads.setOnClickListener(v -> {
            View dialogView = StoreListActivity.this.getLayoutInflater().inflate(R.layout.best_match_dialog,null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StoreListActivity.this);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            bottomSheetDialog.setContentView(dialogView);
//            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.show();
            ImageView close = dialogView.findViewById(R.id.icon_close);
            RadioGroup group = dialogView.findViewById(R.id.radio_group);
            Button ok = dialogView.findViewById(R.id.btn_ok);
            RadioButton rbnewads=dialogView.findViewById(R.id.new_ads);
            rbnewads.setVisibility(View.GONE);
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
        initialStoreListData(0);


//        bottomNavigation();
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }

        Service apiService=Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    int group=response.body().getProfile().getGroup();
                    if(group==3){
                        bnaviga1.setVisibility(View.VISIBLE);
                        bnaviga.setVisibility(View.GONE);
                        bnaviga1.getMenu().getItem(1).setChecked(true);
                        bnaviga1.setOnNavigationItemSelectedListener(mlistener1);
                    }else{
                        bnaviga1.setVisibility(View.GONE);
                        bnaviga.setVisibility(View.VISIBLE);
                        bnaviga.getMenu().getItem(1).setChecked(true);
                        bnaviga.setOnNavigationItemSelectedListener(mlistener);
                    }
                }else{
                    bnaviga1.setVisibility(View.GONE);
                    bnaviga.setVisibility(View.VISIBLE);
                    bnaviga.getMenu().getItem(1).setChecked(true);
                    bnaviga.setOnNavigationItemSelectedListener(mlistener);
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });

//        CheckGroup check = new CheckGroup();
//        g = check.getGroup(pk,this);
//        //Log.e("MSMSMS","Group "+g);
//        if (g == 3){
//            bnaviga1.setVisibility(View.VISIBLE);
//            bnaviga.setVisibility(View.GONE);
//            bnaviga1.getMenu().getItem(1).setChecked(true);
//            bnaviga1.setOnNavigationItemSelectedListener(mlistener1);
//        }else {
//            bnaviga1.setVisibility(View.GONE);
//            bnaviga.setVisibility(View.VISIBLE);
//            bnaviga.getMenu().getItem(1).setChecked(true);
//            bnaviga.setOnNavigationItemSelectedListener(mlistener);
//        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mlistener
            = item -> {
        switch (item.getItemId()) {
            case R.id.home:
                Intent myIntent = new Intent(StoreListActivity.this, Home.class);
                startActivity(myIntent);
                break;
            case R.id.notification:
                break;
            case R.id.camera:
                if (prefer.contains("token") || prefer.contains("id")) {
                    Intent myIntent3 = new Intent(StoreListActivity.this, Camera.class);
                    startActivity(myIntent3);
                }else {
                    Intent intent=new Intent(StoreListActivity.this, LoginActivity.class);
                    intent.putExtra("Login_verify","camera");
                    startActivity(intent);
                }
                break;
            case R.id.message:
                if (prefer.contains("token") || prefer.contains("id")) {
                    Intent myIntent4 = new Intent(StoreListActivity.this, ChatMainActivity.class);
                    startActivity(myIntent4);
                }else {
                    Intent intent=new Intent(StoreListActivity.this, LoginActivity.class);
                    intent.putExtra("Login_verify","message");
                    startActivity(intent);
                }
                break;
            case R.id.account:
                if (prefer.contains("token") || prefer.contains("id")) {
                    Intent myIntent5 = new Intent(StoreListActivity.this, Account.class);
                    startActivity(myIntent5);
                }else {
                    Intent intent=new Intent(StoreListActivity.this, LoginActivity.class);
                    intent.putExtra("Login_verify","account");
                    startActivity(intent);
                }
                break;
        }
        return true;
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mlistener1
            = item -> {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(StoreListActivity.this, Home.class));
                break;
            case R.id.notification:
                break;
            case R.id.dealer:
                if (prefer.contains("token") || prefer.contains("id")) {
                    startActivity(new Intent(StoreListActivity.this, DealerStoreActivity.class));
                }else {
                    Intent intent=new Intent(StoreListActivity.this, LoginActivity.class);
                    intent.putExtra("Login_verify","camera");
                    startActivity(intent);
                }
                break;
            case R.id.message:
                if (prefer.contains("token") || prefer.contains("id")) {
                    startActivity(new Intent(StoreListActivity.this, ChatMainActivity.class));
                }else {
                    Intent intent=new Intent(StoreListActivity.this, LoginActivity.class);
                    intent.putExtra("Login_verify","message");
                    startActivity(intent);
                }
                break;
            case R.id.account:
                if (prefer.contains("token") || prefer.contains("id")) {
                    startActivity(new Intent(StoreListActivity.this, Account.class));
                }else {
                    Intent intent=new Intent(StoreListActivity.this, LoginActivity.class);
                    intent.putExtra("Login_verify","account");
                    startActivity(intent);
                }
                break;
        }
        return true;
    };


    @Override
    protected void onStart() {
        super.onStart();
        bnaviga1.getMenu().getItem(1).setChecked(true);
        bnaviga.getMenu().getItem(1).setChecked(true);

//        CheckGroup check = new CheckGroup();
//        int g = check.getGroup(pk,this);
//        if (g == 3){
//            bnaviga1.getMenu().getItem(1).setChecked(true);
//        }else {
//            bnaviga.getMenu().getItem(1).setChecked(true);
//        }
    }


    @Override
    public void onRefresh() {
//        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showBottomProvince(View view){
        BottomChooseProvince addBotttomDialogFragment=BottomChooseProvince.newInstance();
        addBotttomDialogFragment.show(getSupportFragmentManager(),BottomChooseProvince.TAG);
    }

    @Override
    public void onClickProvinceItem(String item) {
        mButtonLocation.setText(item);
    }

    @Override
    public void AddIDProvince(int id) {
        selectedProvinceId=id;
        initialStoreListData(selectedProvinceId);
        //Log.e("TAG","Selected province "+selectedProvinceId);
    }

    private void initialStoreListData(int id){
        mRecyclerViewStoreList.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerViewStoreList.setLayoutManager(mLayoutManager);
        mStoreList=new ArrayList<>();
        mAdapter=new StoreListAdapter(mStoreList);
        //get storelist from api here
        if(id==0){
            Service apiService= Client.getClient().create(Service.class);
            Call<APIShopResponse> call=apiService.GetStoreList();
            call.enqueue(new Callback<APIShopResponse>() {
                @Override
                public void onResponse(Call<APIShopResponse> call, Response<APIShopResponse> response) {
                    if(!response.isSuccessful()){
                        Log.e("TAG","Get Shop Result failure:"+response.code());
                    }else{
                        //Log.e("TAG",response.body().getresults().toString());
                        //mStoreList=response.body().getresults();
                        List<ShopViewModel> results=response.body().getresults();
                        mStoreList=new ArrayList<>();
                        for (int i=0;i<results.size();i++){
                            if(results.get(i).getRecord_status()==1)
                                mStoreList.add(results.get(i));
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            List<ShopViewModel> mmStoreList=mStoreList.stream().sorted(Comparator.comparing(ShopViewModel::getShop_name)).collect(Collectors.toList());
                            mAdapter.addItems(mmStoreList);
                        }
                        else
                            mAdapter.addItems(mStoreList);
                        mRecyclerViewStoreList.setAdapter(mAdapter);
                        ViewCompat.setNestedScrollingEnabled(mRecyclerViewStoreList,false);
                        mAdapter.notifyDataSetChanged();
                        if (mStoreList.size()==0)
                            rlNoResult.setVisibility(View.VISIBLE);
                        else
                            rlNoResult.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<APIShopResponse> call, Throwable t) {
                    Log.e("TAG","Get Filter Result failure:"+t.getMessage());
                }
            });
        }
        else{
            Service apiService= Client.getClient().create(Service.class);
            Call<APIShopResponse> call=apiService.GetStoreList(id);
            call.enqueue(new Callback<APIShopResponse>() {
                @Override
                public void onResponse(Call<APIShopResponse> call, Response<APIShopResponse> response) {
                    if(!response.isSuccessful()){
                        Log.e("TAG","Get Shop Result failure:"+response.code());
                    }else{
                        //Log.e("TAG",response.body().getresults().toString());
                        //mStoreList=response.body().getresults();
                        List<ShopViewModel> results=response.body().getresults();
                        mStoreList=new ArrayList<>();
                        for (int i=0;i<results.size();i++){
                            if(results.get(i).getRecord_status()==1)
                                mStoreList.add(results.get(i));
                        }
                        mAdapter.addItems(mStoreList);
                        mRecyclerViewStoreList.setAdapter(mAdapter);
                        ViewCompat.setNestedScrollingEnabled(mRecyclerViewStoreList,false);
                        mAdapter.notifyDataSetChanged();
                        if (mStoreList.size()==0)
                            rlNoResult.setVisibility(View.VISIBLE);
                        else
                            rlNoResult.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<APIShopResponse> call, Throwable t) {
                    Log.e("TAG","Get Filter Result failure:"+t.getMessage());
                }
            });
        }
    }
}
