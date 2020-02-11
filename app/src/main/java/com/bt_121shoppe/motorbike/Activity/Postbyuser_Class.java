package com.bt_121shoppe.motorbike.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Product_dicount.Detail_Discount;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.RentViewModel;
import com.bt_121shoppe.motorbike.models.SaleViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.custom.sliderimage.logic.SliderImage;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;

public class Postbyuser_Class extends AppCompatActivity {
    private Integer postId = 0;
    private int pk=0;
    private String TAG = Detail_Discount.class.getSimpleName();
    PostViewModel postDetail = new PostViewModel();
    SharedPreferences sharedPref;
    private String Encode = "";
    String name = "";
    String pass = "";
    private int p=0;
    private int pt=0;
    String encodeAuth = "";
    String front_image,right_image,back_image,left_image;
    String postTitle;
    private String postPrice;
    private String postFrontImage;
    Double discount = 0.0;

    SliderImage slider;
    ImageView back;
    TextView edit;
    TextView tv_price,tv_title,tv_dox,tv_discount,tv_discount_per;
    TextView tv_postcode,tv_brand,tv_type,tv_model,tv_year,tv_color,tv_condition;
    private TextView whole_ink,wheel_sets,the_whole_screw,pumps,engine_counter,engine_head,assembly,console,accessories;
    TextView tv_seller,tv_phone,tv_email,tv_address;
    private String con;
    View line_rela;
    RelativeLayout rela_eta;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView no_result;
    private TextView description;

    Double latitude= (double) 0;
    Double longtitude= (double) 0;
    TextView tvColor1,tvColor2;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postbyuser_layout);
        //id_call
        slider = findViewById(R.id.slider);
        back = findViewById(R.id.img_back);
        edit = findViewById(R.id.edit);
        tv_price = findViewById(R.id.tv_price);
        tv_title = findViewById(R.id.title);
        tv_dox = findViewById(R.id.style_dox);
        tv_discount = findViewById(R.id.tv_discount);
        tv_discount_per = findViewById(R.id.tv_discount_per);

        tv_postcode = findViewById(R.id.tvPostCode);
        tv_brand = findViewById(R.id.tvBrand);
        tv_type = findViewById(R.id.type);
        tv_model = findViewById(R.id.tv_Model);
        tv_year = findViewById(R.id.tv_Year);
//        tv_color = findViewById(R.id.tv_Color);
        tvColor1=findViewById(R.id.tv_color1);
        tvColor2=findViewById(R.id.tv_color2);
        tv_condition = findViewById(R.id.tv_Condition);

        //motor machin and other
        whole_ink = findViewById(R.id.tvViewWholeInk);
        wheel_sets = findViewById(R.id.tvViewFrontReal);
        the_whole_screw = findViewById(R.id.tvViewWholeScrew);
        pumps = findViewById(R.id.tvPumps);
        engine_counter = findViewById(R.id.tvCounter);
        engine_head = findViewById(R.id.tvEngine);
        assembly = findViewById(R.id.tvAssembly);
        console = findViewById(R.id.tvConsole);
        accessories = findViewById(R.id.tvAccessories);

        //Description
        description = findViewById(R.id.tv_Description);
        //relate_post
        recyclerView = findViewById(R.id.list_rela);
        progressBar = findViewById(R.id.mprogressbar);
        no_result = findViewById(R.id.txt_noresult);

        //contact
        tv_seller = findViewById(R.id.tv_name_sell);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        tv_address = findViewById(R.id.address);

        postId = getIntent().getIntExtra("ID",0);
        discount = getIntent().getDoubleExtra("Discount",0.0);
        sharedPref = getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = sharedPref.getString("name", "");
        pass = sharedPref.getString("pass", "");
        Encode = getEncodedString(name,pass);
        if (sharedPref.contains("token")) {
            pk = sharedPref.getInt("Pk",0);
        } else if (sharedPref.contains("id")) {
            pk = sharedPref.getInt("id", 0);
        }
        if (pk!=0) {
            encodeAuth = "Basic "+ getEncodedString(name,pass);
        }
        p = getIntent().getIntExtra("ID",0);
        pt = getIntent().getIntExtra("postt",0);
        i = getIntent().getIntExtra("Sold_Remove",0);
        if (i==2){
            edit.setVisibility(View.GONE);
        }
        back.setOnClickListener(v -> finish());
        tv_dox.setVisibility(View.GONE);
        initialProductPostDetail(Encode);

        //ban
        line_rela = findViewById(R.id.line_rela);
        rela_eta = findViewById(R.id.relative_eta);
        line_rela.setVisibility(View.GONE);
        rela_eta.setVisibility(View.GONE);

        //edit
        edit.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(), Camera.class);
            intent.putExtra("process_type",2);
            intent.putExtra("id_product",Integer.parseInt(String.valueOf(postDetail.getId())));
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initialProductPostDetail(String encode) {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        String url;
        Request request;
        String auth = "Basic" + encode;
//        if (pt == 1) {
//            url = ConsumeAPI.BASE_URL + "postbyuser/" + postId;
////            request = new Request.Builder()
////                    .url(url)
////                    .header("Accept", "application/json")
////                    .header("Content-Type", "application/json")
////                    .header("Authorization", auth)
////                    .build();
//        } else {
//            url = ConsumeAPI.BASE_URL + "detailposts/" + postId;
////            request = new Request.Builder()
////                    .url(url)
////                    .header("Accept", "application/json")
////                    .header("Content-Type", "application/json")
////                    .build();
//        }
        url=ConsumeAPI.BASE_URL+"detailposts/"+postId;
        request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Request", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.d(TAG + "3333", mMessage);
                Gson json = new Gson();
                try {
                    runOnUiThread(() -> {
                        postDetail = json.fromJson(mMessage,PostViewModel.class);
                        Log.e(TAG,"D"+mMessage);
                        description.setText(postDetail.getDescription());
                        postFrontImage=postDetail.getFront_image_path();
                        postPrice=discount.toString();

                        String title_language = postDetail.getPost_sub_title();
                        String strPostTitle="";
                        if (title_language==null || title_language.isEmpty()){

                        }else {
                            if(language.equals("View"))
                                strPostTitle=title_language.split(",")[0];
                            else
                                strPostTitle=title_language.split(",")[1];
                        }
                        Log.e(TAG,title_language+" " +strPostTitle );
                        tv_title.setText(strPostTitle);
                        tv_title.setTextSize(22F);

//                        if (postDetail.getDiscount_type().equals("percent")) {
//                            double cost=Double.parseDouble(postDetail.getCost());
//                            double discountPrice=cost*(Double.parseDouble(postDetail.getDiscount())/100);
//                            int per1 = (int) ( Double.parseDouble(postDetail.getDiscount()));
//                            cost=cost-discountPrice;
//                            tv_price.setText("$ "+cost);
//                            tv_discount.setText("$"+postDetail.getDiscount());
//                            tv_discount_per.setText(per1+"%");
//                            tv_dox.setVisibility(View.VISIBLE);
//                            tv_discount_per.setVisibility(View.VISIBLE);
//                        }
                        if (discount != 0.00){
                            double cost=Double.parseDouble(postDetail.getCost());
                            double discountPrice=cost*(Double.parseDouble(postDetail.getDiscount())/100);
                            double result = cost - discountPrice;
                            int per1 = (int) ( Double.parseDouble(postDetail.getDiscount()));
                            tv_price.setText("$ "+ result);
                            tv_discount_per.setText("- "+per1+"%");
                            tv_discount.setText("$"+postDetail.getDiscount());
                            tv_discount_per.setVisibility(View.VISIBLE);
                            tv_dox.setVisibility(View.GONE);
                        }
                        if (discount == 0.00){
                            tv_discount.setVisibility(View.GONE);
                            tv_discount_per.setVisibility(View.GONE);
                            tv_dox.setVisibility(View.GONE);
                            tv_price.setText("$ "+postDetail.getCost());
                            postPrice = postDetail.getCost();
                        }
                        String st = "$ "+postDetail.getCost();
                        st = st.substring(0, st.length()-1);
                        SpannableString ms = new  SpannableString(st);
                        StrikethroughSpan mst = new  StrikethroughSpan();
                        ms.setSpan(mst,0,st.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_discount.setText(ms);

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
                        //Log.d("@ moret image","numbers:"+extra_image1+","+extra_image2);
                        slider.setItems(arrayList2);
                        slider.addTimerToSlide(3000);
                        slider.removeTimerSlide();
                        slider.getIndicator();

                        tv_postcode.setText(postDetail.getPost_code()!=null?postDetail.getPost_code().toString():"");
                        //get color
//                            color.setText(postDetail.getColor().toString());
                        String[] splitColor=postDetail.getMulti_color_code().split(",");
                        GradientDrawable shape = new GradientDrawable();
                        shape.setShape(GradientDrawable.OVAL);
                        shape.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[0])));
                        Log.d("View color",String.valueOf(CommonFunction.getColorHexbyColorName(splitColor[0])));
                        tvColor1.setBackground(shape);
                        tvColor2.setVisibility(View.GONE);
                        if(splitColor.length>1){
                            tvColor2.setVisibility(View.VISIBLE);
                            GradientDrawable shape1 = new GradientDrawable();
                            shape1.setShape(GradientDrawable.OVAL);
                            shape1.setColor(Color.parseColor(CommonFunction.getColorHexbyColorName(splitColor[1])));
                            tvColor2.setBackground(shape1);
                        }
                        //end
                        con = postDetail.getCondition().toString();
                        if (con.equals("new")) {
                            tv_condition.setText(R.string.newl);
                        } else if (con.equals("used")) {
                            tv_condition.setText(R.string.used);
                        }
                        //type
                        int inType = postDetail.getCategory();
                        if (inType == 1) {
                            tv_type.setText(R.string.electronic);
                        } else if (inType == 2) {
                            tv_type.setText(R.string.motor);
                            if (con.equals("used")) {
//                                line_rela.setVisibility(View.VISIBLE);
                                rela_eta.setVisibility(View.VISIBLE);
                            }
                        }
                        //brand
//                        tv_brand.setText(String.valueOf(postDetail.getModeling()));
                        //year
                        if (postDetail.getYear()!=0) {
                            if (postDetail.getYear() == 1) {
                                tv_year.setText(R.string.year1);
                            } else if (postDetail.getYear() == 2) {
                                tv_year.setText(R.string.year2);
                            } else if (postDetail.getYear() == 3) {
                                tv_year.setText(R.string.year3);
                            } else if (postDetail.getYear() == 4) {
                                tv_year.setText(R.string.year4);
                            } else if (postDetail.getYear() == 5) {
                                tv_year.setText(R.string.year5);
                            } else if (postDetail.getYear() == 6) {
                                tv_year.setText(R.string.year6);
                            } else if (postDetail.getYear() == 7) {
                                tv_year.setText(R.string.year7);
                            } else if (postDetail.getYear() == 8) {
                                tv_year.setText(R.string.year8);
                            } else if (postDetail.getYear() == 9) {
                                tv_year.setText(R.string.year9);
                            } else if (postDetail.getYear() == 10) {
                                tv_year.setText(R.string.year10);
                            } else if (postDetail.getYear() == 11) {
                                tv_year.setText(R.string.year11);
                            } else if (postDetail.getYear() == 12) {
                                tv_year.setText(R.string.year12);
                            } else if (postDetail.getYear() == 13) {
                                tv_year.setText(R.string.year13);
                            } else if (postDetail.getYear() == 14) {
                                tv_year.setText(R.string.year14);
                            } else if (postDetail.getYear() == 15) {
                                tv_year.setText(R.string.year15);
                            } else if (postDetail.getYear() == 16) {
                                tv_year.setText(R.string.year16);
                            } else if (postDetail.getYear() == 17) {
                                tv_year.setText(R.string.year17);
                            } else if (postDetail.getYear() == 18) {
                                tv_year.setText(R.string.year18);
                            } else if (postDetail.getYear() == 19) {
                                tv_year.setText(R.string.year19);
                            } else if (postDetail.getYear() == 20) {
                                tv_year.setText(R.string.year20);
                            } else if (postDetail.getYear() == 21){
                                tv_year.setText(R.string.year21);
                            }else if (postDetail.getYear() == 22){
                                tv_year.setText(R.string.year22);
                            }else if (postDetail.getYear() == 23){
                                tv_year.setText(R.string.year23);
                            }else if (postDetail.getYear() == 24){
                                tv_year.setText(R.string.year24);
                            }
                        }

                        //model
//                        tv_model.setText(String.valueOf(postDetail.getModeling()));
                        if (postDetail.getModeling()!=0) {
                            if (postDetail.getModeling() == 1) {
                                tv_brand.setText(R.string.honda);
                                tv_model.setText(R.string.dream);
                            } else if (postDetail.getModeling() == 3) {
                                tv_brand.setText(R.string.lg);
                                tv_model.setText(R.string.lgg_86_4k);
                            } else if (postDetail.getModeling() == 4) {
                                tv_brand.setText(R.string.lg);
                                tv_model.setText(R.string.lgg_4k_full);
                            } else if (postDetail.getModeling() == 8) {
                                tv_brand.setText(R.string.susuki);
                                tv_model.setText(R.string.smash_v);
                            } else if (postDetail.getModeling() == 7) {
                                tv_brand.setText(R.string.panasonic);
                                tv_model.setText(R.string.panasonicc);
                            } else if (postDetail.getModeling() == 6) {
                                tv_brand.setText(R.string.honda);
                                tv_model.setText(R.string.scoopy);
                            } else if (postDetail.getModeling() == 2) {
                                tv_brand.setText(R.string.honda);
                                tv_model.setText(R.string.icon);
                            } else if (postDetail.getModeling() == 5) {
                                tv_brand.setText(R.string.honda);
                                tv_model.setText(R.string.zoomer_x);
                            }
                        }

                        //for section
                        //Convert
                        double db_e1 = Double.valueOf(postDetail.getUsed_eta1());
                        int in_e1 = (int)db_e1;
                        whole_ink.setText(in_e1 + " %");

                        double db_e2 = Double.valueOf(postDetail.getUsed_eta2());
                        int in_e2 = (int)db_e2;
                        wheel_sets.setText(in_e2 + " %");

                        double db_e3 = Double.valueOf(postDetail.getUsed_eta3());
                        int in_e3 = (int)db_e3;
                        the_whole_screw.setText(in_e3 + " %");

                        double db_e4 = Double.valueOf(postDetail.getUsed_eta4());
                        int in_e4 = (int)db_e4;
                        pumps.setText(in_e4 + " %");

                        double db_m1 = Double.valueOf(postDetail.getUsed_machine1());
                        int in_m1 = (int)db_m1;
                        engine_counter.setText(in_m1 + " %");

                        double db_m2 = Double.valueOf(postDetail.getUsed_machine2());
                        int in_m2 = (int)db_m2;
                        engine_head.setText(in_m2 + " %");

                        double db_m3 = Double.valueOf(postDetail.getUsed_machine3());
                        int in_m3= (int)db_m3;
                        assembly.setText(in_m3 + " %");

                        double db_m4 = Double.valueOf(postDetail.getUsed_machine4());
                        int in_m4 = (int)db_m4;
                        console.setText(in_m4 + " %");

                        double db_o1 = Double.valueOf(postDetail.getUsed_other1());
                        int in_o1 = (int)db_o1;
                        accessories.setText(in_o1 + " %");
                        //end section

                        //contact
                        tv_seller.setText(postDetail.getMachine_code());
                        String contact_phone = postDetail.getContact_phone();
                        String[] splitPhone = contact_phone.split(",");
                        if (splitPhone.length ==1){
                            tv_phone.setText(splitPhone[0]);
                        }else if (splitPhone.length==2){
                            tv_phone.setText(splitPhone[0]+"\n"+splitPhone[1]);
                        }else if (splitPhone.length==3){
                            tv_phone.setText(splitPhone[0]+"\n"+splitPhone[1]+"\n"+splitPhone[2]);
                        }

                        tv_email.setText(postDetail.getContact_email());
                        //address
                        String addr = postDetail.getContact_address();
                        if (addr.isEmpty()) {

                        } else {
                            String[] splitAddr = (addr.split(","));
                            latitude = Double.valueOf(splitAddr[0]);
                            longtitude = Double.valueOf(splitAddr[1]);
                            try {
                                Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = geo.getFromLocation(latitude, longtitude, 1);
                                String select_add = addresses.get(0).getAddressLine(0);
                                if (addresses.isEmpty()) {
                                    tv_address.setText("no location");
                                } else {
                                    addresses.size();
                                    tv_address.setText(select_add);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        String postType = "";
                        RentViewModel[] rent = postDetail.getRents();
                        SaleViewModel[] sale = postDetail.getSales();
                        if (rent.length > 0) {
                            postType = "rent";
                        }
                        if (sale.length > 0) {
                            postType = "sell";
                        }
//                        initialRelatedPost(encode, postType, postDetail.getCategory(), postDetail.getModeling(), Float.parseFloat(postDetail.getCost()));
                    });
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    private void initialRelatedPost(String encode,String postType,int category,int modeling,float cost){
//        ArrayList itemApi =  new ArrayList<Item_API>();
//        String URL_ENDPOINT=ConsumeAPI.BASE_URL+"relatedpost/?post_type="+postType+"&category="+category+"&modeling="+modeling+"&min_price="+(cost-500)+"&max_price="+(cost+500);
//        Log.d("URL123",URL_ENDPOINT);
//        OkHttpClient client= new  OkHttpClient();
//        Request request= new Request.Builder()
//                .url(URL_ENDPOINT)
//                .header("Accept","application/json")
//                .header("Content-Type","application/json")
//                .header("Authorization",encode)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                String mMessage = e.getMessage().toString();
//                Log.w("failure Response", mMessage);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String mMessage = response.body().string();
//                runOnUiThread(() -> {
//                        try {
//                            JSONObject object = new JSONObject(mMessage);
//                            JSONArray jsonArray = object.getJSONArray("results");
//                            int jsonCount = object.getInt("count");
//                            Log.w("Relate", mMessage);
//                            progressBar.setVisibility(View.GONE);
//                            if (jsonCount == 0) {
//                                no_result.setVisibility(View.VISIBLE);
//                            } else {
//                                no_result.setVisibility(View.GONE);
//                            }
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject obj = jsonArray.getJSONObject(i);
//                                String title = obj.getString("title");
//                                int id = obj.getInt("id");
//                                int user_id = obj.getInt("created_by");
//                                String condition = obj.getString("condition");
//                                double cost = obj.getDouble("cost");
//                                String image = obj.getString("front_image_path");
//                                String img_user = obj.getString("right_image_path");
//                                String postType = obj.getString("post_type");
//                                String phoneNumber = obj.getString("contact_phone");
//                                String discount_type = obj.getString("discount_type");
//                                double discount = obj.getDouble("discount");
//                                String postsubtitle = obj.getString("post_sub_title");
//                                String color = obj.getString("color");
//                                int model = obj.getInt("modeling");
//                                int year = obj.getInt("year");
//
//                                String ago = "";
//                                if (postId != id) {
//                                    itemApi.add(new Item_API(id, user_id, img_user, image, title, cost, condition, postType, ago, String.valueOf(jsonCount), color, model, year, discount_type, discount, postsubtitle));
////                                itemApi.add(Modeling(id,userId,img_user,image,title,cost,condition,postType,location_duration,jsonCount.toString(),discount_type,discount))
//                                    no_result.setVisibility(View.GONE);
//                                    recyclerView.setAdapter(new MyAdapter_list_grid_image(itemApi, "Grid", getApplicationContext()));
//                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                                }
//                            }
//                        } catch (JsonParseException | JSONException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//        });
//    }
}
