package com.bt_121shoppe.motorbike.homes;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.searches.SearchTypeAdapter;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFilterResultFragment extends android.app.Fragment {
    private static final String TAG= HomeFilterResultFragment.class.getSimpleName();

    private View view;
    private Bundle bundle;
    private int mPostTypeId=0,mCategoryId=0,mBrandId=0,mYearId=0;
    private double mMinPrice=0,mMaxPrice=0;
    private String mViewType="",mCurrentLanguage;
    private ArrayList<PostViewModel> mPosts;
    private int[] modelIdListItems;
    private int countresult=0;

    private ImageView mGridView,mListView,mGallaryView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog mProgress;
    private SearchTypeAdapter mAdapter;
    private ProgressBar mProgressbar;
    private TextInputEditText mFilterCategory,mFilterBrand,mFilterYear,mFilterPriceRange,mFilterPostType;
    private TextView mCountResultTextView,mNoResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home_search, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        mCurrentLanguage = preferences.getString("My_Lang", "");

        bundle=getArguments();
        mPostTypeId=bundle.getInt("postTypeId",0);
        mCategoryId=bundle.getInt("categoryId",0);
        mBrandId=bundle.getInt("brandId",0);
        mYearId=bundle.getInt("yearId",0);
        mMinPrice=bundle.getDouble("minPrice",0);
        mMaxPrice=bundle.getDouble("maxPrice",0);

        Log.d(TAG,"post="+mPostTypeId+" cat= "+mCategoryId+" brand="+mBrandId+" year="+mYearId);

        mProgress=new ProgressDialog(getContext());
        mProgress.setMessage(getString(R.string.please_wait));

        mRecyclerView=view.findViewById(R.id.RecylerView);
        mListView=view.findViewById(R.id.img_list);
        mGridView=view.findViewById(R.id.grid);
        mGallaryView=view.findViewById(R.id.btn_image);
        mProgressbar=view.findViewById(R.id.progress_bar);
        mFilterCategory=view.findViewById(R.id.editTextCategory);
        mFilterBrand=view.findViewById(R.id.editTextBrand);
        mFilterYear=view.findViewById(R.id.editTextYear);
        mFilterPriceRange=view.findViewById(R.id.editTextPriceRange);
        mFilterPostType=view.findViewById(R.id.editTextPostType);
        mCountResultTextView=view.findViewById(R.id.countResult);
        mNoResultTextView=view.findViewById(R.id.noResult);

        mFilterPostType.setFocusable(false);
        mFilterCategory.setFocusable(false);
        mFilterBrand.setFocusable(false);
        mFilterYear.setFocusable(false);
        mFilterPriceRange.setFocusable(false);

        /* initial value to filter control */
        //Post Type
        switch (mPostTypeId){
            case 0:
                mFilterPostType.setText(getString(R.string.all));
                break;
            case 1:
                mFilterPostType.setText(getString(R.string.sell));
                break;
            case 2:
                mFilterPostType.setText(getString(R.string.rent));
                break;
        }
        //Category
        if(mCategoryId==0){
            mFilterCategory.setText(getString(R.string.all));
        }else {
            try {
                String responseCategory = CommonFunction.doGetRequest(ConsumeAPI.BASE_URL + "api/v1/categories/"+mCategoryId);
                try{
                    JSONObject obj=new JSONObject(responseCategory);
                    if(mCurrentLanguage.equals("km"))
                        mFilterCategory.setText(obj.getString("cat_name_kh"));
                    else
                        mFilterCategory.setText(obj.getString("cat_name"));
                }catch (JSONException je){
                    je.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Brand
        if(mBrandId==0){
            mFilterBrand.setText(getString(R.string.all));
        }else{
            try{
                String responseBrand=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/brands/"+mBrandId);
                try{
                    JSONObject obj=new JSONObject(responseBrand);
                    if(mCurrentLanguage.equals("km"))
                        mFilterBrand.setText(obj.getString("brand_name_as_kh"));
                    else
                        mFilterBrand.setText(obj.getString("brand_name"));
                }catch (JSONException je){
                    je.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //Year
        if(mYearId==0){
            mFilterYear.setText(getString(R.string.all));
        }else{
            try{
                String responseYear=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/years/"+mYearId);
                try{
                    JSONObject obj=new JSONObject(responseYear);
                    mFilterYear.setText(obj.getString("year"));
                }catch (JSONException je){
                    je.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //price range
        if(mMinPrice>1||mMaxPrice>1){
            if(mMinPrice>1&&mMaxPrice>1)
                mFilterPriceRange.setText("$"+mMinPrice+" - $"+mMaxPrice);
            else if(mMinPrice>1&&mMaxPrice<1)
                mFilterPriceRange.setText("$"+mMinPrice+" - $");
            else if(mMinPrice<1&&mMaxPrice>1)
                mFilterPriceRange.setText("$0 - $"+mMaxPrice);
        }else
            mFilterPriceRange.setText(getString(R.string.all));

        /* end initial value to filter control */

        /* start action event listener */
        mFilterPostType.setOnClickListener(new View.OnClickListener() {
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

        mFilterCategory.setOnClickListener(new View.OnClickListener() {
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

        mFilterBrand.setOnClickListener(new View.OnClickListener() {
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

        mFilterYear.setOnClickListener(new View.OnClickListener() {
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

        mFilterPriceRange.setOnClickListener(new View.OnClickListener() {
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

        modelIdListItems=getModelIdList(mBrandId);
        setupFilterResults(String.valueOf(mPostTypeId),"List",mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);

        return view;
    }

    private void setupFilterResults(String postType,String viewType,int categoryId,int[] modelsId,int yearId,double minPrice,double maxPrice){
        mProgress.show();
        mPosts=new ArrayList<>();
        mLayoutManager=new GridLayoutManager(getContext(),1);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable= ContextCompat.getDrawable(getContext(),R.drawable.divider_drawable);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mAdapter=new SearchTypeAdapter(new ArrayList<>(),viewType);
        readPosts(postType,categoryId,modelsId,yearId,minPrice,maxPrice);
        mProgressbar.setVisibility(View.GONE);

    }

    private void readPosts(String postType,int categoryId,int[] modelIdList,int yearId,double minPrice,double maxPrice){
        String category=categoryId==0?"":String.valueOf(categoryId);
        String year=yearId==0?"":String.valueOf(yearId);
        String strMinPrice=minPrice<1?"":String.valueOf(minPrice);
        String strMaxPrice=maxPrice<1?"":String.valueOf(maxPrice);
        String type=postType.equals("1")?"sell":postType.equals("2")?"rent":"";

        //Log.d(TAG,"model id "+modelIdList.length);
        if(modelIdList.length==0) {
            mPosts = new ArrayList<>();
            //old process
            /*
            String url = ConsumeAPI.BASE_URL + "relatedpost/?post_type=" + type + "&category=" + category + "&modeling=&min_price="+strMinPrice+"&max_price="+strMaxPrice+"&year=" + year;
            String response = "";
            try {
                response = CommonFunction.doGetRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "response " + response);
            APIResponse APIResponse = new APIResponse();
            Gson gson = new Gson();
            APIResponse = gson.fromJson(response, APIResponse.class);
            mPosts = APIResponse.getresults();
            mAdapter.addItems(mPosts);
            countresult=mPosts.size();
            */
            //new process
            Service apiService= Client.getClient().create(Service.class);
            Call<APIResponse> call=apiService.getFilterResult(type,category,strMinPrice,strMaxPrice,year);
            call.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    if(!response.isSuccessful()){
                        Log.e(TAG,"Get Filter Result failure:"+response.code());
                    }else{
                        mPosts=response.body().getresults();
                        mAdapter.addItems(mPosts);
                        countresult =mPosts.size();
                        mCountResultTextView.setText(mPosts.size()+" "+getString(R.string.result_name));
                        if(countresult ==0)
                            mNoResultTextView.setVisibility(View.VISIBLE);
                        else
                            mNoResultTextView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Log.e(TAG,"Get Filter Result failure:"+t.getMessage());
                }
            });



        }else{

            for(int i=0;i<modelIdList.length;i++){
                String modelId=String.valueOf(modelIdList[i]);
                mPosts = new ArrayList<>();
                /*
                String url = ConsumeAPI.BASE_URL + "relatedpost/?post_type=" + type + "&category=" + category + "&modeling="+modelId+"&min_price="+strMinPrice+"&max_price="+strMaxPrice+"&year=" + year;
                String response = "";
                try {
                    response = CommonFunction.doGetRequest(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.d(TAG, "response with model " + response);
                APIResponse APIResponse = new APIResponse();
                Gson gson = new Gson();
                APIResponse = gson.fromJson(response, APIResponse.class);
                mPosts = APIResponse.getresults();
                mAdapter.addItems(mPosts);
                countresult = countresult +mPosts.size();
                */
                //new process
                Service apiService= Client.getClient().create(Service.class);
                Call<APIResponse> call=apiService.getFilterResult(type,category,strMinPrice,strMaxPrice,year);
                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        if(!response.isSuccessful()){
                            Log.e(TAG,"Get Filter Result failure:"+response.code());
                        }else{
                            mPosts=response.body().getresults();
                            mAdapter.addItems(mPosts);
                            countresult =countresult+mPosts.size();
                            mCountResultTextView.setText(countresult+" "+getString(R.string.result_name));

                            if(countresult ==0)
                                mNoResultTextView.setVisibility(View.VISIBLE);
                            else
                                mNoResultTextView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        Log.e(TAG,"Get Filter Result failure:"+t.getMessage());
                    }
                });

            }
        }
        //mCountResultTextView.setText(countresult+" "+getString(R.string.result_name));
        mRecyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView,false);
        mAdapter.notifyDataSetChanged();
        mProgress.dismiss();

//        if(countresult ==0)
//            mNoResultTextView.setVisibility(View.VISIBLE);
//        else
//            mNoResultTextView.setVisibility(View.GONE);

        mListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="List";
                mListView.setImageResource(R.drawable.icon_list_c);
                mGridView.setImageResource(R.drawable.icon_grid);
                mGallaryView.setImageResource(R.drawable.icon_image);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"List"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
        });

        mGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="Grid";
                mListView.setImageResource(R.drawable.icon_list);
                mGridView.setImageResource(R.drawable.icon_grid_c);
                mGallaryView.setImageResource(R.drawable.icon_image);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"Grid"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            }
        });

        mGallaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="Image";
                mListView.setImageResource(R.drawable.icon_list);
                mGridView.setImageResource(R.drawable.icon_grid);
                mGallaryView.setImageResource(R.drawable.icon_image_c);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"Image"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
        });
    }

    private int[] getModelIdList(int brandId){
        int[] modelsId=new int[0];
        String response="";
        int count=0,ccount=0;
        try{
            response=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/models/");
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            JSONObject obj=new JSONObject(response);
            JSONArray results=obj.getJSONArray("results");
            for(int i=0;i<results.length();i++){
                JSONObject oobj=results.getJSONObject(i);
                if(brandId==oobj.getInt("brand"))
                    count++;
            }
            modelsId=new int[count];
            for(int i=0;i<results.length();i++){
                JSONObject oobj=results.getJSONObject(i);
                if(brandId==oobj.getInt("brand")){
                    modelsId[ccount]=oobj.getInt("id");
                    ccount++;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return modelsId;
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}
