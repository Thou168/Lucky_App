package com.bt_121shoppe.motorbike.homes;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapter;
import com.bt_121shoppe.motorbike.adapters.PostBestDealAdapter;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.classes.PreCachingLayoutManager;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class HomeApiFragment extends Fragment {
    private static final String TAG=HomeApiFragment.class.getSimpleName();
    private RecyclerView mBestDealRecyclerView,mAllPostsRecyclerView;
    private TextView mBestDealNoResult,mAllPostsNoResult,mAllPostNoMoreResult,mBestDealText;
    private PreCachingLayoutManager mLayoutManager;
    private LinearLayoutManager mAllPostLayoutManager;
    private HomePostBestDealAdapter mPostBestDealAdpater;
    private HomeAllPostAdapter mAllPostAdapter;
    private ImageView mListView,mGridView,mGallaryView;
    private List<PostViewModel> mAllPosts;
    private List<PostViewModel> mPostBestDeals;
    private ProgressBar mBestDealProgressbar,mAllPostProgressbar;
    ScrollingPagerIndicator recyclerIndicator;
    Parcelable state;
    TextView connection;
    RelativeLayout rl_besdeal;
    RelativeLayout rl_newpost;
    ConstraintLayout ct_layout;
    TextView best_match;
    int index = 0;
    private View view;

    public HomeApiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home_frist, container, false);
        connection=view.findViewById(R.id.txt_connection);
        rl_besdeal=view.findViewById(R.id.rl_besdeal);
        mBestDealRecyclerView=view.findViewById(R.id.horizontal);
        mBestDealNoResult=view.findViewById(R.id.text);
        mBestDealProgressbar=view.findViewById(R.id.progress_bar);
        mAllPostsRecyclerView=view.findViewById(R.id.list_new_post);
        mAllPostProgressbar=view.findViewById(R.id.progress_bar1);
        mAllPostsNoResult=view.findViewById(R.id.text1);
        mAllPostNoMoreResult=view.findViewById(R.id.textViewAllPostNoMoreResult);
        mListView=view.findViewById(R.id.img_list);
        mGridView=view.findViewById(R.id.grid);
        mGallaryView=view.findViewById(R.id.btn_image);
        mBestDealText=view.findViewById(R.id.bestDeal);
        rl_newpost= view.findViewById(R.id.rl_newpost);
        ct_layout=view.findViewById(R.id.ct_layout);
        recyclerIndicator = view.findViewById(R.id.indicator);
        best_match=view.findViewById(R.id.best_match);

        setUpBestDeal();
        setupAllPosts(index);
        call_function();

        best_match.setOnClickListener(v -> {
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.best_match_dialog,null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
            ImageView close = dialogView.findViewById(R.id.icon_close);
            RadioGroup group = dialogView.findViewById(R.id.radio_group);
            RadioButton radioButton1 = dialogView.findViewById(R.id.new_ads);
            RadioButton radioButton2 = dialogView.findViewById(R.id.most_hit_ads);
            RadioButton radioButton3 = dialogView.findViewById(R.id.low_to_high);
            RadioButton radioButton4 = dialogView.findViewById(R.id.high_to_low);
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
                    if (index==0) {
                        best_match.setText(R.string.new_ads);
                    }else if (index==1){
                        best_match.setText(R.string.most_hit_ads);
                    }else if (index==2){
                        best_match.setText(R.string.low_to_high);
                    }else if (index==3){
                        best_match.setText(R.string.high_to_low);
                    }
                    setupAllPosts(index);
                    current_list();
                    bottomSheetDialog.dismiss();

                }
            });
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        state = mLayoutManager.onSaveInstanceState();
    }
    @Override
    public void onResume() {
        super.onResume();
        state = mLayoutManager.onSaveInstanceState();
    }

    private void setUpBestDeal(){
        Display mDisplay=getActivity().getWindowManager().getDefaultDisplay();
        final int width=mDisplay.getWidth();
        final int height=mDisplay.getHeight();
        mPostBestDeals=new ArrayList<>();
        mLayoutManager=new PreCachingLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mLayoutManager.setExtraLayoutSpace(height);
        mBestDealRecyclerView.setHasFixedSize(true);
        mBestDealRecyclerView.setItemViewCacheSize(20);
        mBestDealRecyclerView.setDrawingCacheEnabled(true);
        mBestDealRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mBestDealRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mBestDealRecyclerView.setLayoutManager(mLayoutManager);
        mBestDealRecyclerView.setNestedScrollingEnabled(false);
        mBestDealRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable= ContextCompat.getDrawable(getContext(),R.drawable.divider_drawable);
        mPostBestDealAdpater=new HomePostBestDealAdapter(new ArrayList<>());

        com.bt_121shoppe.motorbike.Api.api.Service apiService= Client.getClient().create(Service.class);
        Call<APIResponse> call=apiService.getPostBestDeal();
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(!response.isSuccessful()){
                    mBestDealNoResult.setVisibility(View.VISIBLE);
                }else{
                    int count=response.body().getCount();
                    if(count==0)
                        mBestDealNoResult.setVisibility(View.VISIBLE);
                    else{
                        mBestDealNoResult.setVisibility(View.GONE);
                        mPostBestDeals=response.body().getresults();
                        Collections.sort(mPostBestDeals, (s1, s2) -> Integer.compare(s2.getId(), s1.getId()));
                        mPostBestDealAdpater.addItems(mPostBestDeals);
                        mBestDealRecyclerView.setAdapter(mPostBestDealAdpater);
                        recyclerIndicator.attachToRecyclerView(mBestDealRecyclerView);
                        mPostBestDealAdpater.notifyDataSetChanged();
                    }
                }
                mBestDealProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e(TAG,"Failed best deal with message:"+t.getMessage());
                mBestDealProgressbar.setVisibility(View.GONE);
                mBestDealNoResult.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupAllPosts(int index){
        mAllPosts=new ArrayList<>();
        mAllPostLayoutManager=new GridLayoutManager(getContext(),1);
        mAllPostLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mAllPostLayoutManager.setAutoMeasureEnabled(true);
        mAllPostsRecyclerView.setHasFixedSize(true);
        //mAllPostsRecyclerView.setItemViewCacheSize(20);
        mAllPostsRecyclerView.setDrawingCacheEnabled(true);
        mAllPostsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAllPostsRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mAllPostsRecyclerView.setLayoutManager(mAllPostLayoutManager);
        mAllPostsRecyclerView.setNestedScrollingEnabled(false);
        mAllPostsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable=ContextCompat.getDrawable(getContext(),R.drawable.divider_drawable);

        Service apiService = Client.getClient().create(Service.class);
                    Call<APIResponse> call = apiService.getAllPosts();
                    call.enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                            if (!response.isSuccessful()) {
                                mAllPostsNoResult.setVisibility(View.VISIBLE);
                            } else {
                                int count = response.body().getCount();
                                if (count == 0) {
                                    mAllPostsNoResult.setVisibility(View.VISIBLE);
                                    mAllPostNoMoreResult.setVisibility(View.GONE);
                                } else {
                                    mAllPostsNoResult.setVisibility(View.GONE);
                                    mAllPostNoMoreResult.setVisibility(View.VISIBLE);
                                    mAllPosts = response.body().getresults();
                                    switch (index){
                                        case 0:
                                            Collections.sort(mAllPosts, (s1, s2) -> Integer.compare(s2.getId(), s1.getId()));
                                            best_match.setText(R.string.new_ads);
                                            break;
                                        case 1:
                                            //Collections.sort(mAllPosts, (s1, s2) -> Integer.compare(s2.getCountView(), s1.getCountView()));
                                            best_match.setText(R.string.most_hit_ads);
                                            break;
                                        case 2:
                                            Collections.sort(mAllPosts, (s1, s2) -> Double.compare(Double.valueOf(s1.getCost()), Double.valueOf(s2.getCost())));
                                            best_match.setText(R.string.low_to_high);
                                            break;
                                        case 3:
                                            Collections.sort(mAllPosts, (s1, s2) -> Double.compare(Double.valueOf(s2.getCost()), Double.valueOf(s1.getCost())));
                                            best_match.setText(R.string.high_to_low);
                                            break;
                                    }


                                    mAllPostAdapter=new HomeAllPostAdapter(mAllPosts,"List");
                                    mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                                    ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                                    mAllPostAdapter.notifyDataSetChanged();
                                    mAllPostProgressbar.setVisibility(View.GONE);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {
                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                            Log.e(TAG, "onFailure: " + t.getMessage());
                        }
                    });
    }

    private void current_list(){
        mListView.setImageResource(R.drawable.list_brown);
        mGridView.setImageResource(R.drawable.grid);
        mGallaryView.setImageResource(R.drawable.image);
    }

    private void call_function(){

        //List
        mListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.setImageResource(R.drawable.list_brown);
                mGridView.setImageResource(R.drawable.grid);
                mGallaryView.setImageResource(R.drawable.image);
                mAllPostsRecyclerView.setAdapter(new HomeAllPostAdapter(mAllPosts,"List"));
                mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

                Service apiService = Client.getClient().create(Service.class);
                Call<APIResponse> call = apiService.getAllPosts();
                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                        if (!response.isSuccessful()) {
                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                        } else {
                            int count = response.body().getCount();
                            if (count == 0) {
                                mAllPostsNoResult.setVisibility(View.VISIBLE);
                                mAllPostNoMoreResult.setVisibility(View.GONE);
                            } else {
                                mAllPostsNoResult.setVisibility(View.GONE);
                                mAllPostNoMoreResult.setVisibility(View.VISIBLE);
                                mAllPosts = response.body().getresults();

                                mAllPostAdapter=new HomeAllPostAdapter(mAllPosts,"List");
                                mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                                mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                                ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                                mAllPostAdapter.notifyDataSetChanged();
                                mAllPostProgressbar.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        mAllPostsNoResult.setVisibility(View.VISIBLE);
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        //Grid
        mGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.setImageResource(R.drawable.list);
                mGridView.setImageResource(R.drawable.grid_brown);
                mGallaryView.setImageResource(R.drawable.image);

                Service apiService = Client.getClient().create(Service.class);
                Call<APIResponse> call = apiService.getAllPosts();
                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                        if (!response.isSuccessful()) {
                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                        } else {
                            int count = response.body().getCount();
                            if (count == 0) {
                                mAllPostsNoResult.setVisibility(View.VISIBLE);
                                mAllPostNoMoreResult.setVisibility(View.GONE);
                            } else {
                                mAllPostsNoResult.setVisibility(View.GONE);
                                mAllPostNoMoreResult.setVisibility(View.VISIBLE);
                                mAllPosts = response.body().getresults();

                                mAllPostAdapter=new HomeAllPostAdapter(mAllPosts,"Grid");
                                mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                                mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                                ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                                mAllPostAdapter.notifyDataSetChanged();
                                mAllPostProgressbar.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        mAllPostsNoResult.setVisibility(View.VISIBLE);
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        //Image
        mGallaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.setImageResource(R.drawable.list);
                mGridView.setImageResource(R.drawable.grid);
                mGallaryView.setImageResource(R.drawable.image_brown);
                Service apiService = Client.getClient().create(Service.class);
                Call<APIResponse> call = apiService.getAllPosts();
                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                        if (!response.isSuccessful()) {
                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                        } else {
                            int count = response.body().getCount();
                            if (count == 0) {
                                mAllPostsNoResult.setVisibility(View.VISIBLE);
                                mAllPostNoMoreResult.setVisibility(View.GONE);
                            } else {
                                mAllPostsNoResult.setVisibility(View.GONE);
                                mAllPostNoMoreResult.setVisibility(View.VISIBLE);
                                mAllPosts = response.body().getresults();

                                mAllPostAdapter=new HomeAllPostAdapter(mAllPosts,"Image");
                                mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                                mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                                ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                                mAllPostAdapter.notifyDataSetChanged();
                                mAllPostProgressbar.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        mAllPostsNoResult.setVisibility(View.VISIBLE);
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });
    }

}
