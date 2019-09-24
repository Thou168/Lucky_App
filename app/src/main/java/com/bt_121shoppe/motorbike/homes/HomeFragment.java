package com.bt_121shoppe.motorbike.homes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapter;
import com.bt_121shoppe.motorbike.adapters.PostBestDealAdapterV2;
import com.bt_121shoppe.motorbike.classes.DividerItemDecoration;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.classes.PreCachingLayoutManager;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final String TAG= HomeFragment.class.getSimpleName();
    private View view;
    private TextInputLayout mFilterCategory,mFilterBrand,mFilterYear,mFilterPriceRange,mFilterPostType;
    private TextInputEditText mETFilterCategory,mETFilterBrandET,mETFilterYear,mETFilterPriceRange,mETFilterPostType;
    private RecyclerView mBestDealRecyclerView,mAllPostsRecyclerView;
    private TextView mBestDealNoResult,mAllPostsNoResult;
    private ProgressBar mBestDealProgressbar,mAllPostProgressbar;
    private PreCachingLayoutManager mLayoutManager;
    private LinearLayoutManager mAllPostLayoutManager;
    private PostBestDealAdapterV2 mPostBestDealAdpater;
    private AllPostAdapter mAllPostAdapter;
    private ImageView mListView,mGridView,mGallaryView;
    private TextView mBestDealText;

    private int mPostTypeId=0,mCategoryId=0,mBrandId=0,mYearId=0;
    private double mMinPrice=0,mMaxPrice=0;
    private List<PostViewModel> mPostBestDeals;
    private List<PostProduct> mAllPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home_frist, container, false);
        mFilterCategory=view.findViewById(R.id.CategoryLayout);
        mFilterBrand=view.findViewById(R.id.BrandLayout);
        mFilterYear=view.findViewById(R.id.YearLayout);
        mFilterPriceRange=view.findViewById(R.id.PriceRangeLayout);
        mFilterPostType=view.findViewById(R.id.PostTypeLayout);
        mETFilterPostType=view.findViewById(R.id.editTextPostType);
        mETFilterCategory=view.findViewById(R.id.editTextCategory);
        mETFilterBrandET=view.findViewById(R.id.editTextBrand);
        mETFilterYear=view.findViewById(R.id.editTextYear);
        mETFilterPriceRange=view.findViewById(R.id.editTextPriceRange);
        mBestDealRecyclerView=view.findViewById(R.id.horizontal);
        mBestDealNoResult=view.findViewById(R.id.text);
        mBestDealProgressbar=view.findViewById(R.id.progress_bar);
        mAllPostsRecyclerView=view.findViewById(R.id.list_new_post);
        mAllPostProgressbar=view.findViewById(R.id.progress_bar1);
        mAllPostsNoResult=view.findViewById(R.id.text1);
        mListView=view.findViewById(R.id.img_list);
        mGridView=view.findViewById(R.id.grid);
        mGallaryView=view.findViewById(R.id.btn_image);
        mBestDealText=view.findViewById(R.id.bestDeal);

        mETFilterPostType.setFocusable(false);
        mETFilterCategory.setFocusable(false);
        mETFilterBrandET.setFocusable(false);
        mETFilterYear.setFocusable(false);
        mETFilterPriceRange.setFocusable(false);

        mBestDealProgressbar.setVisibility(View.VISIBLE);
        mAllPostProgressbar.setVisibility(View.VISIBLE);

        setUpBestDeal();
        setupAllPosts();

        /* start action event listener */
        mETFilterPostType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType", CommonFunction.FILTERPOSTTYPE);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mETFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType",CommonFunction.FILTERCATEGORY);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mETFilterBrandET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType",CommonFunction.FILTERBRAND);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mETFilterYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType",CommonFunction.FILTERYEAR);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mETFilterPriceRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType",CommonFunction.FILTERPRICERANGE);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });
        /* end action event lister */

        return view;
    }

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
        mPostBestDealAdpater=new PostBestDealAdapterV2(new ArrayList<>());
        prepareBestDealContent();
        mBestDealRecyclerView.setAdapter(mPostBestDealAdpater);
        mPostBestDealAdpater.notifyDataSetChanged();
        //mPostBestDealAdpater.setHasStableIds(true);
    }

    private void prepareBestDealContent(){
        new Handler().postDelayed(()->{
            String urlBestDeal= ConsumeAPI.BASE_URL+"bestdeal/";

            try{
                String response=CommonFunction.doGetRequest(urlBestDeal);
                Log.d(TAG,response);
                APIResponse APIResponse =new APIResponse();
                Gson gson=new Gson();
                APIResponse =gson.fromJson(response, APIResponse.class);
                int count= APIResponse.getresults().size()>0? APIResponse.getresults().size():0;
                mBestDealProgressbar.setVisibility(View.GONE);
                if(count==0){
                    mBestDealNoResult.setVisibility(View.VISIBLE);
                }else{
                    mBestDealNoResult.setVisibility(View.GONE);
                    mPostBestDeals= APIResponse.getresults();
                }
            }catch (IOException ioe) {
                ioe.printStackTrace();
            }
            mPostBestDealAdpater.addItems(mPostBestDeals);
        },2000);
    }

    private void setupAllPosts(){
        mAllPosts=new ArrayList<>();
        mAllPostLayoutManager=new GridLayoutManager(getContext(),1);
        mAllPostLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mAllPostLayoutManager.setAutoMeasureEnabled(true);
        mAllPostsRecyclerView.setHasFixedSize(true);
        mAllPostsRecyclerView.setItemViewCacheSize(20);
        mAllPostsRecyclerView.setDrawingCacheEnabled(true);
        mAllPostsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAllPostsRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mAllPostsRecyclerView.setLayoutManager(mAllPostLayoutManager);
        mAllPostsRecyclerView.setNestedScrollingEnabled(false);
        mAllPostsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable=ContextCompat.getDrawable(getContext(),R.drawable.divider_drawable);
        mAllPostsRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mAllPostAdapter=new AllPostAdapter(new ArrayList<>(),"List");
        prepareAllPostsContent();
        mAllPostProgressbar.setVisibility(View.GONE);
    }

    private void prepareAllPostsContent(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query myQuery=reference.child(ConsumeAPI.FB_POST).orderByChild("createdAt");
        mAllPosts=new ArrayList<>();
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try{
                        JSONObject obj=new JSONObject((Map) snapshot.getValue());
                        int status=obj.getInt("status");
                        if(status==4){
                            String id=obj.getString("id");
                            int user_id = obj.getInt("createdBy");
                            String coverUrl = obj.getString("coverUrl");
                            String createdAt = obj.getString("createdAt");
                            String price=obj.getString("price");
                            String discountAmount=obj.getString("discountAmount");
                            String discountType=obj.getString("discountType");
                            String location=obj.getString("location");
                            int viewCount=obj.getInt("viewCount");
                            String title=obj.getString("subTitle");
                            String type=obj.getString("type");
                            String[] splitTitle=title.split(",");
                            mAllPosts.add(new PostProduct(Integer.parseInt(id),user_id,splitTitle[0],type,coverUrl,price,"",viewCount ,discountType,discountAmount));
                            Log.d(TAG,"count posts before "+mAllPosts.size());
                        }
                    }catch (JSONException je){
                        je.printStackTrace();
                    }
                }
                Log.d(TAG,"count posts "+mAllPosts.size());
                Collections.sort(mAllPosts, (s1, s2)->Integer.compare(s2.getPostId(),s1.getPostId()));
                mAllPostAdapter.addItems(mAllPosts);
                mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                mAllPostAdapter.notifyDataSetChanged();

                mListView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListView.setImageResource(R.drawable.icon_list_c);
                        mGridView.setImageResource(R.drawable.icon_grid);
                        mGallaryView.setImageResource(R.drawable.icon_image);
                        mAllPostsRecyclerView.setAdapter(new AllPostAdapter(mAllPosts,"List"));
                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                    }
                });

                mGridView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListView.setImageResource(R.drawable.icon_list);
                        mGridView.setImageResource(R.drawable.icon_grid_c);
                        mGallaryView.setImageResource(R.drawable.icon_image);
                        mAllPostsRecyclerView.setAdapter(new AllPostAdapter(mAllPosts,"Grid"));
                        mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                    }
                });

                mGallaryView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListView.setImageResource(R.drawable.icon_list);
                        mGridView.setImageResource(R.drawable.icon_grid);
                        mGallaryView.setImageResource(R.drawable.icon_image_c);
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
