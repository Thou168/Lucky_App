package com.bt_121shoppe.motorbike.stores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.responses.APIStorePostResponse;
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.Startup.Search1;
import com.bt_121shoppe.motorbike.activities.Item_API;
import com.bt_121shoppe.motorbike.adapters.AllPostAdapter;
import com.bt_121shoppe.motorbike.adapters.UserPostActiveAdapter;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.classes.DividerItemDecoration;
import com.bt_121shoppe.motorbike.classes.PreCachingLayoutManager;
import com.bt_121shoppe.motorbike.loan.model.Province;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.ShopUpdateViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.StorePostViewModel;
import com.bt_121shoppe.motorbike.stores.adapters.StoreActivePostListAdapter;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StoreDetailActivity extends AppCompatActivity {

    private StoreActivePostListAdapter mAllPostAdapter;
    private List<StorePostViewModel> mAllPosts;
    private String mShopName;
    private ProgressBar mAllPostProgressbar;
    private RecyclerView mAllPostsRecyclerView;
    private TextView mAllPostsNoResult,mAllPostNoMoreResult;
    private ImageView mListView,mGridView,mGallaryView;
    private PreCachingLayoutManager mLayoutManager;
    private LinearLayoutManager mAllPostLayoutManager;
    TextView best_match;
    int index = 0;
    ArrayList<Item_API> item_apis = new ArrayList<>();

    ImageView filter;
    String category,model,year,title_filter;

    //seekbar
    int min;
    int max,shopId=0,mProvinceID;
    String view;
    TextView shopname,location,contact,count_view,number_rate;
    CircleImageView cr_image;
    String name_shop,profile_shop,view_shop,ratenum_shop,currentLanguage,shop_phonenumber;
    byte[] decodedBytes;
    TextView shopbar;
    ImageView backbar;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail_new);
        shopname = findViewById(R.id.textview_shopname);
        location=findViewById(R.id.textview_shoplocation);
        cr_image=findViewById(R.id.img_user);
        contact=findViewById(R.id.textview_shopcontactphone);
        count_view=findViewById(R.id.view);
        number_rate=findViewById(R.id.number_of_rate);

        shopbar=findViewById(R.id.shopbar);
        backbar=findViewById(R.id.backbar);

        mAllPostsRecyclerView=findViewById(R.id.list_new_post);
        mAllPostProgressbar=findViewById(R.id.progress_bar1);
        mAllPostsNoResult=findViewById(R.id.text1);
        mAllPostNoMoreResult=findViewById(R.id.textViewAllPostNoMoreResult);
        mListView=findViewById(R.id.img_list);
        mGridView=findViewById(R.id.grid);
        mGallaryView=findViewById(R.id.btn_image);

        best_match=findViewById(R.id.best_match);
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");

        pd=new ProgressDialog(StoreDetailActivity.this);
        pd.setMessage(getString(R.string.progress_waiting));
        pd.setCancelable(false);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            shopId=bundle.getInt("id",0);
            mShopName=bundle.getString("shopinfo");
            mProvinceID=bundle.getInt("shop_location",0);
            Log.e("fjfff",""+mProvinceID);
            profile_shop=bundle.getString("shop_image");
            view_shop=bundle.getString("shop_view");
            ratenum_shop=bundle.getString("shop_rate_num");
            shop_phonenumber = bundle.getString("shop_phonenumber");
        }
        //submit count shop view
        submitcountshopview(shopId);

        shopname.setText(mShopName);
        count_view.setText(view_shop);
        number_rate.setText(ratenum_shop);
        contact.setText(shop_phonenumber);
        Glide.with(StoreDetailActivity.this).load(profile_shop).placeholder(R.mipmap.ic_launcher_round).centerCrop().into(cr_image);

        try{
            Service apiServiece = Client.getClient().create(Service.class);
            retrofit2.Call<Province> call = apiServiece.getProvince(mProvinceID);
            call.enqueue(new retrofit2.Callback<Province>() {
                @Override
                public void onResponse(retrofit2.Call<Province> call, retrofit2.Response<Province> response) {
                    if (!response.isSuccessful()){
                        Log.e("ONRESPONSE Province", String.valueOf(response.code()));
                    }else {
                        if (currentLanguage.equals("en"))
                            location.setText(response.body().getProvince());
                        else location.setText(response.body().getProvince_kh());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Province> call, Throwable t) { Log.d("Error",t.getMessage()); }
            });
        }catch (Exception e){Log.d("Error e",e.getMessage());}

//        initToolbar(mShopName);
        shopbar.setText(mShopName);
        backbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupAllPosts(index);
        call_function();
        filter=findViewById(R.id.filter_detail);
        filter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Filter_shopdetail.class);
                startActivityForResult(intent,1);
            }
        });
        //filter.setVisibility(View.GONE);
        filter.setVisibility(View.VISIBLE);
        best_match.setOnClickListener(v -> {

            View dialogView = StoreDetailActivity.this.getLayoutInflater().inflate(R.layout.best_match_dialog,null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StoreDetailActivity.this);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            bottomSheetDialog.setContentView(dialogView);
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
                }
            });
        });
    }


    private void setupAllPosts(int index){
        mAllPosts=new ArrayList<>();
        mAllPostLayoutManager=new GridLayoutManager(getApplicationContext(),1);
        mAllPostLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mAllPostLayoutManager.setAutoMeasureEnabled(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        mAllPostsRecyclerView.setHasFixedSize(true);
        mAllPostsRecyclerView.setLayoutManager(layoutManager1);
        //mAllPostsRecyclerView.setItemViewCacheSize(20);
        mAllPostsRecyclerView.setDrawingCacheEnabled(true);
        mAllPostsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAllPostsRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mAllPostsRecyclerView.setLayoutManager(mAllPostLayoutManager);
        mAllPostsRecyclerView.setNestedScrollingEnabled(false);
        mAllPostsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.divider_drawable);
        mAllPostsRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mAllPostAdapter=new StoreActivePostListAdapter(new ArrayList<>(),"List");
        //mAllPostAdapter=new AllPostAdapterV2(new ArrayList<>(),"List");
        prepareAllPostsContent(index);
        //mAllPostProgressbar.setVisibility(View.GONE);
    }

    private void prepareAllPostsContent(int index){
        mAllPosts=new ArrayList<>();
        mAllPostAdapter=new StoreActivePostListAdapter(mAllPosts,"List");
        Service api=Client.getClient().create(Service.class);
        retrofit2.Call<APIStorePostResponse> model=api.GetStoreActivePost(shopId);
        model.enqueue(new retrofit2.Callback<APIStorePostResponse>() {
            @Override
            public void onResponse(retrofit2.Call<APIStorePostResponse> call, retrofit2.Response<APIStorePostResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("TAG","55"+response.code()+": "+response.errorBody());
                }
                int count=response.body().getCount();
                if(count==0){
                    mAllPostsNoResult.setVisibility(View.VISIBLE);
                }else{
                    mAllPostsNoResult.setVisibility(View.GONE);
                    List<StorePostViewModel> results=response.body().getResults();
//                    List<StorePostViewModel> postListItems=new ArrayList<>();
//                    for(int i=0;i<results.size();i++){
//                        int postId=results.get(i).getPost();
//                        retrofit2.Call<Item> call1=api.getDetailpost(postId);
//                        call1.enqueue(new retrofit2.Callback<Item>() {
//                            @Override
//                            public void onResponse(retrofit2.Call<Item> call, retrofit2.Response<Item> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(retrofit2.Call<Item> call, Throwable t) {
//
//                            }
//                        });
//                    }

                    mAllPostAdapter.addItems(results);
                    mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                    mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                    ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                    mAllPostAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<APIStorePostResponse> call, Throwable t) {

            }
        });

    }

    private  void Search_data(String title, String category, String model, String year, int min, int max){
//        String url = ConsumeAPI.BASE_URL+"postsearch/?search="+title+"&category="+category+"&modeling="+model+"&year="+year+"&min_price"+min+"&max_price"+max;
        String url1 = ConsumeAPI.BASE_URL+"relatedpost/?post_type="+"&category="+category+"&modeling="+model+"&min_price="+min+"&max_price="+max+"&year="+year;
        Log.d("Url:",url1);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url1)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                Log.d("Search:",respon);
                runOnUiThread(() -> {

                    try {
                        JSONObject jsonObject = new JSONObject(respon);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        int count = jsonObject.getInt("count");
                        if (count == 0) {
                            mAllPostProgressbar.setVisibility(View.GONE);
//                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                        }else {
                            mAllPostsNoResult.setVisibility(View.GONE);
                            mAllPostProgressbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                int user_id = object.getInt("user");
                                String postsubtitle = object.getString("post_sub_title");
                                double cost = object.getDouble("cost");
                                String condition = object.getString("condition");
                                String image = object.getString("front_image_path");
                                String img_user = object.getString("right_image_path");
                                String post_type = object.getString("post_type");
                                String discount_type = object.getString("discount_type");
                                Double discount = object.getDouble("discount");
                                String color = object.getString("color");
                                String color_mul = object.getString("multi_color_code");
                                int model1 = object.getInt("modeling");
                                int year1 = object.getInt("year");
                                int category = object.getInt("category");

                                //
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                                long time = sdf.parse(object.getString("approved_date")).getTime();
                                long now = System.currentTimeMillis();
                                CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

                                String url_endpoint = String.format("%s%s", ConsumeAPI.BASE_URL, "countview/?post=" + id);
                                OkHttpClient client1 = new OkHttpClient();
                                Request request1 = new Request.Builder()
                                        .url(url_endpoint)
                                        .header("Accept", "application/json")
                                        .header("Content-Type", "application/json")
                                        .build();
                                client1.newCall(request1).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call1, IOException e) {
                                        String message = e.getMessage().toString();
                                        Log.d("failure Response", message);
                                    }

                                    @Override
                                    public void onResponse(Call call1, Response response1) throws IOException {
                                        String mMessage = response1.body().string();
                                        try {
                                            JSONObject object_count = new JSONObject(mMessage);
                                            String json_count = object_count.getString("count");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    item_apis.add(new Item_API(id,user_id, img_user, image, postsubtitle, cost, condition, post_type, ago.toString(), json_count,color, model1, year1,discount_type,discount,postsubtitle,category,color_mul));
                                                    MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "List", StoreDetailActivity.this);
                                                    mAllPostsRecyclerView.setAdapter(adapterUserPost);
                                                    mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(StoreDetailActivity.this, 1));
//                                                        mListView.setImageResource(R.drawable.list);
//                                                        mListView.setOnClickListener(new View.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(View v) {
//                                                                if(view.equals("list")){
//                                                                    mListView.setImageResource(R.drawable.grid_brown);
//                                                                    view = "grid";
//                                                                    MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "Grid",StoreDetailActivity.this);
//                                                                    mAllPostsRecyclerView.setAdapter(adapterUserPost);
//                                                                    mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(StoreDetailActivity.this, 2));
//                                                                }else if (view.equals("grid")){
//                                                                    mListView.setImageResource(R.drawable.image_brown);
//                                                                    view = "image";
//                                                                    MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "Image",StoreDetailActivity.this);
//                                                                    mAllPostsRecyclerView.setAdapter(adapterUserPost);
//                                                                    mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(StoreDetailActivity.this, 1));
//                                                                }else {
//                                                                    mListView.setImageResource(R.drawable.list_brown);
//                                                                    view = "list";
//                                                                    MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "List",StoreDetailActivity.this);
//                                                                    mAllPostsRecyclerView.setAdapter(adapterUserPost);
//                                                                    mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(StoreDetailActivity.this, 1));
//                                                                }
//                                                            }
//                                                        });
                                                }
                                            });

                                        } catch (JsonParseException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        }
                    }catch (JSONException | ParseException e){
                        e.printStackTrace();
                    }
                });

            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode == 2) {
                if (data != null) {
                    item_apis.clear();
                    title_filter = data.getExtras().getString("title_search");
                    category = data.getExtras().getString("category");
                    model = data.getStringExtra("brand");
                    year = data.getStringExtra("year");
                    min = data.getIntExtra("min_price",0);
                    max = data.getIntExtra("max_price",0);

                    mAllPostProgressbar.setVisibility(View.VISIBLE);
                    Log.d("RESULTtttttttt",title_filter+","+category+","+model+","+year+","+min+","+max+",");
                    Search_data(title_filter, category, model, year,min,max);
                }
            }
        }
    }
    private void submitcountshopview(int shopId){
        Service api=Client.getClient().create(Service.class);
        retrofit2.Call<ShopViewModel> call=api.getDealerShop(shopId);
        call.enqueue(new retrofit2.Callback<ShopViewModel>() {
            @Override
            public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response) {
                if(response.isSuccessful()){
                    Log.e("TAG","Shop Detail "+response.body().getShop_view());
                    ShopViewModel shop=response.body();
                    int oldView=shop.getShop_view();
                    shop.setShop_view(oldView+1);
                    retrofit2.Call<ShopViewModel> call1=api.updateShopCountView(shopId,new ShopUpdateViewModel(shop.getId(),shop.getUser(),oldView+1,shop.getShop_image()));
                    call1.enqueue(new retrofit2.Callback<ShopViewModel>() {
                        @Override
                        public void onResponse(retrofit2.Call<ShopViewModel> call, retrofit2.Response<ShopViewModel> response1) {
                            Log.e("TAG","Submit count view sucess "+response1.message());
                            count_view.setText(String.valueOf(oldView+1));
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ShopViewModel> call, Throwable t) {
                            Log.e("TAG","fail submit count view "+t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ShopViewModel> call, Throwable t) {

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
                pd.show();
                mAllPosts=new ArrayList<>();
                mAllPostAdapter=new StoreActivePostListAdapter(mAllPosts,"List");
                mListView.setImageResource(R.drawable.list_brown);
                mGridView.setImageResource(R.drawable.grid);
                mGallaryView.setImageResource(R.drawable.image);
                //mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                //mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

                Service api=Client.getClient().create(Service.class);
                retrofit2.Call<APIStorePostResponse> model=api.GetStoreActivePost(shopId);
                model.enqueue(new retrofit2.Callback<APIStorePostResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<APIStorePostResponse> call, retrofit2.Response<APIStorePostResponse> response) {
                        if (!response.isSuccessful()){
                            Log.d("TAG","55"+response.code()+": "+response.errorBody());
                        }
                        int count=response.body().getCount();
                        if(count==0){
                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                        }else{
                            mAllPostsNoResult.setVisibility(View.GONE);
                            List<StorePostViewModel> postListItems=response.body().getResults();
                            mAllPostAdapter.addItems(postListItems);
                            mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                            mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                            ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                            mAllPostAdapter.notifyDataSetChanged();

                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<APIStorePostResponse> call, Throwable t) {

                    }
                });
            }
        });

        //Grid
        mGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                mAllPosts=new ArrayList<>();
                mAllPostAdapter=new StoreActivePostListAdapter(mAllPosts,"Grid");
                mListView.setImageResource(R.drawable.list);
                mGridView.setImageResource(R.drawable.grid_brown);
                mGallaryView.setImageResource(R.drawable.image);
                //mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                //mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

                Service api=Client.getClient().create(Service.class);
                retrofit2.Call<APIStorePostResponse> model=api.GetStoreActivePost(shopId);
                model.enqueue(new retrofit2.Callback<APIStorePostResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<APIStorePostResponse> call, retrofit2.Response<APIStorePostResponse> response) {
                        if (!response.isSuccessful()){
                            Log.d("TAG","55"+response.code()+": "+response.errorBody());
                        }
                        int count=response.body().getCount();
                        if(count==0){
                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                        }else{
                            mAllPostsNoResult.setVisibility(View.GONE);
                            List<StorePostViewModel> postListItems=response.body().getResults();
                            mAllPostAdapter.addItems(postListItems);
                            mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                            mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                            ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                            mAllPostAdapter.notifyDataSetChanged();

                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<APIStorePostResponse> call, Throwable t) {

                    }
                });
            }
        });

        //Image
        mGallaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                mAllPosts=new ArrayList<>();
                mAllPostAdapter=new StoreActivePostListAdapter(mAllPosts,"Image");
                mListView.setImageResource(R.drawable.list);
                mGridView.setImageResource(R.drawable.grid);
                mGallaryView.setImageResource(R.drawable.image_brown);
                //mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                //mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

                Service api=Client.getClient().create(Service.class);
                retrofit2.Call<APIStorePostResponse> model=api.GetStoreActivePost(shopId);
                model.enqueue(new retrofit2.Callback<APIStorePostResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<APIStorePostResponse> call, retrofit2.Response<APIStorePostResponse> response) {
                        if (!response.isSuccessful()){
                            Log.d("TAG","55"+response.code()+": "+response.errorBody());
                        }
                        int count=response.body().getCount();
                        if(count==0){
                            mAllPostsNoResult.setVisibility(View.VISIBLE);
                        }else{
                            mAllPostsNoResult.setVisibility(View.GONE);
                            List<StorePostViewModel> postListItems=response.body().getResults();
                            mAllPostAdapter.addItems(postListItems);
                            mAllPostsRecyclerView.setAdapter(mAllPostAdapter);
                            mAllPostsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                            ViewCompat.setNestedScrollingEnabled(mAllPostsRecyclerView, false);
                            mAllPostAdapter.notifyDataSetChanged();

                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<APIStorePostResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

}