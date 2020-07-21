package com.bt_121shoppe.motorbike.activities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.models.NotificationResponseViewModel;
import com.bt_121shoppe.motorbike.models.NotificationViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.notifications.DetailNotification;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.notifications.Token;
import com.bt_121shoppe.motorbike.stores.StoreListActivity;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import com.bt_121shoppe.motorbike.Login_Register.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG= NotificationActivity.class.getSimpleName();
    private int pk=0;
    private String username="",password="",encodeAuth="";

    private SharedPreferences prefs=null;
    private BottomNavigationView mBottomNavigation,mBottomNavigation1;
    private RelativeLayout mNoNotification;
    private RecyclerView mRecyclerView;
    private ArrayList<NotificationViewModel> mNotifications;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseUser fuser;
    DatabaseReference reference;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_notification);

        Toolbar toolbar=findViewById(R.id.toolbar);
        //TextView mTitle=toolbar.findViewById(R.id.toolbar_title);
        //mTitle.setText(getString(R.string.notification));
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        prefs = getSharedPreferences("Register",MODE_PRIVATE);
        username = prefs.getString("name","");
        password = prefs.getString("pass","");
        encodeAuth = getEncodedString(username,password);
        if(prefs.contains("token"))
            pk=prefs.getInt("Pk",0);
        else
            pk=prefs.getInt("id",0);

        Log.d("Pk",""+pk);

        Active_user activeUser=new Active_user();
        String active=activeUser.isUserActive(pk,this);
        if(active.equals("false"))
            activeUser.clear_session(this);

        ImageView tvBack=findViewById(R.id.tvBack);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* start implementation bottom navigation
        CheckGroup check = new CheckGroup();
        int g = check.getGroup(pk,this);
        if (g == 3){
            mBottomNavigation1=findViewById(R.id.bottom_nav);
            mBottomNavigation1.setVisibility(View.VISIBLE);
            mBottomNavigation1.getMenu().getItem(1).setChecked(true);
            mBottomNavigation1.setOnNavigationItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(NotificationActivity.this, Home.class));
                        break;
                    case R.id.notification:
                        if(prefs.contains("token") || prefs.contains("id")){
                            startActivity(new Intent(NotificationActivity.this, StoreListActivity.class));
                        }else{
                            Intent intent=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent.putExtra("verify","camera");
                            startActivity(intent);
                        }
                        break;
                    case R.id.dealer:
                        if(prefs.contains("token") || prefs.contains("id")){
                            startActivity(new Intent(NotificationActivity.this, DealerStoreActivity.class));
                        }else{
                            Intent intent=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent.putExtra("verify","camera");
                            startActivity(intent);
                        }
                        break;
                    case R.id.message:
                        if(prefs.contains("token") || prefs.contains("id")){
                            startActivity(new Intent(NotificationActivity.this, ChatMainActivity.class));
                        }else{
                            Intent intent=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent.putExtra("verify","camera");
                            startActivity(intent);
                        }
                        break;
                    case R.id.account:
                        if(prefs.contains("token") || prefs.contains("id")){
                            startActivity(new Intent(NotificationActivity.this, Account.class));
                        }else{
                            Intent intent=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent.putExtra("verify","camera");
                            startActivity(intent);
                        }
                        break;
                }
                return false;
            });
        }else {
            mBottomNavigation=findViewById(R.id.bnaviga);
            mBottomNavigation.setVisibility(View.VISIBLE);
            mBottomNavigation.getMenu().getItem(1).setChecked(true);
            mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Intent intent=new Intent(NotificationActivity.this,Home.class);
                        startActivity(intent);
                        break;
                    case R.id.notification:
                        if(prefs.contains("token") || prefs.contains("id")){
//                        if(Active_user.isUserActive(this,pk)){
//                            startActivity(new Intent(NotificationActivity.this, NotificationActivity.class));
//                        }else{
//                            Active_user.clearSession(this);
//                        }
                            startActivity(new Intent(NotificationActivity.this, NotificationActivity.class));
                        }else{
                            Intent intent1=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent1.putExtra("verify","camera");
                            startActivity(intent1);
                        }
                        break;
                    case R.id.camera:
                        if(prefs.contains("token") || prefs.contains("id")){
//                        if(Active_user.isUserActive(this,pk)){
//                            startActivity(new Intent(NotificationActivity.this, Camera.class));
//                        }else{
//                            Active_user.clearSession(this);
//                        }
                            startActivity(new Intent(NotificationActivity.this, Camera.class));
                        }else{
                            Intent intent1=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent1.putExtra("verify","camera");
                            startActivity(intent1);
                        }
                        break;
                    case R.id.message:
                        if(prefs.contains("token") || prefs.contains("id")){
//                        if(Active_user.isUserActive(this,pk)){
//                            startActivity(new Intent(NotificationActivity.this, ChatMainActivity.class));
//                        }else{
//                            Active_user.clearSession(this);
//                        }
                            startActivity(new Intent(NotificationActivity.this, ChatMainActivity.class));
                        }else{
                            Intent intent1=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent1.putExtra("verify","camera");
                            startActivity(intent1);
                        }
                        break;
                    case R.id.account:
                        if(prefs.contains("token") || prefs.contains("id")){
//                        if(Active_user.isUserActive(this,pk)){
//                            startActivity(new Intent(NotificationActivity.this, Account.class));
//                        }else{
//                            Active_user.clearSession(this);
//                        }
                            startActivity(new Intent(NotificationActivity.this, Account.class));
                        }else{
                            Intent intent1=new Intent(NotificationActivity.this, LoginActivity.class);
                            intent1.putExtra("verify","camera");
                            startActivity(intent1);
                        }
                        break;
                }
                return false;
            });
        }
        /*end implementation bottom navigation */

        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mNotifications=new ArrayList<>();

        mNoNotification=findViewById(R.id.rl_noResult);
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotificationAdapter mAdapter=new NotificationAdapter();
        mRecyclerView.setAdapter(mAdapter);

//        fuser= FirebaseAuth.getInstance().getCurrentUser();
//        reference= FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child(ConsumeAPI.FB_Notification).orderByChild("to").equalTo(fuser.getUid());
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mNotifications.clear();
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    JSONObject obj1=new JSONObject((Map) snapshot.getValue());
//                    DataSnapshot meta = snapshot.child("data");
//                    try {
//                        JSONObject obj = new JSONObject((String) meta.getValue());
//                        String userId = obj.getString("to");
//                        String title=obj.getString("title");
//                        String message=obj.getString("message");
//                        String datetime = obj1.getString("datetime");
//                        Boolean isSeen = obj1.getBoolean("isSeen");
//                        mNotifications.add(new NotificationViewModel(message,title,userId,datetime,isSeen));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if(mNotifications.size()>0)
//                    mNoNotification.setVisibility(View.GONE);
//                else
//                    mNoNotification.setVisibility(View.VISIBLE);
//
//                mAdapter.setNotificationList(getBaseContext(),mNotifications);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        updateToken(FirebaseInstanceId.getInstance().getToken());
        Service api = Client.getClient().create(Service.class);
        Call<NotificationResponseViewModel> call = api.getNotificationListItems(pk,encodeAuth);
        call.enqueue(new retrofit2.Callback<NotificationResponseViewModel>() {
            @Override
            public void onResponse(retrofit2.Call<NotificationResponseViewModel> call, retrofit2.Response<NotificationResponseViewModel> response) {
                if (response.isSuccessful()) {
                    if(response.body().getCount()==0)
                        mNoNotification.setVisibility(View.VISIBLE);
                    else {
                        mNoNotification.setVisibility(View.GONE);
                        mNotifications=response.body().getresults();
                        mAdapter.setNotificationList(getBaseContext(),mNotifications);
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<NotificationResponseViewModel> call, Throwable t) {

            }
        });
    }
    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),Base64.NO_WRAP);
    }

    @Override
    public void onStart(){
        super.onStart();
//        CheckGroup check = new CheckGroup();
//        int g = check.getGroup(pk,this);
//        if (g == 3){
//            mBottomNavigation1.getMenu().getItem(1).setChecked(true);
//        }else {
//            mBottomNavigation.getMenu().getItem(1).setChecked(true);
//        }

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //recreate();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },1500);
    }

    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

        private List<NotificationViewModel> notificationList;
        private Context mContext;

        public void setNotificationList(Context mContext,List<NotificationViewModel> notificationList){
            this.mContext = mContext;
            this.notificationList = notificationList;
            notifyDataSetChanged();
        }


        @Override
        public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(mContext).inflate(R.layout.item_notification,parent,false);
            return new NotificationAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
            NotificationViewModel notification=notificationList.get(position);
            holder.tvTitle.setText(notification.getTitle());
            holder.tvDescription.setText(notification.getReject_reason());
            holder.tvDatetime.setText(notification.getDatatime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service api = Client.getClient().create(Service.class);
                    Call<NotificationViewModel> call = api.getNotification(notification.getId());
                    call.enqueue(new retrofit2.Callback<NotificationViewModel>() {
                        @Override
                        public void onResponse(retrofit2.Call<NotificationViewModel> call, retrofit2.Response<NotificationViewModel> response) {
                            if (response.isSuccessful()) {
                                String userId        = response.body().getUserId();
                                String title         = response.body().getTitle();
                                String notify        = response.body().getNotify();
                                String reject_reason = response.body().getReject_reason();
                                int ref_id           = response.body().getRef_id();
                                Boolean isSeen       = response.body().getisSeen();
                                String datetime      = response.body().getDatatime();
                                //Log.e("data",""+userId+","+title+","+notify+","+reject_reason+","+ref_id+","+isSeen+","+datetime);
                                if (isSeen.equals(false)) {
                                    //updateData(new NotificationViewModel(notify, reject_reason, title, true, ref_id, userId, datetime));
                                    updateData(response.body());
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<NotificationViewModel> call, Throwable t) {

                        }
                    });
//                    reference= FirebaseDatabase.getInstance().getReference();
//                    Query query = reference.child(ConsumeAPI.FB_Notification).orderByChild("to").equalTo(notification.getUserId());
//                    query.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                                String key= snapshot.getKey();
////                                updateData(key);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                    Intent intent=new Intent(mContext, DetailNotification.class);
                    intent.putExtra("userId",notification.getUserId());
                    intent.putExtra("title",notification.getTitle());
                    intent.putExtra("message",notification.getReject_reason());
                    intent.putExtra("datetime",notification.getDatatime());
                    intent.putExtra("isSeen",notification.getisSeen());
                    startActivity(intent);
                }
            });
            Check_seen_unseen(holder.tvTitle);
        }

        @Override
        public int getItemCount() {
            if(notificationList!=null)
                return notificationList.size();
            return 0;
        }

        private class MyViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView tvTitle;
            TextView tvDescription;
            TextView tvDatetime;

            public MyViewHolder(View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.notification_image);
                tvTitle=itemView.findViewById(R.id.tvTitle);
                tvDescription=itemView.findViewById(R.id.Description);
                tvDatetime=itemView.findViewById(R.id.datetime);
            }
        }
    }
//
//    private class NotificationViewModel{
//
//        private String body;
//        private String datetime;
//        private String title;
//        private String userId;
//        private boolean isSeen;
//
//        public NotificationViewModel(){ }
//
//        public NotificationViewModel(String body,String title,String userId,String datetime, Boolean isSeen){
//            this.body=body;
//            this.datetime=datetime;
//            this.title=title;
//            this.userId=userId;
//            this.isSeen=isSeen;
//        }
//
//        public String getUserId() {
//            return userId;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDatetime() {
//            return datetime;
//        }
//
//        public String getBody() {
//            return body;
//        }
//
//        public boolean getisSeen() {
//            return isSeen;
//        }
//
//        public void setUserId(String userId) {
//            this.userId = userId;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public void setisSeen(boolean seen) {
//            isSeen = seen;
//        }
//
//        public void setBody(String body) {
//            this.body = body;
//        }
//
//        public void setDatetime(String datetime) {
//            this.datetime = datetime;
//        }
//    }

//    private void Check_seen_unseen(TextView title){
//        fuser= FirebaseAuth.getInstance().getCurrentUser();
//        reference= FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child(ConsumeAPI.FB_Notification).orderByChild("to").equalTo(fuser.getUid());
//        query.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    JSONObject obj=new JSONObject((Map) snapshot.getValue());
//                    try {
//                        Boolean isSeen = obj.getBoolean("isSeen");
//                        if (isSeen.equals(false)){
//                            title.setTextAppearance(NotificationActivity.this, R.style.ListnUnseen);
//                        }else {
//                            title.setTextAppearance(NotificationActivity.this, R.style.ListSeen);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//    private void updateData(String key) {
//        FirebaseDatabase fuser = FirebaseDatabase.getInstance();
//        DatabaseReference reference = fuser.getReference();
//        reference.child(ConsumeAPI.FB_Notification).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                dataSnapshot.getRef().child("isSeen").setValue(true);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("User", databaseError.getMessage());
//            }
//        });
//    }
    private void Check_seen_unseen(TextView title){
        Service api = Client.getClient().create(Service.class);
        Call<NotificationViewModel> call = api.getNotification(pk);
        call.enqueue(new retrofit2.Callback<NotificationViewModel>() {
            @Override
            public void onResponse(retrofit2.Call<NotificationViewModel> call, retrofit2.Response<NotificationViewModel> response) {
                if (response.isSuccessful()) {
                    Boolean isSeen = response.body().getisSeen();
                    if (isSeen.equals(false)){
                        title.setTextAppearance(NotificationActivity.this, R.style.ListnUnseen);
                    }else {
                        title.setTextAppearance(NotificationActivity.this, R.style.ListSeen);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<NotificationViewModel> call, Throwable t) {

            }
        });
    }
    private void updateData(NotificationViewModel notification) {
        Service api = Client.getClient().create(Service.class);
        Call<NotificationViewModel> call = api.updateNotification(notification.getId(),notification);
        call.enqueue(new Callback<NotificationViewModel>() {
            @Override
            public void onResponse(Call<NotificationViewModel> call, Response<NotificationViewModel> response) {
                if (response.isSuccessful()){
                    Log.e("successfully",""+response.body());
                }
            }

            @Override
            public void onFailure(Call<NotificationViewModel> call, Throwable t) {
                Log.e("Failed",""+t.getMessage());
            }
        });
    }
}
