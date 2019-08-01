package com.bt_121shoppe.lucky_app.chats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.adapters.MessageAdapter;
import com.bt_121shoppe.lucky_app.models.Chat;
import com.bt_121shoppe.lucky_app.models.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG=ChatActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tvusername,tvactive,tvposttitle,tvpostprice,tvreceiver;
    private ImageView imageView;
    private RelativeLayout rlUsername,rlPost;
    private ImageButton btn_send;
    private EditText text_send;
    private Bundle bundle;

    private int userPk;
    private String postUsername,postTitle,postPrice,postImage,postUserId,postId,postType,userId;

    private MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    FirebaseUser fuser;
    DatabaseReference databaseReference;
    User chatUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView btnBack=(TextView) findViewById(R.id.tvBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        rlUsername=(RelativeLayout) findViewById(R.id.rlusername);
        rlPost=(RelativeLayout) findViewById(R.id.rlPost);
        tvusername=(TextView) findViewById(R.id.toolbar_user_title);
        tvactive=(TextView) findViewById(R.id.tv_active_duration);
        tvposttitle=(TextView) findViewById(R.id.tv_post_title);
        tvpostprice=(TextView) findViewById(R.id.tv_price);
        imageView=(ImageView) findViewById(R.id.image_view);
        btn_send=(ImageButton) findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);
        tvreceiver=findViewById(R.id.text_userid);

        chatUser=getUserToInformation();

        bundle=getIntent().getExtras();
        if(bundle!=null){
            postId=String.valueOf(bundle.getInt("postId",0));
            postTitle=bundle.getString("postTitle");
            postPrice=bundle.getString("postPrice");
            postImage=bundle.getString("postImage");
            postUsername=bundle.getString("postUsername");
            postUserId=bundle.getString("postUserId");
            userPk=bundle.getInt("postUserPk",0);
            postType=bundle.getString("postType");

            tvusername.setText(postUsername);
            tvposttitle.setText(postTitle);
            tvpostprice.setText("$ "+postPrice);

            if(postImage.isEmpty()){
                Glide.with(this).load("https://www.straitstimes.com/sites/default/files/styles/article_pictrure_780x520_/public/articles/2018/10/22/ST_20181022_NANVEL_4360142.jpg?itok=ZB5zgW7e&timestamp=1540134011").into(imageView);
            }
            else{
                byte[] decodedString1 = Base64.decode(postImage, Base64.DEFAULT);
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imageView.setImageBitmap(bitmapImage);
            }
        }

        fuser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        //Log.d(TAG,"UusER "+userId+ " From clastt ");

        rlUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rlPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),userId,msg,postId,postType);
                }else{
                    Toast.makeText(ChatActivity.this,"You can't send empty message",Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuAbout:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuLogout:
                Toast.makeText(this, "You clicked logout", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    private User getUserToInformation(){
        User user=new User();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User uuser=snapshot.getValue(User.class);
                    //Log.d(TAG,"RUN "+uuser.getUsername()+" "+postUserId);

                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(fuser.getMetadata().getLastSignInTimestamp());
                    String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

                    //Log.d(TAG,"RUN GET USER "+fuser.getMetadata().getLastSignInTimestamp()+" "+date);
                    if(uuser.getUsername().equals(postUserId)){
                        //tvreceiver.setText(uuser.getId());
                        readMessage(fuser.getUid(),uuser.getId(),postId);
                        setUserId(uuser.getId());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        user.setId(tvreceiver.getText().toString());
        Log.d(TAG,"here i got firebase chat to user.... "+user.getId());
        return user;
    }

    private void sendMessage(String sender,String receiver,String message,String postId,String chatType){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("post",postId);
        hashMap.put("type",chatType);
        hashMap.put("isseen",false);
        reference.child("chats").push().setValue(hashMap);
    }

    private void readMessage(String myid,String userid,String postid){
        CharSequence usedrId=tvreceiver.getText();
        Log.d(TAG,"I get from read message function "+usedrId);
        mChat=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    Log.d(TAG,"RUN CHAT Master"+postId+" "+myid+" "+userid);
                    Log.d(TAG,"RUN CHAT "+chat.getPost()+" "+chat.getSender()+" "+chat.getReceiver());
                    if(chat.getPost().equals(postid) && (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid))){
                        mChat.add(chat);
                        Log.d(TAG,"DAMN TRUE");
                    }
                    messageAdapter=new MessageAdapter(ChatActivity.this,mChat,"default");
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserId(String id){
        userId=id;
        //Log.d(TAG,"Call get user "+id);
    }
}
