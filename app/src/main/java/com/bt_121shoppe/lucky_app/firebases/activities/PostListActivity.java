package com.bt_121shoppe.lucky_app.firebases.activities;

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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.Activity.HomeActivity;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.classes.DividerItemDecoration;
import com.bt_121shoppe.lucky_app.classes.PreCachingLayoutManager;
import com.bt_121shoppe.lucky_app.firebases.adapters.SportAdapter;
import com.bt_121shoppe.lucky_app.firebases.models.Sport;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimeZone;

//import butterknife.ButterKnife;

public class PostListActivity extends AppCompatActivity implements SportAdapter.Callback {

    private static final String TAG=PostListActivity.class.getSimpleName();
    RecyclerView mRecyclerView,mRecyclerView1;
    SportAdapter mSportAdapter,mSportAdapter1;
    ProgressBar mProgressbar;
    ImageView mGallaryView,mGridView,mListView;
    NestedScrollView mNestedScroll;
    PreCachingLayoutManager mLayoutManager;
    ArrayList<Sport> mSports;
    ArrayList<Sport> mPosts;
    TextView mNoResult;
    String url= ConsumeAPI.BASE_URL +"allposts/?page=1";
    boolean isLoading=false;
    int itemCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mRecyclerView=findViewById(R.id.mRecyclerView);
        mRecyclerView1=findViewById(R.id.mRecyclerView1);
        mProgressbar=findViewById(R.id.progressBar);
        mGallaryView=findViewById(R.id.mGallaryView);
        mGridView=findViewById(R.id.mGridView);
        mListView=findViewById(R.id.mListView);
        mNoResult=findViewById(R.id.text_no_result);
//        ButterKnife.bind(this);
        mNestedScroll=findViewById(R.id.nestedScrollView);

        Glide.with(this).load(R.drawable.icon_grid).into(mGridView);
        Glide.with(this).load(R.drawable.icon_image).into(mGallaryView);
        Glide.with(this).load(R.drawable.icon_list_c).into(mListView);

        setUp();

        mNoResult.setVisibility(View.GONE);
        //initialNestScrollLinstener();
        //initialScrollListener();

    }

    private void setUp() {
        Display mDisplay = this.getWindowManager().getDefaultDisplay();
        final int width  = mDisplay.getWidth();
        final int height = mDisplay.getHeight();
        mProgressbar.setVisibility(View.GONE);
        mSports=new ArrayList<>();
        //mLayoutManager=new GridLayoutManager(this,1);

        mLayoutManager=new PreCachingLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setExtraLayoutSpace(height);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable= ContextCompat.getDrawable(this,R.drawable.divider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mSportAdapter = new SportAdapter(new ArrayList<>());
        readAllPosts();
        //mSportAdapter.setHasStableIds(true);
//        prepareDemoContent();
//        mRecyclerView.setAdapter(mSportAdapter);
//        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
    }

    private void readAllPosts(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Sport> mmSport=new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //FBPost post=snapshot.getValue(FBPost.class);
                    try {
                        String locationDT="";
                        JSONObject obj = new JSONObject((Map) snapshot.getValue());
                        boolean isProduction=obj.getBoolean("isProduction");
                        if(isProduction==ConsumeAPI.IS_PRODUCTION) {
                            itemCount++;
                            String coverUrl = obj.getString("coverUrl");
                            String createdAt = obj.getString("createdAt");
                            String price=obj.getString("price");
                            String discountAmount=obj.getString("discountAmount");
                            String discountType=obj.getString("discountType");
                            String locaion=obj.getString("location");
                            int viewCount=obj.getInt("viewCount");
                            String title=obj.getString("title");
                            String type=obj.getString("type");

                            if(!locaion.isEmpty()){
                                String[] lateLong=locaion.split(",");
                                locaion=CommonFunction.getAddressFromMap(PostListActivity.this,Double.parseDouble(lateLong[0]),Double.parseDouble(lateLong[1]));
                                locationDT=locaion+" - ";
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
                            mSports.add(new Sport(coverUrl, locationDT, type, title+" "+itemCount));
                            mmSport.add(new Sport(coverUrl, locationDT, type, title+" "+itemCount));
                            Log.d(TAG, "Result " + locationDT);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                mSportAdapter.addItems(mmSport);
                mRecyclerView.setAdapter(mSportAdapter);
                ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
                mProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareDemoContent() {
        //viewDialog.showDialog();
        mProgressbar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            //prepare data and show loading
            //mSports = new ArrayList<>();
            //viewDialog.hideDialog();
            if (!url.equals("null")){
                mNoResult.setVisibility(View.GONE);
                ArrayList<Sport> mmSport=new ArrayList<>();
                try {
                    String response = CommonFunction.doGetRequest(url);
                    try {
                        JSONObject obj = new JSONObject(response);
                        url = obj.getString("next");
                        JSONArray results = obj.getJSONArray("results");
                        //Log.d(TAG,"result "+results);
                        for (int i = 0; i < results.length(); i++) {
                            itemCount++;
                            JSONObject object = results.getJSONObject(i);
                            String title = object.getString("title");
                            String type = object.getString("post_type");
                            String cost = object.getString("cost");
                            String decrption = object.getString("description");
                            String frontImage=object.getString("front_image_path");
                            String[] splitPath=frontImage.split("/");
                            String imageUrl=ConsumeAPI.BASE_URL+splitPath[splitPath.length-1];
                            mSports.add(new Sport(imageUrl, decrption, type, title + " " + itemCount));
                            mmSport.add(new Sport(imageUrl, decrption, type, title + " " + itemCount));
                            Log.d(TAG, "post " + object.getInt("id") + title + " count " + itemCount);
                        }
                    } catch (JSONException ej) {
                        ej.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            /*
            String[] sportsList = getResources().getStringArray(R.array.sports_titles);
            String[] sportsInfo = getResources().getStringArray(R.array.sports_info);
            String[] sportsImage = getResources().getStringArray(R.array.sports_images);
            for (int i = 0; i < sportsList.length; i++) {
                mSports.add(new Sport(sportsImage[i], sportsInfo[i], "News", sportsList[i]));
            }
            */
                mSportAdapter.addItems(mmSport);
            }else{
                mNoResult.setVisibility(View.VISIBLE);
            }
        }, 5000);
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void onEmptyViewRetryClick() {
        prepareDemoContent();
    }

    private void initialScrollListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager=(LinearLayoutManager)mRecyclerView.getLayoutManager();
                if(!isLoading){
                    if(linearLayoutManager!=null && linearLayoutManager.findLastCompletelyVisibleItemPosition()==mSports.size()-1){
                        isLoading=true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void initialNestScrollLinstener(){
        mNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        int visibleItemCount = mLayoutManager.getChildCount();
                        int totalItemCount = mLayoutManager.getItemCount();
                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (!isLoading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                isLoading=true;
                                loadMore();
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        mSports.add(null);
        mSportAdapter.notifyItemInserted(mSports.size()-1);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSports.remove(mSports.size()-1);
                int scrollPosition=mSports.size();
                mSportAdapter.notifyItemRemoved(scrollPosition);
                int currrentSize=scrollPosition;
                prepareDemoContent();
                mSportAdapter.notifyDataSetChanged();
                isLoading=false;
            }
        },8000);
    }
}
