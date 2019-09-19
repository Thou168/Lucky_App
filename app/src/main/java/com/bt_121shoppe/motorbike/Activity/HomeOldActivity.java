package com.bt_121shoppe.motorbike.Activity;

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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapter;
import com.bt_121shoppe.motorbike.adapters.PostBestDealAdapter;
import com.bt_121shoppe.motorbike.classes.DividerItemDecoration;
import com.bt_121shoppe.motorbike.classes.PreCachingLayoutManager;
import com.bt_121shoppe.motorbike.classes.ViewDialog;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.custom.sliderimage.logic.SliderImage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class HomeOldActivity extends AppCompatActivity implements PostBestDealAdapter.Callback {

    private static final String TAG= HomeOldActivity.class.getSimpleName();
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
    TextView mBestDealNoResult;
    ArrayList<PostProduct> mPostBestDeals;
    ArrayList<PostProduct> mAllPosts;
    String mBestDealUrl=ConsumeAPI.BASE_URL+"posts/?page=1";
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
        mProgressbar=(ProgressBar)findViewById(R.id.progress_bar);
        mViewList=findViewById(R.id.img_list);
        mViewGrid=findViewById(R.id.grid);
        mViewImage=findViewById(R.id.btn_image);
        mSliderImage=findViewById(R.id.slider);
        viewDialog=new ViewDialog(this);
        mBestDealNoResult=findViewById(R.id.text);

        mProgressbar.setVisibility(View.VISIBLE);

        fSetupSlider();
        setUpBestDeal();
        //setUpAllPost();

        //initialBestDealScrollListener();

        mViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewImage.setImageResource(R.drawable.icon_image_c);
                mViewGrid.setImageResource(R.drawable.icon_grid);
                mViewList.setImageResource(R.drawable.icon_list);
                //Collections.sort(mAllPosts, (s1,s2)->Integer.compare(s2.getPostId(),s1.getPostId()));
                mAllPostAdapter=new AllPostAdapter(mAllPosts,"Image");
                mAllPostRecyclerView.setAdapter(mAllPostAdapter);
                maLayoutManager=new GridLayoutManager(HomeOldActivity.this,1);
                mAllPostRecyclerView.setLayoutManager(maLayoutManager);
            }
        });

        mViewGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewImage.setImageResource(R.drawable.icon_image);
                mViewGrid.setImageResource(R.drawable.icon_grid_c);
                mViewList.setImageResource(R.drawable.icon_list);
                //Collections.sort(mAllPosts, (s1,s2)->Integer.compare(s2.getPostId(),s1.getPostId()));
                mAllPostAdapter=new AllPostAdapter(mAllPosts,"Grid");
                mAllPostRecyclerView.setAdapter(mAllPostAdapter);
                maLayoutManager=new GridLayoutManager(HomeOldActivity.this,2);
                mAllPostRecyclerView.setLayoutManager(maLayoutManager);
            }
        });

        mViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewImage.setImageResource(R.drawable.icon_image);
                mViewGrid.setImageResource(R.drawable.icon_grid);
                mViewList.setImageResource(R.drawable.icon_list_c);
                //Collections.sort(mAllPosts, (s1,s2)->Integer.compare(s2.getPostId(),s1.getPostId()));
                mAllPostAdapter=new AllPostAdapter(mAllPosts,"List");
                mAllPostRecyclerView.setAdapter(mAllPostAdapter);
                maLayoutManager=new GridLayoutManager(HomeOldActivity.this,1);
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
        readAllPosts();
    }

    private void prepareBestDealContent(){
        new Handler().postDelayed(() -> {
            if (!mBestDealUrl.equals("null")){
                ArrayList<PostProduct> mmPost=new ArrayList<>();
                try {
                    String response = CommonFunction.doGetRequest(mBestDealUrl);
                    try {
                        JSONObject obj = new JSONObject(response);
                        int count=obj.getInt("count");
                        if(count==0){
                            mProgressbar.setVisibility(View.GONE);
                            mBestDealNoResult.setVisibility(View.VISIBLE);
                        }
                        mProgressbar.setVisibility(View.GONE);
                        mBestDealUrl = obj.getString("next");
                        JSONArray results = obj.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            itemCount++;
                            JSONObject object = results.getJSONObject(i);
                            String locationDT="";
                            int id=object.getInt("id");
                            int user_id = object.getInt("user");
                            String title = object.getString("title");
                            String type = object.getString("post_type");
                            String cost = object.getString("cost");
                            String address = object.getString("contact_address");
                            String approvedDate=object.getString("approved_date");
                            //String approvedDate=object.getString("created");
                            String discountType=object.getString("discount_type");
                            String discountAmount=object.getString("discount");
                            int pstatus=object.getInt("status");
                            int pcreatedby=object.getInt("created_by");
                            String frontImage=object.getString("front_image_path");
                            String[] splitPath=frontImage.split("/");
                            String imageUrl=ConsumeAPI.IMAGE_STRING_PATH+splitPath[splitPath.length-1];
                            FBPostCommonFunction.SubmitPost(String.valueOf(id),title,type,frontImage,cost,discountAmount,discountType,address,approvedDate,pstatus,pcreatedby);
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
                            mPostBestDeals.add(new PostProduct(id,user_id,title,type,imageUrl,cost,locationDT, 0,discountType,discountAmount));
                            mmPost.add(new PostProduct(id,user_id,title,type,imageUrl,cost,locationDT,0,discountType,discountAmount));

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
        //mProgressbar.setVisibility(View.GONE);
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

    private void readAllPosts(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query myQuery=reference.child("posts").orderByChild("createdAt");

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<PostProduct> mmPost=new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try {
                        String locationDT="";
                        JSONObject obj = new JSONObject((Map) snapshot.getValue());
                        boolean isProduction=obj.getBoolean("isProduction");
                        int status=obj.getInt("status");
                        if(isProduction==ConsumeAPI.IS_PRODUCTION && status==4) {
                            itemCount++;
                            String id=obj.getString("id");
                            int user_id = obj.getInt("user");
                            String coverUrl = obj.getString("coverUrl");
                            String createdAt = obj.getString("createdAt");
                            String price=obj.getString("price");
                            String discountAmount=obj.getString("discountAmount");
                            String discountType=obj.getString("discountType");
                            String location=obj.getString("location");
                            int viewCount=obj.getInt("viewCount");
                            String title=obj.getString("title");
                            String type=obj.getString("type");

                            if(!location.isEmpty()){
                                String[] lateLong=location.split(",");
                                location=CommonFunction.getAddressFromMap(HomeOldActivity.this,Double.parseDouble(lateLong[0]),Double.parseDouble(lateLong[1]));
                                locationDT=location+" - ";
                            }
                            if(!createdAt.equals("null")){
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    long time = sdf.parse(createdAt).getTime();
                                    long now = System.currentTimeMillis();

                                    CharSequence ago =
                                            DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                                    //Log.d(TAG,approvedDate+" "+ago.toString());
                                    locationDT=locationDT+ago.toString();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            mAllPosts.add(new PostProduct(Integer.parseInt(id),user_id,title,type,coverUrl,price,locationDT,viewCount ,discountType,discountAmount));
                            mmPost.add(new PostProduct(Integer.parseInt(id),user_id,title,type,coverUrl,price,locationDT, viewCount,discountType,discountAmount));
                            Log.d(TAG, "Result " + locationDT);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                Collections.sort(mmPost, (s1,s2)->Integer.compare(s2.getPostId(),s1.getPostId()));
                mAllPostAdapter.addItems(mmPost);
                mAllPostRecyclerView.setAdapter(mAllPostAdapter);
                ViewCompat.setNestedScrollingEnabled(mAllPostRecyclerView, false);
                mAllPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
