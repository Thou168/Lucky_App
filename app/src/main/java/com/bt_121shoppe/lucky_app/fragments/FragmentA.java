package com.bt_121shoppe.lucky_app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.Activity.Item_API;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.adapters.RecyclerViewAdapter;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentA extends Fragment {
    RecyclerView recyclerView;
    ListView list;
    ArrayList<Item_API> list_item;
    MyAdapter_list_grid_image ad_list;
    SharedPreferences prefer;
    private String name,pass,Encode;
    public FragmentA(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        Get(container.getContext());
        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        return view;
        /*
        list = (ListView) view.findViewById(R.id.list);
        ArrayList stringList= new ArrayList();

        stringList.add("Item 1A");
        stringList.add("Item 1B");
        stringList.add("Item 1C");
        stringList.add("Item 1D");
        stringList.add("Item 1E");
        stringList.add("Item 1F");
        stringList.add("Item 1G");
        stringList.add("Item 1H");
        stringList.add("Item 1I");
        stringList.add("Item 1J");
        stringList.add("Item 1K");
        stringList.add("Item 1L");
        stringList.add("Item 1M");
        stringList.add("Item 1N");
        stringList.add("Item 1O");
        stringList.add("Item 1P");
        stringList.add("Item 1Q");
        stringList.add("Item 1R");
        stringList.add("Item 1S");
        stringList.add("Item 1T");
        stringList.add("Item 1U");
        stringList.add("Item 1V");
        stringList.add("Item 1W");
        stringList.add("Item 1X");
        stringList.add("Item 1Y");
        stringList.add("Item 1Z");

        CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        list.setAdapter(adapter);

        return view;
        */
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] items = getResources().getStringArray(R.array.tab_A);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
    private void Get(Context cx) {

        String url =ConsumeAPI.BASE_URL+"postbyuser/";
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
                Log.d("Response",respon);
                try{
                    JSONObject jsonObject = new JSONObject(respon);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        String title = object.getString("title");
                        int id = object.getInt("id");
                        String condition = object.getString("condition");
                        String img_user = object.getString("base64_front_image");
                        String image = object.getString("base64_front_image");
                        Double cost = object.getDouble("cost");
                        String postType = object.getString("post_type");

                        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Long time = sdf.parse(object.getString("created")).getTime();
                        Long now = System.currentTimeMillis();
                        CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                        String discount_type = object.getString("discount_type");
                        Double discount = object.getDouble("discount");

                        String URL_ENDPOINT1= ConsumeAPI.BASE_URL+"countview/?post="+id;
                        OkHttpClient client = new OkHttpClient();
                        String auth = "Basic "+ Encode;
                        Request request = new Request.Builder()
                                .url(URL_ENDPOINT1)
                                .header("Accept","application/json")
                                .header("Content-Type","application/json")
                                .header("Authorization",auth)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String respon = response.body().string();
                                try{
                                    JSONObject jsonObject = new JSONObject(respon);
                                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                                    String jsonCount=jsonObject.getString("count");
                                    list_item.add(new Item_API(id,img_user,image,title,cost,condition,postType,ago.toString(),jsonCount,discount_type,discount));
                                    ad_list = new MyAdapter_list_grid_image(list_item,"List",cx);
                                    recyclerView.setAdapter(ad_list);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        /*
                        list_item.add(new Item_API(id,img_user,image,title,cost,condition,postType,ago,));
                        ad_list = new MyAdapter_list_grid_image(list_item,"List");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(ad_list);
                            }
                        });
                        */
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Error",call.toString());
            }
        });
    }

}
