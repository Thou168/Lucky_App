package com.bt_121shoppe.lucky_app.Startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.Activity.Item_API;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;

import com.bt_121shoppe.lucky_app.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.lucky_app.R;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    String category,model,year;
    TextView not_found,tv_filter;
    ProgressBar mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextView back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mProgress = (ProgressBar) findViewById(R.id.progress_search);
        not_found = (TextView) findViewById(R.id.tvSearch_notFound);
        tv_filter = findViewById(R.id.tv_filter);
        sv= (SearchView) findViewById(R.id.mSearch);
        rv = (RecyclerView)findViewById(R.id.myRecycler) ;
        sv.setFocusable(true);
        sv.setIconified(false);
        sv.requestFocusFromTouch();

        tv_filter.setOnClickListener(v -> {
            Intent intent = new Intent(Search1.this,Filter.class);
            startActivity(intent);
        });

        items = new ArrayList<Item>();
//        items = (ArrayList<Item>)getIntent().getSerializableExtra("items");
        items.addAll(Item.Companion.getList());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        final MyAdapter adapter1 = new MyAdapter(getBaseContext(),items);
//        RecyclerView recy_horizontal1 = (RecyclerView) view.findViewById(R.id.list_new_post);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager1);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        model    = intent.getStringExtra("brand");
        year     = intent.getStringExtra("year");

        Log.d("SSSeach","Category:"+category+" Brand:"+ model + "Year:"+year);
    sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            item_apis.clear();
            tv_filter.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.VISIBLE);
            String title = sv.getQuery().toString();
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

    }  // create

    private  void Search_data(String title, String category, String model, String year){
        String url = "http://103.205.26.103:8000/postsearch/?search="+title+"&category="+category+"&modeling="+model+"&year="+year;
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
                    Log.d("CCCCCCCC", String.valueOf(count));
                    if (count == 0) {
                        mProgress.setVisibility(View.GONE);
                        not_found.setVisibility(View.VISIBLE);
                    }else {
                        not_found.setVisibility(View.GONE);
                        mProgress.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String title = object.getString("title");
                        double cost = object.getDouble("cost");
                        String condition = object.getString("condition");
                        String image = object.getString("front_image_base64");
                        String img_user = object.getString("right_image_base64");
                        String post_type = object.getString("post_type");
                        String discount_type = object.getString("discount_type");
                        Double discount = object.getDouble("discount");

                        /////////////
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        long time = sdf.parse(object.getString("created")).getTime();
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
                                            item_apis.add(new Item_API(id, img_user, image, title, cost, condition, post_type, ago.toString(), json_count,discount_type,discount));
                                            MyAdapter_list_grid_image adapterUserPost = new MyAdapter_list_grid_image(item_apis, "List");
                                            rv.setAdapter(adapterUserPost);
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        // Inflate menu to add items to action bar if it is present.
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem search_item = menu.findItem(R.id.action_search);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setFocusable(true);
//        searchView.setQueryHint("Search for Products, Brands and More");
//        search_item.expandActionView();
//
//        return true;
//    }
//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            //use the query to search your data somehow
//        }
//    }
}
