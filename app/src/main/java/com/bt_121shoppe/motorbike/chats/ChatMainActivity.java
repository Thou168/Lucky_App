package com.bt_121shoppe.motorbike.chats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.activities.Account;
import com.bt_121shoppe.motorbike.activities.Camera;
import com.bt_121shoppe.motorbike.activities.CheckGroup;
import com.bt_121shoppe.motorbike.activities.DealerStoreActivity;
import com.bt_121shoppe.motorbike.activities.Home;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.Chat;
import com.bt_121shoppe.motorbike.models.UserChat;
import com.bt_121shoppe.motorbike.notifications.Token;
import com.bt_121shoppe.motorbike.stores.StoreListActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMainActivity extends AppCompatActivity {

    private final static String TAG=ChatMainActivity.class.getSimpleName();
    SharedPreferences preferences;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseAnalytics firebaseAnalytics;
    private String uusername="";
    private String password="";
    private String encodeAuth="";
    private int pk=0;
    private RecyclerView recyclerView;
    private ArrayList<UserChat> userList;
    private ArrayList<UserChat> userChatList;
    private RelativeLayout mNoChat;

    SharedPreferences prefer;
    private String name,pass,Encode;

    private CircleImageView profile_image;
    private TextView username;
    private  BottomNavigationView bnavigation,bnavigation1;
    Bundle bundle;
    String login_verify;
    String theLastMessage="default";
    String register_intent="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_chat_main);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            login_verify = bundle.getString("Login_verify");
            register_intent = bundle.getString("Register_verify");
        }

        prefer = getSharedPreferences("Register", MODE_PRIVATE);
        name = prefer.getString("name", "");
        pass = prefer.getString("pass", "");
        Encode = CommonFunction.getEncodedString(name, pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        //check active and deactive account by samang 2/09/19
//        Active_user activeUser = new Active_user();
//        String active;
//        active = activeUser.isUserActive(pk, this);
//        if (active.equals("false")) {
//            activeUser.clear_session(this);
//        }
        // end

        bnavigation1 = findViewById(R.id.bottom_nav);
        bnavigation = findViewById(R.id.bnaviga);
        Service apiService= Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    int group=response.body().getProfile().getGroup();
                    if (group == 3) {

                        bnavigation1.setVisibility(View.VISIBLE);
                        bnavigation1.getMenu().getItem(3).setChecked(true);
                        bnavigation1.setOnNavigationItemSelectedListener(menuItem -> {
                            switch (menuItem.getItemId()) {
                                case R.id.home:
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                    break;
                                case R.id.notification:
                                    if (prefer.contains("token") || prefer.contains("id")) {
                                        startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                    break;
                                case R.id.dealer:
                                    if (prefer.contains("token") || prefer.contains("id")) {
                                        startActivity(new Intent(getApplicationContext(), DealerStoreActivity.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                    break;
                                case R.id.message:
                                    break;
                                case R.id.account:
                                    if (prefer.contains("token") || prefer.contains("id")) {
                                        startActivity(new Intent(getApplicationContext(), Account.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                    break;
                            }
                            return false;
                        });
                    } else {

                        bnavigation.setVisibility(View.VISIBLE);
                        bnavigation.getMenu().getItem(3).setChecked(true);
                        bnavigation.setOnNavigationItemSelectedListener(menuItem -> {
                            switch (menuItem.getItemId()) {
                                case R.id.home:
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    startActivity(intent);
                                    break;
                                case R.id.notification:
                                    if (prefer.contains("token") || prefer.contains("id")) {
                                        startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                    break;
                                case R.id.camera:
                                    if (prefer.contains("token") || prefer.contains("id")) {
                                        startActivity(new Intent(getApplicationContext(), Camera.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                    break;
                                case R.id.message:
                                    break;
                                case R.id.account:
                                    if (prefer.contains("token") || prefer.contains("id")) {
                                        startActivity(new Intent(getApplicationContext(), Account.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                    break;
                            }
                            return false;
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });

//        CheckGroup check = new CheckGroup();
//        int g = check.getGroup(pk, this);
//        if (g == 3) {
//            bnavigation1 = findViewById(R.id.bottom_nav);
//            bnavigation1.setVisibility(View.VISIBLE);
//            bnavigation1.getMenu().getItem(3).setChecked(true);
//            bnavigation1.setOnNavigationItemSelectedListener(menuItem -> {
//                switch (menuItem.getItemId()) {
//                    case R.id.home:
//                        startActivity(new Intent(getApplicationContext(), Home.class));
//                        break;
//                    case R.id.notification:
//                        if (prefer.contains("token") || prefer.contains("id")) {
//                            startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
//                        } else {
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        }
//                        break;
//                    case R.id.dealer:
//                        if (prefer.contains("token") || prefer.contains("id")) {
//                            startActivity(new Intent(getApplicationContext(), DealerStoreActivity.class));
//                        } else {
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        }
//                        break;
//                    case R.id.message:
//                        break;
//                    case R.id.account:
//                        if (prefer.contains("token") || prefer.contains("id")) {
//                            startActivity(new Intent(getApplicationContext(), Account.class));
//                        } else {
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        }
//                        break;
//                }
//                return false;
//            });
//        } else {
//            bnavigation = findViewById(R.id.bnaviga);
//            bnavigation.setVisibility(View.VISIBLE);
//            bnavigation.getMenu().getItem(3).setChecked(true);
//            bnavigation.setOnNavigationItemSelectedListener(menuItem -> {
//                switch (menuItem.getItemId()) {
//                    case R.id.home:
//                        Intent intent = new Intent(getApplicationContext(), Home.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.notification:
//                        if (prefer.contains("token") || prefer.contains("id")) {
//                            startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
//                        } else {
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        }
//                        break;
//                    case R.id.camera:
//                        if (prefer.contains("token") || prefer.contains("id")) {
//                            startActivity(new Intent(getApplicationContext(), Camera.class));
//                        } else {
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        }
//                        break;
//                    case R.id.message:
//                        break;
//                    case R.id.account:
//                        if (prefer.contains("token") || prefer.contains("id")) {
//                            startActivity(new Intent(getApplicationContext(), Account.class));
//                        } else {
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        }
//                        break;
//                }
//                return false;
//            });
//        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        profile_image = findViewById(R.id.profile_image);
        //username=findViewById(R.id.username);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            Intent intent = new Intent(ChatMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        preferences = getSharedPreferences("RegisterActivity", MODE_PRIVATE);
        uusername = preferences.getString("name", "");
        password = preferences.getString("pass", "");
        encodeAuth = "Basic " + CommonFunction.getEncodedString(uusername, password);
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk", 0);
        } else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light);
        //swipeRefreshLayout.setOnRefreshListener(ChatMainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatMainActivity.this));
        RecyclerviewAdapter recyclerViewAdapter = new RecyclerviewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        mNoChat = findViewById(R.id.rl_noResult);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userChatList = new ArrayList<>();
        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_CHAT);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userChatList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getSender().equals(firebaseUser.getUid()) )  {
                            userChatList.add(new UserChat(chat.getReceiver(), chat.getPost()));
                        }
                        if(chat.getReceiver()!=null){
                            if (chat.getReceiver().equals(firebaseUser.getUid())) {
                                userChatList.add(new UserChat(chat.getSender(), chat.getPost()));
                            }
                        }

                        Log.d("ERROR Receiver", chat.getReceiver() + "," + firebaseUser.getUid());
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
            //getUserProfile();
            //basicReadWrite();

//        TabLayout tabLayout=findViewById(R.id.tab_layout);
//        ViewPager viewPager=findViewById(R.id.view_pager);
//
//        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
//
//        viewPagerAdapter.addFragment(new ChatAllFragment(),getString(R.string.all_text));
//        viewPagerAdapter.addFragment(new ChatSellFragment(),getString(R.string.buy));
//        viewPagerAdapter.addFragment(new ChatSellFragment(),getString(R.string.sell));
//        viewPagerAdapter.addFragment(new ChatRentFragment(),getString(R.string.rent));

            //viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
            //viewPagerAdapter.addFragment(new UsersFragment(),"Users");
            //viewPagerAdapter.addFragment(new ProfileFragment(),"Profile");

//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

        }
    }// onCreate

        class ViewPagerAdapter extends FragmentPagerAdapter {

            private ArrayList<Fragment> fragments;
            private ArrayList<String> titles;

            public ViewPagerAdapter(FragmentManager fm) {
                super(fm);
                this.fragments = new ArrayList<>();
                this.titles = new ArrayList<>();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            public void addFragment(Fragment fragment, String title) {
                fragments.add(fragment);
                titles.add(title);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        }

        private void status (String status){
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);

            databaseReference.updateChildren(hashMap);
        }

        @Override
        protected void onResume () {
            super.onResume();
            status("online");
        }

        @Override
        protected void onPause () {
            super.onPause();
            status("offline");
        }

        @Override
        public void onBackPressed () {
            if (login_verify != null) {
                startActivity(new Intent(ChatMainActivity.this, Home.class));
            } else finish();
        }

        @Override
        protected void onStart () {
            super.onStart();
            CheckGroup check = new CheckGroup();
            int g = check.getGroup(pk, this);
            if (g == 3) {
                bnavigation1.getMenu().getItem(3).setChecked(true);
            } else {
                bnavigation.getMenu().getItem(3).setChecked(true);
            }
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
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
                context = parent.getContext();
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                UserChat user = userList.get(position);
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUserId());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        com.bt_121shoppe.motorbike.models.User fuser = dataSnapshot.getValue(com.bt_121shoppe.motorbike.models.User.class);

                        if (fuser.getImageURL().equals("default"))
                            holder.profileImage.setImageResource(R.drawable.group_2293);
                        else
                            Glide.with(context).load(fuser.getImageURL()).into(holder.profileImage);

                        //initial user information
                        int userPK = 0, postId = 0;
                        String postUsername = "", postTitle = "", postPrice = "", postFrontImage = "", postType = "", title = "";
                        String username = fuser.getUsername();
                        String userRespone = "";
                        try {
                            userRespone = CommonFunction.doGetRequest(ConsumeAPI.BASE_URL + "api/v1/userfilter/?last_name=&username=" + username);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject obj = new JSONObject(userRespone);
                            int count = obj.getInt("count");
                            if (count > 0) {
                                JSONArray results = obj.getJSONArray("results");
                                JSONObject auser = results.getJSONObject(0);
//                            Log.d(TAG,"User "+auser);
                                userPK = auser.getInt("id");
                                if (auser.getString("first_name").isEmpty() || auser.getString("first_name") == null)
                                    postUsername = auser.getString("username");
                                else
                                    postUsername = auser.getString("first_name");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //get post information
                        String postResult = "";
                        try {
                            postResult = CommonFunction.doGetRequest(ConsumeAPI.BASE_URL + "detailposts/" + user.getPostId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject object = new JSONObject(postResult);
                            postId = object.getInt("id");
                            postTitle = object.getString("post_sub_title");
                            postFrontImage = object.getString("front_image_path");
                            postPrice = object.getString("cost");
                            postType = object.getString("post_type");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        title = postTitle.split(",").length > 1 ? postTitle.split(",")[1] : postTitle.split(",")[0];

                        holder.tvUsername.setText(postUsername);
                        holder.tvTitle.setText(title);
                        holder.userChat = new UserChat(postId, title, postPrice, postFrontImage, postType, userPK, postUsername, username);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.tvTitle.setText(userList.get(position).getUserId());
                //get post image from firebase
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST).child(user.getPostId());
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            try {
                                JSONObject obj = new JSONObject((Map) dataSnapshot.getValue());
                                String coverUrl = obj.getString("coverUrl");
                                if (coverUrl.equals("default")) {
                                    Glide.with(context).load(R.drawable.no_image_available).thumbnail(0.1f).into(holder.postImage);
                                } else {
                                    Glide.with(context).load(coverUrl).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(holder.postImage);
                                }
                                Log.d(TAG, obj.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Check_seen_unseen(user.getUserId(), user.getPostId(), holder.tvUsername);
                lastMessage(user.getUserId(), user.getPostId(), holder.tvLastChat);
            }

            @Override
            public int getItemCount() {
                if (userList != null) {
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
                    profileImage = itemView.findViewById(R.id.profile_image);
                    postImage = itemView.findViewById(R.id.imageView);
                    tvUsername = itemView.findViewById(R.id.tvUsername);
                    tvDate = itemView.findViewById(R.id.tvDate);
                    tvTitle = itemView.findViewById(R.id.tvTitle);
                    tvLastChat = itemView.findViewById(R.id.tvLastChat);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            Query query = databaseReference.child(ConsumeAPI.FB_CHAT).orderByChild("to").equalTo(userChat.getUserId());
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String key = snapshot.getKey();
                                        Log.e("jjjjjjj", key);
                                        updateData(key);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Intent intent = new Intent(ChatMainActivity.this, ChatActivity.class);
                            intent.putExtra("postId", userChat.getPostIdd());
                            intent.putExtra("postTitle", userChat.getPostTitle());
                            intent.putExtra("postPrice", userChat.getPostPrice());
                            intent.putExtra("postImage", userChat.getPostFrontImage());
                            intent.putExtra("postUserPk", userChat.getUserPk());
                            intent.putExtra("postUsername", userChat.getPostUsername());
                            intent.putExtra("postUserId", userChat.getFirebaseUsename());
                            intent.putExtra("postType", userChat.getPostType());
                            startActivity(intent);
                        }
                    });

                }
            }

        }
    private void lastMessage(String userid, String postid, TextView last_msg) {
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_CHAT);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver()!=null){
                        if ((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) && chat.getPost().equals(postid)) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }
                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("No message");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Check_seen_unseen(String userid, String postid, TextView title) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_CHAT);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.e("isSeen", String.valueOf(chat.isIsseen()));
                    if(chat.getReceiver()!=null){
                        if ((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) && chat.getPost().equals(postid)) {
                            if (!chat.isIsseen()) {
                                title.setTextAppearance(ChatMainActivity.this, R.style.ListnUnseen);
                            } else {
                                title.setTextAppearance(ChatMainActivity.this, R.style.ListSeen);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateData(String key) {
        FirebaseDatabase fuser = FirebaseDatabase.getInstance();
        DatabaseReference reference = fuser.getReference();
        reference.child(ConsumeAPI.FB_CHAT).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("isseen").setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }
}
