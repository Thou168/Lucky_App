package com.bt_121shoppe.motorbike.Startup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.activities.Item_API;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;

import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.searches.SearchConditionFragment;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Search1 extends AppCompatActivity {

    ArrayList<Item_API> item_apis = new ArrayList<>();
    SearchView sv;
    RecyclerView rv;
    ArrayList<Item> items;
    String category,model,year,title_filter,post_type,brand;
    TextView not_found;
    ImageView tv_filter;
    TextView show_view;
    ProgressBar mProgress;
    Fragment currentFragment;
    CardView cardView;
    RelativeLayout relative_view;

    //seekbar
    int min;
    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        locale();
        relative_view = findViewById(R.id.rl_filter);
        TextView back = findViewById(R.id.Closed_activity);
        cardView = findViewById(R.id.card_filter);
        show_view = findViewById(R.id.show_view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        currentFragment=new SearchConditionFragment();
        if(savedInstanceState==null){
            SearchConditionFragment details=new SearchConditionFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,details).commit();
        }

        mProgress = findViewById(R.id.progress_search);
        not_found = (TextView) findViewById(R.id.tvSearch_notFound);
        not_found.setVisibility(View.GONE);
        tv_filter = findViewById(R.id.tv_filter);
        sv= (SearchView) findViewById(R.id.mSearch);
        rv = (RecyclerView)findViewById(R.id.myRecycler) ;
        EditText editText = sv.findViewById(Integer.parseInt(String.valueOf(androidx.appcompat.R.id.search_src_text)));
        editText.setHintTextColor(getResources().getColor(R.color.dark_gray));
        editText.setTextColor(getResources().getColor(R.color.black));
        sv.setFocusable(false);
        sv.requestFocusFromTouch();

        items = new ArrayList<Item>();
        items.addAll(Item.Companion.getList());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
//        final MyAdapter adapter1 = new MyAdapter(getBaseContext(),items);
//        RecyclerView recy_horizontal1 = (RecyclerView) view.findViewById(R.id.list_new_post);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager1);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                item_apis.clear();
                category="";
                model="";
                year="";
//                tv_filter.setVisibility(View.VISIBLE);
                relative_view.setVisibility(View.VISIBLE);
                rv.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);

                String title = sv.getQuery().toString();
                Search_data(title,category,brand,model,year, min, max,post_type);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    relative_view.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        tv_filter.setOnClickListener(v -> {
            Log.e("TAG","Search condition "+post_type+" , "+category+" , "+model+","+year+","+min+","+max+","+brand);
            Intent intent1 = new Intent(Search1.this,Filter.class);
            intent1.putExtra("title",sv.getQuery().toString());
            intent1.putExtra("post_type",post_type);
            intent1.putExtra("category",category);
            intent1.putExtra("model",model);
            intent1.putExtra("brand",brand);
            intent1.putExtra("year",year);
            intent1.putExtra("minPrice",min);
            intent1.putExtra("maxPrice",max);
            startActivityForResult(intent1,1);
        });
    }  // create

    @Override
    public void onStart() {
        super.onStart();
        currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
    }

    private  void Search_data(String title, String category,String brand, String model, String year, int min, int max,String post_type){
       //String url1 = ConsumeAPI.BASE_URL+"postsearch/?search="+title+"&category="+category+"&modeling="+model+"&year="+year+"&min_price"+min+"&max_price"+max;
        post_type=post_type==null?"":post_type;
        year=year.equals("0")?"":year;
        model=model.equals("0")?"":model;
        brand=brand.equals("0")?"":brand;
        category=category.equals("0")?"":category;
        String url1 = ConsumeAPI.BASE_URL+"relatedpost/?search="+title+"&post_type="+post_type+"&category="+category+"&modeling="+model+"&min_price="+min+"&max_price="+max+"&year="+year;
        if(min==0 && max==0)
            url1 = ConsumeAPI.BASE_URL+"relatedpost/?search="+title+"&post_type="+post_type+"&category="+category+"&modeling="+model+"&min_price=&max_price=&year="+year;
        else if(min==0)
            url1 = ConsumeAPI.BASE_URL+"relatedpost/?search="+title+"&post_type="+post_type+"&category="+category+"&modeling="+model+"&min_price=&max_price="+max+"&year="+year;
        else if(max==0)
            url1 = ConsumeAPI.BASE_URL+"relatedpost/?search="+title+"&post_type="+post_type+"&category="+category+"&modeling="+model+"&min_price="+min+"&max_price=&year="+year;
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
                            mProgress.setVisibility(View.GONE);
                            not_found.setVisibility(View.VISIBLE);
                            show_view.setText("0");
                        }else {
                            show_view.setText(String.valueOf(count));
                            cardView.setVisibility(View.GONE);
                            not_found.setVisibility(View.GONE);
                            mProgress.setVisibility(View.GONE);
                            //Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
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
                                                    MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "List",Search1.this);
                                                    rv.setAdapter(adapterUserPost);
                                                    rv.setLayoutManager(new GridLayoutManager(Search1.this, 1));
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
                    category =String.valueOf(data.getExtras().getInt("category",0));
                    brand =String.valueOf(data.getIntExtra("brand",0));
                    model =String.valueOf(data.getIntExtra("model",0));
                    year =String.valueOf(data.getIntExtra("year",0));
                    min = data.getIntExtra("min_price",0);
                    max = data.getIntExtra("max_price",0);
                    post_type=data.getStringExtra("posttype");
                    sv.setQuery(title_filter, false);

                    mProgress.setVisibility(View.VISIBLE);
                    //Log.d("RESULTtttttttt",title_filter+","+category+","+model+","+year+","+min+","+max+","+post_type);
                    Search_data(title_filter, category,brand, model, year,min,max,post_type);
                }
            }
        }

    }
    private void language(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration confi =new  Configuration();
        confi.locale = locale;
        getBaseContext().getResources().updateConfiguration(confi, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    private void locale() {
        SharedPreferences prefer = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefer.getString("My_Lang","");
        Log.d("language",language);
        language(language);
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}
