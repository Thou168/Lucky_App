package com.bt_121shoppe.motorbike.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.Chat;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.models.UserChat;
import com.bt_121shoppe.motorbike.notifications.Token;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAllFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG=ChatAllFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<UserChat> userList;
    private ArrayList<UserChat> userChatList;
    private RelativeLayout mNoChat;
    FirebaseUser fuser;
    DatabaseReference reference;

    public static ChatAllFragment newInstance(){
        return new ChatAllFragment();
    }

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

        mNoChat=view.findViewById(R.id.rl_noResult);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        userChatList=new ArrayList<>();
        if(fuser!=null) {
            reference = FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_CHAT);
            reference.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userChatList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getSender().equals(fuser.getUid())) {
                            userChatList.add(new UserChat(chat.getReceiver(), chat.getPost()));
                        }

//                        if (chat.getReceiver().equals(fuser.getUid())) {
//                            userChatList.add(new UserChat(chat.getSender(), chat.getPost()));
//                        }
                        Log.d("ERROR Receiver", chat.getReceiver() + "," + fuser.getUid());
                    }

                    userList = new ArrayList<>();
                    Map<String, Map<String, List<UserChat>>> map = userChatList.stream().collect(Collectors.groupingBy(UserChat::getUserId, Collectors.groupingBy(UserChat::getPostId)));
                    //Log.d("CHAT","Map "+map.toString());

                    Set keys = map.keySet();
                    for (Object key : keys) {
                        Map<String, List<UserChat>> keys1 = map.get(key);
                        Set keyss = keys1.keySet();
                        for (Object key1 : keyss) {
                            //Log.d("Chat value ","User "+key+" Post: "+key1);
                            userList.add(new UserChat(key.toString(), key1.toString()));
                        }
                        //Log.d("Chat value ","C "+map.get(key));
                    }

                    recyclerViewAdapter.setUserList(userList);

                    if (userList.size() > 0)
                        mNoChat.setVisibility(View.GONE);
                    else
                        mNoChat.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            updateToken(FirebaseInstanceId.getInstance().getToken());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedStateInstance){
        super.onViewCreated(view,savedStateInstance);
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
    }

    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
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
        private String theLastMessage;

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
                    String postUsername="",postTitle="",postPrice="",postFrontImage="",postType="",title="";
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
//                            Log.d(TAG,"User "+auser);
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
                        postTitle=object.getString("post_sub_title");
                        postFrontImage=object.getString("front_image_path");
                        postPrice=object.getString("cost");
                        postType=object.getString("post_type");

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    title = postTitle.split(",").length>1?postTitle.split(",")[1]:postTitle.split(",")[0];

                    holder.tvUsername.setText(postUsername);
                    holder.tvTitle.setText(title);
                    holder.userChat=new UserChat(postId,title,postPrice,postFrontImage,postType,userPK,postUsername,username);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.tvTitle.setText(userList.get(position).getUserId());
            //get post image from firebase
            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(user.getPostId());
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null) {
                        try {
                            JSONObject obj = new JSONObject((Map) dataSnapshot.getValue());
                            String coverUrl = obj.getString("coverUrl");
                            if(coverUrl.equals("default")){
                                Glide.with(context).load(R.drawable.no_image_available).thumbnail(0.1f).into(holder.postImage);
                            }else{
                                Glide.with(context).load(coverUrl).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(holder.postImage);
                            }
                            Log.d(TAG, obj.toString());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            lastMessage(user.getUserId(),user.getPostId(),holder.tvLastChat);
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

        private void lastMessage(String userid,String postid,TextView last_msg){
            theLastMessage="default";
            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_CHAT);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Chat chat=snapshot.getValue(Chat.class);
                        if((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) && chat.getPost().equals(postid)){
                            theLastMessage=chat.getMessage();
                        }
                    }
                    switch (theLastMessage){
                        case "default":
                            last_msg.setText("No message");
                            break;
                            default:
                                last_msg.setText(theLastMessage);
                                break;
                    }
                    theLastMessage="default";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
