package com.bt_121shoppe.motorbike.Startup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Item_API;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;

import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.motorbike.R;
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
    Bundle bundle;
    String category,model,year,title_filter;
    TextView not_found,tv_filter;
    ProgressBar mProgress;
    ImageView viewlist;
    LinearLayoutManager manager;
    String view = "list";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        locale();
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewlist = findViewById(R.id.viewlist);

        mProgress = findViewById(R.id.progress_search);
        not_found = (TextView) findViewById(R.id.tvSearch_notFound);
        not_found.setVisibility(View.GONE);
        tv_filter = findViewById(R.id.tv_filter);
        sv= (SearchView) findViewById(R.id.mSearch);
        rv = (RecyclerView)findViewById(R.id.myRecycler) ;
        sv.setFocusable(true);
        sv.setIconified(false);
        sv.requestFocusFromTouch();
        items = new ArrayList<Item>();
        items.addAll(Item.Companion.getList());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        final MyAdapter adapter1 = new MyAdapter(getBaseContext(),items);
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
                tv_filter.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.VISIBLE);
                 String  title = sv.getQuery().toString();
                Search_data(title,category,model,year);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    tv_filter.setVisibility(View.GONE);
                return false;
            }
        });

        tv_filter.setOnClickListener(v -> {
            Intent intent1 = new Intent(Search1.this,Filter.class);
            intent1.putExtra("title",sv.getQuery().toString());
            startActivityForResult(intent1,1);

        });
    }  // create

    private  void Search_data(String title, String category, String model, String year){
        String url = ConsumeAPI.BASE_URL+"postsearch/?search="+title+"&category="+category+"&modeling="+model+"&year="+year;
        Log.d("Url:",url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                Log.d("Search:",respon);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                try {
                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    int count = jsonObject.getInt("count");
                    if (count == 0) {
                        mProgress.setVisibility(View.GONE);
                        not_found.setVisibility(View.VISIBLE);
                    }else {
                        not_found.setVisibility(View.GONE);
                        mProgress.setVisibility(View.GONE);
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
                        int model = object.getInt("modeling");
                        int year = object.getInt("year");

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
                            public void onFailure(Call call, IOException e) {
                                String message = e.getMessage().toString();
                                Log.d("failure Response", message);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String mMessage = response.body().string();
                                try {
                                    JSONObject object_count = new JSONObject(mMessage);
                                    String json_count = object_count.getString("count");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            item_apis.add(new Item_API(id,user_id, img_user, image, postsubtitle, cost, condition, post_type, ago.toString(), json_count,color,model,year,discount_type,discount,postsubtitle));
                                            MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "List",Search1.this);
                                            rv.setAdapter(adapterUserPost);
                                            rv.setLayoutManager(new GridLayoutManager(Search1.this, 1));
                                            viewlist.setImageResource(R.drawable.icon_list);
                                            viewlist.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(view.equals("list")){
                                                        viewlist.setImageResource(R.drawable.icon_grid);
                                                        view = "grid";
                                                        MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "Grid",Search1.this);
                                                        rv.setAdapter(adapterUserPost);
                                                        rv.setLayoutManager(new GridLayoutManager(Search1.this, 2));
                                                    }else if (view.equals("grid")){
                                                        viewlist.setImageResource(R.drawable.icon_image);
                                                        view = "image";
                                                        MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "Image",Search1.this);
                                                        rv.setAdapter(adapterUserPost);
                                                        rv.setLayoutManager(new GridLayoutManager(Search1.this, 1));
                                                    }else {
                                                        viewlist.setImageResource(R.drawable.icon_list);
                                                        view = "list";
                                                        MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "List",Search1.this);
                                                        rv.setAdapter(adapterUserPost);
                                                        rv.setLayoutManager(new GridLayoutManager(Search1.this, 1));
                                                    }
                                                }
                                            });
                                        }
                                    });

                                } catch (JsonParseException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }
                }catch (JSONException e){
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
                    sv.setQuery(title_filter, false);

                    mProgress.setVisibility(View.VISIBLE);
                    Log.d("RESULTtttttttt",title_filter+","+category+","+model+","+year);
                    Search_data(title_filter, category, model, year);
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
}
