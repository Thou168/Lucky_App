package com.bt_121shoppe.motorbike.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.adapter.Adapter_Likebyuser;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Api.api.model.LikebyUser;
import com.bt_121shoppe.motorbike.Api.api.model.change_status_unlike;
import com.bt_121shoppe.motorbike.Fragment_details_post.Detail_1;
import com.bt_121shoppe.motorbike.Fragment_details_post.Detail_2;
import com.bt_121shoppe.motorbike.Fragment_details_post.Detail_3;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;
import com.bt_121shoppe.motorbike.Product_dicount.Detail_Discount;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.chats.ChatActivity;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.newversion.CustomViewPager;
import com.custom.sliderimage.logic.SliderImage;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Detail_new_post_java extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    TextView back_view;
    //ImageButton btn_share;
    RelativeLayout btn_share;
    ImageView like,like1,call,message,loan;
    private Integer postId = 0;
    private int pk=0;
    Request request;
    OkHttpClient client;
    private String TAG = Detail_Discount.class.getSimpleName();
    PostViewModel postDetail = new PostViewModel();
    SharedPreferences sharedPref;
    private String Encode = "";
    String name = "";
    String pass = "";
    private int p=0;
    private int pt=0;
    String encodeAuth = "";
    TextView tvPrice;
    TextView tvDiscount;
    TextView tvDiscountPer;
    TextView tvPostType;
    TextView view;
    TextView tvPostTitle;
    SliderImage sliderImage;
    String front_image,right_image,back_image,left_image;
    String postTitle;
    TextView tvUsername;
    private String postPrice;
    private String postFrontImage;
    private String postUsername;
    private String postUserId;
    private String postType;
    private boolean isChecked = false;
    RelativeLayout layout_call_chat_like_loan;
    Double discount = 0.0;
    private int REQUEST_PHONE_CALL =1;
    TabLayout tabLayout;
    private CustomViewPager mViewPager;
    TextView typeView;
    TextView tv_dox;
    boolean loaned = false;
    boolean EditLoan;
    private List<LikebyUser> datas;
    String basic_Encode;
    int loanID,userOwnerId=0;
    String login_verify;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_newpost_java);
        locale();
        call_methot_id();
        layout_call_chat_like_loan = findViewById(R.id.Constrainlayout_call_chat_like_loan);
        bundle = getIntent().getExtras();
        if (bundle!=null){
            login_verify = bundle.getString("Login_verify");
        }
        postId = getIntent().getIntExtra("ID",0);
        discount = getIntent().getDoubleExtra("Discount",0.0);
        sharedPref = getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = sharedPref.getString("name", "");
        pass = sharedPref.getString("pass", "");
        Encode = getEncodedString(name,pass);
        basic_Encode = "Basic "+getEncodedString(name,pass);
        if (sharedPref.contains("token")) {
            pk = sharedPref.getInt("Pk",0);
        } else if (sharedPref.contains("id")) {
            pk = sharedPref.getInt("id", 0);
        }
        p = getIntent().getIntExtra("ID",0);
        pt = getIntent().getIntExtra("postt",0);
        loanID = getIntent().getIntExtra("id",0);
        EditLoan = getIntent().getBooleanExtra("LoanEdit",false);
        initialProductPostDetail(basic_Encode);
        submitCountView(Encode);
        countPostView(Encode);
        back_view.setOnClickListener(v -> onBackPressed());
        tvUsername=findViewById(R.id.post_username);
        //like
        already_like(Encode);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPref.contains("token") || sharedPref.contains("id")) {
                    Like_post(Encode);
                }else{
                    Intent intent =new  Intent(Detail_new_post_java.this, LoginActivity.class);
                    intent.putExtra("Login_verify","detail");
                    intent.putExtra("verify","detail");
                    intent.putExtra("product_id",postId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        like1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Unlike_post();
                AlertDialog.Builder alertDialog = new  AlertDialog.Builder(Detail_new_post_java.this);
                alertDialog.setMessage("You liked this post already.");
                alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
                        });
                                            alertDialog.show();
            }
        });
        //call
        if (ContextCompat.checkSelfPermission(Detail_new_post_java.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Detail_new_post_java.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }
        //chat
        message.setOnClickListener(v -> {
            if (sharedPref.contains("token") || sharedPref.contains("id")) {
                postUserId=tvUsername.getText().toString();
                Intent intent =new  Intent(Detail_new_post_java.this, ChatActivity.class);
                intent.putExtra("postId",postId);
                intent.putExtra("postTitle",postTitle);
                intent.putExtra("postPrice",postPrice);
                intent.putExtra("postImage",postFrontImage);
                intent.putExtra("postUserPk",pk);
                intent.putExtra("postUsername",postUsername);
                intent.putExtra("postUserId",postUserId);
                intent.putExtra("postType",postType);
                intent.putExtra("postOwnerId",userOwnerId);
                startActivity(intent);
            }else{
                Intent intent =new  Intent(Detail_new_post_java.this, LoginActivity.class);
                intent.putExtra("verify","detail");
                intent.putExtra("product_id",postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //loan
        if (EditLoan) {
            loan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(sharedPref.contains("token") || sharedPref.contains("id"))) {
                        Intent intent = new Intent(Detail_new_post_java.this, LoginActivity.class);
                        intent.putExtra("verify", "detail");
                        intent.putExtra("product_id", postId);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Create_Load.class);
                        intent.putExtra("product_id", postId);
                        intent.putExtra("LoanID", loanID);
                        intent.putExtra("LoanEdit", EditLoan);
//                Log.e("343434343",cuteString(tvPrice.text.toString(),1))
                        intent.putExtra("price", cuteString(tvPrice.getText().toString(), 1));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }else {
            loan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(sharedPref.contains("token") || sharedPref.contains("id"))) {
                        Intent intent = new Intent(Detail_new_post_java.this, LoginActivity.class);
                        intent.putExtra("verify", "detail");
                        intent.putExtra("product_id", postId);
                        startActivity(intent);
                    } else {
                        encodeAuth = "Basic "+ getEncodedString(name,pass);
                        getMyLoan();
                    }
                }
            });
        }

        btn_share.setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Share!",Toast.LENGTH_SHORT).show());

        mViewPager = findViewById(R.id.pagerMain);
        mViewPager.setOnTouchListener((v, event) -> true);

        tabLayout = findViewById(R.id.tab_layout_detail);
        tabLayout.setupWithViewPager(mViewPager);
        setUpPager();
        setupTabIcons();
    }

    private void call_methot_id(){
        tvPostTitle = findViewById(R.id.title);
        tvPrice = findViewById(R.id.tv_price);
        tvDiscount = findViewById(R.id.tv_discount);
        tvDiscountPer = findViewById(R.id.tv_discount_per);
        view = findViewById(R.id.view);

        tv_dox = findViewById(R.id.style_dox);
        tv_dox.setVisibility(View.GONE);
        typeView=findViewById(R.id.post_type);

        back_view = findViewById(R.id.tv_back);
        btn_share = findViewById(R.id.btn_share);
        like = findViewById(R.id.btn_like);
        like1 = findViewById(R.id.btn_like1);
        call = findViewById(R.id.btn_call);
        message = findViewById(R.id.btn_sms);
        loan = findViewById(R.id.btn_loan);

        sliderImage = findViewById(R.id.slider);
    }

    private void setUpPager(){
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.ic_info_black_24dp)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.ic_contacts_black_24dp)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.ic_crop_landscape_black_24dp)));
        tabLayout.setOnTabSelectedListener(this);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (login_verify!=null){
            startActivity(new Intent(Detail_new_post_java.this,Home.class));
        }else finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        int tabCount;
        MyPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    return new Detail_1();
                case 1:
                    return new Detail_2();
                case 2:
                    return new Detail_3();
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            return tabCount;
        }

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.path_3922);
        tabLayout.getTabAt(1).setIcon(R.drawable.path_3903);
        tabLayout.getTabAt(2).setIcon(R.drawable.path_3904);
    }

    private void Phone_call(String contactPhone) {
        String[] splitPhone = (contactPhone.split(","));

        call.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(Detail_new_post_java.this);
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.call_sheet_dialog,null);
            dialog.setContentView(view);
            dialog.show();

            TextView phone1 = view.findViewById(R.id.call_phone1);
            TextView phone2 = view.findViewById(R.id.call_phone2);
            TextView phone3 = view.findViewById(R.id.call_phone3);
            View line = view.findViewById(R.id.line);
            View line1 = view.findViewById(R.id.line1);
            View line2 = view.findViewById(R.id.line2);

            if (splitPhone.length==1){
                phone1.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                phone1.setText("  "+splitPhone[0]);
                phone1.setOnClickListener(v1 -> {
                    Intent intent = new  Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+splitPhone[0]));
                    startActivity(intent);
                });

            }else if (splitPhone.length==2){
                phone1.setVisibility(View.VISIBLE);
                phone2.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                phone1.setText("  "+splitPhone[0]);
                phone2.setText("  "+splitPhone[1]);
                phone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new  Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+splitPhone[0]));
                        startActivity(intent);
                    }
                });
                phone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new  Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+splitPhone[1]));
                        startActivity(intent);
                    }
                });
            }else if (splitPhone.length==3){
                phone1.setVisibility(View.VISIBLE);
                phone2.setVisibility(View.VISIBLE);
                phone3.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                phone1.setText("  "+splitPhone[0]);
                phone2.setText("  "+splitPhone[1]);
                phone3.setText("  "+splitPhone[2]);

                //Log.d("Phone 3:",splitPhone[0]+","+ splitPhone[1] +","+ splitPhone[2]);
                phone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new  Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+splitPhone[0]));
                        startActivity(intent);
                    }
                });
                phone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new  Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+splitPhone[1]));
                        startActivity(intent);
                    }
                });
                phone3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new  Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+splitPhone[2]));
                        startActivity(intent);
                    }
                });
            }
        });
          // call
    }

    private void initialProductPostDetail(String encode){
        SharedPreferences prefer = getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String language = prefer.getString("My_Lang","");
        String url;
        Request request;
        String auth = "Basic" + encode;
//        if (pt==1){
//            url = ConsumeAPI.BASE_URL + "postbyuser/" + postId;
//            request = new  Request.Builder()
//                    .url(url)
//                    .header("Accept","application/json")
//                    .header("Content-Type","application/json")
//                    .header("Authorization",auth)
//                    .build();
//        }
//        else {
//            url = ConsumeAPI.BASE_URL + "detailposts/" + postId;
//            request = new  Request.Builder()
//                    .url(url)
//                    .header("Accept","application/json")
//                    .header("Content-Type", "application/json")
//                    .build();
//        }

        url = ConsumeAPI.BASE_URL + "detailposts/" + postId;
        request = new  Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type", "application/json")
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Request",mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                //Log.d(TAG+"3333",mMessage);
                Gson json = new Gson();
                try {
                    runOnUiThread(() -> {
                        postDetail = json.fromJson(mMessage,PostViewModel.class);
                        //Log.e(TAG,"D"+mMessage);

                        int create_by = Integer.parseInt(postDetail.getCreated_by());
                        if (create_by == pk){
                            layout_call_chat_like_loan.setVisibility(View.GONE);
//                            if (postDetail.getPost_type().equals("sell")) {
//                                tabLayout.removeTabAt(2);
//                            }
                        }
                        postFrontImage=postDetail.getFront_image_path();
                        postPrice=discount.toString();
                        postType=postDetail.getPost_type();

//                        if (postDetail.getDiscount_type().equals("percent")) {
//                            Double cost=Double.parseDouble(postDetail.getCost());
//                            Double discountPrice=cost*(Double.parseDouble(postDetail.getDiscount())/100);
//                            int per1 = (int) ( Double.parseDouble(postDetail.getDiscount()));
//                            cost=cost-discountPrice;
//                            tvPrice.setText("$ "+cost);
//                            tvDiscount.setText("$"+postDetail.getDiscount());
//                            tvDiscountPer.setText(per1+"%");
//                            tv_dox.setVisibility(View.VISIBLE);
//                            tvDiscountPer.setVisibility(View.VISIBLE);
//                        }else {
                        double result=0;
                        if (discount!=0.00) {
                            double pricefull = Double.parseDouble(postDetail.getCost());
                            double discountPrice = pricefull * (Double.parseDouble(postDetail.getDiscount()) / 100);
                            int per1 = (int) (Double.parseDouble(postDetail.getDiscount()));
                            result = pricefull - discountPrice;
                            tvPrice.setText("$" + result);
                            tvDiscount.setText("$" + postDetail.getDiscount());
                            tvDiscountPer.setText("- " + per1 + "%");
                            tv_dox.setVisibility(View.GONE);
                            tvDiscountPer.setVisibility(View.VISIBLE);
//                            if (pricefull==0.00){
//                                tvPrice.setText("$ 0.00");
//                            }
                        }
//                        }
                        postPrice=String.valueOf(result);
                        if (discount == 0.00){
                            tvDiscount.setVisibility(View.GONE);
                            tvDiscountPer.setVisibility(View.GONE);
                            tv_dox.setVisibility(View.GONE);
                            tvPrice.setText("$ "+postDetail.getCost());
                            postPrice = postDetail.getCost();
                        }

                        String st = "$ "+postDetail.getCost();
                        st = st.substring(0, st.length()-1);
                        SpannableString ms = new  SpannableString(st);
                        StrikethroughSpan mst = new  StrikethroughSpan();
                        ms.setSpan(mst,0,st.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvDiscount.setText(ms);

                        String contact_phone = postDetail.getContact_phone();
                        Phone_call(contact_phone);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//                        sdf.getTimeZone() = TimeZone.getTimeZone("GMT+7");
                        front_image ="";
                        right_image ="";
                        back_image ="";
                        left_image ="";
                        String extra_image1 ="";
                        String extra_image2 ="";
                        front_image=postDetail.getFront_image_path();
                        right_image=postDetail.getRight_image_path();
                        left_image=postDetail.getLeft_image_path();
                        back_image=postDetail.getBack_image_path();

                        ArrayList arrayList2 = new  ArrayList<String>(6);
                        arrayList2.add(front_image);
                        arrayList2.add(right_image);
                        arrayList2.add(left_image);
                        arrayList2.add(back_image);
                        if (postDetail.getExtra_image1()!=null){
                            arrayList2.add(postDetail.getExtra_image1());
                        }
                        if (postDetail.getExtra_image2()!=null){
                            arrayList2.add(postDetail.getExtra_image2());
                        }
                        sliderImage.setItems(arrayList2);
                        sliderImage.addTimerToSlide(3000);
                        sliderImage.removeTimerSlide();
                        sliderImage.getIndicator();

                        //view type
                        if (postDetail.getPost_type().equals("rent")){
                            typeView.setText(R.string.ren);
                            typeView.setTextSize(16);
                            typeView.setBackgroundResource(R.drawable.roundimage_rent_newpost);
                            tabLayout.removeTabAt(2);
                        }
                        if (postDetail.getPost_type().equals("sell")){
                            typeView.setText(R.string.sell);
                            typeView.setTextSize(16);
                            typeView.setBackgroundResource(R.drawable.roundimage_sell_newpost);
                        }
                        userOwnerId=Integer.parseInt(postDetail.getCreated_by());
                        getPostOwnerInformation(userOwnerId);
                    });
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void Like_post(String encode) {
        String url_like = ConsumeAPI.BASE_URL+"like/?post="+p+"&like_by="+pk;
        //Log.d("asdf",url_like);
        OkHttpClient client =new  OkHttpClient();
        Request request =new  Request.Builder()
                .url(url_like)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                Log.d("Response",respon);

                try {
                    JSONObject jsonObject = new  JSONObject(respon);
                    int count = jsonObject.getInt("count");
                    if (count == 0){
                        String url = ConsumeAPI.BASE_URL + "like/";
                        MediaType MEDIA_TYPE = MediaType.parse("application/json");
                        JSONObject post =new  JSONObject();
                        try {
                            post.put("post", p);
                            post.put("like_by", pk);
                            post.put("record_status", 1);

                            OkHttpClient client = new  OkHttpClient();
                            RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
                            String auth = "Basic"+encode;
                            Request request =new  Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", auth)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("Error", call.toString());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String respon = String.valueOf(response.body());
                                    Log.d("Like", respon);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),R.string.like_post,Toast.LENGTH_SHORT).show();
                                            like1.setVisibility(View.VISIBLE);
                                            like1.setImageResource(R.drawable.android_heart);
                                            //like1.setMaxWidth(30);
                                            like1.setMaxHeight(30);
                                            like.setVisibility(View.GONE);
                                            //like.setVisibility(View.GONE);
//                                            like1.setImageResource(R.drawable.group_28);
//                                            like1.setMaxWidth(30);
//                                            like1.setMaxHeight(30);
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void Unlike_post(String encode){
        final LikebyUser model = new LikebyUser();
        String iditem = String.valueOf((int)model.getPost());
        String itemid_like = String.valueOf((int)model.getLike_by());

        Service api = Client.getClient().create(Service.class);
        retrofit2.Call<Item> call = api.getDetailpost(Integer.parseInt(iditem),basic_Encode);
        call.enqueue(new retrofit2.Callback<Item>() {
            @Override
            public void onResponse(retrofit2.Call<Item> call, retrofit2.Response<Item> response) {
                try {
                    Service api1 = Client.getClient().create(Service.class);
                    change_status_unlike unlike = new change_status_unlike(null,Integer.parseInt(iditem),pk,2);

                    retrofit2.Call<change_status_unlike> call_unlike = api1.getputStatusUnlike(Integer.valueOf(itemid_like),unlike,basic_Encode);
                    call_unlike.enqueue(new retrofit2.Callback<change_status_unlike>() {
                        @Override
                        public void onResponse(retrofit2.Call<change_status_unlike> call, retrofit2.Response<change_status_unlike> response) {
                            String response1 = String.valueOf(response.body());
                            Log.d("Response unlike", response1);
//                    Toast.makeText(Detail_new_post_java.this,"Unlike",Toast.LENGTH_SHORT).show();
//                            like.setVisibility(View.VISIBLE);
//                            like.setImageResource(R.drawable.group_28);
//                            like.setMaxWidth(30);
//                            like.setMaxHeight(30);
//                            like1.setVisibility(View.GONE);
//                            recreate();
                        }
                        @Override
                        public void onFailure(retrofit2.Call<change_status_unlike> call, Throwable t) {

                        }
                    });
                }catch (Exception e){}
            }

            @Override
            public void onFailure(retrofit2.Call<Item> call, Throwable t) {

            }
        });
    }

    private void already_like(String encode){
        String url_like = ConsumeAPI.BASE_URL+"like/?post="+p+"&like_by="+pk;
        OkHttpClient client =new  OkHttpClient();
        Request request =new  Request.Builder()
                .url(url_like)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                Log.d("Response alreadyLike",respon);

                try {
                    JSONObject jsonObject = new  JSONObject(respon);
                    int jsonCount = jsonObject.getInt("count");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonCount>0){
                                like.setVisibility(View.GONE);
                                like1.setVisibility(View.VISIBLE);
                                like1.setImageResource(R.drawable.android_heart);
                                like1.setMaxHeight(30);
                                //like1.setImageResource(R.drawable.android_heart);
//                                like1.setMaxHeight(30);
//                                like1.setMaxWidth(30);
                            }else{
                                like.setVisibility(View.VISIBLE);
                                like1.setVisibility(View.GONE);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void submitCountView(String encode) {
        String url=ConsumeAPI.BASE_URL+"countview/";
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject post =new  JSONObject();
        try{
            post.put("post", p);
            post.put("number",1);

            OkHttpClient client = new  OkHttpClient();
            RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
            String auth = "Basic"+encode;
            Request request =new  Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Error",call.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Log.d("Response",respon);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void countPostView(String encode){
        String URL_ENDPOINT=ConsumeAPI.BASE_URL+"countview/?post="+postId;
        MediaType MEDIA_TYPE=MediaType.parse("application/json");
        OkHttpClient client= new  OkHttpClient();
        Request request=new Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                //Log.d("JJJJJJJJJJJJJ",mMessage)
                Gson gson =new  Gson();
                try {
                    JSONObject jsonObject =new  JSONObject(mMessage);
                    int jsonCount = jsonObject.getInt("count");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setText(""+jsonCount);
                            if (jsonCount>1000){
                                view.setText(""+jsonCount+"K");
                            }
                            //submit count view to firebase
                            FBPostCommonFunction.addCountView(String.valueOf(postId),jsonCount);
                        }
                    });

                } catch (JsonParseException | JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }

    private Bitmap getImageLocal(String filePath, Integer reqWidth, Integer reqHeight){
        if(reqWidth==-1||reqHeight==-1){ // no subsample and no
            return BitmapFactory.decodeFile(filePath);
        }else {
            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize
            options.inSampleSize = BitmapUtil.calculateInSampleSize(options, reqWidth, reqHeight);
            //Log.d(TAG, "options inSampleSize "+options.inSampleSize);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }
    }

    public static class BitmapUtil {
        static int calculateInSampleSize(BitmapFactory.Options options, Integer reqWidth, Integer reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round(height /reqHeight);
                final int widthRatio = Math.round(width / reqWidth);
                if(heightRatio<widthRatio)
                    inSampleSize=heightRatio;
                else
                    inSampleSize=widthRatio;
            }
            return inSampleSize;
        }

//        static  class companion{
//            private static int REQUEST_WIDTH = 100;
//            private static int REQUEST_HEIGHT = 100;
//
//            static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
//                // Raw height and width of image
//                final int height = options.outHeight;
//                final int width = options.outWidth;
//                int inSampleSize = 1;
//                if (height > reqHeight || width > reqWidth) {
//                    final int heightRatio = Math.round(height /reqHeight);
//                    final int widthRatio = Math.round(width / reqWidth);
//                    if(heightRatio<widthRatio)
//                        inSampleSize=heightRatio;
//                    else
//                        inSampleSize=widthRatio;
//                }
//                return inSampleSize;
//            }
//            static int calculateInSampleSize(BitmapFactory.Options options){
//                return calculateInSampleSize(options, REQUEST_WIDTH, REQUEST_HEIGHT);
//            }
//        }
    }

    private void language(String lang) {
        Locale locale = new  Locale(lang);
        Locale.setDefault(locale);
        Configuration confi = new  Configuration();
        confi.locale = locale;
        getBaseContext().getResources().updateConfiguration(confi, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
    private void locale() {
        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefer.getString("My_Lang", "");
        Log.d("language",language);
        language(language);
    }

    String cuteString(String st, int index) {
        String[] separated = st.split(" ");
        return separated[index];
    }

    private void getMyLoan(){
        Log.e("Encode",""+encodeAuth);
        ArrayList<Integer> IDPOST = new ArrayList<>();
        String URL_ENDPOINT = ConsumeAPI.BASE_URL+"loanbyuser/";
        OkHttpClient client= new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encodeAuth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String mMessage = response.body().string();
                Log.d(TAG,"Laon_status "+mMessage);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(mMessage);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int post_id = object.getInt("post");
                                int re_status = object.getInt("record_status");
//                                Log.d("Status Id123", String.valueOf(post_id));
//                                Log.d("Status 123",postId.toString());
                                if (re_status != 12)
                                    IDPOST.add(post_id);
                            }
                            //Log.d("ARRayList", String.valueOf(IDPOST.size()));
                            for (int i=0;i<IDPOST.size();i++){
                                if (IDPOST.get(i).equals(postId)){
                                    loaned = true;
                                }
                            }
                            loan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (loaned){
//                                Toast.makeText(this@Detail_New_Post,"Created",Toast.LENGTH_SHORT).show()
                                        withStyle();
                                    }else{
                                        Intent intent = new  Intent(Detail_new_post_java.this, Create_Load.class);
                                        intent.putExtra("product_id",postId);
                                        //intent.putExtra("price",cuteString(tvPrice.getText().toString(),1));
                                        intent.putExtra("price",postPrice);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
    private void getPostOwnerInformation(int id){
        Service api = com.bt_121shoppe.motorbike.Api.api.Client.getClient().create(Service.class);
        retrofit2.Call<User> call = api.getuser(id);
        call.enqueue(new retrofit2.Callback<com.bt_121shoppe.motorbike.Api.User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful()){
                    postUserId=response.body().getUsername();
                    postUsername=response.body().getFirst_name();
                    //Log.e("TAG","username in view detail initail "+response.body().getUsername());
                    tvUsername.setText(response.body().getUsername());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });

    }

    private void withStyle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(R.string.for_loan_title);
        builder.setIcon(R.drawable.tab_message_selector);
        builder.setMessage(R.string.already_created);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


}
