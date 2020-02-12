package com.bt_121shoppe.motorbike.chats;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.adapters.MessageAdapter;
import com.bt_121shoppe.motorbike.interfaces.APIService;
import com.bt_121shoppe.motorbike.models.Chat;
import com.bt_121shoppe.motorbike.models.FBPostViewModel;
import com.bt_121shoppe.motorbike.models.PostProduct;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.notifications.Client;
import com.bt_121shoppe.motorbike.notifications.Data;
import com.bt_121shoppe.motorbike.notifications.MyResponse;
import com.bt_121shoppe.motorbike.notifications.Sender;
import com.bt_121shoppe.motorbike.notifications.Token;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG=ChatActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tvusername,tvactive,tvposttitle,tvpostprice,tvreceiver;
    private ImageView imageView;
    private RelativeLayout rlUsername,rlPost;
    private ImageButton btn_send;
    private EditText text_send;
    private Bundle bundle;
    private int userPk,postOwnerID=0;
    private String postUsername,postTitle,postPrice,postImage,postUserId,postId,postType,userId;

    private MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    FirebaseUser fuser;
    DatabaseReference databaseReference;
    User chatUser;

    APIService apiService;
    boolean notify=false;

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

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
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
            postOwnerID=bundle.getInt("userOwnerId",0);
            Log.e("TAG","Chat Main Activity "+postUserId);

            tvusername.setText(postUsername);
            //tvposttitle.setText(postTitle);
            tvpostprice.setText("$"+postPrice);

            if(postImage==null|| postImage.isEmpty()){
                Glide.with(this).load(R.drawable.no_image_available).thumbnail(0.1f).into(imageView);
            }
            else{
                Glide.with(this).load(postImage).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(imageView);
//                byte[] decodedString1 = Base64.decode(postImage, Base64.DEFAULT);
//                Bitmap bitmapImage = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
//                imageView.setImageBitmap(bitmapImage);
            }
            initialPostInformation(postId);
        }

        fuser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        rlUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        rlPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String msg=text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),userId,msg,postId,postType);
                }else{
                    Toast.makeText(ChatActivity.this,getString(R.string.message),Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });
    }

    private User getUserToInformation(){
        User user=new User();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    com.bt_121shoppe.motorbike.models.User uuser=snapshot.getValue(com.bt_121shoppe.motorbike.models.User.class);

                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(fuser.getMetadata().getLastSignInTimestamp());
                    String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                    if(uuser.getUsername()!=null){
                        if(uuser.getUsername().equals(postUserId)){
                            tvreceiver.setText(uuser.getId());
                            readMessage(fuser.getUid(),uuser.getId(),postId,uuser.getImageURL());
                            setUserId(uuser.getId());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        user.setId(tvreceiver.getText().toString());
        //Log.d(TAG,"here i got firebase chat to user.... "+user.getId());
        return user;
    }

    private void initialPostInformation(String id){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_POST);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    try{
                        JSONObject obj=new JSONObject((Map) snapshot.getValue());
                        if(obj.getString("id")!=null){

                            String fbPostID=obj.getString("id");
                            if(fbPostID.equals(id)){
                                if(obj.getString("subTitle")!=null){
                                    String postSubTitle=obj.getString("subTitle");
                                    String[] postTitle=postSubTitle.split(",");
                                    tvposttitle.setText(postTitle[0]);
                                }
                                int postOwnerId=obj.getInt("createdBy");
                                Log.e("TAG","POSt owner id "+postOwnerId);
                                /* post profile */
                                try{
                                    Service api = com.bt_121shoppe.motorbike.Api.api.Client.getClient().create(Service.class);
                                    Call<com.bt_121shoppe.motorbike.Api.User> call = api.getuser(postOwnerId);
                                    call.enqueue(new retrofit2.Callback<com.bt_121shoppe.motorbike.Api.User>() {
                                        @Override
                                        public void onResponse(Call<com.bt_121shoppe.motorbike.Api.User> call, Response<com.bt_121shoppe.motorbike.Api.User> response) {
                                            if (response.isSuccessful()){
                                                //tvusername.setText(response.body().getFirst_name());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<com.bt_121shoppe.motorbike.Api.User> call, Throwable t) {
                                            Log.d("Error",t.getMessage());
                                        }
                                    });
                                }catch (Exception e){ Log.d("TRY CATCH",e.getMessage());}
                            }
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        reference.child(ConsumeAPI.FB_CHAT).push().setValue(hashMap);

        //add user to chat fragment

        //send notification
        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(notify) {
                    sendNotification(receiver, user.getUsername(), msg);
                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver,String username,String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        if(receiver!=null){
            Query query=tokens.orderByKey().equalTo(receiver);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Token token=snapshot.getValue(Token.class);
                        Data data=new Data(fuser.getUid(),R.mipmap.ic_launcher,username+" : "+message,"New Message",userId);

                        Sender sender=new Sender(data,token.getToken());
                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        Log.d(TAG,"Response "+response);
                                        if(response.code()==200){
                                            if(response.body().success!=1){
                                                Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void readMessage(String myid,String userid,String postid,String imageUrl){
        CharSequence usedrId=tvreceiver.getText();
        mChat=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference(ConsumeAPI.FB_CHAT);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getPost().equals(postid) && (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid))){
                        mChat.add(chat);
                    }
                    messageAdapter=new MessageAdapter(ChatActivity.this,mChat,imageUrl);
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
