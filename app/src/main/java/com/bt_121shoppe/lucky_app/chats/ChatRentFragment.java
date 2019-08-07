package com.bt_121shoppe.lucky_app.chats;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.lucky_app.Activity.Account;
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.interfaces.OnLoadMoreListener;
import com.bt_121shoppe.lucky_app.models.Student;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;
import com.bt_121shoppe.lucky_app.utils.PaginationScrollListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG=ChatRentFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;

    private TextView tvEmptyView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Student> studentList;
    protected Handler handler;
    private String url;
    private int itemCount=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_rent, container, false);
        ButterKnife.bind(this.getActivity());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mRecyclerView=view.findViewById(R.id.myRecyclerView);
        swipeRefresh=view.findViewById(R.id.swipeRefresh);
        tvEmptyView=(TextView) view.findViewById(R.id.empty_view);
        studentList=new ArrayList<Student>();
        handler=new Handler();
        url= ConsumeAPI.BASE_URL+"allposts/?page=1";
        //call load data
        loadData();
        swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new DataAdapter(studentList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLaodMore() {
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size()-1);
                handler.postDelayed((Runnable) () -> {
                    //   remove progress item
                    studentList.remove(studentList.size() - 1);
                    mAdapter.notifyItemRemoved(studentList.size());
                    //add items one by one
                    /*
                    int start = studentList.size();
                    int end = start + 20;

                    for (int i = start + 1; i <= end; i++) {
                        studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                        mAdapter.notifyItemInserted(studentList.size());
                    }
                    */
                    loadData();
                    mAdapter.setLoaded();
                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();

                },2000);
            }
        });

        return view;
    }

    // load initial data
    private void loadData() {
        if(url.isEmpty() || url.equals("null")){

        }
        else{
            try {
                Log.d(TAG, "RUN URL" + url);
                String response = CommonFunction.doGetRequest(url);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    url = jsonObject.getString("next");
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String title = object.getString("title");
                        String postType=object.getString("post_type");
                        String cost=object.getString("cost");
                        String discountType=object.getString("discount_type");
                        String discountAccount=object.getString("discount");
                        String location=object.getString("contact_address");
                        String modifiedDate=object.getString("approved_date");

                        Log.d(TAG, "run: " + i);
                        itemCount++;
                        studentList.add(new Student("post "+itemCount,title));
                        //mAdapter.notifyItemInserted(studentList.size());
                    }
                } catch (JSONException ej) {
                    ej.printStackTrace();
                }
                Log.d(TAG, "AFTER RUN URL" + url);
            } catch (IOException eo) {
                eo.printStackTrace();
            }
        }
        /*
        for (int i = 1; i <= 20; i++) {
            studentList.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));
        }
        */
    }


    @Override
    public void onRefresh() {
        itemCount=0;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    public class DataAdapter extends RecyclerView.Adapter{
        private final int VIEW_ITEM=1;
        private final int VIEW_PROG=0;
        private List<Student> studentList;
        private int visibleThreshold=5;
        private int lastVisibleItem,totalItemCount;
        private boolean loading;
        private OnLoadMoreListener onLoadMoreListener;

        public DataAdapter(List<Student> students,RecyclerView recyclerView){
            studentList=students;
            if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                final LinearLayoutManager linearLayoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount=linearLayoutManager.getItemCount();
                        lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                        if(!loading && totalItemCount<=(lastVisibleItem+visibleThreshold)){
                            if(onLoadMoreListener!=null){
                                onLoadMoreListener.onLaodMore();
                            }
                            loading=true;
                        }
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position){
            return studentList.get(position)!=null ?VIEW_ITEM:VIEW_PROG;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            if(viewType==VIEW_ITEM){
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_rent,parent,false);
                vh=new StudentViewHolder(v);
            }
            else{
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar,parent,false);
                vh=new ProgressViewHolder(v);
            }
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof StudentViewHolder){
                Student singleStudent=(Student) studentList.get(position);
                ((StudentViewHolder)holder).tvTitle.setText(singleStudent.getName());
                ((StudentViewHolder)holder).tvLocationDuration.setText(singleStudent.getEmailId());
                ((StudentViewHolder)holder).tvOriginalPrice.setText(singleStudent.getName());
                Glide.with(mRecyclerView.getContext()).load("https://media.kmall24.com/media/catalog/product/cache/12/image/9df78eab33525d08d6e5fb8d27136e95/0/_/0_1e5v5bvgemjckn9g.jpg").into(((StudentViewHolder)holder).imageView);


                ((StudentViewHolder)holder).student=singleStudent;
            }else{
                ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return studentList.size();
        }

        public void setLoaded(){
            loading=false;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
            this.onLoadMoreListener=onLoadMoreListener;
        }
    }

    //
    public static class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvLocationDuration;
        TextView tvOriginalPrice;
        Student student;
        ImageView imageView;

        public StudentViewHolder(View v) {
            super(v);
            tvTitle=(TextView) v.findViewById(R.id.tvTitle);
            tvLocationDuration=(TextView) v.findViewById(R.id.tvLocationDuration);
            tvOriginalPrice=(TextView) v.findViewById(R.id.tvOriginalPrice);
            imageView=(ImageView) v.findViewById(R.id.imageView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"OnClick: "+student.getName()+" \n "+student.getEmailId(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        ProgressViewHolder(View v) {
            super(v);
            progressBar=(ProgressBar)v.findViewById(R.id.progressBar1);
        }
    }

}
