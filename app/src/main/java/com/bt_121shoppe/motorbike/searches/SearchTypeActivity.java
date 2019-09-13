package com.bt_121shoppe.motorbike.searches;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.classes.PostResponse;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.custom.sliderimage.logic.SliderImage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchTypeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG=SearchTypeActivity.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLinearLayoutBuy,mLinearLayoutSell,mLinerLayoutRent;
    private TextView mBuy,mSell,mRent;
    private ImageView mImageViewEnglish,mImageViewKher,mGridView,mListView,mGallaryVIew;
    private RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavigation;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressbar;
    private Button mFilterCategory,mFilterBrand,mFilterYear,mFilterPriceRange;
    private ProgressDialog mProgress;
    private Context context=this;

    private String myReferences="mypref";
    private String name="",pass="",Encode="",mViewType="List",mPostType="";
    private int pk=0,mCategoryId=0,mBrandId=0,mYearId=0;
    private float mMinPrice=0,mMaxPrice=0;
    private ArrayList<PostViewModel> mPosts;
    private LinearLayoutManager mLayoutManager;
    private SearchTypeAdapter mAdapter;
    private Bundle bundle;
    private String[] categoryListItems,categoryKHListItems,yearListItems,brandListItems,brandKHListitems;
    private int[] categoryIdListItems,yearIdListItems,brandIdListItems,modelIdListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locale();
        mSharedPreferences=getSharedPreferences(myReferences, Context.MODE_PRIVATE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_search_type);

        SharedPreferences prefer=getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String language=prefer.getString("My_Lang","");
        //Log.d(TAG,"Current Language is "+language);
        if(language.isEmpty()){
            language("km");
            recreate();
        }

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mImageViewEnglish=findViewById(R.id.english);
        mImageViewKher=findViewById(R.id.khmer);
        mLinearLayoutBuy=(LinearLayout) findViewById(R.id.rela_buy);
        mLinearLayoutSell=findViewById(R.id.rela_sell);
        mLinerLayoutRent=findViewById(R.id.rela_rent);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        NavigationView mNavigationView=findViewById(R.id.nav_view);
        mBuy=findViewById(R.id.buy);
        mSell=findViewById(R.id.sell);
        mRent=findViewById(R.id.rent);
        mSwipeRefreshLayout=findViewById(R.id.refresh);
        mRecyclerView=findViewById(R.id.list_new_post);
        mProgressbar=findViewById(R.id.progress_bar1);
        mListView=findViewById(R.id.img_list);
        mGridView=findViewById(R.id.grid);
        mGallaryVIew=findViewById(R.id.btn_image);
        mFilterCategory=findViewById(R.id.filterCategory);
        mFilterBrand=findViewById(R.id.filterBrand);
        mFilterYear=findViewById(R.id.filterYear);
        mFilterPriceRange=findViewById(R.id.filterPriceRange);

        SharedPreferences sharedPref=getSharedPreferences("RegisterActivity",Context.MODE_PRIVATE);
        ActionBarDrawerToggle mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        if(sharedPref.contains("token") || sharedPref.contains("id")){
            mNavigationView.setVisibility(View.VISIBLE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            name=sharedPref.getString("name","");
            pass=sharedPref.getString("pass","");
            Encode= CommonFunction.getEncodedString(name,pass);
            if(sharedPref.contains("token"))
                pk=sharedPref.getInt("Pk",0);
            else if (sharedPref.contains("id"))
                pk=sharedPref.getInt("id",0);
        }else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        if(language.equals("km")){
            mImageViewEnglish.setVisibility(View.GONE);
            mImageViewKher.setVisibility(View.VISIBLE);
        }else{
            mImageViewEnglish.setVisibility(View.VISIBLE);
            mImageViewKher.setVisibility(View.GONE);
        }

        mNavigationView.setNavigationItemSelectedListener(this);

        /* start implementation bottom navigation */
        mBottomNavigation=findViewById(R.id.bnaviga);
        mBottomNavigation.getMenu().getItem(0).setChecked(true);
        mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.home:
                    finish();
                    break;
                case R.id.notification:
                    break;
                case R.id.camera:
                    break;
                case R.id.message:
                    break;
                case R.id.account:
                    break;
            }
            return false;
        });
        /*end implementation bottom navigation */

        /* start implementation slider navigation */
        SliderImage mSliderImages=findViewById(R.id.slider);
        List<String> mImages=new ArrayList<>();
        mImages.add("https://www.allkpop.com/upload/2018/12/content/131156/rv-1jpg.jpg");
        mImages.add("https://www.sbs.com.au/popasia/sites/sbs.com.au.popasia/files/styles/full/public/redvelvet_1.jpg?itok=0ZNzn25v&mtime=1522728750");
        mImages.add("https://sa.kapamilya.com/absnews/abscbnnews/media/2019/news/05/27/korean.jpg");
        mImages.add("https://akns-images.eonline.com/eol_images/Entire_Site/2019719/rs_1024x576-190819194533-e-asia-red-velvet-umpah-umpah.jpg?fit=inside|900:auto&output-quality=90");
        mSliderImages.setItems(mImages);
        mSliderImages.addTimerToSlide(3000);
        mSliderImages.getIndicator();
        /* end implementation slider navigation */
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mProgressbar.setVisibility(View.VISIBLE);

        mProgress=new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        //mProgress.show();

        modelIdListItems=new int[0];
        getCategoryListItems();
        getYearListItems();
        getBrandListItems(mCategoryId);

        bundle=getIntent().getExtras();
        if(bundle!=null){
            mPostType=bundle.getString("postType");
            int searchType=bundle.getInt("searchType",0);

            if(searchType==1){ // 1= click on buy/sell/rent
                changePostTypeUI(mPostType);
                setupPostsList(mPostType,mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
            }else if(searchType==2){ //2= click on filter condition

            }
        }

        /* -------------- start event listener -------------------------*/
        mImageViewKher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language("km");
                recreate();
            }
        });

        mImageViewEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language("en");
                recreate();
            }
        });

        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clearItems();
                mPostType="buy";
                changePostTypeUI("buy");
                setupPostsList("buy",mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
            }
        });

        mSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clearItems();
                mPostType="sell";
                changePostTypeUI("sell");
                setupPostsList("sell",mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
            }
        });

        mRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clearItems();
                mPostType="rent";
                changePostTypeUI("rent");
                setupPostsList("rent",mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
            }
        });

        mFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchTypeActivity.this);
                //mBuilder.setTitle(getString(R.string.choose_year));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(categoryKHListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFilterCategory.setText(categoryKHListItems[i]);
                            mCategoryId=categoryIdListItems[i];
                            getBrandListItems(mCategoryId);
                            dialogInterface.dismiss();
                            mProgress.show();
                            mAdapter.clearItems();
                            setupPostsList(mPostType,mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
                        }
                    });
                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(categoryListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFilterCategory.setText(categoryListItems[i]);
                            mCategoryId=categoryIdListItems[i];
                            getBrandListItems(mCategoryId);
                            dialogInterface.dismiss();
                            mProgress.show();
                            mAdapter.clearItems();
                            setupPostsList(mPostType,mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
                        }
                    });
                }

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mFilterBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchTypeActivity.this);
                //mBuilder.setTitle(getString(R.string.choose_year));
                if (language.equals("km")){
                    mBuilder.setSingleChoiceItems(brandKHListitems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFilterBrand.setText(brandKHListitems[i]);
                            mBrandId=brandIdListItems[i];
                            dialogInterface.dismiss();
                            modelIdListItems=getModelIdList(mBrandId);
                            mProgress.show();
                            mAdapter.clearItems();
                            setupPostsList(mPostType,mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
                        }
                    });
                }else if (language.equals("en")){
                    mBuilder.setSingleChoiceItems(brandListItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFilterBrand.setText(brandListItems[i]);
                            mBrandId=brandIdListItems[i];
                            dialogInterface.dismiss();
                            modelIdListItems=getModelIdList(mBrandId);
                            mProgress.show();
                            mAdapter.clearItems();
                            setupPostsList(mPostType,mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
                        }
                    });
                }

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mFilterYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchTypeActivity.this);
                mBuilder.setSingleChoiceItems(yearListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFilterYear.setText(yearListItems[i]);
                        mYearId=yearIdListItems[i];
                        mAdapter.clearItems();
                        setupPostsList(mPostType,mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mFilterPriceRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog dialogBuilder = new AlertDialog.Builder(context).create();
                LayoutInflater inflater = SearchTypeActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                final EditText mMinPriceEditText = (EditText) dialogView.findViewById(R.id.filter_min_price);
                final EditText mMaxPriceEditText= (EditText) dialogView.findViewById(R.id.filter_max_price);
                Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
                Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });

                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // DO SOMETHINGS
                        String strMinPrice=mMinPriceEditText.getText().toString();
                        String strMaxPrice=mMaxPriceEditText.getText().toString();
                        mMinPrice=strMinPrice.isEmpty()?0: Float.parseFloat(strMinPrice);
                        mMaxPrice=strMaxPrice.isEmpty()?0: Float.parseFloat(strMaxPrice);
                        mFilterPriceRange.setText("$ "+mMinPrice+" - $ "+mMaxPrice);
                        setupPostsList(mPostType,mViewType,mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
                        dialogBuilder.dismiss();
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        });

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },1500);
    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void onStart(){
        super.onStart();
        mBottomNavigation.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private void language(String lang){
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration confi=new Configuration();
        confi.locale=locale;
        getBaseContext().getResources().updateConfiguration(confi,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    private void locale(){
        SharedPreferences prefer=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefer.getString("My_Lang","");
        language(language);
    }

    private void setupPostsList(String postType,String viewType,int categoryId,int[] modelsId,int yearId,float minPrice,float maxPrice){
        mProgress.show();
        mPosts=new ArrayList<>();
        mLayoutManager=new GridLayoutManager(this,1);
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
        Drawable dividerDrawable= ContextCompat.getDrawable(this,R.drawable.divider_drawable);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mAdapter=new SearchTypeAdapter(new ArrayList<>(),viewType);
        readPosts(postType,categoryId,modelsId,yearId,minPrice,maxPrice);
        mProgressbar.setVisibility(View.GONE);

    }

    private void readPosts(String postType,int categoryId,int[] modelIdList,int yearId,float minPrice,float maxPrice){
        String category=categoryId==0?"":String.valueOf(categoryId);
        String year=yearId==0?"":String.valueOf(yearId);
        String strMinPrice=minPrice<1?"":String.valueOf(minPrice);
        String strMaxPrice=maxPrice<1?"":String.valueOf(maxPrice);

        Log.d(TAG,"model id "+modelIdList.length);
        if(modelIdList.length==0) {
            mPosts = new ArrayList<>();
            String url = ConsumeAPI.BASE_URL + "relatedpost/?post_type=" + postType + "&category=" + category + "&modeling=&min_price="+strMinPrice+"&max_price="+strMaxPrice+"&year=" + year;
            String response = "";
            try {
                response = CommonFunction.doGetRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "response " + response);
            PostResponse postResponse = new PostResponse();
            Gson gson = new Gson();
            postResponse = gson.fromJson(response, PostResponse.class);
            mPosts = postResponse.getresults();
            mAdapter.addItems(mPosts);
        }else{
            for(int i=0;i<modelIdList.length;i++){
                String modelId=String.valueOf(modelIdList[i]);
                mPosts = new ArrayList<>();
                String url = ConsumeAPI.BASE_URL + "relatedpost/?post_type=" + postType + "&category=" + category + "&modeling="+modelId+"&min_price="+strMinPrice+"&max_price="+strMaxPrice+"&year=" + year;
                String response = "";
                try {
                    response = CommonFunction.doGetRequest(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "response with model " + response);
                PostResponse postResponse = new PostResponse();
                Gson gson = new Gson();
                postResponse = gson.fromJson(response, PostResponse.class);
                mPosts = postResponse.getresults();
                mAdapter.addItems(mPosts);
            }
        }

        mRecyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView,false);
        mAdapter.notifyDataSetChanged();
        mProgress.dismiss();
        mListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="List";
                mListView.setImageResource(R.drawable.icon_list_c);
                mGridView.setImageResource(R.drawable.icon_grid);
                mGallaryVIew.setImageResource(R.drawable.icon_image);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"List"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(SearchTypeActivity.this,1));
            }
        });

        mGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="Grid";
                mListView.setImageResource(R.drawable.icon_list);
                mGridView.setImageResource(R.drawable.icon_grid_c);
                mGallaryVIew.setImageResource(R.drawable.icon_image);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"Grid"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(SearchTypeActivity.this,2));
            }
        });

        mGallaryVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="Image";
                mListView.setImageResource(R.drawable.icon_list);
                mGridView.setImageResource(R.drawable.icon_grid);
                mGallaryVIew.setImageResource(R.drawable.icon_image_c);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"Image"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(SearchTypeActivity.this,1));
            }
        });
    }

    private void changePostTypeUI(String postType){
        switch (postType){
            case "buy":
                mLinearLayoutBuy.setBackgroundColor(getColor(R.color.logo_orange));
                mLinearLayoutSell.setBackgroundColor(getColor(R.color.white));
                mLinerLayoutRent.setBackgroundColor(getColor(R.color.white));
                mBuy.setTextColor(getColor(R.color.white));
                mSell.setTextColor(getColor(R.color.logo_green));
                mRent.setTextColor(getColor(R.color.logo_red));
                break;
            case "sell":
                mLinearLayoutBuy.setBackgroundColor(getColor(R.color.white));
                mLinearLayoutSell.setBackgroundColor(getColor(R.color.logo_green));
                mLinerLayoutRent.setBackgroundColor(getColor(R.color.white));
                mBuy.setTextColor(getColor(R.color.logo_orange));
                mSell.setTextColor(getColor(R.color.white));
                mRent.setTextColor(getColor(R.color.logo_red));
                break;
            case "rent":
                mLinearLayoutBuy.setBackgroundColor(getColor(R.color.white));
                mLinearLayoutSell.setBackgroundColor(getColor(R.color.white));
                mLinerLayoutRent.setBackgroundColor(getColor(R.color.logo_red));
                mBuy.setTextColor(getColor(R.color.logo_orange));
                mSell.setTextColor(getColor(R.color.logo_green));
                mRent.setTextColor(getColor(R.color.white));
                break;
        }
    }

    private void getCategoryListItems(){
        String response="";
        try{
            response=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/categories/");
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            JSONObject obj=new JSONObject(response);
            JSONArray results=obj.getJSONArray("results");
            categoryIdListItems=new int[results.length()];
            categoryListItems=new String[results.length()];
            categoryKHListItems=new String[results.length()];
            for(int i=0;i<results.length();i++){
                JSONObject oobj=results.getJSONObject(i);
                categoryIdListItems[i]=oobj.getInt("id");
                categoryListItems[i]=oobj.getString("cat_name");
                categoryKHListItems[i]=oobj.getString("cat_name_kh");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getBrandListItems(int categoryId){
        mBrandId=0;
        mFilterBrand.setText(getString(R.string.brand));
        modelIdListItems=new int[0];

        String response="";
        try{
            response=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/brands/");
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            JSONObject obj=new JSONObject(response);
            JSONArray results=obj.getJSONArray("results");
            if(categoryId==0){
                brandIdListItems=new int[results.length()];
                brandListItems=new String[results.length()];
                brandKHListitems=new String[results.length()];
                for(int i=0;i<results.length();i++){
                    JSONObject oobj=results.getJSONObject(i);
                    brandIdListItems[i]=oobj.getInt("id");
                    brandListItems[i]=oobj.getString("brand_name");
                    brandKHListitems[i]=oobj.getString("brand_name_as_kh");
                }
            }
            else{
                int count=0,ccount=0;
                for(int i=0;i<results.length();i++){
                    JSONObject oobj=results.getJSONObject(i);
                    if(categoryId==oobj.getInt("category"))
                        count++;
                }
                brandIdListItems=new int[count];
                brandListItems=new String[count];
                brandKHListitems=new String[count];
                for(int i=0;i<results.length();i++){
                    JSONObject oobj=results.getJSONObject(i);
                    if(categoryId==oobj.getInt("category"))
                    {
                        brandIdListItems[ccount]=oobj.getInt("id");
                        brandListItems[ccount]=oobj.getString("brand_name");
                        brandKHListitems[ccount]=oobj.getString("brand_name_as_kh");
                        ccount++;
                    }
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void getYearListItems(){
        String response="";
        try{
            response=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/years/");
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            JSONObject obj=new JSONObject(response);
            JSONArray results=obj.getJSONArray("results");
            yearIdListItems=new int[results.length()];
            yearListItems=new String[results.length()];
            for(int i=0;i<results.length();i++){
                JSONObject oobj=results.getJSONObject(i);
                yearIdListItems[i]=oobj.getInt("id");
                yearListItems[i]=oobj.getString("year");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
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

}
