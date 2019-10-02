package com.bt_121shoppe.motorbike.homes;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.FilterConditionAdapter;
import com.bt_121shoppe.motorbike.classes.DividerItemDecoration;
import com.bt_121shoppe.motorbike.classes.PreCachingLayoutManager;
import com.bt_121shoppe.motorbike.models.FilterConditionViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class HomeFilterConditionFragment extends Fragment {

    private static final String TAG=HomeFilterConditionFragment.class.getSimpleName();
    private View view;
    private int filterType=0,postTypeId=0,categoryId=0,brandId=0,yearId=0;
    private ArrayList<FilterConditionViewModel> mFilterItemsList;
    private String mFilterUrl="",currentLanguage;
    private Bundle bundle;
    private double minPrice=0,maxPrice=0;

    private RecyclerView mRecylerView;
    private FilterConditionAdapter mAdapter;
    private PreCachingLayoutManager mLayoutManager;
    private TextView mBack,mFilterTitle;
    private ProgressBar mProgressbar;
    private TextInputEditText mFilterCategory,mFilterBrand,mFilterYear,mFilterPriceRange,mFilterPostType;
    private RelativeLayout mFilterPriceRangeLayout,mFilterListLayout;
    private Button mSubmitFilterPriceRange;
    private MaterialEditText mFilterMinPrice,mFilterMaxPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_home_filter_condition, container, false);

        bundle=getArguments();
        filterType=bundle.getInt("filterType",0);
        postTypeId=bundle.getInt("postTypeId",0);
        categoryId=bundle.getInt("categoryId",0);
        brandId=bundle.getInt("brandId",0);
        yearId=bundle.getInt("yearId",0);
        minPrice=bundle.getDouble("minPrice",0);
        maxPrice=bundle.getDouble("maxPrice",0);

        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");

        mRecylerView=view.findViewById(R.id.filterRecylerView);
        mBack=view.findViewById(R.id.backTextView);
        mFilterTitle=view.findViewById(R.id.filterTypeTitle);
        mProgressbar=view.findViewById(R.id.progress_bar);
        mFilterCategory=view.findViewById(R.id.editTextCategory);
        mFilterBrand=view.findViewById(R.id.editTextBrand);
        mFilterYear=view.findViewById(R.id.editTextYear);
        mFilterPriceRange=view.findViewById(R.id.editTextPriceRange);
        mFilterPostType=view.findViewById(R.id.editTextPostType);
        mFilterPriceRangeLayout=view.findViewById(R.id.priceRangeFilterLayout);
        mFilterListLayout=view.findViewById(R.id.filterListLayout);
        mSubmitFilterPriceRange=view.findViewById(R.id.submitFilter);
        mFilterMinPrice=view.findViewById(R.id.filterMinPrice);
        mFilterMaxPrice=view.findViewById(R.id.filterMaxPrice);

        mFilterPriceRangeLayout.setVisibility(View.GONE);

        mFilterPostType.setFocusable(false);
        mFilterCategory.setFocusable(false);
        mFilterBrand.setFocusable(false);
        mFilterYear.setFocusable(false);
        mFilterPriceRange.setFocusable(false);

        /* initial value to filter control */
        //Post Type
        switch (postTypeId){
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
        if(categoryId==0){
            mFilterCategory.setText(getString(R.string.all));
        }else {
            try {
                String responseCategory = CommonFunction.doGetRequest(ConsumeAPI.BASE_URL + "api/v1/categories/"+categoryId);
                try{
                    JSONObject obj=new JSONObject(responseCategory);
                    if(currentLanguage.equals("km"))
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
        if(brandId==0){
            mFilterBrand.setText(getString(R.string.all));
        }else{
            try{
                String responseBrand=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/brands/"+brandId);
                try{
                    JSONObject obj=new JSONObject(responseBrand);
                    if(currentLanguage.equals("km"))
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
        if(yearId==0){
            mFilterYear.setText(getString(R.string.all));
        }else{
            try{
                String responseYear=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/years/"+yearId);
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
        if(minPrice>1||maxPrice>1){
            if(minPrice>1&&maxPrice>1)
                mFilterPriceRange.setText("$"+minPrice+" - $"+maxPrice);
            else if(minPrice>1&&maxPrice<1)
                mFilterPriceRange.setText("$"+minPrice+" - $");
            else if(minPrice<1&&maxPrice>1)
                mFilterPriceRange.setText("$0 - $"+maxPrice);
        }else
            mFilterPriceRange.setText(getString(R.string.all));

        /* end initial value to filter control */

        /* start action event listener */
        mFilterPostType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType", CommonFunction.FILTERPOSTTYPE);
                bundle.putInt("postTypeId",postTypeId);
                bundle.putInt("categoryId",categoryId);
                bundle.putInt("brandId",brandId);
                bundle.putInt("yearId",yearId);
                bundle.putDouble("minPrice",minPrice);
                bundle.putDouble("maxPrice",maxPrice);
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
                bundle.putInt("postTypeId",postTypeId);
                bundle.putInt("categoryId",categoryId);
                bundle.putInt("brandId",brandId);
                bundle.putInt("yearId",yearId);
                bundle.putDouble("minPrice",minPrice);
                bundle.putDouble("maxPrice",maxPrice);
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
                bundle.putInt("postTypeId",postTypeId);
                bundle.putInt("categoryId",categoryId);
                bundle.putInt("brandId",brandId);
                bundle.putInt("yearId",yearId);
                bundle.putDouble("minPrice",minPrice);
                bundle.putDouble("maxPrice",maxPrice);
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
                bundle.putInt("postTypeId",postTypeId);
                bundle.putInt("categoryId",categoryId);
                bundle.putInt("brandId",brandId);
                bundle.putInt("yearId",yearId);
                bundle.putDouble("minPrice",minPrice);
                bundle.putDouble("maxPrice",maxPrice);
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
                bundle.putInt("postTypeId",postTypeId);
                bundle.putInt("categoryId",categoryId);
                bundle.putInt("brandId",brandId);
                bundle.putInt("yearId",yearId);
                bundle.putDouble("minPrice",minPrice);
                bundle.putDouble("maxPrice",maxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mSubmitFilterPriceRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minPrice=Double.parseDouble(mFilterMinPrice.getText().toString());
                maxPrice=Double.parseDouble(mFilterMaxPrice.getText().toString());
                Bundle bundle=new Bundle();
                bundle.putInt("postTypeId", postTypeId);
                bundle.putInt("categoryId", categoryId);
                bundle.putInt("brandId", brandId);
                bundle.putInt("yearId", yearId);
                bundle.putDouble("minPrice", minPrice);
                bundle.putDouble("maxPrice", maxPrice);
                Fragment fragment=new HomeFilterResultFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("postTypeId", postTypeId);
                bundle.putInt("categoryId", categoryId);
                bundle.putInt("brandId", brandId);
                bundle.putInt("yearId", yearId);
                bundle.putDouble("minPrice", minPrice);
                bundle.putDouble("maxPrice", maxPrice);
                Fragment fragment=new HomeFilterResultFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        /* end action event lister */

        switch (filterType){
            case CommonFunction.FILTERPOSTTYPE:
                mFilterTitle.setText(getString(R.string.post_type));
                break;
            case CommonFunction.FILTERCATEGORY:
                mFilterTitle.setText(getString(R.string.category));
                break;
            case CommonFunction.FILTERBRAND:
                mFilterTitle.setText(getString(R.string.brand));
                break;
            case CommonFunction.FILTERYEAR:
                mFilterTitle.setText(getString(R.string.year));
                break;
                default:
                    mFilterTitle.setText(getString(R.string.price));
        }
        setupFilterItemsList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //move your code from onViewCreated() here
        /* initial value to filter control */
        //Post Type
        switch (postTypeId){
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
        if(categoryId==0){
            mFilterCategory.setText(getString(R.string.all));
        }else {
            try {
                String responseCategory = CommonFunction.doGetRequest(ConsumeAPI.BASE_URL + "api/v1/categories/"+categoryId);
                try{
                    JSONObject obj=new JSONObject(responseCategory);
                    if(currentLanguage.equals("km"))
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
        if(brandId==0){
            mFilterBrand.setText(getString(R.string.all));
        }else{
            try{
                String responseBrand=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/brands/"+brandId);
                try{
                    JSONObject obj=new JSONObject(responseBrand);
                    if(currentLanguage.equals("km"))
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
        if(yearId==0){
            mFilterYear.setText(getString(R.string.all));
        }else{
            try{
                String responseYear=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/years/"+yearId);
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
        if(minPrice>1||maxPrice>1){
            if(minPrice>1&&maxPrice>1)
                mFilterPriceRange.setText("$"+minPrice+" - $"+maxPrice);
            else if(minPrice>1&&maxPrice<1)
                mFilterPriceRange.setText("$"+minPrice+" - $");
            else if(minPrice<1&&maxPrice>1)
                mFilterPriceRange.setText("$0 - $"+maxPrice);
        }else
            mFilterPriceRange.setText(getString(R.string.all));

        /* end initial value to filter control */

    }

    private void setupFilterItemsList(){
        mFilterItemsList=new ArrayList<>();
        mLayoutManager=new PreCachingLayoutManager(getContext());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setItemViewCacheSize(20);
        mRecylerView.setDrawingCacheEnabled(true);
        mRecylerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecylerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setNestedScrollingEnabled(false);
        mRecylerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable= ContextCompat.getDrawable(getContext(),R.drawable.divider_drawable);
        mRecylerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));

        switch (filterType){
            case CommonFunction.FILTERPOSTTYPE:
                mAdapter=new FilterConditionAdapter(new ArrayList<>(),postTypeId,CommonFunction.FILTERPOSTTYPE,new FilterConditionViewModel(postTypeId,categoryId,brandId,yearId,minPrice,maxPrice));
                prepareFilterConditionPostType();
                break;
            case CommonFunction.FILTERCATEGORY:
                mAdapter=new FilterConditionAdapter(new ArrayList<>(),categoryId,CommonFunction.FILTERCATEGORY,new FilterConditionViewModel(postTypeId,categoryId,brandId,yearId,minPrice,maxPrice));
                prepareFilterConditionCategory();
                break;
            case CommonFunction.FILTERBRAND:
                mAdapter=new FilterConditionAdapter(new ArrayList<>(),brandId,CommonFunction.FILTERBRAND,new FilterConditionViewModel(postTypeId,categoryId,brandId,yearId,minPrice,maxPrice));
                prepareFilterConditionBrand(categoryId);
                break;
            case CommonFunction.FILTERYEAR:
                mAdapter=new FilterConditionAdapter(new ArrayList<>(),yearId,CommonFunction.FILTERYEAR,new FilterConditionViewModel(postTypeId,categoryId,brandId,yearId,minPrice,maxPrice));
                prepareFilterConditionYear();
                break;
            default:
                mAdapter=new FilterConditionAdapter(new ArrayList<>(),0,CommonFunction.FILTERPRICERANGE,new FilterConditionViewModel(postTypeId,categoryId,brandId,yearId,minPrice,maxPrice));
                mFilterPriceRangeLayout.setVisibility(View.VISIBLE);
                mFilterListLayout.setVisibility(View.GONE);
                mFilterMinPrice.setText(String.valueOf(minPrice));
                mFilterMaxPrice.setText(String.valueOf(maxPrice));
                break;
        }

        mRecylerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mProgressbar.setVisibility(View.VISIBLE);
    }

    private void prepareFilterConditionYear(){
        new Handler().postDelayed(()->{
            mProgressbar.setVisibility(View.GONE);
            mFilterUrl= ConsumeAPI.BASE_URL+"api/v1/years/";
            ArrayList<FilterConditionViewModel> mFilters=new ArrayList<>();
            mFilters.add(new FilterConditionViewModel(0,getString(R.string.all)));
            try{
                String response= CommonFunction.doGetRequest(mFilterUrl);
                try{
                    JSONObject obj=new JSONObject(response);
                    int count=obj.getInt("count");
                    if(count==0){

                    }
                    JSONArray results=obj.getJSONArray("results");
                    for(int i=0;i<results.length();i++){
                        JSONObject result=results.getJSONObject(i);
                        int id=result.getInt("id");
                        String year=result.getString("year");
                        mFilters.add(new FilterConditionViewModel(id,year));
                    }
                }catch (JSONException eo){
                    eo.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            mAdapter.addItems(mFilters);
        },500);
        //mProgressbar.setVisibility(View.GONE);
    }

    private void prepareFilterConditionPostType(){
        new Handler().postDelayed(()->{
            mProgressbar.setVisibility(View.GONE);
            ArrayList<FilterConditionViewModel> mFilters=new ArrayList<>();
            mFilters.add(new FilterConditionViewModel(0,getString(R.string.all)));
            mFilters.add(new FilterConditionViewModel(1,getString(R.string.sel)));
            mFilters.add(new FilterConditionViewModel(2,getString(R.string.ren)));
            mAdapter.addItems(mFilters);
        },500);
    }

    private void prepareFilterConditionCategory(){
        new Handler().postDelayed(()->{
            mFilterUrl=ConsumeAPI.BASE_URL+"api/v1/categories/";
            ArrayList<FilterConditionViewModel> mFilters=new ArrayList<>();
            mFilters.add(new FilterConditionViewModel(0,getString(R.string.all)));
            try{
                String response=CommonFunction.doGetRequest(mFilterUrl);
                try{
                    JSONObject obj=new JSONObject(response);
                    int count=obj.getInt("count");
                    if(count>0){
                        mProgressbar.setVisibility(View.GONE);
                        JSONArray results=obj.getJSONArray("results");
                        for(int i=0;i<results.length();i++){
                            JSONObject rs=results.getJSONObject(i);
                            String name="";
                            int id=rs.getInt("id");
                            if(currentLanguage.equals("km"))
                                name=rs.getString("cat_name_kh");
                            else
                                name=rs.getString("cat_name");
                            mFilters.add(new FilterConditionViewModel(id,name));
                        }
                    }
                }catch (JSONException je){
                    je.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            mAdapter.addItems(mFilters);
        },500);
    }



    private void prepareFilterConditionBrand(int categoryId){
        new Handler().postDelayed(()->{
            mFilterUrl=ConsumeAPI.BASE_URL+"api/v1/brands/";
            ArrayList<FilterConditionViewModel> mFilters=new ArrayList<>();
            mFilters.add(new FilterConditionViewModel(0,getString(R.string.all)));
            try{
                String response=CommonFunction.doGetRequest(mFilterUrl);
                try{
                    JSONObject obj=new JSONObject(response);
                    int count=obj.getInt("count");
                    if(count>0){
                        mProgressbar.setVisibility(View.GONE);
                        JSONArray results=obj.getJSONArray("results");
                        if(categoryId==0){
                            for(int i=0;i<results.length();i++){
                                JSONObject rs=results.getJSONObject(i);
                                String name="";
                                int id=rs.getInt("id");
                                if(currentLanguage.equals("km"))
                                    name=rs.getString("brand_name_as_kh");
                                else
                                    name=rs.getString("brand_name");
                                mFilters.add(new FilterConditionViewModel(id,name));
                            }
                        }else{
                            for(int i=0;i<results.length();i++){
                                JSONObject rs=results.getJSONObject(i);
                                String name="";
                                int cid=rs.getInt("category");
                                if(cid==categoryId){
                                    int id=rs.getInt("id");
                                    if(currentLanguage.equals("km"))
                                        name=rs.getString("brand_name_as_kh");
                                    else
                                        name=rs.getString("brand_name");
                                    mFilters.add(new FilterConditionViewModel(id,name));
                                }
                            }
                        }
                    }
                }catch (JSONException je){
                    je.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            mAdapter.addItems(mFilters);
        },500);
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}
