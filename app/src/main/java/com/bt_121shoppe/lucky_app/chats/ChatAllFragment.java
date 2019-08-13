package com.bt_121shoppe.lucky_app.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.lucky_app.Api.ConsumeAPI;
import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.adapters.RecyclerViewAdapter;
import com.bt_121shoppe.lucky_app.models.Chat;
import com.bt_121shoppe.lucky_app.models.User;
import com.bt_121shoppe.lucky_app.models.UserChat;
import com.bt_121shoppe.lucky_app.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAllFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG=ChatAllFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<UserChat> userList;
    private ArrayList<UserChat> userChatList;
    FirebaseUser fuser;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_all, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerviewAdapter recyclerViewAdapter=new RecyclerviewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        userChatList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userChatList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(fuser.getUid())){
                        userChatList.add(new UserChat(chat.getReceiver(),chat.getPost()));
                    }
                    if(chat.getReceiver().equals(fuser.getUid())){
                        userChatList.add(new UserChat(chat.getSender(),chat.getPost()));
                    }

                }

                userList=new ArrayList<>();
                //Map<String,List<UserChat>> chatList=userChatList.stream().collect(Collectors.groupingBy(w->w.getUserId()));
                Map<String, Map<String,List<UserChat>>> map=userChatList.stream().collect(Collectors.groupingBy(UserChat::getUserId,Collectors.groupingBy(UserChat::getPostId)));
                Log.d("CHAT","Map "+map.toString());

                Set keys=map.keySet();
                for(Object key:keys){
                    Map<String,List<UserChat>>keys1= map.get(key);
                    Set keyss=keys1.keySet();
                    for(Object key1:keyss){
                        Log.d("Chat value ","User "+key+" Post: "+key1);
                        userList.add(new UserChat(key.toString(),key1.toString()));
                    }
                    //Log.d("Chat value ","C "+map.get(key));
                }

                recyclerViewAdapter.setUserList(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }


    public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {

        private List<UserChat> userList;
        private Context context;

        public void setUserList(List<UserChat> userList) {
            this.userList = userList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user,parent,false);
            context=parent.getContext();
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerviewAdapter.MyViewHolder holder, int position) {
            UserChat user=userList.get(position);
            reference=FirebaseDatabase.getInstance().getReference("users").child(user.getUserId());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User fuser=dataSnapshot.getValue(User.class);

                    if(fuser.getImageURL().equals("default"))
                        holder.profileImage.setImageResource(R.drawable.user);
                    else
                        Glide.with(context).load(fuser.getImageURL()).into(holder.profileImage);

                    //initial user information
                    int userPK=0,postId=0;
                    String postUsername="",postTitle="",postPrice="",postFrontImage="",postType="";
                    String username=fuser.getUsername();
                    String userRespone="";
                    try{
                        userRespone= CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/userfilter/?last_name=&username="+username);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    try{
                        JSONObject obj=new JSONObject(userRespone);
                        int count=obj.getInt("count");
                        if(count>0){
                            JSONArray results=obj.getJSONArray("results");
                            JSONObject auser=results.getJSONObject(0);
                            //Log.d(TAG,"User "+auser);
                            userPK=auser.getInt("id");
                            if(auser.getString("first_name").isEmpty()||auser.getString("first_name")==null)
                                postUsername=auser.getString("username");
                            else
                                postUsername=auser.getString("first_name");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    //get post information
                    String postResult="";
                    try{
                        postResult=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"detailposts/"+user.getPostId());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    try{
                        JSONObject object=new JSONObject(postResult);
                        postId=object.getInt("id");
                        postTitle=object.getString("title");
                        postFrontImage=object.getString("front_image_base64");
                        postPrice=object.getString("cost");
                        postType=object.getString("post_type");

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    holder.tvUsername.setText(postUsername);
                    holder.tvTitle.setText(postTitle);
                    holder.userChat=new UserChat(postId,postTitle,postPrice,postFrontImage,postType,userPK,postUsername,username);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.tvTitle.setText(userList.get(position).getUserId());
            Glide.with(context).load("http://www.sclance.com/pngs/image-placeholder-png/image_placeholder_png_698951.png").into(holder.postImage);
        }

        @Override
        public int getItemCount() {
            if(userList != null){
                return userList.size();
            }
            return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            CircleImageView profileImage;
            ImageView postImage;
            TextView tvUsername;
            TextView tvDate;
            TextView tvTitle;
            TextView tvLastChat;
            UserChat userChat;

            public MyViewHolder(View itemView) {
                super(itemView);
                profileImage=itemView.findViewById(R.id.profile_image);
                postImage=itemView.findViewById(R.id.imageView);
                tvUsername =itemView.findViewById(R.id.tvUsername);
                tvDate=itemView.findViewById(R.id.tvDate);
                tvTitle=itemView.findViewById(R.id.tvTitle);
                tvLastChat=itemView.findViewById(R.id.tvLastChat);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ChatAllFragment.this.getContext(),ChatActivity.class);
                        intent.putExtra("postId",userChat.getPostIdd());
                        intent.putExtra("postTitle",userChat.getPostTitle());
                        intent.putExtra("postPrice",userChat.getPostPrice());
                        intent.putExtra("postImage",userChat.getPostFrontImage());
                        intent.putExtra("postUserPk",userChat.getUserPk());
                        intent.putExtra("postUsername",userChat.getPostUsername());
                        intent.putExtra("postUserId",userChat.getFirebaseUsename());
                        intent.putExtra("postType",userChat.getPostType());
                        startActivity(intent);
                    }
                });

            }
        }
    }

}
