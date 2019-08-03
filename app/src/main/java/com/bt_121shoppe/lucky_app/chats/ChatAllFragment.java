package com.bt_121shoppe.lucky_app.chats;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.adapters.RecyclerViewAdapter;
import com.bt_121shoppe.lucky_app.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatAllFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_all, container, false);
        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerviewAdapter recyclerViewAdapter=new RecyclerviewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        userList=new ArrayList<>();
        User user=new User("1","Rith","default","online","rith","default","default");
        userList.add(user);
        user=new User("2","J Sros so cute","default","online","rith","default","default");
        userList.add(user);
        recyclerViewAdapter.setUserList(userList);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                User user=new User("3","mak ming admin","default","online","rith","default","default");
                userList.add(user);
                recyclerViewAdapter.setUserList(userList);
                swipeRefreshLayout.setRefreshing(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });

        return view;
    }

    public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {

        private List<User> userList;

        public void setUserList(List<User> userList) {
            this.userList = userList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerviewAdapter.MyViewHolder holder, int position) {
            holder.tvTitle.setText(userList.get(position).getUsername());
        }

        @Override
        public int getItemCount() {
            if(userList != null){
                return userList.size();
            }
            return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.username);
            }
        }
    }

}
