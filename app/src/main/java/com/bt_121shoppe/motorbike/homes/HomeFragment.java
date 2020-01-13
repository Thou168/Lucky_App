package com.bt_121shoppe.motorbike.homes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.DateInterval;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.CheckNetworkConnectionHelper;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapter;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapterV2;
import com.bt_121shoppe.motorbike.adapters.PostBestDealAdapter;
import com.bt_121shoppe.motorbike.adapters.PostBestDealAdapterV2;
import com.bt_121shoppe.motorbike.classes.DividerItemDecoration;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.classes.PreCachingLayoutManager;
import com.bt_121shoppe.motorbike.listener.OnNetworkConnectionChangeListener;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String TAG= HomeFragment.class.getSimpleName();
    private View view;
    private TextInputLayout mFilterCategory,mFilterBrand,mFilterYear,mFilterPriceRange,mFilterPostType;
    private TextInputEditText mETFilterCategory,mETFilterBrandET,mETFilterYear,mETFilterPriceRange,mETFilterPostType;
    private RecyclerView mBestDealRecyclerView,mAllPostsRecyclerView;
    private TextView mBestDealNoResult,mAllPostsNoResult,mAllPostNoMoreResult;
    private ProgressBar mBestDealProgressbar,mAllPostProgressbar;
    private PreCachingLayoutManager mLayoutManager;
    private LinearLayoutManager mAllPostLayoutManager;
    //private PostBestDealAdapterV2 mPostBestDealAdpater;
    //private AllPostAdapterV2 mAllPostAdapter;
    private PostBestDealAdapter mPostBestDealAdpater;
    private AllPostAdapter mAllPostAdapter;
    private ImageView mListView,mGridView,mGallaryView;
    private TextView mBestDealText;

    private int mPostTypeId=0,mCategoryId=0,mBrandId=0,mYearId=0;
    private double mMinPrice=0,mMaxPrice=0;
    //private List<PostViewModel> mPostBestDeals;
    //private List<PostViewModel> mAllPosts;
    private List<PostProduct> mAllPosts;
    private List<PostProduct> mPostBestDeals;

    TextView best_match;
    int best_m = 0;
    private int [] bestm;
    int index = 0;

    TextView connection;
    RelativeLayout rl_besdeal;
    RelativeLayout rl_newpost;
    ConstraintLayout ct_layout;
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

//        best match
        best_match=view.findViewById(R.id.best_match);
        //Log.e(TAG,"Before run best deal");
        setUpBestDeal();
        setupAllPosts(index);

        best_match.setOnClickListener(v -> {
            //hide keyboard
            InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(best_match, InputMethodManager.SHOW_FORCED);

            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.best_match_dialog,null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
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
                    bottomSheetDialog.dismiss();
//                    if (mAllPosts.size()!=0) {
//                        if (index == 0) {
//                            Toast.makeText(getActivity(), "Index 0", Toast.LENGTH_SHORT).show();
//                            mAllPostsRecyclerView.setAdapter(new AllPostAdapter(mAllPosts, "List"));
//                            mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
//                            bottomSheetDialog.dismiss();
//                        } else if (index == 1) {
//                            Toast.makeText(getActivity(), "Index 1", Toast.LENGTH_SHORT).show();
//                            bottomSheetDialog.dismiss();
//                        } else if (index == 2) {
//                            Toast.makeText(getActivity(), "Index 2", Toast.LENGTH_SHORT).show();
//                            bottomSheetDialog.dismiss();
//                        } else if (index == 3) {
//                            Toast.makeText(getActivity(), "Index 3", Toast.LENGTH_SHORT).show();
//                            bottomSheetDialog.dismiss();
//                        }
//                    }else {
//                        if (index == 0) {
//                            Toast.makeText(getActivity(), "Index 0", Toast.LENGTH_SHORT).show();
//                            bottomSheetDialog.dismiss();
//                        } else if (index == 1) {
//                            Toast.makeText(getActivity(), "Index 1", Toast.LENGTH_SHORT).show();
//                            bottomSheetDialog.dismiss();
//                        } else if (index == 2) {
//                            Toast.makeText(getActivity(), "Index 2", Toast.LENGTH_SHORT).show();
//                            bottomSheetDialog.dismiss();
//                        } else if (index == 3) {
//                            Toast.makeText(getActivity(), "Index 3", Toast.LENGTH_SHORT).show();
//                            bottomSheetDialog.dismiss();
//                        }
//                    }
                }
            });
        });

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(isAdded()) {
//            mETFilterPostType.setText(getString(R.string.all));
//            mETFilterCategory.setText(getString(R.string.all));
//            mETFilterBrandET.setText(getString(R.string.all));
//            mETFilterYear.setText(getString(R.string.all));
//            mETFilterPriceRange.setText(getString(R.string.all));
//            //setupAllPosts();
//        }
//
//    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
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
        mBestDealRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        //mPostBestDealAdpater=new PostBestDealAdapterV2(new ArrayList<>());
        mPostBestDealAdpater=new PostBestDealAdapter(new ArrayList<>());
        prepareBestDealContent();
//        mBestDealRecyclerView.setAdapter(mPostBestDealAdpater);
//        mPostBestDealAdpater.notifyDataSetChanged();
        //mPostBestDealAdpater.setHasStableIds(true);

    }

    private void prepareBestDealContent(){

//        Service apiService= Client.getClient().create(Service.class);
//        Call<APIResponse> call=apiService.getPostBestDeal();
//        call.enqueue(new Callback<APIResponse>() {
//            @Override
//            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//
//                if(!response.isSuccessful()){
//                    mBestDealNoResult.setVisibility(View.VISIBLE);
//                }else{
//                    int count=response.body().getCount();
//                    if(count==0)
//                        mBestDealNoResult.setVisibility(View.VISIBLE);
//                    else{
//                        mBestDealNoResult.setVisibility(View.GONE);
//                        mPostBestDeals=response.body().getresults();
//                        mPostBestDealAdpater.addItems(mPostBestDeals);
//                    }
//                }
//                mBestDealProgressbar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<APIResponse> call, Throwable t) {
//                Log.e(TAG,"Failed best deal with message:"+t.getMessage());
//                mBestDealProgressbar.setVisibility(View.GONE);
//                mBestDealNoResult.setVisibility(View.VISIBLE);
//            }
//        });

        //From Firebase
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query myQuery=reference.child(ConsumeAPI.FB_POST).orderByChild("createdAt");
        mPostBestDeals=new ArrayList<>();
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPostBestDeals=new ArrayList<>();
                mPostBestDealAdpater= new PostBestDealAdapter(new ArrayList<>());
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try{
                        JSONObject obj=new JSONObject((Map) snapshot.getValue());
                        int status=obj.getInt("status");
                        String discountAmount=obj.getString("discountAmount");
                        String type = obj.getString("type");
//                        float category = obj.getInt("category");
                        if(status==4 && Double.parseDouble(discountAmount)>0 && !type.equals("buy")){
                            String createdAt = obj.getString("createdAt");
                            long diffInDays=0;
                            if(createdAt!=null) {
                                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                Date date = utcFormat.parse(createdAt);
//                              DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//                              pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));
                                Date currentdate = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String ccdate=utcFormat.format(currentdate);
                                Date endDate   = utcFormat.parse(ccdate);
                                long duration  = endDate.getTime() - date.getTime();
                                diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

                            }
                            //Log.d(TAG,"Days "+diffInDays);
                            if(diffInDays<=7) {
                                String id = obj.getString("id");
                                int user_id = obj.getInt("createdBy");
                                String coverUrl = obj.getString("coverUrl");
                                String price = obj.getString("price");
                                String discountType = obj.getString("discountType");
                                int viewCount = obj.getInt("viewCount");
                                String title = obj.getString("subTitle");
                                //String[] splitTitle=title.split(",");
                                String fcolor=obj.getString("color");
                                mPostBestDeals.add(new PostProduct(Integer.parseInt(id), user_id, title, type, coverUrl, price, "", viewCount, discountType, discountAmount,fcolor));
                            }
                        }
                    }catch (JSONException | ParseException je){
                        je.printStackTrace();
                    }
                }

                if(mPostBestDeals.size()==0)
                    mBestDealNoResult.setVisibility(View.VISIBLE);
                else {
                    //Collections.sort(mAllPosts, (s1, s2)->Integer.compare(s2.getId(),s1.getId()));
                    Collections.sort(mPostBestDeals, (s1, s2) -> Integer.compare(s1.getPostId(), s2.getPostId()));
                    mPostBestDealAdpater.addItems(mPostBestDeals);
                    mBestDealRecyclerView.setAdapter(mPostBestDealAdpater);
                    mPostBestDealAdpater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        mAllPostsRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mAllPostAdapter=new AllPostAdapter(new ArrayList<>(),"List");
        //mAllPostAdapter=new AllPostAdapterV2(new ArrayList<>(),"List");
        prepareAllPostsContent(index);
        //mAllPostProgressbar.setVisibility(View.GONE);
    }

    private void prepareAllPostsContent(int index){
        //new process
        //new Handler().postDelayed(()-> {

//                    Service apiService = Client.getClient().create(Service.class);
//                    Call<APIResponse> call = apiService.getAllPosts();
//                    call.enqueue(new Callback<APIResponse>() {
//                        @Override
//                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//
//                            if (!response.isSuccessful()) {
//                                mAllPostsNoResult.setVisibility(View.VISIBLE);
//                            } else {
//                                int count = response.body().getCount();
//                                if (count == 0) {
//                                    mAllPostsNoResult.setVisibility(View.VISIBLE);
//                                    mAllPostNoMoreResult.setVisibility(View.GONE);
//                                } else {
//                                    mAllPostsNoResult.setVisibility(View.GONE);
//                                    mAllPostNoMoreResult.setVisibility(View.VISIBLE);
//                                    mAllPosts = response.body().getresults();
//                                }
//
//                                Log.d(TAG, "count posts " + mAllPosts.size());
//                                Collections.sort(mAllPosts, (s1, s2) -> Integer.compare(s2.getId(), s1.getId()));
//                                mAllPostAdapter.addItems(mAllPosts);
//                                mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
//                                ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
//                                mAllPostAdapter.notifyDataSetChanged();
//                                mAllPostProgressbar.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<APIResponse> call, Throwable t) {
//                            mAllPostsNoResult.setVisibility(View.VISIBLE);
//                            Log.e(TAG, "onFailure: " + t.getMessage());
//                        }
//                    });
//        mListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListView.setImageResource(R.drawable.icon_list_c);
//                mGridView.setImageResource(R.drawable.icon_grid);
//                mGallaryView.setImageResource(R.drawable.icon_image);
//                mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts, "List"));
//                mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
//            }
//        });
//
//        mGridView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListView.setImageResource(R.drawable.icon_list);
//                mGridView.setImageResource(R.drawable.icon_grid_c);
//                mGallaryView.setImageResource(R.drawable.icon_image);
//                mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts, "Grid"));
//                mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//            }
//        });
//
//        mGallaryView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListView.setImageResource(R.drawable.icon_list);
//                mGridView.setImageResource(R.drawable.icon_grid);
//                mGallaryView.setImageResource(R.drawable.icon_image_c);
//                mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts, "Image"));
//                mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
//            }
//        });
                //},5000);

        //get from api
//        new Handler().postDelayed(()->{
//            try {
//                String urlAllPosts=ConsumeAPI.BASE_URL+"allposts/";
//                String response=CommonFunction.doGetRequest(urlAllPosts);
//                Log.d(TAG,"All posts response:"+response);
//                APIResponse apiResponseObj=new APIResponse();
//                Gson gson=new Gson();
//                apiResponseObj=gson.fromJson(response,APIResponse.class);
//                int count=apiResponseObj.getresults().size()>0?apiResponseObj.getresults().size():0;
//                mAllPostProgressbar.setVisibility(View.GONE);
//                if(count==0) {
//                    mAllPostsNoResult.setVisibility(View.VISIBLE);
//                    mAllPostNoMoreResult.setVisibility(View.GONE);
//                }else{
//                    mAllPostsNoResult.setVisibility(View.GONE);
//                    mAllPostNoMoreResult.setVisibility(View.VISIBLE);
//                    mAllPosts=apiResponseObj.getresults();
//                }
//
//                Log.d(TAG,"count posts "+mAllPosts.size());
//                Collections.sort(mAllPosts, (s1, s2)->Integer.compare(s2.getId(),s1.getId()));
//                mAllPostAdapter.addItems(mAllPosts);
//                mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
//                ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
//                mAllPostAdapter.notifyDataSetChanged();
//
//                mListView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mListView.setImageResource(R.drawable.icon_list_c);
//                        mGridView.setImageResource(R.drawable.icon_grid);
//                        mGallaryView.setImageResource(R.drawable.icon_image);
//                        mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts,"List"));
//                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
//                    }
//                });
//
//                mGridView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mListView.setImageResource(R.drawable.icon_list);
//                        mGridView.setImageResource(R.drawable.icon_grid_c);
//                        mGallaryView.setImageResource(R.drawable.icon_image);
//                        mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts,"Grid"));
//                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
//                    }
//                });
//
//                mGallaryView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mListView.setImageResource(R.drawable.icon_list);
//                        mGridView.setImageResource(R.drawable.icon_grid);
//                        mGallaryView.setImageResource(R.drawable.icon_image_c);
//                        mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts,"Image"));
//                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
//                    }
//                });
//
//            }catch (IOException ioe) {
//                ioe.printStackTrace();
//            }
//        },500);

        //get from firebase
        //Log.e(TAG,"Here call best deal");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query myQuery=reference.child(ConsumeAPI.FB_POST).orderByChild("createdAt");
        mAllPosts=new ArrayList<>();
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAllPosts=new ArrayList<>();
                mAllPostAdapter=new AllPostAdapter(new ArrayList<>(),"List");
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try{
                        JSONObject obj=new JSONObject((Map) snapshot.getValue());
                        String type=obj.getString("type");
//                        float category = obj.getInt("category");
                        int status=obj.getInt("status");
                        String discountAmount=obj.getString("discountAmount");
                        if(status==4 && Double.parseDouble(discountAmount)<=0 && !type.equals("buy")){
                            String createdAt = obj.getString("createdAt");
                            long diffInDays=0;
                            if(createdAt!=null) {
                                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                Date date = utcFormat.parse(createdAt);
                                Date currentdate = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String ccdate=utcFormat.format(currentdate);
                                Date startDate = date;
                                Date endDate   = utcFormat.parse(ccdate);
                                long duration  = endDate.getTime() - startDate.getTime();
                                diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
                            }
                            if(diffInDays<=15) {
                                String id = obj.getString("id");
                                int user_id = obj.getInt("createdBy");
                                String coverUrl = obj.getString("coverUrl");
                                String price = obj.getString("price");
                                String discountType = obj.getString("discountType");
                                int viewCount = obj.getInt("viewCount");
                                String title = obj.getString("subTitle");
                                String fcolor=obj.getString("color");
                                //String type = obj.getString("type");
                                //String[] splitTitle=title.split(",");
                                mAllPosts.add(new PostProduct(Integer.parseInt(id), user_id, title, type, coverUrl, price, "", viewCount, discountType, discountAmount,fcolor));
                            }
                        }
                    }catch (JSONException | ParseException je) {
                        je.printStackTrace();
                    }
                }


                if(mAllPosts.size()==0){
                    mAllPostsNoResult.setVisibility(View.VISIBLE);
                }else {
                    mAllPostsNoResult.setVisibility(View.GONE);
                    //Collections.sort(mAllPosts, (s1, s2)->Integer.compare(s2.getId(),s1.getId()));
                    switch (index){
                        case 0:
                            Collections.sort(mAllPosts, (s1, s2) -> Integer.compare(s2.getPostId(), s1.getPostId()));
                            break;
                        case 1:
                            Collections.sort(mAllPosts, (s1, s2) -> Integer.compare(s2.getCountView(), s1.getCountView()));
                            break;
                        case 2:
                            Collections.sort(mAllPosts, (s1, s2) -> Double.compare(Double.valueOf(s1.getPostPrice()), Double.valueOf(s2.getPostPrice())));
                            break;
                        case 3:
                            Collections.sort(mAllPosts, (s1, s2) -> Double.compare(Double.valueOf(s2.getPostPrice()), Double.valueOf(s1.getPostPrice())));
                            break;
                    }


                    mAllPostAdapter.addItems(mAllPosts);
                    mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                    ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                    mAllPostAdapter.notifyDataSetChanged();
                }
                mListView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListView.setImageResource(R.drawable.list_brown);
                        mGridView.setImageResource(R.drawable.grid);
                        mGallaryView.setImageResource(R.drawable.image);
                        //mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts,"List"));
                        mAllPostsRecyclerView.setAdapter(new AllPostAdapter(mAllPosts,"List"));
                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                    }
                });

                mGridView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListView.setImageResource(R.drawable.list);
                        mGridView.setImageResource(R.drawable.grid_brown);
                        mGallaryView.setImageResource(R.drawable.image);
                        //mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts,"Grid"));
                        mAllPostsRecyclerView.setAdapter(new AllPostAdapter(mAllPosts,"Grid"));
                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                    }
                });

                mGallaryView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListView.setImageResource(R.drawable.list);
                        mGridView.setImageResource(R.drawable.grid);
                        mGallaryView.setImageResource(R.drawable.image_brown);
                        //mAllPostsRecyclerView.setAdapter(new AllPostAdapterV2(mAllPosts,"Image"));
                        mAllPostsRecyclerView.setAdapter(new AllPostAdapter(mAllPosts,"Image"));
                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
