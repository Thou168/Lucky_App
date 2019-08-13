package com.bt_121shoppe.lucky_app.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.adapters.AllPostAdapter;
import com.bt_121shoppe.lucky_app.adapters.PostBestDealAdapter;
import com.bt_121shoppe.lucky_app.classes.DividerItemDecoration;
import com.bt_121shoppe.lucky_app.classes.PreCachingLayoutManager;
import com.bt_121shoppe.lucky_app.classes.ViewDialog;
import com.bt_121shoppe.lucky_app.models.PostProduct;
import com.bt_121shoppe.lucky_app.utils.CommomAPIFunction;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;
import com.custom.sliderimage.logic.SliderImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity implements PostBestDealAdapter.Callback {

    private static final String TAG=HomeActivity.class.getSimpleName();

    RecyclerView mAllPostRecyclerView;
    RecyclerView mBestDealRecyclerView;
    PostBestDealAdapter mPostBestDealAdpater;
    AllPostAdapter mAllPostAdapter;
    ProgressBar mProgressbar;
    PreCachingLayoutManager mLayoutManager;
    LinearLayoutManager maLayoutManager;
    ViewDialog viewDialog;
    ImageView mViewGrid,mViewList,mViewImage;
    SliderImage mSliderImage;

    ArrayList<PostProduct> mPostBestDeals;
    ArrayList<PostProduct> mAllPosts;
    String mBestDealUrl=ConsumeAPI.BASE_URL+"bestdeal/?page=1";
    String mAllPostUrl=ConsumeAPI.BASE_URL+"allposts/?page=1";
    boolean isLoading=false,isAPLoading=false;
    int itemCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mBestDealRecyclerView=findViewById(R.id.horizontal);
        mAllPostRecyclerView=findViewById(R.id.list_new_post);
        mProgressbar=findViewById(R.id.progressBar);
        mViewList=findViewById(R.id.img_list);
        mViewGrid=findViewById(R.id.grid);
        mViewImage=findViewById(R.id.btn_image);
        mSliderImage=findViewById(R.id.slider);

        viewDialog=new ViewDialog(this);

        fSetupSlider();
        setUpBestDeal();
        setUpAllPost();
        initialBestDealScrollListener();
        //initialAllPostScrollListener();

        mViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewImage.setImageResource(R.drawable.icon_image_c);
                mViewGrid.setImageResource(R.drawable.icon_grid);
                mViewList.setImageResource(R.drawable.icon_list);
                mAllPostAdapter=new AllPostAdapter(mAllPosts,"Image");
                mAllPostRecyclerView.setAdapter(mAllPostAdapter);
                maLayoutManager=new GridLayoutManager(HomeActivity.this,1);
                mAllPostRecyclerView.setLayoutManager(maLayoutManager);
            }
        });
    }

    private void setUpBestDeal(){
        Display mDisplay=this.getWindowManager().getDefaultDisplay();
        final int width=mDisplay.getWidth();
        final int height=mDisplay.getHeight();
        mPostBestDeals=new ArrayList<>();
        mLayoutManager=new PreCachingLayoutManager(this);
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
        Drawable dividerDrawable= ContextCompat.getDrawable(this,R.drawable.divider_drawable);
        mBestDealRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mPostBestDealAdpater=new PostBestDealAdapter(new ArrayList<>());
        prepareBestDealContent();
        mBestDealRecyclerView.setAdapter(mPostBestDealAdpater);
        mPostBestDealAdpater.notifyDataSetChanged();
        //mPostBestDealAdpater.setHasStableIds(true);
    }

    private void setUpAllPost(){
        Display mDisplay=this.getWindowManager().getDefaultDisplay();
        final int width=mDisplay.getWidth();
        final int height=mDisplay.getHeight();
        mAllPosts=new ArrayList<>();
        maLayoutManager=new GridLayoutManager(this,1);
        maLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //maLayoutManager.setExtraLayoutSpace(height);
        maLayoutManager.setAutoMeasureEnabled(true);
        mAllPostRecyclerView.setHasFixedSize(true);
        mAllPostRecyclerView.setItemViewCacheSize(20);
        mAllPostRecyclerView.setDrawingCacheEnabled(true);
        mAllPostRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAllPostRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mAllPostRecyclerView.setLayoutManager(maLayoutManager);
        mAllPostRecyclerView.setNestedScrollingEnabled(false);
        mAllPostRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable= ContextCompat.getDrawable(this,R.drawable.divider_drawable);
        mAllPostRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mAllPostAdapter=new AllPostAdapter(new ArrayList<>(),"List");
        prepareAllPostContent();
        mAllPostRecyclerView.setAdapter(mAllPostAdapter);
        mAllPostAdapter.notifyDataSetChanged();
        //mAllPostAdapter.setHasStableIds(true);
    }

    private void prepareBestDealContent(){
        //viewDialog.showDialog();
        new Handler().postDelayed(() -> {
            //prepare data and show loading
            //viewDialog.hideDialog();
            if (!mBestDealUrl.equals("null")){
                ArrayList<PostProduct> mmPost=new ArrayList<>();
                try {
                    String response = CommonFunction.doGetRequest(mBestDealUrl);
                    try {
                        JSONObject obj = new JSONObject(response);
                        mBestDealUrl = obj.getString("next");
                        JSONArray results = obj.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            itemCount++;
                            JSONObject object = results.getJSONObject(i);
                            String locationDT="";
                            int id=object.getInt("id");
                            String title = object.getString("title");
                            String type = object.getString("post_type");
                            String cost = object.getString("cost");
                            String address = object.getString("contact_address");
                            //String approvedDate=object.getString("approved_date");
                            String approvedDate=object.getString("created");
                            String discountType=object.getString("discount_type");
                            String discountAmount=object.getString("discount");

                            String frontImage=object.getString("front_image_path");
                            String[] splitPath=frontImage.split("/");
                            String imageUrl=ConsumeAPI.IMAGE_STRING_PATH+splitPath[splitPath.length-1];

                            if(!address.isEmpty()){
                                String[] lateLong=address.split(",");
                                address=CommonFunction.getAddressFromMap(this,Double.parseDouble(lateLong[0]),Double.parseDouble(lateLong[1]));
                                locationDT=address+" - ";
                            }
                            if(!approvedDate.equals("null")){
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    long time = sdf.parse(approvedDate).getTime();
                                    long now = System.currentTimeMillis();

                                    CharSequence ago =
                                            DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                                    Log.d(TAG,approvedDate+" "+ago.toString());
                                    locationDT=locationDT+ago.toString();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            mPostBestDeals.add(new PostProduct(id,title,type,imageUrl,cost,locationDT, CommomAPIFunction.getPostView(id),discountType,discountAmount));
                            mmPost.add(new PostProduct(id,title,type,imageUrl,cost,locationDT,CommomAPIFunction.getPostView(id),discountType,discountAmount));

                        }
                    } catch (JSONException ej) {
                        ej.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mPostBestDealAdpater.addItems(mmPost);
            }
        }, 2000);
    }

    private void prepareAllPostContent(){
        //viewDialog.showDialog();
        new Handler().postDelayed(() -> {
            //prepare data and show loading
            //viewDialog.hideDialog();
            if (!mAllPostUrl.equals("null")){
                ArrayList<PostProduct> mmPost=new ArrayList<>();
                try {
                    String response = CommonFunction.doGetRequest(mAllPostUrl);
                    try {
                        JSONObject obj = new JSONObject(response);
                        mAllPostUrl = obj.getString("next");
                        JSONArray results = obj.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            itemCount++;
                            JSONObject object = results.getJSONObject(i);
                            String locationDT="";
                            int id=object.getInt("id");
                            String title = object.getString("title");
                            String type = object.getString("post_type");
                            String cost = object.getString("cost");
                            String address = object.getString("contact_address");
                            //String approvedDate=object.getString("approved_date");
                            String approvedDate=object.getString("created");
                            String discountType=object.getString("discount_type");
                            String discountAmount=object.getString("discount");

                            String frontImage=object.getString("front_image_path");
                            String[] splitPath=frontImage.split("/");
                            String imageUrl=ConsumeAPI.IMAGE_STRING_PATH+splitPath[splitPath.length-1];

                            if(!address.isEmpty()){
                                String[] lateLong=address.split(",");
                                address=CommonFunction.getAddressFromMap(this,Double.parseDouble(lateLong[0]),Double.parseDouble(lateLong[1]));
                                locationDT=address+" - ";
                            }
                            if(!approvedDate.equals("null")){
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    long time = sdf.parse(approvedDate).getTime();
                                    long now = System.currentTimeMillis();

                                    CharSequence ago =
                                            DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                                    //Log.d(TAG,approvedDate+" "+ago.toString());
                                    locationDT=locationDT+ago.toString();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            mAllPosts.add(new PostProduct(id,title,type,imageUrl,cost,locationDT, CommomAPIFunction.getPostView(id),discountType,discountAmount));
                            mmPost.add(new PostProduct(id,title,type,imageUrl,cost,locationDT,CommomAPIFunction.getPostView(id),discountType,discountAmount));

                        }
                    } catch (JSONException ej) {
                        ej.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mAllPostAdapter.addItems(mmPost);
            }
        }, 2000);
    }

    @Override
    public void onEmptyViewRetryClick() {
        prepareBestDealContent();
    }

    private void initialBestDealScrollListener(){
        mBestDealRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager=(LinearLayoutManager)mBestDealRecyclerView.getLayoutManager();
                if(!isLoading){
                    if(linearLayoutManager!=null && linearLayoutManager.findLastCompletelyVisibleItemPosition()==mPostBestDeals.size()-1){
                        isLoading=true;
                        loadBestDealMore();
                    }
                }
            }
        });
    }

    private void initialAllPostScrollListener(){
        mAllPostRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager=(LinearLayoutManager)mAllPostRecyclerView.getLayoutManager();
                if(!isAPLoading){
                    if(linearLayoutManager!=null && linearLayoutManager.findLastCompletelyVisibleItemPosition()==mAllPosts.size()-1){
                        isAPLoading=true;
                        loadAllPostMore();
                    }
                }
            }
        });
    }

    private void loadBestDealMore() {
        mPostBestDeals.add(null);
        mPostBestDealAdpater.notifyItemInserted(mPostBestDeals.size()-1);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPostBestDeals.remove(mPostBestDeals.size()-1);
                int scrollPosition=mPostBestDeals.size();
                mPostBestDealAdpater.notifyItemRemoved(scrollPosition);
                int currrentSize=scrollPosition;
                prepareBestDealContent();
                mPostBestDealAdpater.notifyDataSetChanged();
                isLoading=false;
            }
        },2000);
    }

    private void loadAllPostMore(){
        mAllPosts.add(null);
        mAllPostAdapter.notifyItemInserted(mAllPosts.size()-1);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAllPosts.remove(mAllPosts.size()-1);
                int scrollPosition=mAllPosts.size();
                mAllPostAdapter.notifyItemRemoved(scrollPosition);
                int currrentSize=scrollPosition;
                prepareAllPostContent();
                mAllPostAdapter.notifyDataSetChanged();
                isAPLoading=false;
            }
        },2000);
    }

    private void fSetupSlider(){
       List<String> images=new ArrayList<>();
       images.add("https://i.redd.it/glin0nwndo501.jpg");
       images.add("https://i.redd.it/obx4zydshg601.jpg");
       images.add("https://i.redd.it/glin0nwndo501.jpg");
       images.add("https://i.redd.it/obx4zydshg601.jpg");
//        images.add("https://sa.kapamilya.com/absnews/abscbnnews/media/2019/entertainment/06/05/20190605-red-velvet.jpg");
//        images.add("https://www.allkpop.com/upload/2018/09/af_org/01133025/red-velvet.jpg");
//        images.add("https://pbs.twimg.com/media/EBV_x5_U0AAGToV.jpg");
//        images.add("https://www.njpac.org/assets/img/718x370Red-Velvet-47f488e175.jpg");
//        images.add("https://upload.wikimedia.org/wikipedia/commons/5/5d/180902_%EC%8A%A4%EC%B9%B4%EC%9D%B4%ED%8E%98%EC%8A%A4%ED%8B%B0%EB%B2%8C_%EB%A0%88%EB%93%9C%EB%B2%A8%EB%B2%B3.jpg");
       mSliderImage.setItems(images);
       mSliderImage.addTimerToSlide(3000);
       mSliderImage.getIndicator();
    }
}
