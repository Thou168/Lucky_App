package com.bt_121shoppe.lucky_app.AccountTab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.R;

import java.util.ArrayList;
import java.util.List;

public class LoansList extends Fragment {
    public static final String TAG = "SubLoanFragement";
    RecyclerView rvNewsList;
    LinearLayout main;
    public LoansList(){

    }

    public static LoansList newInstance(){
        LoansList fragment=new LoansList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rvNewsList = getView() != null ? (RecyclerView) getView() : (RecyclerView)inflater.inflate(R.layout.news_recycler_view, container, false);
        return rvNewsList;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        List<String> content = new ArrayList<>();
        for(int i =0; i<5; i++)
        {
            content.add("content "+ i);
        }
        rvNewsList.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rvNewsList.setLayoutManager(lm);
        LoansList.MySimpleAdapter adapter = new LoansList.MySimpleAdapter(content);
        rvNewsList.setAdapter(adapter);
    }
    private class MySimpleAdapter extends RecyclerView.Adapter{

        List<String> content = new ArrayList<>();

        public MySimpleAdapter(List<String> c){
            content.addAll(c);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
            LoansList.MySimpleAdapter.MyViewHolder vh = new LoansList.MySimpleAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LoansList.MySimpleAdapter.MyViewHolder vh = (LoansList.MySimpleAdapter.MyViewHolder) holder;
            vh.tv.setText("Option " + position);
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView)itemView.findViewById(R.id.tvRv);
            }
        }
    }
}
