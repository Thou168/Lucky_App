package com.bt_121shoppe.motorbike.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Login_Register.UserAccountActivity;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.chats.ChatMainActivity;
import com.bt_121shoppe.motorbike.notifications.Token;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bt_121shoppe.motorbike.utils.Notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG= NotificationActivity.class.getSimpleName();
    private int pk=0;
    private String username="",password="",encodeAuth="";

    private SharedPreferences prefs=null;
    private BottomNavigationView mBottomNavigation;
    private RelativeLayout mNoNotification;
    private RecyclerView mRecyclerView;
    private ArrayList<NotificationViewModel> mNotifications;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseUser fuser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_notification);

        Toolbar toolbar=findViewById(R.id.toolbar);
        TextView mTitle=toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.notification));
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        prefs=getSharedPreferences("Register", Context.MODE_PRIVATE);
        username=prefs.getString("name","");
        password=prefs.getString("pass","");
        encodeAuth="Basic "+ CommonFunction.getEncodedString(username,password);
        if(prefs.contains("token"))
            pk=prefs.getInt("PK",0);
        else
            pk=prefs.getInt("id",0);

        Active_user activeUser=new Active_user();
        String active=activeUser.isUserActive(pk,this);
        if(active.equals("false"))
            activeUser.clear_session(this);

        /* start implementation bottom navigation */
        mBottomNavigation=findViewById(R.id.bnaviga);
        mBottomNavigation.getMenu().getItem(0).setChecked(true);
        mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.home:
                    Intent intent=new Intent(NotificationActivity.this,Home.class);
                    startActivity(intent);
                    break;
                case R.id.notification:
                    if(prefs.contains("token") || prefs.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(NotificationActivity.this, NotificationActivity.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent1=new Intent(NotificationActivity.this, UserAccountActivity.class);
                        intent1.putExtra("verify","camera");
                        startActivity(intent1);
                    }
                    break;
                case R.id.camera:
                    if(prefs.contains("token") || prefs.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(NotificationActivity.this, Camera.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent1=new Intent(NotificationActivity.this, UserAccountActivity.class);
                        intent1.putExtra("verify","camera");
                        startActivity(intent1);
                    }
                    break;
                case R.id.message:
                    if(prefs.contains("token") || prefs.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(NotificationActivity.this, ChatMainActivity.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent1=new Intent(NotificationActivity.this, UserAccountActivity.class);
                        intent1.putExtra("verify","camera");
                        startActivity(intent1);
                    }
                    break;
                case R.id.account:
                    if(prefs.contains("token") || prefs.contains("id")){
                        if(Active_user.isUserActive(this,pk)){
                            startActivity(new Intent(NotificationActivity.this, Account.class));
                        }else{
                            Active_user.clearSession(this);
                        }
                    }else{
                        Intent intent1=new Intent(NotificationActivity.this, UserAccountActivity.class);
                        intent1.putExtra("verify","camera");
                        startActivity(intent1);
                    }
                    break;
            }
            return false;
        });
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

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNotifications.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try{
                        JSONObject obj=new JSONObject((Map) snapshot.getValue());
                        String userId=obj.getString("userId");
                        if(userId.equals(fuser.getUid())) {
                            String body = obj.getString("body");
                            String datetime = obj.getString("datetime");
                            String notifyType = obj.getString("notifyType");
                            String title = obj.getString("title");
                            mNotifications.add(new NotificationViewModel(body, datetime, notifyType, title, userId));
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                if(mNotifications.size()>0)
                    mNoNotification.setVisibility(View.GONE);
                else
                    mNoNotification.setVisibility(View.VISIBLE);

                mAdapter.setNotificationList(mNotifications);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onStart(){
        super.onStart();
        mBottomNavigation.getMenu().getItem(3).setChecked(true);
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
        private Context context;

        public void setNotificationList(List<NotificationViewModel> notificationList){
            this.notificationList=notificationList;
            notifyDataSetChanged();
        }


        @Override
        public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false));
        }

        @Override
        public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
            NotificationViewModel notification=notificationList.get(position);
            holder.tvTitle.setText(notification.getTitle());
            holder.tvDescription.setText(notification.getBody());
            holder.tvDatetime.setText(notification.getDatetime());
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
                tvDatetime=itemView.findViewById(R.id.tvDate);
            }
        }
    }

    private class NotificationViewModel{

        private String body;
        private String datetime;
        private String notifyType;
        private String title;
        private String userId;
        private boolean isSeen;

        public NotificationViewModel(){ }

        public NotificationViewModel(String body,String datetime,String notifyType,String title,String userId){
            this.body=body;
            this.datetime=datetime;
            this.notifyType=notifyType;
            this.title=title;
            this.userId=userId;
            //this.isSeen=isSeen;
        }

        public String getUserId() {
            return userId;
        }

        public String getTitle() {
            return title;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getBody() {
            return body;
        }

        public String getNotifyType() {
            return notifyType;
        }

        public boolean isSeen() {
            return isSeen;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSeen(boolean seen) {
            isSeen = seen;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setNotifyType(String notifyType) {
            this.notifyType = notifyType;
        }
    }

}
